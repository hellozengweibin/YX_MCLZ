<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="100px" style="margin-top: 20px;">
    <el-form-item label="原密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="请输入原密码" type="password" show-password/>
    </el-form-item>
    <el-form-item label="手机验证码" prop="messageCode">
      <el-input
        v-model="user.messageCode"
        type="text"
        onkeyup="value=value.replace(/[^\d]/g,'')"
        auto-complete="off"
        placeholder="短信验证码"
        size="large">
        <el-button slot="append" :disabled="codeDisabled" @click="getPhoneCode">{{ codeName }}</el-button>
      </el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input
        v-model="user.newPassword"
        onkeyup="value=value.replace(/[^\x00-\xff]/g, '')"
        placeholder="请输入新密码"
        type="password"
        show-password
        autocomplete="new-password"/>
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        onkeyup="value=value.replace(/[^\x00-\xff]/g, '')"
        placeholder="请确认密码"
        type="password"
        show-password/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {updateUserPwd} from "@/api/system/user";
import {getMessageCode} from "@/api/login";
import {checkPwd, checkPwdNoBack} from "@/utils/rules.js";
import {mapGetters} from "vuex";

export default {
  props: {
    userInfo: {
      type: Object,
      default: {},
    },
  },
  computed: {
    ...mapGetters(['currentPlatformType', 'currentPlatformName']),
  },
  data() {
    const equalToPassword = (rule, value, callback) => {
      let resultPd = checkPwdNoBack(value, this.userInfo.userName);
      if (resultPd === "true") {
        if (this.user.newPassword !== value) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      }
      callback(new Error(resultPd));
    };
    return {
      platform: this.currentPlatformType,
      user: {
        messageCode: undefined,
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
      },
      // 表单校验
      rules: {
        messageCode: [
          {required: true, trigger: "blur", message: "请输入验证码"},
        ],
        oldPassword: [
          {required: true, message: "原密码不能为空", trigger: "blur"},
        ],
        newPassword: [
          {required: true, message: "新密码不能为空", trigger: "blur"},
          {required: true, validator: checkPwd, trigger: "blur"},
        ],
        confirmPassword: [
          {required: true, message: "确认密码不能为空", trigger: "blur"},
          {required: true, validator: equalToPassword, trigger: "blur"},
        ],
      },
      codeDisabled: false, // 点击获取验证码进cd
      second: 60,
      codeName: "发送验证码",
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateUserPwd(
            this.platform,
            this.user.messageCode,
            this.user.oldPassword,
            this.user.newPassword
          ).then((response) => {
            this.$modal.msgSuccess("密码修改成功，重新登录后生效");
            this.close();
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    },
    getPhoneCode() {
      const query = {
        account: this.userInfo.userName,
        type: 1,
        platform: this.currentPlatformType,
      };
      this.codeDisabled = true;
      const result = setInterval(() => {
        --this.second;
        this.codeName = `重新发送(${this.second})`;
        if (this.second < 1) {
          clearInterval(result);
          this.codeDisabled = false;
          this.second = 60;
          this.codeName = "发送验证码";
        }
      }, 1000);
      getMessageCode(query).then((res) => {
        if (res.code === 200)
          this.$message.success("验证码已发送，请留意手机上查收");
      });
    },
  },
};
</script>
