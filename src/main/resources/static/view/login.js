let template = // html
`
<div style="padding:10px">
    登录
    <p style='margin-bottom: 2px;'>密码：</p>
    <input type="text" style="width: 100%;" v-model="loginForm.username" placeholder="用户名">
    <p style='margin-bottom: 2px;'>用户名：</p>
    <input type="password" style="width: 100%;" v-model="loginForm.password" placeholder="密码" />
    <br><br>
    <button style="width: 100%;" @click="login()">登录</button>
    <br>
    <a href="/#/register">注册</a>
</div>
`
import request from '../lib/request.js'
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
