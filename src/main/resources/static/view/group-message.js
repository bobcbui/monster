let template = // html
`
<cNav v-if='thisGroup' :title='thisGroup.name' back='true'>
    <button @click='$router.push({name:"message"})' >返回</button>&nbsp;
	<cModal buttonName='设置'>
        名称：{{thisGroup.name}}<br/>
        群号：{{thisGroup.account}}<br/>
	</cModal>
</cNav>
<div style='height: calc(100% - 84px);overflow-y: scroll;padding-bottom: 10px;' v-if='thisGroup' :id='"show_words_" + thisGroup.account'>
  <div v-for='(item,index) in $store.state.groupMessageList[$route.query.account]'>
    <div v-if='item.sendAccount == member.account' style=' margin: 5px; margin-bottom: 0px;text-align: right'>
        <strong style='color:#3c3ce2'>{{member.username}}</strong>
        <p  style='border: 1px solid black; border-radius: 5px; margin: 0px; margin-bottom: 0px;'>{{item.content}}</p>
    </div>
    <div v-else  style='margin: 5px; margin-bottom: 0px;'>
        <strong style='color:#3c3ce2'>{{item.sendNickname}} </strong>
        <p style='border: 1px solid black;border-radius: 5px;margin: 0px;margin-bottom: 0px;'>{{item.content}}</p>
    </div>
  </div>
</div>
<div style="height:40px;border-top:1px solid black;padding:4px 10px" v-if="thisGroup">
    <input v-model="message" style="width: calc(100% - 48px); height: 100%;"/>
    <button @click="send" style="float:right; height: 100%;">发送</button>
</div>
`
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
export default {
    template: template,
    data: function () {
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
                down(this.$route.query.account)
            },
            deep: true,
            immediate: true,
        }
    },
    computed: {
        member() {
            // 当前登录用户 
            return this.$store.state.member;
        },
        groupMap(){
            return this.$store.state.groupMap
        },
        thisGroup() {
            return this.groupMap[this.$route.query.account]
        },
        
        thisGroupSocket() {
            return this.$store.state.socketGroup[this.thisGroup.account]
        }
    },
    methods: {
        toGroupInfo() {
            this.$router.push({
                path: '/group-info',
                query: {
                    account: this.thisGroup.account
                }
            })
        },
        send() {
            this.thisGroupSocket.send(JSON.stringify({
                type: "message",
                content: this.message,
            }))
        }
    },
    created() {
        // 100毫秒后执行
        setTimeout(() => {
            down(this.$route.query.account)
        }, 100);
    }
}
