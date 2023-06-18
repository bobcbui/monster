let template = // html
`
<div style='text-align: center;padding-top:8px;border-bottom:1px solid black;padding-bottom:10px'>
	<router-link :to="{name:'message'}"> 消息 </router-link>
	<router-link :to="{name:'group'}"> 群组 </router-link>
	<router-link :to="{name:'member'}"> 好友 </router-link>
	<router-link :to="{name:'me'}"> 我的 </router-link>
</div>
<div style='height: calc(100% - 40px);'>
	<router-view></router-view>
</div>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
		}
	},
	destroyed() {
		
	},
	methods: {
		createGroupWebSocket(account){
			let that = this;
			// one-token
			request({
				method: 'get',
				url: '/one-token',
			}).then(response => {
				// base64 decode
				let ws = decodeAccount(account);
				let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
				that.$store.state.socketGroup[account] = socket;
				socket.onopen = function(e){
					socket.send(JSON.stringify({type: "groupMessage"}))
				};
				socket.onmessage = function(e){
					let data = JSON.parse(e.data);
					if(data.type == "message"){
						if(that.$store.state.groupListMessage[account] == undefined){
							that.$store.state.groupListMessage[account] = []
						}
						that.$store.state.groupListMessage[account].push(data.data)
					}
					if(data.type == "groupMessage"){
						that.$store.state.groupListMessage[account] = data.data
					}
				};
				socket.onclose = function(e){
		
				};
				socket.onerror = function(e){
		
				};
			}).catch(function (error) {
		
			});
		
		},
		createLoadWebSocket(url){
			let that = this;
			console.log(url + "?token=" + localStorage.getItem("token"))
			let socket = new WebSocket(url + "?token=" + localStorage.getItem("token"));
			socket.onopen = function(e){
				// 加载成功获取member 和 group 列表
				that.$store.state.socketLocal = socket;
				socket.send(JSON.stringify({type: "memberList"}))
				socket.send(JSON.stringify({type: "groupList"}))
			};
			socket.onmessage = function(e){
				let data = JSON.parse(e.data);

				if(data.type == "memberList"){
					that.$store.state.memberList = data.data
					console.log(data.data)
				}

				if(data.type == "loadMemberMessage"){
					that.$store.state.memberListMessage[data.data.account] = data.data.data
				}

				if(data.type == "memberMessage"){
					console.log(data)
				}
				
				if(data.type == "groupList"){
					that.$store.state.groupList = data.data
					data.data.forEach((item) => {
						that.createGroupWebSocket(item.groupAccount)
					})
					
				}

				if(data.type == "groupMessage"){
					that.$store.state.groupList = data
					console.log(data.data)
				}
				if(data.type == "message"){
					data.data.to = false
					data.data.state = true
					if(that.$store.state.memberListMessage[data.data.sendAccount] == undefined){
						that.$store.state.memberListMessage[data.data.sendAccount] = []
					}
					that.$store.state.memberListMessage[data.data.sendAccount].push(data.data)
				}
			};
			socket.onclose = function(e){
				console.log("onclose")
				console.log(e)
			};
			socket.onerror = function(e){
				console.log("onerror")
				console.log(e)
			};
			
		}
	},
	created() {
		
	},
	mounted() {
		request({
			url: "/authenticate/info",
			method: "GET"
		}).then((response) => {
			this.$store.state.member = response.data
		});
		this.createLoadWebSocket("ws://localhost:9090/local")
	}
}
