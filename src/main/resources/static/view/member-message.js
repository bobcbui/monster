let template = // html
`
<div style='height: calc(100% - 30px);overflow-y: scroll;' v-if="withMember" :id='"show_words_" + withMember.account'>
    <div style='border-bottom:1px solid red' v-if='withMember'>{{withMember.username}}</div>
    <div v-for='(item,index) in $store.state.memberListMessage[$route.query.account]'>
        <p v-if='item.sendAccount != null && $store.state.member.account != item.sendAccount' style='border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;'>
            {{$store.state.memberMap[item.sendAccount].username}} : {{item.content}}
        </p>
        <p v-if='item.sendAccount != null && $store.state.member.account == item.sendAccount' style='border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;text-align: right;'>
             {{item.content}} : {{$store.state.memberMap[item.sendAccount].username}}
        </p>
    </div>
</div>
<div style="height:30px">
    <input v-model="message.content" style="width:70%;height:30px"/><button @click="send" style="width:30%;height:30px">发送</button>
</div>
`
import request from '../lib/request.js';
export default {
    template: template,
    data: function () {
        return {
            memberSocket: null,
            socketState: false,
            message:{
                type: "message",
                content: "",
                orderId: null,
                sendAccount: null,
            },
            member: this.$store.state.member,
            memberMap : this.$store.state.memberMap,
            memberListMessage:this.$store.state.memberListMessage,
            account:this.$route.query.account
        }
    },
    computed:{
        withMember(){
            return this.$store.state.memberMap[this.$route.query.account];
        }
    },
    // 监听
    watch:{
        "$store.state.memberListMessage":{
            handler(val){
                down(this.$route.query.account)
            },
            deep: true, //a 是否开启深度监听
            immediate: true, // 是否初始化时就执行一次
        }
    },
    methods: {
        createMemberSocket() {
            let that = this;
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeAccount(this.$route.query.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                socket.onopen = function (e) {
                    that.memberSocket = socket;
                    that.socketState = true;
                };
                socket.onmessage = function (e) {
                    let data = JSON.parse(e.data);
                    // 发送消息的响应
                    if (data.type == "message") {
                        // 修改消息状态
                        that.memberListMessage[that.$route.query.account].filter(item => {
                            if (item.orderId == data.data.orderId) {
                                item.state = 1;
                                // 保存消息
                                that.$store.state.socketLocal.send(JSON.stringify({
                                    type: "saveMessage",
                                    content: that.message.content,
                                    orderId: data.data.orderId,
                                    withAccount: that.account
                                }))
                                that.message.content = ""
                                return true
                            }
                            return false
                        })
                        
                    }
                };
                socket.onclose = function (e) {

                };
                socket.onerror = function (e) {

                };
            }).catch(function (error) {

            });


        },
        send() {
            this.message.orderId = new Date().getTime() + Math.random().toString(16).slice(2);
            this.message.state = 0;
            this.message.sendAccount = this.$store.state.member.account;
            this.memberSocket.send(JSON.stringify(this.message))
            this.memberListMessage[this.$route.query.account].push({...this.message})
        }
    },
    created() {
        this.createMemberSocket()
        
    },
    mounted(){
        setTimeout(() => {
            this.memberListMessage[this.$route.query.account].push({})
        }, 100);
    }
}
