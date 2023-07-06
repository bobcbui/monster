let template = // html
`
<cNav title='好友'>
	<button @click='$refs["cModal"].show = true'>添加</button>
	<cModal ref='cModal'>
        <input  style='width:100%;margin-bottom:5px;' v-model="account" placeholder='账户地址'>
        <button style='width:100%;margin-bottom:5px;' @click="info()">查询好友</button>
        <div v-if='searchMember' style='padding:10px;padding-top:0px;'>
            username:{{searchMember.username}}
        </div>
        <button v-if='searchMember' style='width:100%;margin-bottom:5px;' @click="join">增加好友</button>
	</cModal>
</cNav>
<div style='padding:10px'>
    <ul style='margin:0px'>
        <li style='padding:0px 5px;border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;' 
        v-for='(item,index) in memberMap' :key='index' @click='toMemberMessage(item)'>
            {{item.username}}
        </li>
    </ul>
</div>
`
import request from '../lib/request.js'
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
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
            this.memberSocket.send(JSON.stringify({ type: "join"}))
        }
    },
    created() {
       
    }
}
