<template>
  <div class="login" style="padding:10px">
    <h1 align="center">登录</h1>
	 <var-input
      placeholder="请输入邮箱"
	  type = username
      v-model="loginFrom.mail"
    />
    <br> <br>
    <var-input
      placeholder="请输入密码"
	  type = password
      v-model="loginFrom.password"
    />
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
import { ref } from 'vue'
import { Input,Dialog,Slider    } from '@varlet/ui'
export default {
  name: "login",
  data() {
    return {
        loginFrom:{
            mail:"",
            password:"",
			id: "",
			ymail:"",
        }
    };
  },
  computed: {
    
  },
  created() {

  },
  methods: {
      login(){
		  if(this.loginFrom.mail == ""){
			  alert("未输入邮箱");
		  }else{
			  if(this.loginFrom.password == ""){
				  alert("未输入密码");
			  }else{
				  request({
				      url: '/api/authenticate',
				      method: 'post',
				      data:this.loginFrom,
				      }).then((response) => {
				          let tokenData = jwt_decode(response.token)
				          localStorage.setItem("token",response.token)
				          localStorage.setItem("member",JSON.stringify(tokenData.member.member))
				          location.href = '/'
				      })
			  }
			  
		  }
      },
	  
     selectId(){
		 request({
			 url: '/other/selectId',
			 method:'get',
			 data:this.loginFrom.mail,
		 })
	 },
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