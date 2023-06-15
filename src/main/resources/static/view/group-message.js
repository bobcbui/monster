let template = // html
    `
<div style='height: calc(100% - 30px);overflow-y: scroll;'>
<div style='border-bottom:1px solid red' @click='toGroupInfo()'>群：{{thisGroup == undefined ? "" : thisGroup.name}}</div>
  <div v-for='(item,index) in $store.state.groupListMessage[$route.query.account]'>
  <p v-if='item.sendAccount == member.account' style='text-align: right;border: 1px solid black; border-radius: 5px; margin: 5px; margin-bottom: 0px;'>
        {{item.content}} : {{member.username}}
    </p>
    <p v-else style='border: 1px solid black;border-radius: 5px;margin: 5px;margin-bottom: 0px;'>
        {{item.sendNickname}} : {{item.content}}
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
            // 当前登录用户 
            return this.$store.state.member;
        },
        thisGroup() {
            let _thisGroup;
            this.$store.state.groupList.filter(item => {
                if (item.account == this.$route.query.account) {
                    _thisGroup = item; 
                }
            })
            return _thisGroup;
        },
        toGroupInfo() {
            this.$router.push({
                path: '/group-info',
                query: {
                    account: this.thisGroup.account
                }
            })
        },
        thisGroupSocket() {
            return this.$store.state.socketGroup[this.thisGroup.account]
        }
    },
    methods: {
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
