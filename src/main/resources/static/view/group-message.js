let template = // html
`
<cNav v-if='group' :title='"👥" + group.name' back='true'>
    <button @click='$router.push({name:"message"})' class='h-100'>返回</button>&nbsp;
	<cModal buttonName='设置'>
        <div class='p-b-5'>名称：{{group.name}}</div>
        <div class='p-b-5'>群号：{{group.account}}</div>
        <div class='p-b-5'><button class='w-100' @click='deleteGroup()'>退出</button></div>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding-bottom: 10px;background: var(--bottomColor);' v-if='group' :id='"show_words_" + group.account'>
  <div v-for='(item,index) in $store.state.groupMessageListMap[$route.query.account]'>
    <div v-if='item.sendAccount == member.account' class='m-10 m-b-0 text-right'>
        <strong class='name-color'>{{member.username}}</strong>
        <p class='message-body'>{{item.content}}</p>
    </div>
    <div v-else-if='item.sendAccount == "system"' class='m-10 m-b-0'>
        <p class='message-body  text-center'>{{item.content}}</p>
    </div>
    <div v-else class='m-10 m-b-0'>
        <strong class='name-color'>{{item.sendNickname}} </strong>
        <p class='message-body'>{{item.content}}</p>
    </div>
  </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px;background: var(--topColor);" v-if="group">
    <input v-model="message" style="width: calc(100% - 48px); height: 100%;"/>
    <button @click="send" style="float:right; height: 100%;">发送</button>
</div>
`
import cModal from '../component/modal.js'
import cNav from '../component/nav.js'
export default {
    template: template,
    data: () => {
        return {
            socketState: false
        }
    },
    components:{
        cNav,cModal
    },
    watch:{
        "$store.state.groupMessageListMap":{
            handler(val){
                this.$nextTick(() => {
                    down(this.$route.query.account);
                });
                this.updateGroupReadTime();
            },
            deep: true,
            immediate: true,
        }
    },
    computed: {
        member() {
            return this.$store.state.member;
        },
        group() {
            return this.groupMap[this.$route.query.account];
        },
        groupMap(){
            return this.$store.state.groupMap;
        },
        groupSocket() {
            return this.$store.state.socketGroup[this.group.account];
        },
        groupMessageListMap(){
            return this.$store.state.groupMessageListMap;
        },
        socketLocal(){
            return this.$store.state.socketLocal;
        }
    },
    methods: {
        send() {
            let that = this;
            if(!this.message){
                alert("消息不能为空！");
                return
            }
            this.groupSocket.send({
                type: "message",
                content: this.message,
            },(data) => {
                that.message = "";
            })
        },
        updateGroupReadTime(){
            if(!this.socketLocal || !this.groupMap[this.group.account]){
                setTimeout(() => {
                    this.updateGroupReadTime();
                }, 100);
                return;
            }
            let that = this;
            this.socketLocal.send({ type: "updateGroupReadTime", account: this.group.account }, (data, socket) => {
                console.log("更新群组消息已读时间");
                that.groupMap[that.group.account].readTime = data.data;
            });
        },
        deleteGroup(){
            let that = this;
            this.groupSocket.send({ type: "quit", account: this.group.account }, (data, socket) => {
                that.socketLocal.send({ type: "quit", account: that.group.account }, (data, socket) => {
                    console.log("退出群组");
                    delete that.groupMap[that.group.account]
                    that.$router.push({name:"group"});
                   
                });
            });
        }

    },
    created() {
        
    },
    mounted() {
       
    }
}
