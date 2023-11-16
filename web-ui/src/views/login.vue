<template>
  <div class="login">
    <div class="l-title-view">
      <img class="l-t-logo" src="../assets/layout/logo2.png"/>
      <div class="l-t-title">模板系统</div>
    </div>
    <div class="l-bg-view"/>
    <el-form ref="loginForm" :model="loginForm" :rules="activeName == 'first' ? loginRules : loginRules2"
             class="login-form">
      <h3 class="title">欢迎登录</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          maxlength="50"
          placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          maxlength="50"
          placeholder="密码"
          @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <!-- <el-form-item prop="code" v-if="captchaOnOff">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item> -->
      <!-- </el-tab-pane>
      <el-tab-pane label="短信登录" name="second" style="margin-top:20px;"> -->
      <!-- <el-form-item prop="phone">
        <el-input
          v-model="loginForm.phone"
          type="text"
          auto-complete="off"
          maxlength="11"
          placeholder="手机号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item> -->
      <el-form-item prop="platform">
        <el-select size="small" v-model="loginForm.platform" style="width: 100%">
          <el-option label="管理端" :value="1"></el-option>
          <el-option label="客户端" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="messageCode" style="margin-top: 10px;margin-bottom: 30px">
        <el-input
          v-model="loginForm.messageCode"
          auto-complete="off"
          placeholder="请输入验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
        </el-input>
        <div class="login-code login-code2">
          <el-button :disabled="smsDisabled" size="medium" @click="getSmsCode" plain
                     :type="smsDisabled ? 'success' : 'primary'">{{ btnTitle }}
          </el-button>
        </div>
      </el-form-item>

      <el-checkbox v-show="loginForm.loginWay == 1" v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">
        记住密码
      </el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div @click="showForgetPwd(true)" class="forget-pwd">
          <span>忘记密码</span>
        </div>
      </el-form-item>
    </el-form>
    <!-- 忘记密码 -->
    <el-form v-show="showForgetPwdView" ref="forgetForm" :model="forgetForm" :rules="forgetRules" style="z-index:99;"
             class="login-form">
      <h3 class="title">忘记密码</h3>
      <el-form-item prop="account">
        <el-input
          v-model="forgetForm.account"
          type="text"
          auto-complete="off"
          maxlength="50"
          placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" style="margin-top: 10px;margin-bottom: 20px">
        <el-input
          v-model="forgetForm.code"
          auto-complete="off"
          placeholder="请输入验证码"
          style="width: 63%">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
        </el-input>
        <div class="login-code login-code2">
          <el-button :disabled="smsDisabled" size="medium" @click="getSmsCode" plain
                     :type="smsDisabled ? 'success' : 'primary'">{{ btnTitle }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item prop="password">
        <el-popover
          placement="top"
          title="密码规则"
          width="420"
          trigger="manual"
          v-model="visible">
          <div>
            <div>1.密码长度为8-16位</div>
            <div>2.至少包含数字、小写字母、大写字母、特殊符号4类中的3类</div>
            <div>3.任意相邻3位密码之间不能出现连续相同或相邻键盘字符</div>
          </div>
          <el-input
            v-model="forgetForm.password"
            slot="reference"
            @focus="showPwdTip"
            @blur="hidePwdTip"
            type="password"
            auto-complete="off"
            maxlength="50"
            placeholder="请输入新密码">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-popover>
      </el-form-item>
      <el-form-item prop="newPassword">
        <el-input
          v-model="forgetForm.newPassword"
          type="password"
          auto-complete="off"
          maxlength="50"
          placeholder="确认密码">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          size="medium"
          type="primary"
          style="width:100%;"
          @click="updatePwd"
        >
          <span>确认修改</span>
        </el-button>
        <div @click="showForgetPwd(false)" class="forget-pwd">
          <span>返回登录</span>
        </div>
      </el-form-item>
    </el-form>
    <!-- end -->
    <div class="el-login-footer">
      <span>Copyright Eshore © 2023  All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import {getCodeImg, getMessageCode, resetPwd} from "@/api/login";
import Cookies from "js-cookie";
import RSA from '@/utils/jsencrypt'
import {session_storage} from '@/utils/sessionStorage'
import AES from "@/utils/aesSecret";
import {removeToken} from '@/utils/auth'
// import { checkPwd } from '@/utils/validate'

export default {
  name: "Login",
  data() {
    return {
      activeName: 'first',
      codeUrl: "",
      loginForm: {
        loginWay: 1, // 登录方式 1-账号密码登录 2-手机验证码登录
        platform: 1, //平台类型
        username: "", // 用户名
        password: "", // 密码
        rememberMe: false,
        phone: '', // 手机号
        messageCode: '', // 短信验证码
        code: "", // 图片验证码
        uuid: ""
      },
      loginRules: {
        username: [
          {required: true, trigger: "blur", message: "请输入您的账号"}
        ],
        password: [
          {required: true, trigger: "blur", message: "请输入您的密码"},
          {min: 8, max: 16, message: '密码长度必须介于8~16之间', trigger: 'blur'}
        ],
        // code: [{ required: true, trigger: "change", message: "请输入验证码" }],
        messageCode: [
          {required: true, trigger: "change", message: "请输入短信验证码"},
        ],
        platform: [
          {required: true, trigger: "change", message: "请选择登录的平台"},
        ]
      },
      loginRules2: {
        phone: [
          {required: true, trigger: "blur", message: "请输入您的手机号"},
          {required: true, pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "手机号码格式不正确", trigger: "blur"},
        ],
        messageCode: [
          {required: true, trigger: "change", message: "请输入短信验证码"},
        ]
      },
      loading: false,
      // 验证码开关
      captchaOnOff: true,
      // 注册开关
      register: false,
      redirect: undefined,
      curEnv: process.env.NODE_ENV, // 'development' 'production'
      totalTime: 60,  // 倒计时时间
      btnTitle: '获取验证码',
      smsDisabled: false,
      // 发送短信验证参数
      smsSendParam: {
        appCode: '1', // 平台标识
        account: null,
        phoneNum: null,
        messageCode: null,
        type: 0, // 0标识登录
        platform: null
      },
      showForgetPwdView: false,
      forgetForm: {
        account: '', // 手机号
        appCode: 'dzsb', // 平台标识
        code: "",
        password: "", // 密码
        newPassword: "" // 密码
      },
      forgetRules: {
        account: [
          {required: true, trigger: "blur", message: "请输入您的账号"}
        ],
        password: [
          {required: true, trigger: "blur", message: "请输入您的密码"},
          {min: 8, max: 16, message: '密码长度必须介于8~16之间', trigger: 'blur'}
        ],
        // password: [{ validator: checkPwd }],
        newPassword: [
          {required: true, trigger: "blur", message: "请输入您的密码"},
          {min: 8, max: 16, message: '密码长度必须介于8~16之间', trigger: 'blur'}
        ],
        // code: [{ required: true, trigger: "change", message: "请输入验证码" }],
        code: [
          {required: true, trigger: "change", message: "请输入短信验证码"},
        ]
      },
      visible: false
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    session_storage('e-cur-platform', 1)
    // this.getCode();
    removeToken()
    this.getCookie();
  },
  mounted() {
    const type = this.$route.query.type
    if (type) {
      this.loginForm.platform = Number(type)
    }
  },
  methods: {
    showPwdTip() {
      this.visible = true
    },
    hidePwdTip() {
      this.visible = false
    },
    showForgetPwd(show) {
      this.resetForgetPwd()
      this.showForgetPwdView = show
    },
    resetForgetPwd() {
      this.forgetForm = {
        account: '', // 手机号
        appCode: 'drowning-prevention', // 平台标识
        code: "",
        password: "", // 密码
        newPassword: "" // 密码
      }
    },
    updatePwd() {
      if (this.forgetForm.password != this.forgetForm.newPassword) {
        this.$notify.warning({
          title: '提示',
          message: '2次输入的密码不一致，请确认',
          offset: 50
        })
        return
      }
      let newPwd = AES.encrypt(this.forgetForm.newPassword)
      this.forgetForm.newPassword = newPwd
      resetPwd(this.forgetForm).then(res => {
        console.log(res)
        if (res && res.code === 200) {
          this.resetForgetPwd()
          this.showForgetPwdView = false
          this.$notify.success({
            title: '提示',
            message: '密码修改成功',
            offset: 50
          })
        } else {
          this.$notify.warning({
            title: '提示',
            message: res.msg,
            offset: 50
          })
        }
      })
    },
    handleClick(tab, event) {
      console.log('>>>>>>>>>', tab.name, event);
      switch (tab.name) {
        case 'first':
          this.loginForm.loginWay = 1
          this.loginForm.phone = null
          this.smsSendParam.phoneNum = null
          break
        case 'second':
          this.loginForm.loginWay = 2
          this.loginForm.username = null
          this.loginForm.password = null
          this.loginForm.code = null
          this.loginForm.uuid = null
          break
      }
    },
    getCode() {
      if (this.loginForm.loginWay === 1) {
        getCodeImg().then(res => {
          const {captchaOnOff, img, uuid} = res.data
          this.captchaOnOff = captchaOnOff === undefined ? true : captchaOnOff;
          if (this.captchaOnOff) {
            this.codeUrl = "data:image/gif;base64," + img;
            this.loginForm.uuid = uuid;
          }
        });
      }
    },
    getSmsCode() {
      const field = this.showForgetPwdView ? ['account'] : ['username']
      const form = this.showForgetPwdView ? 'forgetForm' : 'loginForm'
      this.$refs[form].validateField(field, valid => {
        if (!valid) {
          if (this.showForgetPwdView == false) {
            if (!this.loginForm.username) {
              this.$notify.warning({
                title: '提示',
                message: '请先输入您的账号',
                offset: 50
              })
              return false
            }
            // 下面放获取验证码接口
            // this.smsSendParam.phoneNum = this.loginForm.phone
            this.smsSendParam.type = 0
            this.smsSendParam.account = this.loginForm.username
          } else {
            if (!this.forgetForm.account) {
              this.$notify.warning({
                title: '提示',
                message: '请先输入您的账号',
                offset: 50
              })
              return false
            }
            // 下面放获取验证码接口
            this.smsSendParam.type = 2
            this.smsSendParam.account = this.forgetForm.account
          }
          this.smsSendParam.platform = this.loginForm.platform
          getMessageCode(this.smsSendParam).then(res => {
            if (res && res.code === 200) {
              this.$notify.success({
                title: '提示',
                message: '短信发送成功，请留意手机查收',
                offset: 50
              })
              let countDown = setInterval(() => {
                if (this.totalTime < 1) {
                  this.smsDisabled = false
                  this.btnTitle = '获取验证码'
                  this.totalTime = 60
                  clearInterval(countDown)
                } else {
                  this.smsDisabled = true
                  this.btnTitle = this.totalTime-- + 's后重发'
                }
              }, 1000)
            } else {
              this.$nextTick(() => {
                this.$notify.warning({
                  title: '提示',
                  message: res.msg,
                  offset: 50
                })
              })
            }
          })
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm.username = username === undefined ? this.loginForm.username : username
      this.loginForm.password = password === undefined ? this.loginForm.password : RSA.decrypt(password)
      this.loginForm.rememberMe = rememberMe === undefined ? false : Boolean(rememberMe)
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.loginWay == 1 && this.loginForm.username) {
            this.loginForm.username = this.loginForm.username.trim()
          }
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, {expires: 30});
            Cookies.set("password", RSA.encrypt(this.loginForm.password), {expires: 30});
            Cookies.set('rememberMe', this.loginForm.rememberMe, {expires: 30});
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }

          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
            // this.$router.push({ path: this.redirect || "/bigscreen" }).catch(()=>{});
            // this.$router.push({ path: "/bigscreen" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaOnOff) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  // display: flex;
  // justify-content: center;
  // align-items: center;
  width: 100%;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: 100% 100%;
  position: relative;

  .forget-pwd {
    float: right;
    font-size: 14px;
    line-height: 38px;
    color: #687D9D;
    cursor: pointer;
  }

  .l-bg-view {
    //background-image: url("../assets/images/login-background.png");
    background-size: 100% 100%;
    width: 73%;
    height: 73%;
    position: absolute;
    top: 160px;
    left: 30px;
  }

  .l-title-view {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 50px 0 0 60px;

    .l-t-logo {
      width: 60px;
      height: 60px;
      margin-right: 15px;
    }

    .l-t-title {
      color: rgba(255, 255, 255, 1);
      font-size: 40px;
      font-weight: 400;
      font-family: "YouSheBiaoTiHei";
    }
  }

  .l-login-view {

  }
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: rgba(3, 39, 92, 1);
  font-size: 22px;
  font-weight: 700;
}

.login-form {
  border-radius: 10px;
  border: 1px solid rgba(248, 248, 248, 1);
  background: rgba(255, 255, 255, 1);
  width: 380px;
  padding: 25px 25px 5px 25px;
  position: absolute;
  right: 150px;
  top: 24%;
  // display: none;
  .el-input {
    height: 38px;
    line-height: 38px;

    input {
      height: 38px;
      line-height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 34%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.login-code2 {
  //padding-top: 2px;
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}
</style>
