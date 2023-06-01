let template = // html
`
<div style='text-align: center;padding-top:8px'>
	<router-link :to="{name:'message'}"> 消息 </router-link>
	<router-link :to="{name:'group'}"> 群组 </router-link>
	<router-link :to="{name:'member'}"> 好友 </router-link>
	<router-link :to="{name:'me'}"> 我的 </router-link>
	<hr>
</div>
<div>
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
			let socket = new WebSocket(url + "?token=" + localStorage.getItem("token"));
			this.$store.state.socketLocal = socket;
			let that = this;

			socket.onopen = function(e){
				// 加载成功获取member 和 group 列表
				socket.send(JSON.stringify({type: "memberList"}))
				socket.send(JSON.stringify({type: "groupList"}))
			};
			socket.onmessage = function(e){
				let data = JSON.parse(e.data);

				if(data.type == "memberList"){
					that.$store.state.memberList = JSON.parse(data.data)
					console.log(data.data)
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
		request({
			url: "/authenticate/info",
			method: "GET"
		}).then((response) => {
			this.$store.state.member = response.data
		});
		this.createLoadWebSocket("ws://localhost:9090/local")
	},
	mounted() {
	}
}
