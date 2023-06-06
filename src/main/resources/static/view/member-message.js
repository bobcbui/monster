let template = // html
`
<div style='height: calc(100% - 30px);overflow-y: scroll;'>
  <div v-for='(item,index) in messageList'>{{item.data}} <span v-if='!item.state' style='color:red'>.</span></div>
</div>
<div style="height:30px">
    <input v-model="message" style="width:70%;height:30px"/><button @click="send" style="width:30%;height:30px">发送</button>
</div>
`
import request from '../lib/request.js'
export default {
    template: template,
    data: function () {
        return {
           memberSocket:null,
           socketState:false,
           messageList:[]
        }
    },
    wathc:{

    },
    computed: {
       
    },
    methods: {
        createMemberSocket(){
            let _this = this;
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let socket = new WebSocket(_this.$route.query.ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                socket.onopen = function(e){
                    _this.memberSocket = socket;
                    _this.socketState = true;
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "message"){
                        let id = data.id
                        _this.messageList.filter(item => {
                            if(item.id == id){
                                console.log(item)
                                item.state = true;
                            }
                        })
                    }
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
            }).catch(function (error) {

            });
            
            
        },
        send(){
            var  long = new Date().getTime() + Math.random().toString(16).slice(2);
            let message = { type: "message", data: this.message, id: long,state:false}
            this.memberSocket.send(JSON.stringify(message))
            this.messageList.push(message)
        }
    },
    created() {
        this.createMemberSocket()
    }
}
