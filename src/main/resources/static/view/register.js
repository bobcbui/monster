let template = // html
`
<div style="margin: auto;width:200px">
    注册
    <p>密码：</p>
    <input type="text" style="width: 100px;" v-model="loginForm.username" placeholder="用户名">
    <p>用户名：</p>
    <input type="password" style="width: 100px;" v-model="loginForm.password" placeholder="密码" />
    <br><br>
    <button style="width: 100px;" @click="register()">注册</button>
    <br><br>
    <a href="/#/login">登录</a>
</div>
`
import request from '../lib/request.js'
export default {
    template: template,
    data: function(){
        return {
            loginForm: {

            }
        }
    },
    methods: {
        register() {
            request({
                method: 'post',
                url: '/register',
                data: this.loginForm,
            }).then(response => {
                alert(JSON.stringify(data))
            }).catch(function (error) {
                console.log(error);
            });
        }
    }
}
