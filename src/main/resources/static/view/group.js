let template = // html
`
<cNav title='群组'>
	<button @click='$refs["cModal"].show = true'>创建</button>&nbsp;
	<cModal ref='cModal'>
		<input style='width:100%;margin-bottom:5px;' placeholder='群号' v-model='createGroupForm.name' >
		<input style='width:100%;margin-bottom:5px;' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button style='width:100%;margin-bottom:5px;' @click='createGroup'>创建</button>
	</cModal>
	<button @click='$refs["jrq"].show = true'>加群</button>
	<cModal ref='jrq'>
		{{joinGroupForm.name}}
		<input style='width:100%;margin-bottom:5px;' placeholder='群号' v-model='joinGroupForm.account' >
		<button style='width:100%;margin-bottom:5px;' @click='searchGroup'>查询</button>
		<button v-if='joinGroupForm.account != null' style='width:100%;margin-bottom:5px;' @click='join'>加入群</button>
	</cModal>
</cNav>
<div style='padding:10px'>
	<ul style='margin:0px'>
		<li style='padding:0px 10px;border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;' v-for='(item,index) in groupMap' :key='index' @click='toGroupMessage(item)'>
		{{item.alias}}</li>
	</ul>
</div>
`
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
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
