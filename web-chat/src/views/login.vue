<template>
  <div class="login" style="padding: 10px">
    <h1 align="center">登录</h1>
    <input
      class="W100"
      placeholder="用户名（任意字符）"
      v-model="loginFrom.username"
    />
    <br /><br />

    <input
      class="W100"
      placeholder="请输入密码"
      v-model="loginFrom.password"
    />
    <br /><br />
    
    <button class="W100" @click="login">登录</button>
    <br /><br />
   
    <div>
      <a href="/register">注册</a><a href="#" style="float: right">忘记密码</a>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import jwt_decode from "jwt-decode";
export default {
  name: "login",
  data() {
    return {
      loginFrom: {
        username: "",
        password: ""
      },
    };
  },
  methods: {
    login() {
      if (this.loginFrom.username == "") {
        alert("未输入邮箱");
      } else {
        if (this.loginFrom.password == "") {
          alert("未输入密码");
        } else {
          request({
            url: "/api/authenticate",
            method: "post",
            data: this.loginFrom,
          }).then((response) => {
            let tokenData = jwt_decode(response.token);
            localStorage.setItem("token", response.token);
            localStorage.setItem("member", JSON.stringify(tokenData.member.member));
            location.href = "/";
          });
        }
      }
    },
    selectId() {
      request({
        url: "/other/selectId",
        method: "get",
        data: this.loginFrom.mail,
      });
    },
  },
  mounted() {
    localStorage.removeItem("token", "");
    localStorage.removeItem("member", "");
  },
};
</script>

<style scoped>
.login {
  max-width: 500px;
  width: 100%;
  margin: auto;
}
.W100 {
  width: 100%;
  box-sizing: border-box;
}
</style>