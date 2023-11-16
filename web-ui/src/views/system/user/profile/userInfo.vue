<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="margin-top: 20px;">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="form.name" maxlength="50" show-word-limit/>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="form.phone" maxlength="11" show-word-limit/>
    </el-form-item>
    <el-form-item label="验证码" prop="code" v-if="phoneChange">
      <el-input v-model="form.code">
        <el-button slot="append" @click="getSmsCode">{{ btnTitle }}</el-button>
      </el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="50" show-word-limit/>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button plain size="mini" @click="resetUser">重置</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {updateUserProfile} from "@/api/system/user";
import {getMessageCode} from "@/api/login";
import {mapGetters} from 'vuex'

export default {
  props: {
    user: {
      type: Object
    }
  },
  computed: {
    ...mapGetters(['currentPlatformType', 'currentPlatformName']),
    phoneChange() {
      return this.form.phone !== this.user.phonenumber
    }
  },
  watch: {},
  data() {
    return {
      form: {
        name: '',
        phone: '',
        email: '',
        code: '',
        sex: ''
      },
      totalTime: 60, // 倒计时时间
      btnTitle: '获取验证码',
      smsDisabled: false,
      smsSendParam: {
        account: null,
        phoneNum: null,
        type: 3, // 3修改手机号
        platform: this.currentPlatformType
      },
      // 表单校验
      rules: {
        phone: [
          {required: true, message: '手机号不能为空', trigger: 'blur'},
          {required: true, pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '手机号格式不正确', trigger: 'blur'},
        ],
        code: [{required: true, trigger: 'change', message: '请输入验证码'}],
        name: [
          {required: true, message: "姓名不能为空", trigger: "blur"}
        ],
        email: [
          {required: false, message: "邮箱地址不能为空", trigger: "blur"},
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
      }
    };
  },
  created() {
    console.log("user", this.user)
    // this.resetUser()
  },
  mounted() {
    setTimeout(() => {
      this.resetUser()
    }, 300)
  },
  methods: {
    resetUser() {
      const params = {
        email: this.user.email,
        name: this.user.nickName,
        phone: this.user.phonenumber,
        sex: this.user.sex
      }
      this.$set(this, 'form', params)
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const params = {
            sex: this.form.sex,
            email: this.form.email,
            nickName: this.form.name
          }
          if (this.phoneChange) { // 手机号变了才传手机号和验证码(后端要求)
            Object.defineProperty(params, 'phonenumber', {value: this.form.phone, enumerable: true})
            Object.defineProperty(params, 'code', {value: this.form.code * 1, enumerable: true})
          }
          updateUserProfile(params).then((res) => {
            if (res.code === 200) {
              this.$message.success('修改成功，即将刷新页面')
              setTimeout(() => {
                this.close()
              }, 1000)
            }
          })
        }
      });
    },
    close() {
      this.$tab.closePage();
    },
    getSmsCode() {
      Object.defineProperty(this.smsSendParam, 'account', {value: this.user.userName, enumerable: true})
      if (this.form.phone) {
        this.smsSendParam.phoneNum = this.form.phone
      }
      getMessageCode(this.smsSendParam).then(res => {
        if (res && res.code === 200) {
          this.$notify.success({
            title: '提示',
            message: '短信发送成功，请留意手机查收',
            offset: 50
          })
          const countDown = setInterval(() => {
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
    },
  }
};
</script>
