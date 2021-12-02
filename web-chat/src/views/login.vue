<template>
  <div class="login">
    <h1 align="center">登录</h1>
    <input type="text" class="W100" v-model="loginFrom.mail">
    <br> <br>
    <input type="text" class="W100" v-model="loginFrom.password">
     <br> <br>
    <button  class="W100" @click="login">登录</button>
     <br> <br>
     <div>
       <a href="/register">注册</a><a href="#" style="float:right">忘记密码</a>
     </div>
     
  </div>
</template>

<script>
import request from '../utils/request'
import jwt_decode from "jwt-decode"
export default {
  name: "login",
  data() {
    return {
        loginFrom:{
            mail:"user1",
            password:"mima135654.."
        }
    };
  },
  computed: {
    
  },
  created() {

  },
  methods: {
      login(){
          request({
              url: '/api/authenticate',
              method: 'post',
              data:this.loginFrom
              }).then((response) => {
                  let tokenData = jwt_decode(response.token)
                  localStorage.setItem("token",response.token)
                  localStorage.setItem("member",JSON.stringify(tokenData.member))
                  location.href = '/'
              })
      }
    
  },
  mounted(){
   
  }
};
</script>

<style scoped>
.login{
  max-width: 500px;
  width: 100%;
  margin: auto;
}
.W100{
  width: 100%;
  box-sizing: border-box;
}
</style>