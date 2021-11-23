<template>
  <input type="text" v-model="loginFrom.mail">
  <input type="text" v-model="loginFrom.password">
  <button @click="login">登录</button>
</template>

<script>
import request from '../utils/request'
import jwt_decode from "jwt-decode"
export default {
  name: "login_index",
  data() {
    return {
        loginFrom:{
            mail:"792190997@qq.com",
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
                  let tokenData = jwt_decode(response.jwt_token)
                  localStorage.setItem("token",response.jwt_token)
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
.send-but{
  position:fixed;
  bottom: 0px;
}
.message-body{
  height: 500px;
  width: 300px;
  background: rgb(167, 166, 166);
}
.message{
  padding-bottom: 10px;
}

</style>