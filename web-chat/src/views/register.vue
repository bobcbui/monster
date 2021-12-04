<template>
  <div class="login" style="padding:10px">
    <h1>注册</h1>
    邮箱号：
   <span>注意只有邮箱号才能注册</span> <input type="text" class="W100" v-model="loginFrom.username" />
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
		
	if(this.loginFrom.username == ""){
		alert("用户名不能为空");
	}else{
		var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
		if (regEmail.test(this.loginFrom.username)) {
			
			if(this.password =="" || this.loginFrom.password == ""){
				alert("密码或确认密码不能为空");
			}else{
				if(this.password == this.loginFrom.password){
					request({
					  url: "/api/register",
					  method: "post",
					  data: this.loginFrom,
					}).then((response) => {
					  location.href = "/login";
					  alert("注册成功");
					});
				}else{
					 alert("两次密码不一致");
				}
			}
		}else{
			alert("用户名格式不对");
		}
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