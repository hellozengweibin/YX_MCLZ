<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="placeId">
        <el-input v-model="queryParams.placeId" placeholder="请输入关联车间id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入通道编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入名称/别名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="ip">
        <el-input v-model="queryParams.ip" placeholder="请输入ip" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="piplineStart">
        <el-input v-model="queryParams.piplineStart" placeholder="请输入流水线起始端" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="piplineEnd">
        <el-input v-model="queryParams.piplineEnd" placeholder="请输入流水线结束端" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="online">
        <el-input v-model="queryParams.online" placeholder="请输入在线状态" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="positionLeft">
        <el-input v-model="queryParams.positionLeft" placeholder="请输入落点地图left" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item prop="positionTop">
        <el-input v-model="queryParams.positionTop" placeholder="请输入落点地图right" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['business:route:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['business:route:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['business:route:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['business:route:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="routeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id"/>
      <el-table-column label="关联车间id" align="center" prop="placeId"/>
      <el-table-column label="通道编号" align="center" prop="code"/>
      <el-table-column label="名称/别名" align="center" prop="name"/>
      <el-table-column label="ip" align="center" prop="ip"/>
      <el-table-column label="流水线起始端" align="center" prop="piplineStart"/>
      <el-table-column label="流水线结束端" align="center" prop="piplineEnd"/>
      <el-table-column label="在线状态" align="center" prop="online"/>
      <el-table-column label="落点地图left" align="center" prop="positionLeft"/>
      <el-table-column label="落点地图right" align="center" prop="positionTop"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:route:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: red;" @click="handleDelete(scope.row)" v-hasPermi="['business:route:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改通道对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="关联车间id" prop="placeId">
          <el-input v-model="form.placeId" placeholder="请输入关联车间id"/>
        </el-form-item>
        <el-form-item label="通道编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入通道编号"/>
        </el-form-item>
        <el-form-item label="名称/别名" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称/别名"/>
        </el-form-item>
        <el-form-item label="ip" prop="ip">
          <el-input v-model="form.ip" placeholder="请输入ip"/>
        </el-form-item>
        <el-form-item label="流水线起始端" prop="piplineStart">
          <el-input v-model="form.piplineStart" placeholder="请输入流水线起始端"/>
        </el-form-item>
        <el-form-item label="流水线结束端" prop="piplineEnd">
          <el-input v-model="form.piplineEnd" placeholder="请输入流水线结束端"/>
        </el-form-item>
        <el-form-item label="在线状态" prop="online">
          <el-input v-model="form.online" placeholder="请输入在线状态"/>
        </el-form-item>
        <el-form-item label="落点地图left" prop="positionLeft">
          <el-input v-model="form.positionLeft" placeholder="请输入落点地图left"/>
        </el-form-item>
        <el-form-item label="落点地图right" prop="positionTop">
          <el-input v-model="form.positionTop" placeholder="请输入落点地图right"/>
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
import { listRoute, getRoute, delRoute, addRoute, updateRoute } from "@/api/business/route";
import {parseTime} from "@/utils/util";
export default {
  name: "Route",
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
      // 通道表格数据
      routeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        placeId: null,
        code: null,
        name: null,
        ip: null,
        piplineStart: null,
        piplineEnd: null,
        online: null,
        positionLeft: null,
        positionTop: null,
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
    /** 查询通道列表 */
    getList() {
      this.loading = true;
      listRoute(this.queryParams).then(response => {
        this.routeList = response.rows;
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
        placeId: null,
        code: null,
        name: null,
        ip: null,
        piplineStart: null,
        piplineEnd: null,
        online: null,
        positionLeft: null,
        positionTop: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.title = "添加通道";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getRoute(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改通道";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id!=null){
            updateRoute(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRoute(this.form).then(response => {
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
        return delRoute(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport(){
      this.download('business/route/export', {...this.queryParams}, `route_${parseTime(new Date().getTime())}.xlsx`)
    }
  }
};
</script>
