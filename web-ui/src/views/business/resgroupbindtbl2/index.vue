<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="resgroupid">
        <el-input v-model="queryParams.resgroupid" placeholder="请输入ResGroupID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="bindresid">
        <el-input v-model="queryParams.bindresid" placeholder="请输入BindResId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['business:resgroupbindtbl2:add']">新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="success" plain icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['business:resgroupbindtbl2:edit']">修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['business:resgroupbindtbl2:remove']">删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['business:resgroupbindtbl2:export']">导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="resgroupbindtbl2List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="ResGroupID" align="center" prop="resgroupid"/>
      <el-table-column label="BindResId" align="center" prop="bindresid"/>
      <el-table-column label="BindResType" align="center" prop="bindrestype"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:resgroupbindtbl2:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: red;" @click="handleDelete(scope.row)" v-hasPermi="['business:resgroupbindtbl2:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改resgroupbindtbl2对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="ResGroupID" prop="resgroupid">
            <el-input v-model="form.resgroupid" placeholder="请输入ResGroupID"/>
          </el-form-item>
          <el-form-item label="BindResId" prop="bindresid">
            <el-input v-model="form.bindresid" placeholder="请输入BindResId"/>
          </el-form-item>
        <el-form-item label="BindResType" prop="bindrestype">
          <el-input v-model="form.bindrestype" placeholder="请输入BindResType"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResgroupbindtbl2, getResgroupbindtbl2, delResgroupbindtbl2, addResgroupbindtbl2, updateResgroupbindtbl2 } from "@/api/business/resgroupbindtbl2";
import {parseTime} from "@/utils/util";
export default {
  name: "Resgroupbindtbl2",
  data() {
    return {
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
      // resgroupbindtbl2表格数据
      resgroupbindtbl2List: [],
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
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询resgroupbindtbl2列表 */
    getList() {
      this.loading = true;
      listResgroupbindtbl2(this.queryParams).then(response => {
        this.resgroupbindtbl2List = response.rows;
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
      this.title = "添加resgroupbindtbl2";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getResgroupbindtbl2(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改resgroupbindtbl2";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id!=null){
              updateResgroupbindtbl2(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addResgroupbindtbl2(this.form).then(response => {
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
        return delResgroupbindtbl2(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  /** 导出按钮操作 */
  handleExport(){
    this.download('business/resgroupbindtbl2/export', {...this.queryParams}, `resgroupbindtbl2_${parseTime(new Date().getTime())}.xlsx`)
  }
}
};
</script>
