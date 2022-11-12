let template = // html
`
<div style='padding:10px'>
创建群.群名：
<input v-model='groupName'>&nbsp;
<button @click='create()'>创建</button>
<hr>
增加群.账户：
<input v-model='groupWs'>&nbsp;
<button @click='joinGroup()'>加入</button>
<hr>
加好友.路径：
<input v-model='ws'>&nbsp;
<button @click='joinUser()'>增加</button>
<hr>
</div>

`
import request from '../lib/request.js'
export default {
    template: template,
    data: function(){
        return {
            ws:null
        }
    },
    methods: {
        create(){
            request({
                url: "/group/create",
                method: "POST",
                data:{groupName:this.groupName}
            }).then((response) => {
                alert(JSON.stringify(response.data))
            });
        },
        joinUser(){
            let _this = this;
            let checkUrl = window.location.origin + "/username/"+this.$store.state.user.username+"/token/"+localStorage.getItem("checkToken")
            this.ws =  new WebSocket(this.ws+"?checkUrl="+checkUrl+"&userWs="+this.$store.state.user.ws)
            this.ws.onmessage = function(e){
                console.log(e.data)
            };
            this.ws.onopen = function(e){
                _this.ws.send(JSON.stringify({type:"join",text:"申请加入"}))
            };
            this.ws.onerror = function(){
            };
            this.ws.onclose = function(){
            };
        },
        joinGroup(){
            let _this = this;
            let checkUrl = window.location.origin + "/username/"+this.$store.state.user.username+"/token/"+localStorage.getItem("checkToken")
            this.ws =  new WebSocket(this.groupWs+"?checkUrl="+checkUrl+"&ws="+this.$store.state.user.ws)
            this.ws.onmessage = function(e){
                console.log(e.data)
            };
            this.ws.onopen = function(e){
                _this.ws.send(JSON.stringify({type:"join",text:"申请加入"}))
            };
            this.ws.onerror = function(){
            };
            this.ws.onclose = function(){
            };
        }
    }
}
