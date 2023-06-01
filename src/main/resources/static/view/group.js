let template = // html
`
<div style='padding:10px;padding-top:0px;'>
	<button style='width:100%;margin-bottom:5px;' @click='showCreateGroup = true'>创建群</button>
	<button style='width:100%;margin-bottom:5px;'>加入群</button>
</div>
<ul style='margin:0px'>
	<li style='padding:0px 10px;border:1px solid black;margin:0px 10px 10px 10px;border-radius:5px;' v-for='(item,index) in groupList' :key='index'>{{item.name}}</li>
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
		<input style='width:100%;margin-bottom:5px;' placeholder='群号' v-model='joinGroupForm.ws' >
		<button style='width:100%;margin-bottom:5px;' @click='searchGroup'>查询</button>
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
				ws: "",
			},
		}
	},
	destroyed() {

	},
	// 计算属性
	computed: {
		groupList(){
			return this.$store.state.groupList;
		}
	},
	methods: {
		createGroup(){
			this.$store.state.socketLocal.send(JSON.stringify({ type: "createGroup", data: this.createGroupForm}))
		},
		searchGroup(){
			request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
                let socket = new WebSocket(this.joinGroupForm.ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let _this = this;
                socket.onopen = function(e){
					_this.groupSocket = socket;
                    socket.send(JSON.stringify({ type: "searchGroup"}))
                };
                socket.onmessage = function(e){
                    let data = JSON.parse(e.data);
                    if(data.type == "searchGroup"){
                       console.log("========================");
                    }
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
                
            }).catch(function (error) {
                console.log(error);
            });
		
		}
	},
	created() {
	
	}
}
