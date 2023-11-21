<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="devpubid">
        <el-input v-model="queryParams.devpubid" placeholder="请输入一级设备账号；" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item prop="nickname">-->
<!--        <el-input v-model="queryParams.nickname" placeholder="请输入通道昵称；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="alive">-->
<!--        <el-input v-model="queryParams.alive" placeholder="请输入Alive" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="channel">-->
<!--        <el-input v-model="queryParams.channel" placeholder="请输入通道数；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="capability">-->
<!--        <el-input v-model="queryParams.capability" placeholder="请输入能力" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="chanpubid">-->
<!--        <el-input v-model="queryParams.chanpubid" placeholder="请输入ChanPubID" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="dmarker">-->
<!--        <el-input v-model="queryParams.dmarker" placeholder="请输入DMarker" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="storageid">-->
<!--        <el-input v-model="queryParams.storageid" placeholder="请输入StorageID" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="platid">-->
<!--        <el-input v-model="queryParams.platid" placeholder="请输入PlatID" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['business:channeltbl:add']">新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="success" plain icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['business:channeltbl:edit']">修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['business:channeltbl:remove']">删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['business:channeltbl:export']">导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="channeltblList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="自增ID；" align="center" prop="id"/>
      <el-table-column label="一级设备账号；" align="center" prop="devpubid"/>
      <el-table-column label="通道昵称；" align="center" prop="nickname"/>
      <el-table-column label="二级设备类型；" align="center" prop="type"/>
      <el-table-column label="Alive" align="center" prop="alive"/>
      <el-table-column label="子类型；" align="center" prop="subtype"/>
      <el-table-column label="通道数；" align="center" prop="channel"/>
      <el-table-column label="能力" align="center" prop="capability"/>
      <el-table-column label="ChanPubID" align="center" prop="chanpubid"/>
      <el-table-column label="DMarker" align="center" prop="dmarker">
        <template slot-scope="scope">
              <dict-tag :options="dict.type.DMarker" :value="scope.row.dmarker"/>
        </template>
      </el-table-column>
      <el-table-column label="StorageID" align="center" prop="storageid"/>
      <el-table-column label="PlatID" align="center" prop="platid"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:channeltbl:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: red;" @click="handleDelete(scope.row)" v-hasPermi="['business:channeltbl:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改创建二级设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="一级设备账号；" prop="devpubid">
            <el-input v-model="form.devpubid" placeholder="请输入一级设备账号；"/>
          </el-form-item>
          <el-form-item label="通道昵称；" prop="nickname">
            <el-input v-model="form.nickname" placeholder="请输入通道昵称；"/>
          </el-form-item>
          <el-form-item label="Alive" prop="alive">
            <el-input v-model="form.alive" placeholder="请输入Alive"/>
          </el-form-item>
          <el-form-item label="通道数；" prop="channel">
            <el-input v-model="form.channel" placeholder="请输入通道数；"/>
          </el-form-item>
          <el-form-item label="能力" prop="capability">
            <el-input v-model="form.capability" placeholder="请输入能力"/>
          </el-form-item>
          <el-form-item label="ChanPubID" prop="chanpubid">
            <el-input v-model="form.chanpubid" placeholder="请输入ChanPubID"/>
          </el-form-item>
          <el-form-item label="DMarker" prop="dmarker">
            <el-input v-model="form.dmarker" placeholder="请输入DMarker"/>
          </el-form-item>
          <el-form-item label="StorageID" prop="storageid">
            <el-input v-model="form.storageid" placeholder="请输入StorageID"/>
          </el-form-item>
          <el-form-item label="PlatID" prop="platid">
            <el-input v-model="form.platid" placeholder="请输入PlatID"/>
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
import { listChanneltbl, getChanneltbl, delChanneltbl, addChanneltbl, updateChanneltbl } from "@/api/business/channeltbl";
import {parseTime} from "@/utils/util";
export default {
  name: "Channeltbl",
  dicts: ['DMarker'],
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
      // 创建二级设备表格数据
      channeltblList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        devpubid: null,
        nickname: null,
        type: null,
        alive: null,
        subtype: null,
        channel: null,
        capability: null,
        chanpubid: null,
        dmarker: null,
        storageid: null,
        platid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
              devpubid: [
          {
            required: true, message: "一级设备账号；不能为空", trigger: "blur" }
        ],
              chanpubid: [
          {
            required: true, message: "ChanPubID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询创建二级设备列表 */
    getList() {
      this.loading = true;
      listChanneltbl(this.queryParams).then(response => {
        this.channeltblList = response.rows;
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
        devpubid: null,
        nickname: null,
        type: null,
        alive: null,
        subtype: null,
        channel: null,
        capability: null,
        chanpubid: null,
        dmarker: null,
        storageid: null,
        platid: null
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
      this.title = "添加创建二级设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getChanneltbl(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改创建二级设备";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id!=null){
              updateChanneltbl(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addChanneltbl(this.form).then(response => {
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
        return delChanneltbl(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  /** 导出按钮操作 */
  handleExport(){
    this.download('business/channeltbl/export', {...this.queryParams}, `channeltbl_${parseTime(new Date().getTime())}.xlsx`)
  }
}
};
</script>
