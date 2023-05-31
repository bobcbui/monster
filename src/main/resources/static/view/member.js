let template = // html
`
<div style='padding:10px;padding-top:0px;'>
    <input  style='width:100%;margin-bottom:5px;' v-model="ws" placeholder='ws地址'>
	<button style='width:100%;margin-bottom:5px;' @click="joinMember">增加好友</button>
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
            checkUrl: ""
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
        
        joinMember(){
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let socket = new WebSocket(this.ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let _this = this;
                socket.onopen = function(e){
                    socket.send(JSON.stringify({ type: "joinMember"}))
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    debugger
                    if(data.type == "joinMember"){
                        _this.$store.state.socketLocal.send(JSON.stringify({ type: "addMember", data: data.member}))
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
        sendMessage() {
            this.$store.state.socketMessage.send(JSON.stringify(this.form))
        },
        initMessageWebSocket() {
            try {
                this.$store.state.socketMessage = new WebSocket(this.$route.query.url + "?checkUrl=" + localStorage.getItem("checkUrl"));
                this.$store.state.socketMessage.onmessage = this.websocketonmessage;
                this.$store.state.socketMessage.onopen = this.websocketonopen;
                this.$store.state.socketMessage.onerror = this.websocketonerror;
                this.$store.state.socketMessage.onclose = this.websocketclose;
            } catch (e) {
                console.log(e)
            }
        },
        websocketonopen() {

        },
        websocketonerror() {
            this.initWebSocket();
        },
        websocketonmessage(e) {
            let msg = JSON.parse(e.data)
            if (msg.type == 'message-member') {
                if (this.$store.state.messageMap[this.$route.query.url] == undefined) {
                    this.$store.state.messageMap[this.$route.query.url] = { name: msg.name, type: "message-member", ws: this.$route.query.url, message: [] }
                }
                this.$store.state.messageMap[this.$route.query.url].message.push(msg);
                msg.ws = this.$route.query.url
                this.$store.state.socketLocal.send(JSON.stringify(msg))
            }
            console.log(e.data)
        },
        websocketclose(e) {
            console.log("断开连接", e);
        },
    },
    created() {
        this.initMessageWebSocket();
    }
}
