let template = // html
`
<div style='padding:10px;padding-top:0px;'>
	<button style='width:100%;margin-bottom:5px;' @click='showCreateGroup = true'>创建群</button>
	<button style='width:100%;margin-bottom:5px;' @click='showJoinGroup = true'>加入群</button>
</div>
<ul style='margin:0px'>
	<li style='padding:0px 10px;border:1px solid black;margin:0px 10px 10px 10px;border-radius:5px;' v-for='(item,index) in groupMap' :key='index' @click='toGroupMessage(item)'>
	{{item.alias}}</li>
</ul>
<div class='mode' v-if='showCreateGroup'>
	<div class='mode-body'>
		<button style='width:100%;margin-bottom:5px;' @click='showCreateGroup = false'>关闭</button>
		<br><br>
		<input style='width:100%;margin-bottom:5px;' placeholder='群号' v-model='createGroupForm.name' >
		<input style='width:100%;margin-bottom:5px;' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button style='width:100%;margin-bottom:5px;' @click='createGroup'>创建</button>
	</div>
</div>

<div class='mode' v-if='showJoinGroup'>
	<div class='mode-body'>
		<button style='width:100%;margin-bottom:5px;' @click='showJoinGroup = false'>关闭</button>
		<br><br>
		{{joinGroupForm.name}}
		<input style='width:100%;margin-bottom:5px;' placeholder='群号' v-model='joinGroupForm.ws' >
		<button style='width:100%;margin-bottom:5px;' @click='searchGroup'>查询</button>
		<button v-if='joinGroupForm.account != null' style='width:100%;margin-bottom:5px;' @click='joinGroup'>加入群</button>
	</div>
</div>
`

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
			this.$router.push({ path: '/group-message', query: { account: item.groupAccount } });
		},
		createGroup(){
			this.$store.state.socketLocal.send(JSON.stringify({ type: "createGroup", data: this.createGroupForm}))
		},
		searchGroup(){
			request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
				let account = this.joinGroupForm.ws
				let ws = decodeAccount(this.joinGroupForm.ws);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
				this.$store.state.socketGroup[account] = socket;

                let _this = this;
                socket.onopen = function(e){
					_this.groupSocket = socket;
                    socket.send(JSON.stringify({ type: "groupInfo"}))

                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "searchGroup"){
                       console.log("========================");
                    }
					if(data.type == "groupInfo"){
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
		joinGroup(){
			this.joinGroupForm.memberAccount = this.$store.state.member.account;
			this.$store.state.socketGroup[this.joinGroupForm.account].send(JSON.stringify({ type: "joinGroup", data: this.joinGroupForm}))
			this.$store.state.socketLocal.send(JSON.stringify({ type: "joinGroup", data: this.joinGroupForm}))
		}
	},
	created() {
	
	}
}
