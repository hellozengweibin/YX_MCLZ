<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="resgroupid">
        <el-input v-model="queryParams.resgroupid" placeholder="请输入ResGroupID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['business:resgroupbindtbl3:add']">新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="success" plain icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['business:resgroupbindtbl3:edit']">修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['business:resgroupbindtbl3:remove']">删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['business:resgroupbindtbl3:export']">导出</el-button>-->
<!--      </el-col>-->

      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-upload"
          size="small"
          @click="handleUpload"
        >批量导入</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--    &lt;!&ndash; 批量导入弹框 &ndash;&gt;-->
    <el-dialog
      title="批量导入"
      :visible.sync="uploadDialog"
      width="40%"
      append-to-body
      @close="handleUploadClose"
    >
      <el-form label-position="right" label-width="95px">

        <el-form-item label="模板下载：">
          <el-col :span="1.5">
            <a href="./Import_template.xlsx" download="导入模板.xlsx">
              <el-button type="primary" plain icon="el-icon-download" size="mini">下载导入模板</el-button>
            </a>
          </el-col>
        </el-form-item>

        <el-form-item label="上传文件：">
          <el-upload
            ref="upload"
            :action="uploadFileUrl"
            :file-list="fileList"
            :limit="1"
            :before-upload="handleBeforeUpload"
            :on-error="handleUploadError"
            :on-success="handleFileSuccess"
            :headers="headers"
            accept=".xls,.xlsx"
            :show-file-list="false"
          >
            <!-- 上传按钮 -->
            <el-button
              type="success"
              plain
              icon="el-icon-upload"
              size="small"
              accept=".xls,.xlsx"
            >点击上传模板文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

    </el-dialog>


    <el-table v-loading="loading" :data="resgroupbindtbl3List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="ResGroupID" align="center" prop="resgroupid"/>
      <el-table-column label="BindResId" align="center" prop="bindresid"/>
      <el-table-column label="BindResType" align="center" prop="bindrestype"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:resgroupbindtbl3:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: red;" @click="handleDelete(scope.row)" v-hasPermi="['business:resgroupbindtbl3:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改resgroupbindtbl3对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <div class="el-dialog-div">
        <el-form ref="form" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="ResGroupID" prop="resgroupid">
            <el-input v-model="form.resgroupid" :disabled="true" placeholder="请输入ResGroupID"/>
          </el-form-item>
          <el-form-item label="BindResId" prop="bindresid">
            <el-input v-model="form.bindresid" placeholder="请输入BindResId"/>
          </el-form-item>
          <el-form-item label="BindResType" prop="bindrestype">
            <el-input v-model="form.bindrestype" :disabled="true" placeholder="请输入BindResType"/>
          </el-form-item>
        </el-form>
      </div>

      <div class="el-dialog-div">
        <el-form ref="form" :model="form2" :rules="rules" label-width="110px">
          <el-form-item label="ResGroupID" prop="resgroupid">
            <el-input v-model="form2.resgroupid" :disabled="true" placeholder="请输入ResGroupID"/>
          </el-form-item>
          <el-form-item label="BindResId" prop="bindresid">
            <el-input v-model="form2.bindresid" placeholder="请输入BindResId"/>
          </el-form-item>
          <el-form-item label="BindResType" prop="bindrestype">
            <el-input v-model="form2.bindrestype" :disabled="true" placeholder="请输入BindResType"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="float: right">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResgroupbindtbl3, getResgroupbindtbl3, delResgroupbindtbl3, addResgroupbindtbl3, updateResgroupbindtbl3 } from "@/api/business/resgroupbindtbl3";
import {parseTime} from "@/utils/util";
import {getToken} from "@/utils/auth";
export default {
  name: "Resgroupbindtbl3",
  data() {
    return {
      // 批量导入
      uploadDialog: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // resgroupbindtbl3表格数据
      resgroupbindtbl3List: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resgroupid: null,
        bindresid: null,
        bindrestype: null
      },
      // 表单参数
      form: {
        resgroupid:null,
        bindrestype:null,
      },
      form2: {
        resgroupid:null,
        bindrestype:null,
      },
      // 表单校验
      rules: {
      },
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/business/resgroupbindtbl3/import",
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      fileList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleUpload() {
      this.uploadDialog = true
    },
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      this.$modal.loading('正在上传中，请稍候...')
      return true
    },
    // 上传失败
    handleUploadError(err) {
      console.log(err)
      this.$modal.msgError('文件上传失败，请重试')
      this.$modal.closeLoading()
    },
    // 上传成功
    handleFileSuccess(response, file, fileList) {
      if (response.code !== 200) {
        console.log(response.msg)
        this.$message({
          type: 'error',
          dangerouslyUseHTMLString: true,
          message: response.msg.replaceAll('\r\n', '<br />')
        })
        // this.$message.error(response.msg)
      } else {
        this.$message.success(response.msg)
      }
      this.$refs.upload.clearFiles()
      setTimeout(() => {
        this.$modal.closeLoading()
      }, 2000)
    },
    handleUploadClose() {
      this.$refs['upload'] && this.$refs['upload'].clearFiles()
    },
    /** 查询resgroupbindtbl3列表 */
    getList() {
      this.loading = true;
      listResgroupbindtbl3(this.queryParams).then(response => {
        this.resgroupbindtbl3List = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        resgroupid: null,
        bindresid: null,
        bindrestype: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加resgroupbindtbl3";
      this.form.resgroupid = '44020000002160000018';

      this.form.bindrestype = 132;
      this.form2.resgroupid = '44020000002160000031';
      this.form2.bindrestype = 132;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getResgroupbindtbl3(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改resgroupbindtbl3";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id!=null){
              updateResgroupbindtbl3(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addResgroupbindtbl3(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
            addResgroupbindtbl3(this.form2).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row && row.id ? [row.id] : this.ids;
      this.$modal.confirm('是否确认删除所选数据项？').then(function () {
        return delResgroupbindtbl3(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  /** 导出按钮操作 */
  handleExport(){
    this.download('business/resgroupbindtbl3/export', {...this.queryParams}, `resgroupbindtbl3_${parseTime(new Date().getTime())}.xlsx`)
  }
}
};
</script>
<style lang="scss" scoped>
.el-dialog-div{
  height: 30vh;//如果高度过高，可用max-height
  overflow: auto;
}
</style>
