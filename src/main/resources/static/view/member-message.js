let template = // html
`
<cNav v-if='withMember' :title='withMember.nickname' back='true'>
    <button @click='$router.push({name:"message"})' >返回</button>
</cNav>
<div style='height: calc(100% - 74px);overflow-y: scroll;padding-bottom:5px;' v-if="withMember" :id='"show_words_" + withMember.account'>
    <div v-for='(item,index) in $store.state.memberMessageList[$route.query.account]'>
        <div v-if='member && member.account != item.sendAccount ' style=' margin: 5px; margin-bottom: 0px;'>
            <strong style='color:#3c3ce2'>{{$store.state.memberMap[item.sendAccount].username}} </strong>
            <p style='border: 1px solid black; border-radius: 5px;margin:0px;padding:2px'>{{item.content}}&nbsp;</p>
        </div>
        <div v-if='member && member.account == item.sendAccount' style=' margin: 5px; margin-bottom: 0px;text-align: right'>
            <strong style='color:#3c3ce2'>{{member.username}}</strong>
            <p style='border: 1px solid black; border-radius: 5px;margin:0px;padding:2px'>&nbsp;{{item.content}}</p>
        </div>
    </div>
</div>
<div style="height:30px;border-top:1px solid black;padding:2px 10px" v-if="withMember">
    <input v-model="messageForm.content" style="width: calc(100% - 48px); height: 23px;"/>
    <button @click="send" style="float:right; height: 23px;margin-top: 1px;">发送</button>
</div>
`
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
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
    components:{
        cNav,cModal
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
                console.log("asdfjkashdfjkahsdfjk")
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
                    down(that.$route.query.account)
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
            this.messageForm.sendAccount = this.$store.state.member.account
            this.memberSocket.send(JSON.stringify(this.messageForm))
            this.memberMessageList[this.$route.query.account].push({...this.messageForm})
            down(this.$route.query.account)
        }
    },
    created() {
        this.createMemberSocket()
    },
    mounted(){
        // $store.state.memberMessageList[$route.query.account] 渲染完毕后，滚动到底部,休眠100毫秒
        
    }
}
