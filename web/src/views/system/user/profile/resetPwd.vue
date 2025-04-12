<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input
        v-model="user.oldPassword"
        placeholder="请输入旧密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input
        v-model="user.newPassword"
        placeholder="请输入新密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        placeholder="请确认密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
      <div class="notice">注：修改密码成功后会跳转登录页重新登录！</div>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from '@/api/system/user';
import CryptoJS from 'crypto-js';

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    return {
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: '旧密码不能为空', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '新密码不能为空', trigger: 'blur' },
          {
            min: 6,
            max: 20,
            message: '长度在 6 到 20 个字符',
            trigger: 'blur',
          },
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          { required: true, validator: equalToPassword, trigger: 'blur' },
        ],
      },
    };
  },
  methods: {
    aes(val) {
      var base64Key = 'XusaWPFl1HO/M25tG327wP2OWUC7TLkgTyawI7+zoMQ=';
      var key = CryptoJS.enc.Base64.parse(base64Key);
      let result = CryptoJS.AES.encrypt(val, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
      });
      return result.toString();
    },
    submit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          updateUserPwd(
            this.aes(this.user.oldPassword),
            this.aes(this.user.newPassword)
          ).then((response) => {
            this.$modal.msgSuccess('修改成功');
            sessionStorage.removeItem('PwdFlag');
            this.$store.dispatch('LogOut').then(() => {
              this.$router.push({ path: '/login' });
            });
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    },
  },
};
</script>
<style lang="scss" scoped>
.notice {
  color: #727171;
  &:before {
    content: '*';
    color: #f56c6c;
    margin-right: 4px;
  }
}
</style>
