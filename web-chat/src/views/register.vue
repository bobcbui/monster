<template>
  <div class="login">
    <h1>注册</h1>
    用户名：
    <input type="text" class="W100" v-model="loginFrom.username" />
    <br />密码：
    <input type="text" class="W100" v-model="loginFrom.password" />
    <br />确认密码：
    <input type="text" class="W100" v-model="password" />
    <br /><br />
    <button  class="W100" @click="register">注册</button> 
    <br /><br />
    <div>
       <a href="/login">登录</a><a href="#" style="float:right">忘记密码</a>
     </div>
  </div>
</template>

<script>
import request from "../utils/request";
export default {
  name: "register",
  data() {
    return {
      loginFrom: {
        username: "",
        password: "",
      },
      password: "",
    };
  },
  computed: {},
  created() {},
  methods: {
    register() {
      if (this.password == loginFrom.password) {
        request({
          url: "/api/register",
          method: "post",
          data: this.loginFrom,
        }).then((response) => {
          location.href = "/login";
        });
      } else {
        alert("两次密码不一致");
      }
    },
  },
  mounted() {},
};
</script>

<style scoped>
.login {
  max-width: 500px;
  width: 100%;
  margin: auto;
}
.W100 {
  box-sizing: border-box;
  width: 100%;
}
</style>