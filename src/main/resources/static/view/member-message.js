let template = // html
    `
<div style='height: calc(100% - 30px);overflow-y: scroll;'>
    <div style='border-bottom:1px solid red'>群：{{thisMember == undefined ? "" : thisMember.username}}</div>
  <div v-for='(item,index) in $store.state.memberListMessage[$route.query.account]'>
  <p v-if='item.sendAccount == member.account' style='text-align: right;border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;'>
    {{item.content}} : {{member.username}}
  </p>
  <p v-else style='border: 1px solid black;border-radius: 5px;margin: 5px;margin-bottom: 0px;'>
    {{thisMember.username}} : {{item.content}}
    </p>
  </div>
</div>
<div style="height:30px">
    <input v-model="message" style="width:70%;height:30px"/><button @click="send" style="width:30%;height:30px">发送</button>
</div>
`
import request from '../lib/request.js';
export default {
    template: template,
    data: function () {
        return {
            memberSocket: null,
            socketState: false
        }
    },
    wathc: {

    },
    computed: {
        member() {
            return this.$store.state.member;
        }, 
        thisMember() {
            let _thisMember;
            this.$store.state.memberList.filter(item => {
                if (item.account == this.$route.query.account) {
                    _thisMember = item;
                }
            })
            return _thisMember;
        }
    },
    methods: {
        createMemberSocket() {
            let _this = this;
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeAccount(this.$route.query.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                socket.onopen = function (e) {
                    _this.memberSocket = socket;
                    _this.socketState = true;
                    _this.$store.state.socketLocal.send(JSON.stringify({type: "loadMemberMessage",account:_this.$route.query.account}))
                };
                socket.onmessage = function (e) {
                    let data = JSON.parse(e.data);
                    if (data.type == "message") {
                        let orderId = data.data.orderId
                        _this.$store.state.memberListMessage[_this.$route.query.account].filter(item => {
                            if (item.orderId == orderId) {
                                console.log(item)
                                item.state = true;
                                let message = {
                                    type: "saveMessage",
                                    content: item.content,
                                    orderId: item.orderId,
                                    account: _this.$route.query.account
                                }
                                _this.$store.state.socketLocal.send(JSON.stringify(message))
                                return;
                            }
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
            var long = new Date().getTime() + Math.random().toString(16).slice(2);
            let message = { type: "message", content: this.message, orderId: long }
            if (this.$store.state.memberListMessage[this.$route.query.account] == undefined) {
                this.$store.state.memberListMessage[this.$route.query.account] = []
            }
            this.memberSocket.send(JSON.stringify(message))
            message.state = false
            message.to = true
            message.sendAccount = this.member.account
            this.$store.state.memberListMessage[this.$route.query.account].push(message)
        }
    },
    created() {
        this.createMemberSocket()
    }
}
