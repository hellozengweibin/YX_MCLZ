<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" />
    <!-- <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper"> -->
    <el-menu
      :default-active="activeMenu"
      :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
      :unique-opened="true"
      :collapse-transition="false"
      mode="horizontal"
    >
      <sidebar-item
        v-for="(route, index) in sidebarRouters"
        :key="route.path + index"
        :item="route"
        :base-path="route.path"
      />
    </el-menu>
    <!-- </el-scrollbar> -->
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <!--                    <img :src="avatar" class="user-avatar">-->
          <!-- <i class="el-icon-caret-bottom" /> -->
          <div class="user-name">欢迎您，{{ nickName ? nickName : name }}</div>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapState(['settings']),
    ...mapGetters(['sidebarRouters', 'sidebar', 'avatar', 'name', 'nickName']),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    }
    // isCollapse() {
    //     return !this.sidebar.opened;
    // }
  },
  methods: {
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          // location.href = '/index';
          this.$router.push(`/login`)
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-container {
    .right-menu {
        flex: 1;
        text-align: right;
        .avatar-container {
            margin-right: 30px;
            .avatar-wrapper {
                position: relative;
                display: flex;
                .user-name {
                    line-height: 50px;
                    color: #fff;
                    font-size: 13px;
                    font-weight: 500;
                    cursor: pointer;
                }
                .user-avatar {
                    cursor: pointer;
                        width: 30px;
                        height: 30px;
                        border-radius: 15px;
                        margin-right: 8px;
                        margin-top: 10px;
                }

                .el-icon-caret-bottom {
                    cursor: pointer;
                    position: absolute;
                    right: -20px;
                    top: 25px;
                    font-size: 12px;
                    color: #fff;
                }
            }
        }
    }

    ::v-deep .el-menu-item:not(.is-disabled):focus, .el-submenu__title:focus {
        background-color: rgba(255,255,255, 0.1);
    }
}
</style>
