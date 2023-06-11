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
		createGroupWebSocket(url){
			let socket = new WebSocket(url + "?checkUrl=" + localStorage.getItem("checkUrl"));
			this.$store.state.socketGroup[url] = socket;
			socket.onopen = function(e){
				
			};
			socket.onmessage = function(e){
				
			};
			socket.onclose = function(e){
				
			};
			socket.onerror = function(e){
				
			};
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
					that.$store.state.memberList = JSON.parse(data.data)
					console.log(data.data)
				}

				if(data.type == "loadMemberMessage"){
					debugger
					that.$store.state.memberListMessage[data.account] = data.data
				}

				if(data.type == "memberMessage"){
					//that.$store.state.memberMessage[data.ws].push(data)
					console.log(data)
				}
				
				if(data.type == "groupList"){
					that.$store.state.groupList = JSON.parse(data.data)
					console.log(data.data)
				}

				if(data.type == "groupMessage"){
					that.$store.state.groupList = data
					console.log(data.data)
				}
				if(data.type == "message"){
					data.to = false
					data.state = true
					if(that.$store.state.memberListMessage[data.sendAccount] == undefined){
						that.$store.state.memberListMessage[data.sendAccount] = []
					}
					that.$store.state.memberListMessage[data.sendAccount].push(data)
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
