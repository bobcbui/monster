let template = // html
`
<cNav v-if='group' :title='group.name' back='true'>
    <button @click='$router.push({name:"message"})' class='h-100'>返回</button>&nbsp;
	<cModal buttonName='设置'>
        <div class='p-b-5'>名称：{{group.name}}</div>
        <div class='p-b-5'>群号：{{group.account}}</div>
        <div class='p-b-5'><button class='w-100' @click='deleteGroup()'>退出</button></div>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding-bottom: 10px;background: rgb(255, 248, 248);' v-if='group' :id='"show_words_" + group.account'>
  <div v-for='(item,index) in $store.state.groupMessageList[$route.query.account]'>
    <div v-if='item.sendAccount == member.account' class='m-10 m-b-0 text-right'>
        <strong class='name-color'>{{member.username}}</strong>
        <p class='message-body'>{{item.content}}</p>
    </div>
    <div v-else class='m-10 m-b-0'>
        <strong class='name-color'>{{item.sendNickname}} </strong>
        <p class='message-body'>{{item.content}}</p>
    </div>
  </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px;background: rgb(255, 222, 252);" v-if="group">
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
        "$store.state.groupMessageList":{
            handler(val){
                this.$nextTick(() => {
                    down(this.$route.query.account);
                });
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
        }
    },
    methods: {
        send() {
            let that = this;
            this.groupSocket.send({
                type: "message",
                content: this.message,
            },(data) => {
                if (that.$store.state.groupMessageList[that.group.account] == null) {
                    that.$store.state.groupMessageList[that.group.account] = [];
                }
                that.$store.state.groupMessageList[that.group.account].push(data.data);
                //渲染完毕执行
                that.$nextTick(() => {
                    down(that.group.account);
                })
                that.message = "";
            })
        }
    },
    created() {
        // 100毫秒后执行
        setTimeout(() => {
            down(this.$route.query.account);
        }, 100);
    }
}
