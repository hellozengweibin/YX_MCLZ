<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--机构数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" placeholder="请输入机构名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick"/>
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="" prop="userName">
            <el-input v-model="queryParams.userName" placeholder="用户名/姓名关键字" clearable style="width: 220px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="" prop="phonenumber">
            <el-input v-model="queryParams.phonenumber" placeholder="手机号码" clearable style="width: 220px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="" prop="status">
            <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 150px"  @change="handleQuery">
              <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="" prop="platformType" v-if="platformOptions.length > 1">
            <el-select v-model="queryParams.platformType" placeholder="所属平台类型" :clearable="userID == 1" style="width: 200px" @change="handleQuery">
              <el-option v-for="item in platformOptions" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              @change="handleQuery"
              type="daterange"
              range-separator="-"
              start-placeholder="创建时间起"
              end-placeholder="创建时间止"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:user:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:user:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" ></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
<!--          <el-table-column label="用户编号" align="center" key="userId" prop="userId" />-->
          <el-table-column label="用户名" align="center" key="userName" prop="userName" :show-overflow-tooltip="true"/>
          <el-table-column label="姓名" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属机构" align="center" key="deptName" prop="dept.deptName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属平台" align="center" key="platformType" prop="platformType" v-hasPermi="['system:user:edit']" show-overflow-tooltip>
            <template slot-scope="scope">
              <template v-if="scope.row.platformType">
<!--                <dict-tag-->
<!--                  v-for="(item, index) in scope.row.platformType.split(',')"-->
<!--                  :key="index"-->
<!--                  :options="dict.type.platform_type"-->
<!--                  :value="item"/>-->
                <div v-for="(item, index) in platformOptions" :style="{marginTop: ((index > 0 ) ? '5px': '0')}">
                  <el-tag :key="'tag_' + index"
                          size="small"
                          v-if="scope.row.platformType.indexOf(item.value) != -1"
                          :closable="scope.row.platformType.indexOf(',') != -1"
                          @close="handleUpdateUserPlatform(item,scope.row)">{{item.label}}</el-tag>
                </div>
              </template>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" width="120"/>
          <el-table-column label="状态" align="center" key="status">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :disabled="scope.row.userId === 1"
                @change="handleStatusChange(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']">编辑
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                style="color: red;"
                v-hasPermi="['system:user:remove']">删除
              </el-button>
<!--              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">-->
<!--                <span class="el-dropdown-link">-->
<!--                  <i class="el-icon-d-arrow-right el-icon&#45;&#45;right"></i>更多-->
<!--                </span>-->
<!--                <el-dropdown-menu slot="dropdown">-->
<!--                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key" v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>-->
<!--                  <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check" v-hasPermi="['system:user:edit']">分配角色</el-dropdown-item>-->
<!--                </el-dropdown-menu>-->
<!--              </el-dropdown>-->
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或编辑用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-row>
              <el-col :span="24">
                <el-form-item label="所属平台：" prop="platformTypes">
                  <el-select
                    v-model="form.platformTypes"
                    placeholder="所属平台"
                    clearable
                    multiple
                    collapse-tags
                    style="width: 85%"
                    @change="platformTypeChange">
                    <template v-for="item in platformOptions">
                      <el-option :key="item.value"
                                 :disabled="form.platformTypes && form.platformTypes.length > 0 ? (form.platformTypes.includes(4) ? item.value != 4 : item.value == 4) : false"
                                 :label="item.label"
                                 :value="item.value"/>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item v-if="form.userId == undefined" label="用户名" prop="userName">
                  <el-input v-model="form.userName" placeholder="请输入用户名" maxlength="30" style="width: 85%;"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="姓名" prop="nickName">
                  <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" style="width: 85%;"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="联系电话" prop="phonenumber">
                  <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" style="width: 85%;"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.sex">
                    <el-radio v-for="dict in dict.type.sys_user_sex" :label="dict.label" :key="dict.value">{{dict.label}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
                  <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password style="width: 85%;"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
            <el-col :span="24">
              <el-form-item label="所属机构" prop="deptId">
                <treeselect v-model="form.deptId" :options="deptTreeData" :show-count="true" placeholder="请选择归属机构" @select="handleDeptSelect" style="width: 85%;"/>
              </el-form-item>
            </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="所属角色">
                  <el-select v-model="form.roleIds" multiple placeholder="请选择角色" style="width: 85%;">
                    <el-option
                      v-for="item in roleOptions"
                      :key="item.roleId"
                      :label="item.roleName"
                      :value="item.roleId"
                      :disabled="item.status == 1"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注">
                  <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" style="width: 85%;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="12">
            <div class="head-container">
              <el-input v-model="elTreeFilterName"
                        :placeholder="'请输入' + (form.platformTypes && form.platformTypes.includes(4) ? '摄像头' : '水域') + '名称'"
                        clearable size="small"
                        prefix-icon="el-icon-search"
                        style="width: 90%;"/>
            </div>
            <el-virtual-tree
              :data="treeOptions"
              :props="defaultProps"
              ref="videoTree"
              node-key="nodeKey"
              height="480px"
              show-checkbox
              default-expand-all
              :filter-node-method="filterNodeWater"
              :default-checked-keys="defaultCheckedKeys"
              @check="nodeCheck">
              <span slot-scope="{ node, data }" class="center-content">
                <span class="overtext"> {{ node.label }} </span>
              </span>
            </el-virtual-tree>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  addUser,
  changeUserStatus,
  delUser,
  getUser,
  getUserPlatform,
  listUser,
  resetUserPwd,
  updateUser, updateUserPlatform
} from "@/api/system/user";
import {treeselect} from "@/api/system/dept";
import {roleSelectOptions} from "@/api/system/role";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex'

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex','platform_type'],
  components: {Treeselect, ElVirtualTree: () => import('@/components/el-virtual-tree')},
  computed: {
    ...mapGetters(['userType', 'userID']),
  },
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 机构树选项
      deptOptions: undefined,
      deptTreeData: [],
      // 是否显示弹出层
      open: false,
      // 机构名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      currentPlatformType: 2,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        platformType: null,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 表单校验
      rules: {
        userName: [
          {required: true, message: "用户名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur'}
        ],
        nickName: [
          {required: true, message: "用户昵称不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "用户密码不能为空", trigger: "blur"},
          {min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur'}
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      defaultCheckedKeys: [],
      // 水域/设备树选项
      treeOptions: [],
      elTreeFilterName: undefined,
      platformOptions: [], // 平台类型选项列表
    };
  },
  watch: {
    // 根据名称筛选机构树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
    elTreeFilterName(val) {
      this.$refs.videoTree.filter(val);
    },

  },
  created() {
     this.getPlatformList()
  },
  mounted() {
     this.$nextTick(() => {
      this.getTreeselect(null,1);
      this.getConfigKey("sys.user.initPassword").then(response => {
        this.initPassword = response.data;
      });
    })
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询机构下拉树结构 */
    getTreeselect(query,type) {
      treeselect(query).then(response => {
        switch (type) {
          case 1:
            this.deptOptions = response.data;
            break
          case 2:
            this.deptTreeData = response.data
            break
        }
      });
    },
    getPlatformList(){
      getUserPlatform().then(res => {
        if(res && res.code === 200) {
          this.platformOptions = res.data
          if(this.platformOptions.length > 0) {
            for (let item of this.platformOptions) {
              if(item.isCurrentLoginPlatform === true) {
                this.currentPlatformType = item.value
                this.queryParams.platformType = item.value
                this.getList()
                break
              }
            }
          } else {
            this.getList();
          }
        }
      }).catch(() => {
        this.getList();
      })
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 筛选节点
    filterNodeWater(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 用户状态编辑
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function () {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: '',
        nickName: '',
        password: '',
        phonenumber: '',
        email: undefined,
        sex: '0',
        status: "0",
        remark: undefined,
        roleIds: [],
        waterAreaIds: [],
        bindDevs: [],
        platformTypes: [this.currentPlatformType]
      };
      this.resetForm("form");
      this.deptTreeData = []
      this.treeOptions = []
      this.form.waterAreaIds = [];
      this.defaultCheckedKeys = [];
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    platformTypeChange(value){
      const deptQuery = {}
      if(value && value.length > 0) {
        if(this.form.platformTypes.includes(4)) {
          deptQuery.platformType = 4
        } else {
          deptQuery.platformType = value[0]
        }
      }
      this.form.deptId = null
      this.form.platformType = this.form.platformTypes.sort().join(',')
      this.getRoleList(value)
      this.getTreeselect(deptQuery,2)
      // 平台类型改变时将树置为空，重新获取
      this.treeOptions = []
      // 清空绑定的数据
      this.form.waterAreaIds = []
      this.form.bindDevs = []
    },
    getRoleList(platformType) {
      const queryParams = {}
      if (platformType) {
        queryParams.platformTypes = platformType.join(',')
      }
      roleSelectOptions(queryParams).then((response) => {
        this.roleOptions = response.data
      })
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect({platformType: this.currentPlatformType},2);
      this.getRoleList(this.form.platformTypes)
      this.open = true;
      this.title = "添加用户";
      this.form.password = this.initPassword;
      // getUser().then(response => {
      //   const {roles} = response.data
      //   this.roleOptions = roles;
      // });
    },
    handleUpdateUserPlatform(item,row) {
      console.log('handleUpdateUserPlatform',item,row)
      let platformType = row.platformType.split(',')
      const newPlatformTypeList = []
      for (let type of platformType) {
        if(item.value == parseInt(type)) {
          continue
        }
        newPlatformTypeList.push(type)
      }

      const param = {
        id: row.userId,
        platformTypeList: newPlatformTypeList
      }
      updateUserPlatform(param).then(res => {
        if(res && res.code === 200) {
          row.platformType = newPlatformTypeList.join(',')
          this.$modal.msgSuccess('操作成功')
        }
      })
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        const {roles, roleIds, data, waterAreaIds,bindDevs} = response.data
        this.form = data;
        this.form.platformTypes = data.platformType.split(',').map(item => parseInt(item))
        this.getRoleList(this.form.platformTypes)
        // this.roleOptions = roles;
        this.form.roleIds = roleIds;
        this.form.waterAreaIds = waterAreaIds;
        this.form.bindDevs = bindDevs;
        const node={};
        node.id=this.form.deptId;
        this.handleDeptSelect(node,null);
        // this.getWaterAreaTreeSelect(this.form.deptId);
        this.open = true;
        this.title = "编辑用户";
        this.form.password = "";
        const deptQuery = {}
        if(this.form.platformTypes.includes(4)) {
          deptQuery.platformType = 4
        } else {
          deptQuery.platformType = 2
        }
        this.getTreeselect(deptQuery,2)

        if (this.form.waterAreaIds) {
          this.form.waterAreaIds.forEach((item, index) => {
            this.defaultCheckedKeys.push(3 + '_' + item);
          });
          setTimeout(() => {
            this.$refs.videoTree.setCheckedKeys(this.defaultCheckedKeys);
          }, 1000)
        }
        if (this.form.bindDevs&&this.form.bindDevs.length>0) {

          this.form.bindDevs.forEach((item, index) => {
            let type;
            if(item.devType===3){
              type=5;
            }else if(item.devType===1){
              type=6;
            }else if(item.devType===2){
              type=7;
            }
            debugger
            this.defaultCheckedKeys.push(type + '_' + item.devId);
          });

          setTimeout(() => {
            this.$refs.videoTree.setCheckedKeys(this.defaultCheckedKeys);
          }, 1000)
        }
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      }).then(({value}) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess("编辑成功，新密码是：" + value);
        });
      }).catch(() => {
      });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      if(this.form.platformTypes.includes(4)) {
        this.form.platformTypes = [4]
        this.form.userType = '01'
      } else {
        this.form.userType = '00'
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
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
      const userIds = row && row.userId ? [row.userId] : this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function () {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    handleDeptSelect(node, instanceId) {
      if(this.form.platformTypes && this.form.platformTypes.length > 0) {
        if(this.form.platformTypes.includes(4)) {
          // 选择了运维管理系统时，查询设备树，否则查询水域
          this.getDeviceTreeSelect(node.id)
        } else {
          this.getWaterAreaTreeSelect(node.id);
        }
      } else {
        this.getWaterAreaTreeSelect(node.id);
      }
    },
    nodeCheck(data, checked) {
      if(this.form.platformTypes && this.form.platformTypes.includes(4)) {
        // 运维机构绑定设备
        const bindDevs = [];
        checked.checkedNodes.forEach((item, index) => {
          if (item.type === 5||item.type === 6||item.type === 7) {
            let type;
            if(item.type===5){
              type=3;
            }else if(item.type===6){
              type=1;
            }else if(item.type===7){
              type=2;
            }
            const dev= {

              devType: type,
              devId: item.id
            }
            bindDevs.push(dev);
          }
        });
        this.form.bindDevs = bindDevs
      } else {
        // 绑定水域
        const waterAreaIds = [];
        checked.checkedNodes.forEach((item, index) => {
          if (item.type === 3) {
            waterAreaIds.push(item.id);
          }
        });
        this.form.waterAreaIds = waterAreaIds
      }
    },
  }
};
</script>
