<!--suppress ALL -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="机构名称" prop="deptName">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入机构名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="机构编码" prop="deptCode">
        <el-input
          v-model="queryParams.deptCode"
          placeholder="请输入机构编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="机构简称" prop="abbreviation">
        <el-input
          v-model="queryParams.abbreviation"
          placeholder="请输入机构简称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域名字" prop="areaName">
        <el-input
          v-model="queryParams.areaName"
          placeholder="请输入区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="机构级别" prop="deptLevel">
        <el-select v-model="queryParams.deptLevel" placeholder="机构级别" clearable>
          <el-option
            v-for="dict in dict.type.dept_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dept:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="deptList"
      row-key="deptId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="deptName" label="机构名称" ></el-table-column>
      <el-table-column prop="deptCode" label="机构编码" width="100"></el-table-column>
      <el-table-column prop="abbreviation" label="机构简称" width="150"></el-table-column>
      <el-table-column prop="deptLevel" label="机构级别" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dept_level" :value="scope.row.deptLevel"/>
        </template>
      </el-table-column>
      <el-table-column prop="areaName" label="区域" width="100"></el-table-column>
      <el-table-column prop="parentName" label="上级机构" ></el-table-column>
      <el-table-column prop="description" label="机构描述" ></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dept:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:dept:add']"
          >新增
          </el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dept:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改机构对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-input v-model="form.platformType"  value="2" v-if="false"/>
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级机构" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级机构"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入机构名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构编码" prop="deptCode">
              <el-input v-model="form.deptCode" placeholder="请输入机构编码"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="机构简称" prop="abbreviation">
              <el-input v-model="form.abbreviation" placeholder="请输入机构描述"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构级别">
              <el-select v-model="form.deptLevel">
                <el-option
                  v-for="dict in dict.type.dept_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col >
            <el-form-item label="选择区域" prop="areaId">
              <treeselect v-model="form.areaId" :options="areaOptions" :normalizer="normalizer1" placeholder="选择区域"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="机构描述" prop="remark">
              <el-input type="textarea" v-model="form.description" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col>
            <el-form-item label="经纬度" prop="latitude">
              <span>{{form.longitude}},{{form.latitude}}</span>
              <el-button @click="showMapView">定位</el-button>
            </el-form-item>
            <point-select-dialog ref="pointSelectDialog" @confirmCllick="bMapPointSelect" />
          </el-col>
        </el-row> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addDept, delDept, getDept, listDept, listDeptExcludeChild, updateDept} from "@/api/system/dept";
import {listArea, listAreaExcludeChild} from "@/api/system/area";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Dept",
  dicts: ['sys_normal_disable','dept_level'],
  components: {
    Treeselect,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 机构树选项
      deptOptions: [],
      // 区域树选项
      areaOptions: [],
      areaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        deptName: undefined,
        deptLevel: undefined,
        deptCode: undefined,
        areaId: undefined,
        areaName: undefined,
        abbreviation: undefined,
        platformType: 2,
        longitude: null,
        latitude: null
      },
      // 表单参数
      form: {
        longitude: null,
        latitude: null
      },
      // 表单校验
      rules: {
        parentId: [
          {required: true, message: "上级机构不能为空", trigger: "blur"}
        ],
        deptName: [
          {required: true, message: "机构名称不能为空", trigger: "blur"}
        ],
        orderNum: [
          {required: true, message: "显示排序不能为空", trigger: "blur"}
        ],
        deptCode: [
          {required: true, message: "机构编码不能为空", trigger: "blur"}
        ]
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询机构列表 */
    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
      });
    },
    /** 转换机构数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    /** 转换区域数据结构 */
    normalizer1(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.areaId,
        label: node.areaName,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        parentId: undefined,
        deptName: undefined,
        deptLevel: undefined,
        deptCode: undefined,
        areaId: undefined,
        areaName: undefined,
        abbreviation: undefined,
        platformType: 2,
        longitude: undefined,
        latitude: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.deptId;
      }
      this.open = true;
      this.title = "添加机构";
      listDept({platformType:2}).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
      listArea().then(response => {
        this.areaList = response.data
        this.areaOptions = this.handleTree(response.data, "areaId");
    });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getDept(row.deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改机构";
      });
      listDeptExcludeChild(row.deptId).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
      listArea().then(response => {
        this.areaList = response.data
        this.areaOptions = this.handleTree(response.data, "areaId");
    });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        console.log(3333, this.form)
        if (valid) {
          this.form.areaName = this.getAreaName(this.form.areaId)
          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名称为"' + row.deptName + '"的数据项？').then(function () {
        return delDept(row.deptId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    getAreaName(areaId){
      var areaArr = [];
      var ancestors = ""
      try {
        this.areaList.forEach(function (item,index) {
          if(item.areaId == areaId) {
            areaArr.push(item.areaName)
            ancestors = item.ancestors
            throw new Error(areaId);
          }
        })
      }catch(e) {
        if(e.message!= areaId) throw e;
      }
      var ancestorsArr = ancestors.split(",")
      if(ancestorsArr.length > 2){
        for(var i = ancestorsArr.length -2; i > 0; i--){
          try {
            this.areaList.forEach(function (item,index) {
              if(item.areaId == ancestorsArr[i]) {
                areaArr.unshift(item.areaName)
                throw new Error(areaId);
              }
            })
          }catch(e) {
            if(e.message!= areaId) throw e;
          }
        }
      }
      return areaArr.join("/")
    },
    // showMapView() {
    //   this.$refs.pointSelectDialog.show()
    // },
    bMapPointSelect(res) {
      this.$set(this.form , 'longitude',  res[0].toFixed(6))
      this.$set(this.form , 'latitude',  res[1].toFixed(6))
    },
  }
};
</script>

<style scoped lang="scss">

</style>
