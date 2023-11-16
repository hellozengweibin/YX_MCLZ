<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="菜单状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="所属平台" prop="platformType" v-if="userID != 1 && platformOptions.length > 1">
        <el-select v-model="queryParams.platformType" :clearable="userID == 1" placeholder="选择平台类型" style="width: 200px" @change="handleQuery">
          <el-option v-for="item in platformOptions" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:menu:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-copy-document" size="mini" @click="openCopy" v-has-role="['role']">复制</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="menuTable" v-if="refreshTable" v-loading="loading" :data="menuList" row-key="menuId" :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :indent="14" @expand-change="expandChange">
      <el-table-column prop="menuName" label="菜单名称" show-overflow-tooltip min-width="80" align="left"></el-table-column>
      <el-table-column prop="platformType" label="平台类型" width="170" align="center">
        <template slot-scope="scope">
          <el-select v-if="scope.row.menuId > 0" v-model="scope.row.platformType"  @change="handleUpdatePlatform(scope.row)">
            <el-option v-for="item in platformOptions" :key="item.value" :value="item.value" :label="item.label" />
          </el-select>
          <dict-tag v-else :options="dict.type.platform_type" :value="scope.row.platformType" />
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" align="center" width="110">
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.icon" :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="135" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.menuId > 0">
            <el-link type="primary" v-show="showSortUpdate==false" @click="showSortUpdate=true" :underline="false">{{scope.row.orderNum}}</el-link>
            <el-input-number v-show="showSortUpdate" v-model="scope.row.orderNum" size="small" @change="handleSortChange(scope.row)" :min="1" :max="100" style="width: 110px;" />
          </template>
          <span v-else>{{scope.row.orderNum}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="perms" label="权限标识" show-overflow-tooltip></el-table-column>
<!--      <el-table-column prop="component" label="组件路径" show-overflow-tooltip></el-table-column>-->
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-switch
            v-if="scope.row.menuId > 0"
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"></el-switch>
          <dict-tag v-else :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
                     type="text"
                     icon="el-icon-edit"
                     v-if="scope.row.menuId > 0"
                     @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:menu:edit']">修改</el-button>
          <el-button size="mini"
                     type="text"
                     icon="el-icon-plus"
                     @click="handleAdd(scope.row)"
                     v-hasPermi="['system:menu:add']">新增</el-button>
          <el-button size="mini"
                     type="text"
                     icon="el-icon-copy-document"
                     @click="openCopy(scope.row)"
                     style="color: #ffba00;"
                     v-has-role="['admin']">复制</el-button>
          <el-button size="mini"
                     type="text"
                     icon="el-icon-delete"
                     v-if="scope.row.menuId > 0"
                     @click="handleDelete(scope.row)"
                     v-hasPermi="['system:menu:remove']"
                     style="color: red;">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body :close-on-click-modal="false">
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="选择上级菜单"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="所属平台" prop="platformType">
              <el-radio-group v-model="form.platformType" size="small" v-if="form.parentId ==0">
                <el-radio-button v-for="item in platformOptions" :label="item.value">{{item.label}}</el-radio-button>
              </el-radio-group>
              <dict-tag v-else :options="dict.type.platform_type" :value="form.platformType" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType" size="small">
                <el-radio-button label="M">目录</el-radio-button>
                <el-radio-button label="C">菜单</el-radio-button>
                <el-radio-button label="F">按钮</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.menuType != 'F'">
            <el-form-item label="菜单图标" prop="icon">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selected"/>
                <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                  <svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon" class="el-input__icon" style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <span slot="label">
                <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                是否外链
              </span>
              <el-radio-group v-model="form.isFrame" size="mini">
                <el-radio-button label="0">是</el-radio-button>
                <el-radio-button label="1">否</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="path">
              <span slot="label">
                <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                路由地址
              </span>
              <el-input v-model="form.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
              <el-input v-model="form.component" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'M'">
            <el-form-item>
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100"/>
              <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限字符
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item>
              <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255"/>
              <span slot="label">
                <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由参数
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item>
              <span slot="label">
                <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                是否缓存
              </span>
              <el-radio-group v-model="form.isCache" size="mini">
                <el-radio-button label="0">缓存</el-radio-button>
                <el-radio-button label="1">不缓存</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <span slot="label">
                <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                显示状态
              </span>
              <el-radio-group v-model="form.visible" size="mini">
                <el-radio-button
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item>
              <span slot="label">
                <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                菜单状态
              </span>
              <el-radio-group v-model="form.status" size="mini">
                <el-radio-button
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="复制菜单" :visible.sync="copyOpen" width="680px" append-to-body>
      <el-divider />
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="100px">
        <el-form-item label="复制到">
          <treeselect
            v-model="copyForm.parentId"
            :options="menuOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="选择目标菜单"/>
        </el-form-item>
        <el-form-item label="目标平台" prop="platformType">
          <el-radio-group v-model="copyForm.platformType" size="small">
            <el-radio-button v-for="item in platformOptions" :label="item.value">{{item.label}}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="复制菜单" prop="menuIds">
<!--          <el-checkbox v-model="copyForm.menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>-->
          <el-checkbox v-model="copyForm.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox>
<!--          <el-tree-->
<!--            class="tree-border"-->
<!--            :data="menuTreeOptions"-->
<!--            show-checkbox-->
<!--            ref="menu"-->
<!--            node-key="id"-->
<!--            :check-strictly="!copyForm.menuCheckStrictly"-->
<!--            empty-text="加载中，请稍候"-->
<!--            :props="defaultProps"></el-tree>-->
          <treeselect v-model="copyForm.menuIds"
                      :options="menuOptions"
                      :normalizer="normalizer"
                      :show-count="true"
                      :autoSelectDescendants="true"
                      :limit="4"
                      :flat="!copyForm.menuCheckStrictly"
                      :multiple="true"
                      :default-expand-level="2"
                      placeholder="选择菜单"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleCopy">确 定</el-button>
        <el-button @click="copyOpen=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addMenu,
  changeMenuStatus,
  copyMenu,
  delMenu,
  getMenu,
  listMenu, treeselect as menuTreeselect,
  updateMenu,
  updatePlatform
} from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";
import {getUserPlatform} from "@/api/system/user";
import {mapGetters} from "vuex";

export default {
  name: "Menu",
  dicts: ['sys_show_hide', 'sys_normal_disable','platform_type'],
  components: {Treeselect, IconSelect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      menuTreeOptions: [],
      platformOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      copyOpen: false,
      copyForm: {
        parentId: null,
        platformType: null,
        menuCheckStrictly: null,
        menuExpand: false,
        menuNodeAll: false,
        menuIds: []
      },
      // 是否展开，默认全部折叠
      isExpandAll: false,
      expandIds: [],
      // 重新渲染表格状态
      refreshTable: true,
      showSortUpdate: false,
      // 查询参数
      queryParams: {
        menuName: undefined,
        visible: undefined,
        platformType: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        menuName: [
          {required: true, message: "菜单名称不能为空", trigger: "blur"}
        ],
        orderNum: [
          {required: true, message: "菜单顺序不能为空", trigger: "blur"}
        ],
        path: [
          {required: true, message: "路由地址不能为空", trigger: "blur"}
        ]
      },
      copyRules: {
        platformType: [
          {required: true, message: "平台类型不能为空", trigger: "blur"}
        ],
        parentId: [
          {required: true, message: "目标菜单不能为空", trigger: "blur"}
        ],
        menuIds: [
          {required: true, message: "复制菜单不能为空", trigger: "blur"}
        ],
      }
    };
  },
  computed: {
    ...mapGetters(['userType', 'userID']),
    menuTreeData: function() {
      return this.menuTreeOptions.filter(item => item.id == (0 - this.copyForm.platformType))
    }
  },
  created() {
    this.getPlatformList()
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name;
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true;
      this.expandIds = []
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, "menuId");
        this.loading = false;
      });
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      };
    },
    expandChange(row,expanded){
      const menuId = row.menuId
      if(expanded && !this.expandIds.includes(menuId)) {
        this.expandIds.push(menuId)
      } else if(!expanded && this.expandIds.includes(menuId)) {
        this.expandIds.splice(this.expandIds.indexOf(menuId),1)
      }
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = {menuId: 0, menuName: '主类目', children: []};
        menu.children = this.handleTree(response.data, "menuId");
        this.menuOptions.push(menu);
      });
    },
    getPlatformList(){
      getUserPlatform().then(res => {
        if(res && res.code === 200) {
          this.platformOptions = res.data
          console.log('--------',this.userID)
          if(this.platformOptions.length > 1&& this.userID != 1) {
            for (let item of this.platformOptions) {
              if(item.isCurrentLoginPlatform === true) {
                this.queryParams.platformType = item.value
                break
              }
            }
          }
        }
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: null,
        menuType: "M",
        orderNum: undefined,
        isFrame: "1",
        isCache: "0",
        visible: "0",
        status: "0",
        platformType: this.platformOptions.filter(item => item.isCurrentLoginPlatform === true)[0].value
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
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId
        // this.form.parentId = row.menuId > 0 ? row.menuId : 0;
        if(row.platformType) {
          this.form.platformType = row.platformType
        }
      } else {
        this.form.parentId = 0;
        if(this.queryParams.platformType) {
          this.form.platformType = this.queryParams.platformType
        }
      }
      this.open = true;
      this.title = "添加菜单";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    handleUpdatePlatform(row){
      console.log(row)
      const param = {
        id: row.menuId ,
        platformType: row.platformType
      }
      console.log('handleUpdatePlatform',param)
      const that = this
      updatePlatform(param).then(res => {
        if(res && res.code === 200) {
          this.$modal.msgSuccess(`${row.menuName}菜单平台类型修改成功`);
          if(row.children) {
            that.$nextTick(() => {
              this.$modal.confirm(`是否同步修改[${row.menuName}]子菜单的平台类型？`).then(function () {
                row.children.forEach(item => {
                  if(item.platformType != row.platformType) {
                    item.platformType = row.platformType
                    that.handleUpdatePlatform(item)
                  }
                })
              })
            })
          }
        } else {
          this.$modal.msgError(res.msg)
          this.getList();
        }
      }).catch(() => {
        this.getList();
      });
    },
    handleStatusChange(row) {
      const that = this
      let text = row.status === "0" ? "启用" : "停用";
      that.$modal.confirm(`确认要[${text}][${row.menuName}]菜单吗？`).then(function () {
          that.updateMenuStatus(text,row)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    updateMenuStatus(text,row){
      const that = this
      changeMenuStatus(row.menuId,row.status).then(res => {
        this.$modal.msgSuccess(`[${row.menuName}]菜单[${text}]成功`);
        if(res.code === 200 && row.children) {
          that.$modal.confirm(`是否同步${text}[${row.menuName}]的子菜单`).then(() => {
            row.children.forEach(item => {
              if(item.status != row.status) {
                item.status = row.status
                that.updateMenuStatus(text,item)
              }
            })
          })
        }
      })
    },
    handleSortChange(row){
      changeMenuStatus(row.menuId, row.status,row.orderNum).then(res => {
        if(res && res.code === 200) {
          this.showSortUpdate = false
          this.$modal.msgSuccess("排序修改成功");
          this.$nextTick(() => {
            this.getList()
            if(this.expandIds.length > 0) {
              this.expandIds.sort().forEach(menuId => {
                this.$refs['menuTable'].toggleRowExpansion({menuId: menuId},true)
              })
            }
          })
        }
      }).catch(() => {
        this.getList()
        if(this.expandIds.length > 0) {
          this.expandIds.sort().forEach(menuId => {
            this.$refs['menuTable'].toggleRowExpansion({menuId: menuId},true)
          })
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getMenu(row.menuId).then(response => {
        this.form = response.data;
        if(row.parentId < 0 && this.form.parentId == 0) this.form.parentId = row.parentId
        this.open = true;
        this.title = "修改菜单";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              if(this.form.platformType != this.queryParams.platformType) {
                this.queryParams.platformType = this.form.platformType
              }
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名称为"' + row.menuName + '"的数据项？').then(function () {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 查询菜单树结构 */
    getMenuTreeselect(query) {
      menuTreeselect(query).then(response => {
        this.menuTreeOptions = response.data;
      });
    },
    /** 新增按钮操作 */
    openCopy(row) {
      this.copyForm = {
        parentId: null,
        platformType: null,
        menuCheckStrictly: false,
        menuExpand: false,
        menuNodeAll: false,
        menuIds: []
      }
      this.getTreeselect()
      if (row != null && row.menuId) {
        this.copyForm.parentId = row.menuId
        if(row.platformType) {
          this.copyForm.platformType = row.platformType
        }
      } else {
        this.copyForm.parentId = 0;
        if(this.queryParams.platformType) {
          this.copyForm.platformType = this.queryParams.platformType
        }
      }
      // this.getMenuTreeselect({platformType: this.copyForm.platformType});
      this.copyOpen = true;
    },
    // 树权限（展开/折叠）
    // handleCheckedTreeExpand(value, type) {
    //   if (type == 'menu') {
    //     let treeList = this.menuOptions;
    //     for (let i = 0; i < treeList.length; i++) {
    //       this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
    //     }
    //   }
    // },
    handleCopy(){
      this.$refs["copyForm"].validate(valid => {
        if (valid) {
          copyMenu(this.copyForm.platformType,this.copyForm.parentId,this.copyForm.menuIds).then(res => {
            if(res && res.code === 200) {
              this.$modal.msgSuccess('菜单复制成功')
              this.copyOpen = false
              this.getList()
            }
          }).catch(() => {
            this.copyOpen = false
            this.getList()
          })
        }
      })
    },
    // 树权限（全选/全不选）
    // handleCheckedTreeNodeAll(value, type) {
    //   if (type == 'menu') {
    //     this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
    //   }
    // },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.copyForm.menuCheckStrictly = value ? true : false;
      } else if (type == 'dept') {
        this.copyForm.deptCheckStrictly = value ? true : false;
      }
    },
  }
};
</script>
