let template = // html
`
<cNav title='好友'>
	<cModal buttonName='添加'>
        <input class='w-100 m-b-5' v-model="account" placeholder='账户地址'>
        <button class='w-100 m-b-5' @click="info()">查询好友</button>
        <div v-if='searchMember' class='p-10 p-t-0'>
            username:{{searchMember.username}}
        </div>
        <input class='w-100  m-b-5' v-model='context' placeholder='请求信息'>
        <button v-if='searchMember' style='width:100%;margin-bottom:5px;' @click="join">增加好友</button>
	</cModal>
</cNav>
<div class='p-10'>
    <ul class='m-0'>
        <li style='padding:0px 5px;border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;' 
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
            searchMember: null,
            context:""
        }
    },
    components:{
        cNav,cModal
    },
    wathc:{
    },
    // 计算属性
    computed: {
        memberMap(){
            return this.$store.state.memberMap;
        },
        member(){
			return this.$store.state.member;
		}
    },
    methods: {
        toMemberMessage(item){
            this.$router.push({ name: 'member-message', query: { account: item.account, routerName: this.$route.name}})
        },
        info(){
            request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let ws = decodeWsAccount(this.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let _this = this;
                socket.onopen = function(e){
                    _this.memberSocket = socket;
                    socket.send(JSON.stringify({ type: "info"}))
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "join"){
                        _this.$store.state.socketLocal.send(JSON.stringify({ type: "addMember", data: data.data}))
                        _this.$store.state.socketLocal.send(JSON.stringify({ type: "memberMap"}))
                    }
                    if(data.type == "info"){
                        _this.searchMember = data.data;
                    }
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
                
            }).catch(function (error) {
                console.log(error);
            });
        },
        join(){
            this.memberSocket.send(JSON.stringify({ type: "join",context: this.context}))
        }
    },
    created() {
       
    }
}
