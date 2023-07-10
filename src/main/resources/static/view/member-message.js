let template = // html
`
<cNav v-if='withMember' :title='withMember.nickname'>
    <button @click='backClick()'>返回</button>&nbsp;
    <button @click='$refs["cModal"].show = true'>设置</button>
	<cModal ref='cModal'>
        <p>账号: {{withMember.account}}</p>
        <button style='width:100%' @click='deleteFriend()'>删除好友</button>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding:5px;' v-if="withMember" :id='"show_words_" + withMember.account'>
    <div v-for='(item,index) in $store.state.memberMessageList[$route.query.account]'>
        <div v-if='member && member.account != item.sendAccount ' style='margin: 5px; margin-bottom: 0px;'>
            <strong style='color:#3c3ce2'>{{$store.state.memberMap[item.sendAccount].username}} </strong>
            <p style='border: 1px solid black; border-radius: 5px;margin:0px;padding:2px'>{{item.content}}&nbsp;</p>
        </div>
        <div v-if='member && member.account == item.sendAccount' style=' margin: 5px; margin-bottom: 0px;text-align: right'>
            <strong style='color:#3c3ce2'>{{member.username}}</strong>
            <p style='border: 1px solid black; border-radius: 5px;margin:0px;padding:2px'>&nbsp;{{item.content}}</p>
        </div>
    </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px" v-if="withMember">
    <input v-model="messageForm.content" style="width: calc(100% - 48px); height: 100%;"/>
    <button @click="send()" style="float:right; height: 100%;">发送</button>
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
            },
            deep: true,
            immediate: true,
        }
    },
    methods: {
        backClick(){
            let routerName = this.$route.query.routerName == null ? "message" : this.$route.query.routerName;
            this.$router.push({name: routerName})
        },
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
                    switch(data.type){
                        case "message":
                            // 修改消息状态
                            that.memberMessageList[that.$route.query.account].filter(item => {
                                debugger
                                if (item.serviceId == data.data) {
                                    item.state = 1;
                                    // 保存消息
                                    debugger
                                    that.$store.state.socketLocal.send(JSON.stringify({
                                        type: "saveMessage",
                                        content: that.messageForm.content,
                                        serviceId: data.data,
                                        withAccount: that.account
                                    }))
                                    
                                    that.messageForm.content = ""
                                    return true
                                }
                                return false
                            })
                        break;
                        case "delete":
                            // 删除好友
                            if(data.code == "200"){
                                delete that.$store.state.memberMap[that.$route.query.account];
                            }
                            break;
                    }
                };
                socket.onclose = function (e) {

                };
                socket.onerror = function (e) {

                };
            }).catch(function (error) {

            });


        },
        deleteFriend(){
            // 删除好友
            this.memberSocket.send(JSON.stringify({ type: "delete" }));
            this.$store.state.socketLocal.send(JSON.stringify({ type: "deleteMember", account: this.$route.query.account }));
        },
        send() {
            this.messageForm.serviceId = new Date().getTime();
            this.messageForm.sendAccount = this.$store.state.member.account;
            this.memberSocket.send(JSON.stringify(this.messageForm));
            this.messageForm.createTime = new Date().getTime();
            this.messageForm.withAccount = this.$route.query.account
            this.memberMessageList[this.$route.query.account].push({...this.messageForm});
            down(this.$route.query.account);
        }
    },
    created() {
        this.createMemberSocket()
    },
    mounted(){
        
    }
}
