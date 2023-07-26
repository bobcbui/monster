let template = // html
    `
<cNav title='好友'>
	<cModal buttonName='添加'>
        <input class='w-100 m-b-5' v-model="account" placeholder='账户地址'>
        <button class='w-100 m-b-5' @click="info()">查询好友</button>
        <div v-if='searchMember.account' class='p-b-10 p-t-0'>
            名称 ： {{searchMember.username}}
            <br>
            账户 ： {{searchMember.account}}
        </div>
        <input  v-if='searchMember.account' class='w-100  m-b-5' v-model='context' placeholder='请求信息'>
        <button v-if='searchMember.account' style='width:100%;margin-bottom:5px;' @click="join">增加好友</button>
	</cModal>
</cNav>
<div class='p-10'>
    <ul class='m-0'>
        <li style='padding:5px;border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;' 
        v-for='(item,index) in memberMap' :key='index' @click='toMemberMessage(item)'>
            {{item.username}}
        </li>
    </ul>
</div>
`
import cModal from '../component/modal.js'
import cNav from '../component/nav.js'
import request from '../lib/request.js'
export default {
    template: template,
    data: function () {
        return {
            form: {
                type: "message-member",
                text: "",
            },
            account: "",
            checkUrl: "",
            memberSocket: null,
            searchMember: {},
            context: ""
        }
    },
    components: {
        cNav, cModal
    },
    wathc: {
    },
    // 计算属性
    computed: {
        memberMap() {
            return this.$store.state.memberMap;
        },
        member() {
            return this.$store.state.member;
        }
    },
    methods: {
        toMemberMessage(item) {
            this.$router.push({ name: 'member-message', query: { account: item.account, routerName: this.$route.name } })
        },
        info() {
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeWsAccount(this.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let that = this;
                socket.onopen = function (e) {
                    that.memberSocket = socket;
                    socket.send(JSON.stringify({ type: "info" }))
                };
                socket.onmessage = function (e) {
                    let data = JSON.parse(e.data);
                    if (data.type == "join") {
                        that.$store.state.socketLocal.send({ type: "joinMember", data: data.data,context: that.context })
                        that.$store.state.socketLocal.send({ type: "memberMap" })
                    }
                    if (data.type == "info") {
                        that.searchMember = data.data;
                    }
                };
                socket.onclose = function (e) {

                };
                socket.onerror = function (e) {

                };

            }).catch(function (error) {
                console.log(error);
            });
        },
        join() {
            this.memberSocket.send(JSON.stringify({ type: "join", context: this.context }))
        }
    },
    created() {

    }
}
