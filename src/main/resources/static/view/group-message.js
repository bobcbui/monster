let template = // html
`
<cNav v-if='thisGroup' :title='thisGroup.alias' back='true'>
    <button @click='$router.push({name:"message"})' >返回</button>&nbsp;
    <button @click='$refs["cModal"].show = true'>设置</button>
	<cModal ref='cModal'>
        名称：{{thisGroup.alias}}<br/>
        群号：{{thisGroup.groupAccount}}<br/>
	</cModal>
</cNav>
<div style='height: calc(100% - 72px);overflow-y: scroll;' v-if='thisGroup'>
  <div v-for='(item,index) in $store.state.groupMessageList[$route.query.account]'>
  <p v-if='item.sendAccount == member.account' style='text-align: right;border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;'>
        {{item.content}} : {{member.username}}
    </p>
    <p v-else style='border: 1px solid black;border-radius: 5px;margin: 5px;margin-bottom: 0px;'>
        {{item.sendNickname}} : {{item.content}}
    </p>
  </div>
</div>
<div style="height:30px;border-top:1px solid black;padding:2px 10px" v-if="thisGroup">
    <input v-model="message" style="width: calc(100% - 48px); height: 23px;"/>
    <button @click="send" style="float:right; height: 23px;margin-top: 1px;">发送</button>
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
    wathc: {

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
            return this.$store.state.socketGroup[this.thisGroup.groupAccount]
        }
    },
    methods: {
        toGroupInfo() {
            this.$router.push({
                path: '/group-info',
                query: {
                    account: this.thisGroup.groupAccount
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
        
    }
}
