let template = // html
`
<div style="margin: auto;width:200px">
    登录
    <p>密码：</p>
    <input type="text" style="width: 100px;" v-model="loginForm.username" placeholder="用户名">
    <p>用户名：</p>
    <input type="password" style="width: 100px;" v-model="loginForm.password" placeholder="密码" />
    <br><br>
    <button style="width: 100px;" @click="login()">登录</button>
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
                location.href = '/index.html'
            }).catch(function (error) {
                console.log(error);
            });
        }
    }
}
