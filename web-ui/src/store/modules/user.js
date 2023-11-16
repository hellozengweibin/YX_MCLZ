import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'

const user = {
  state: {
    userId: '',
    token: getToken(),
    name: '',
    nickName: '',
    userType: '',
    platformType: '',
    // 当前登录的平台信息
    currentPlatform: {
      code: '',
      name: ''
    },
    avatar: '',
    roles: [],
    permissions: [],
    shoutServerUrl: '',
    salt:"",
  },

  mutations: {
    SET_USER_ID: (state, userId) => {
      state.userId = userId
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_USER_TYPE: (state, userType) => {
      state.userType = userType
    },
    SET_PLATFORM_TYPE: (state, platformType) => {
      state.platformType = platformType
    },
    SET_CURRENT_PLATFORM: (state, currentPlatform) => {
      state.currentPlatform = currentPlatform
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_NICK_NAME: (state, name) => {
      state.nickName = name
    },
    SET_SHOUT_SERVER_URL: (state, shoutServerUrl) => {
      state.shoutServerUrl = shoutServerUrl
    },
    SET_SALT: (state, salt) => {
      state.salt = salt
    },
  },

  actions: {
    // 登录
    Login({commit}, loginForm) {
      // const username = loginForm.username.trim()
      // const password = loginForm.password
      // const code = loginForm.code
      // const uuid = loginForm.uuid
      return new Promise((resolve, reject) => {
        login(loginForm).then(res => {
          console.log('>>>>>>>>>>login:',res)
          const { token } = res.data
          if(token) {
            setToken(token)
            commit('SET_TOKEN', token)
          }
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({commit, state}) {
      console.log(">>>>>>>>>>>>>>getInfo")
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const {currentPlatform, platformName, user, roles, permissions,shoutServerUrl,salt} = res.data
          const avatar = user.avatar == "" ? require("@/assets/images/profile.jpg") : user.avatar;
          if (roles && roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', roles)
            commit('SET_PERMISSIONS', permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_USER_ID', user.userId)
          commit('SET_NAME', user.userName)
          commit('SET_NICK_NAME', user.nickName)
          commit('SET_USER_TYPE', user.userType)
          commit('SET_PLATFORM_TYPE', user.platformType)
          const currentPlatformInfo = {
            code: currentPlatform,
            name: platformName
          }
          commit('SET_CURRENT_PLATFORM', currentPlatformInfo)
          commit('SET_AVATAR', avatar)
          commit('SET_SHOUT_SERVER_URL', shoutServerUrl)
          commit('SET_SALT', salt)

          resolve(res.data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({commit, state}) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_USER_ID', '')
          commit('SET_USER_TYPE', '')
          commit('SET_PLATFORM_TYPE', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
