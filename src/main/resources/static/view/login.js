let template = // html
`
<div class='p-10'>
    登录
    <p class='m-b-2'>密码：</p>
    <input class='w-100' type="text" v-model="loginForm.username" placeholder="用户名">
    <p class='m-b-2'>用户名：</p>
    <input class='w-100' type="password" v-model="loginForm.password" placeholder="密码" />
    <br><br>
    <button class='w-100' @click="login()">登录</button>
    <br>
    <a href="/#/register">注册</a>
</div>
`
import request from '../lib/request.js';
export default {
    template: template,
    data: function(){
        return {
            loginForm: {
                username:"",
                password:""
            }
        }
    },
    methods: {
        login() {
            request({
                method: 'post',
                url: '/authenticate',
                data: this.loginForm,
            }).then(response => {
                localStorage.setItem("token", response.data)
                location.href = '/'
                this.$router.push({ name: 'message' })
            }).catch(function (error) {
                console.log(error);
            });
        }
        
    }
}
