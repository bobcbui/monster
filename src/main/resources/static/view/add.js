let template = // html
`
<div class='padding-10'>
    创建群.群名：
    <input v-model='groupName'>&nbsp;
    <button @click='create()'>创建群组</button>
    <hr>
    增加群.账户：
    <input v-model='groupWs'>&nbsp;
    <button @click='joinGroup()'>加入群组</button>
    <hr>
    加好友.路径：
    <input v-model='ws'>&nbsp;
    <button @click='joinUser()'>增加好友</button>
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
                window.location.reload()
            });
        },
        joinUser(){
            let _this = this;
            let webSocket =  new WebSocket(_this.ws+"?checkUrl="+localStorage.getItem("checkUrl"))
            webSocket.onmessage = function(e){
                let data = JSON.parse(e.data)
                _this.$store.state.socketLocal.send(JSON.stringify({
                    type:"add",
                    ws:_this.ws,
                    username:data.data
                }))
                console.log(e.data)
            };
            webSocket.onopen = function(e){
                webSocket.send(JSON.stringify({type:"add"}))
            };
            webSocket.onerror = function(){
            };
            webSocket.onclose = function(){
            };
        },
        joinGroup(){
            let _this = this;
            let webSocket =  new WebSocket(this.groupWs+"?checkUrl="+localStorage.getItem("checkUrl"))
            webSocket.onmessage = function(e){
                let data = JSON.parse(e.data)
                if(data.type == 'join'){
                    _this.$store.state.socketLocal.send(JSON.stringify({
                        type: "groupList"
                    }))
                }
                console.log(e.data)
            };
            webSocket.onopen = function(e){
                webSocket.send(JSON.stringify({type:"join",text:"申请加入"}))
            };
            webSocket.onerror = function(){
            };
            webSocket.onclose = function(){
            };
        }
    }
}
