let template = // html
`
<cNav v-if='withMember' :title='withMember.nickname'>
    <button @click='backClick()'>返回</button>&nbsp;
	<cModal buttonName='设置'>
        <p>账号: {{withMember.account}}</p>
        <button class='w-100' @click='deleteFriend()'>删除好友</button>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding-bottom:10px' v-if="withMember" :id='"show_words_" + withMember.account'>
    <div v-for='(item,index) in $store.state.memberMessageList[$route.query.account]'>
        <div v-if='member && member.account != item.sendAccount ' class='m-10 m-b-0'>
            <strong class='name-color'>{{$store.state.memberMap[item.sendAccount].username}} </strong>
            <p class='message-body'>{{item.content}}&nbsp;</p>
        </div>
        <div v-if='member && member.account == item.sendAccount' class='m-10 m-b-0 text-right' style='text-align: right'>
            <strong class='name-color'>{{member.username}}</strong>
            <p class='message-body'>&nbsp;{{item.content}}</p>
        </div>
    </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px" v-if="withMember">
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
            return this.$store.state.memberMap[this.$route.query.account];
        },
        member() {
            return this.$store.state.member;
        },
        memberMap() {
            return this.$store.state.memberMap;
        },
        memberMessageList() {
            return this.$store.state.memberMessageList;
        },
        account() {
            return this.$route.query.account;
        }
    },
    watch: {
        "$store.state.memberMessageList": {
            handler(val) {
                down(this.$route.query.account)
            },
            deep: true,
            immediate: true,
        }
    },
    methods: {
        backClick() {
            let routerName = this.$route.query.routerName == null ? "message" : this.$route.query.routerName;
            this.$router.push({ name: routerName })
        },
        createMemberSocket() {
            let that = this;
            createMemberSocket(
                that.$route.query.account,
                (appSocket) => {
                    // 加载成功
                    that.memberSocket = appSocket;
                },
                (data, appSocket) => {
                    // 没有回调函数的处理器
                }
            );

        },
        deleteFriend() {
            let that = this;
            // 删除好友服务器记录
            this.memberSocket.send({ type: "delete" }, (data, appSocket) => {
                if (data.code == "200") {
                    delete that.$store.state.memberMap[that.$route.query.account];
                    that.backClick()
                }
            });
            // 删除好友本地记录
            this.$store.state.socketLocal.send({ type: "deleteMember", account: this.$route.query.account });
        },
        send() {
            let that = this;
            that.messageForm.type = "message";
            that.messageForm.serviceId = new Date().getTime();
            that.messageForm.sendAccount = that.$store.state.member.account;
            that.messageForm.createTime = new Date().getTime();
            that.messageForm.withAccount = that.$route.query.account
            that.messageForm.state = 0;
            that.memberMessageList[that.$route.query.account].push({ ...that.messageForm });
            that.memberSocket.send(that.messageForm, (data, appSocket) => {
                // 发送消息回调
                that.$nextTick(() => {
                    down(that.$route.query.account)
                });
                // 修改消息状态
                that.memberMessageList[that.$route.query.account].filter(item => {
                    if (item.serviceId == data.data) {
                        item.state = 1;
                        // 保存消息
                        that.$store.state.socketLocal.send({
                            type: "saveMessage",
                            content: that.messageForm.content,
                            serviceId: data.data,
                            withAccount: that.account
                        }, (data, appSocket) => {
                            console.log("保存消息成功")
                        })
                        that.messageForm.content = ""
                    }
                })
            })
        }
    },
    created() {
        this.createMemberSocket()
    },
    mounted() {
        // 等待几秒调用down
        let _this = this;
        setTimeout(() => {
            down(_this.$route.query.account)
        }, 100)

    }
}
