let template = // html
`
<div style='height: calc(100% - 30px);overflow-y: scroll;' v-if="withMember" :id='"show_words_" + withMember.account'>
    <div style='border-bottom:1px solid red' v-if='withMember'>{{withMember.username}}</div>
    <div v-for='(item,index) in $store.state.memberMessageList[$route.query.account]'>
    {{$store.state.memberMap}}{{item.sendAccount}}
        <p v-if='item.sendAccount && $store.state.memberMap && $store.state.memberMap[item.sendAccount]' style='border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;'>
            {{$store.state.memberMap[item.sendAccount].username}} : {{item.content}}
        </p>
        <p v-if='item.sendAccount && $store.state.memberMap && $store.state.memberMap[item.sendAccount]' style='border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;text-align: right;'>
             {{item.content}} : {{$store.state.memberMap[item.sendAccount].username}}
        </p>
    </div>
</div>
<div style="height:30px">
    <input v-model="messageForm.content" style="width:70%;height:30px"/><button @click="send" style="width:30%;height:30px">发送</button>
</div>
`
import request from '../lib/request.js';
export default {
    template: template,
    data: function () {
        return {
            socketState: false,
            messageForm:{
                type: "message",
                state: 0,
                content: ""
            },
        }
    },
    computed:{
        withMember(){
            return this.$store.state.memberMap[this.$route.query.account];
        },
        member(){
            return this.$store.state.member;
        },
        memberMap(){
            return this.$store.state.memberMap;
        },
        memberMessageList(){
            return this.$store.state.memberMessageList;
        },
        account(){
            return this.$route.query.account;
        }
    },
    watch:{
        "$store.state.memberMessageList":{
            handler(val){
                down(this.$route.query.account)
            },
            deep: true,
            immediate: true,
        }
    },
    methods: {
        createMemberSocket() {
            let that = this;
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeWsAccount(this.$route.query.account);
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
                        that.memberMessageList[that.$route.query.account].filter(item => {
                            if (item.serviceId == data.serviceId) {
                                item.state = 1;
                                // 保存消息
                                that.$store.state.socketLocal.send(JSON.stringify({
                                    type: "saveMessage",
                                    content: that.messageForm.content,
                                    serviceId: data.serviceId,
                                    withAccount: that.account
                                }))
                                that.messageForm.content = ""
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
            this.messageForm.serviceId = new Date().getTime();
            this.sendAccount = this.$store.state.member.account
            this.memberSocket.send(JSON.stringify(this.messageForm))
            this.memberMessageList[this.$route.query.account].push({...this.messageForm})
        }
    },
    created() {
        this.createMemberSocket()
    },
    mounted(){
        setTimeout(() => {
            down(this.$route.query.account)
        }, 100);
        
    }
}
