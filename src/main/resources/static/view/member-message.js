let template = // html
`
<cNav v-if='withMember' :title='"👤" + withMember.nickname'>
    <button @click='backClick()' class='h-100'>返回</button>&nbsp;
	<cModal buttonName='设置'>
        <div class='p-b-5'>名称: {{withMember.nickname}}</div>
        <div class='p-b-5'>账号: {{withMember.account}}</div>
        <div class='p-b-5'><button class='w-100' @click='deleteFriend()'>删除好友</button></div>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding-bottom:10px;background: var(--bottomColor);' v-if="withMember" :id='"show_words_" + withMember.account'>

<div v-for='(item,index) in memberMessageListMap[withMember.account]'>
        <div v-if='member && member.account != item.sendAccount ' class='m-10 m-b-0'>
            <strong class='name-color'>{{memberMap[item.sendAccount].username}} </strong>
            <p class='message-body'>{{item.content}}&nbsp;</p>
        </div>
        <div v-if='member && member.account == item.sendAccount' class='m-10 m-b-0 text-right'>
            <strong class='name-color'>{{member.username}}</strong>
            <p class='message-body'>&nbsp;{{item.content}}</p>
        </div>
    </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px;background: var(--topColor);" v-if="withMember">
    <input v-model="messageForm.content" style="width: calc(100% - 48px);" class='h-100'/>
    <button @click="send()" class='float-end h-100'>发送</button>
</div>
`
import cModal from '../component/modal.js';
import cNav from '../component/nav.js';
import { createMemberSocket } from '../core/app-socket.js';
export default {
    template: template,
    data: () => {
        return {
            socketState: false,
            messageForm: {},
        }
    },
    components: {
        cNav, cModal
    },
    computed: {
        withMember() {
            return this.memberMap[this.$route.query.account];
        },
        member() {
            return this.$store.state.member;
        },
        memberMap() {
            return this.$store.state.memberMap;
        },
        memberMessageListMap() {
            return this.$store.state.memberMessageListMap;
        },
        account() {
            return this.$route.query.account;
        },
        socketLocal() {
            return this.$store.state.socketLocal;
        }
    },
    watch: {
        "$store.state.memberMessageListMap": {
            handler(val) {
                this.$nextTick(() => {
                    down(this.$route.query.account);
                    this.updateMemberReadTime();
                });
            },
            deep: true,
            immediate: true,
        }
    },
    methods: {
        backClick() {
            let routerName = this.$route.query.routerName == null ? "message" : this.$route.query.routerName;
            this.$router.push({ name: routerName });
        },
        createMemberSocket() {
            let that = this;
            createMemberSocket(that.$route.query.account, (socket) => {
                // 加载成功
                that.memberSocket = socket;
            });

        },
        deleteFriend() {
            let that = this;
            // 删除好友服务器记录
            this.memberSocket.send({ type: "delete" }, (data, socket) => {
                if (data.code == "200") {
                    delete that.memberMap[that.$route.query.account];
                    that.backClick();
                }
            });
            // 删除好友本地记录
            this.socketLocal.send({ type: "deleteMember", account: this.$route.query.account });
        },
        send() {
            let that = this;
            if(!that.messageForm.content){
                alert("消息不能为空！");
                return
            }
            that.messageForm.type = "message";
            that.messageForm.serviceId = new Date().getTime();
            that.messageForm.sendAccount = that.$store.state.member.account;
            that.messageForm.createTime = new Date().getTime();
            that.messageForm.withAccount = that.$route.query.account;
            that.messageForm.state = 0;
            that.memberMessageListMap[that.$route.query.account].push({ ...that.messageForm });
            that.memberSocket.send(that.messageForm, (data, socket) => {
                // 发送消息回调
                that.$nextTick(() => {
                    down(that.$route.query.account);
                });
                // 修改消息状态
                that.memberMessageListMap[that.$route.query.account].filter(item => {
                    if (item.serviceId == data.data) {
                        item.state = 1;
                        // 保存消息
                        that.socketLocal.send({
                            type: "saveMessage",
                            content: that.messageForm.content,
                            serviceId: data.data,
                            withAccount: that.account
                        }, (data, socket) => {
                            console.log("保存消息成功");
                            console.log(socket);
                        })
                        that.messageForm.content = "";
                    }
                })
            })
        },
        updateMemberReadTime(){
            let that = this;
            if(this.socketLocal == null){
                // 等待socket连接
                setTimeout(() => {
                    that.updateMemberReadTime();
                }, 1000);
            }else{
                this.socketLocal.send({ type: "updateMemberReadTime", account: this.$route.query.account }, (data, socket) => {
                    console.log("更新群组消息已读时间");
                    that.memberMap[that.$route.query.account].readTime = data.data;
                });
            }
        }
    },
    created() {
        console.log('member-message')
    },
    mounted() {
        this.createMemberSocket()
    }
}
