let template = // html
`
<cNav title='群组'>
	<cModal buttonName='创建'>
		<input class='w-100 m-b-5' placeholder='群号' v-model='createGroupForm.name'>
		<input class='w-100 m-b-5' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button class='w-100 m-b-5' @click='createGroup'>创建</button>
	</cModal>&nbsp;
	<cModal buttonName='加群'>
		{{joinGroupForm.name}}
		<input class='w-100 m-b-5' placeholder='群号' v-model='joinGroupForm.account' >
		<button class='w-100 m-b-5' @click='searchGroup'>查询</button>
		<button class='w-100 m-b-5' v-if='joinGroupForm.account != null' @click='join'>加入群</button>
	</cModal>
</cNav>
<div class='p-10'>
	<ul class='m-0'>
		<li style='padding:0px 10px;border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;' v-for='(item,index) in groupMap' :key='index' @click='toGroupMessage(item)'>
		{{item.name}}</li>
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
			showCreateGroup: false,
			createGroupForm: {
				name: "",
				nickname: "",
			},
			showJoinGroup: false,
			joinGroupForm: {
				
			}
		}
	},
	components: {
		cNav,cModal
	},
	destroyed() {

	},
	// 计算属性
	computed: {
		groupMap(){
			return this.$store.state.groupMap;
		}
	},
	methods: {
		toGroupMessage(item){
			this.$router.push({ path: '/group-message', query: { account: item.account } });
		},
		createGroup(){
			this.$store.state.socketLocal.send(JSON.stringify({ type: "createGroup", data: this.createGroupForm}))
		},
		searchGroup(){
			request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
				let account = this.joinGroupForm.account
				let ws = decodeWsAccount(this.joinGroupForm.account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
				this.$store.state.socketGroup[account] = socket;

                let _this = this;
                socket.onopen = function(e){
					_this.groupSocket = socket;
                    socket.send(JSON.stringify({ type: "info"}))

                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "searchGroup"){
                       console.log("========================");
                    }
					if(data.type == "info"){
						_this.joinGroupForm = data.data;
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
			this.joinGroupForm.memberAccount = this.$store.state.member.account;
			this.$store.state.socketGroup[this.joinGroupForm.account].send(JSON.stringify({ type: "join", data: this.joinGroupForm}))
			this.$store.state.socketLocal.send(JSON.stringify({ type: "join", data: this.joinGroupForm}))
		}
	},
	created() {
	
	}
}
