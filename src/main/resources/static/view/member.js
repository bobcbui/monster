let template = // html
`
<div style='padding:10px;padding-top:0px;'>
    <input  style='width:100%;margin-bottom:5px;' v-model="ws" placeholder='ws地址'>
    <button style='width:100%;margin-bottom:5px;' @click="searchMember">查询好友</button>
	<div v-if='member' style='padding:10px;padding-top:0px;'>
        username:{{member.username}}<br>
        nickname:{{member.nickname}}<br>
    </div>
    <button v-if='member' style='width:100%;margin-bottom:5px;' @click="joinMember">增加好友</button>
</div>


<ul style='margin:0px'>
	<li style='padding:0px 10px;border:1px solid black;margin:0px 10px 10px 10px;border-radius:5px;' v-for='(item,index) in memberList' :key='index'>{{item.username}}</li>
</ul>
`
import request from '../lib/request.js'
export default {
    template: template,
    data: function () {
        return {
            form: {
                type: "message-member",
                text: "",
            },
            ws: "",
            checkUrl: "",
            memberSocket: null,
            member: null,
        }
    },
    wathc:{
    },
    // 计算属性
    computed: {
        memberList(){
            return this.$store.state.memberList;
        }
    },
    methods: {
        searchMember(){
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let socket = new WebSocket(this.ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let _this = this;
                socket.onopen = function(e){
                    _this.memberSocket = socket;
                    socket.send(JSON.stringify({ type: "searchMember"}))
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                   
                    if(data.type == "joinMember"){
                        _this.$store.state.socketLocal.send(JSON.stringify({ type: "addMember", data: data.member}))
                        _this.$store.state.socketLocal.send(JSON.stringify({ type: "memberList"}))
                    }
                    if(data.type == "searchMember"){
                        _this.member = data.member;
                    }
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
                
            }).catch(function (error) {
                console.log(error);
            });
        },
        joinMember(){
            this.memberSocket.send(JSON.stringify({ type: "joinMember"}))
        }
    },
    created() {
       
    }
}
