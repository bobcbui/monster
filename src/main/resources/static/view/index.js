let template = // html
`
<div style='text-align: center;padding-top:8px'>
	<router-link :to="{name:'message'}"> message </router-link>
	<router-link :to="{name:'group'}"> group </router-link>
	<router-link :to="{name:'member'}"> member </router-link>
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
		createLocalWebSocket(ws){
			// 建立连接
			let localWebSocket = new WebSocket(ws);
			localWebSocket.onopen = function(){
				console.log("onopen")
				localWebSocket.send(JSON.stringify({
					type:"login",token:localStorage.getItem("token")
				}))
			}
			localWebSocket.onmessage = function(e){
				console.log(e)
				let data = JSON.parse(e.data);
				switch(data.type){
					case 'system':
						console.log("System:"+data.context)
						break;
					case 'group':
						break;
					case 'member':
						break;
					case 'loginMessage':
						if(data.context === "OK"){
							// 登录成功后处理的事情
							localWebSocket.send(JSON.stringify({
								'type':'groupList'
							}))
						}
						break;
				}
			}
			localWebSocket.onclose = function(e){
				console.log(e)
			}
			localWebSocket.onerror = function(e){
				console.log(e)
			}
			return localWebSocket;
			
		},
		createMemberWebSocket(ws){
			// 建立连接
			let localWebSocket = new WebSocket(ws);
			localWebSocket.onopen = function(){
				console.log("onopen")
			}
			localWebSocket.onmessage = function(e){
				
				console.log(e)
			}
			localWebSocket.onclose = function(e){
				console.log(e)
			}
			localWebSocket.onerror = function(e){
				console.log(e)
			}
			return localWebSocket;
		},
		createGroupWebSocket(ws){
			// 建立连接
			let localWebSocket = new WebSocket(ws);
			localWebSocket.onopen = function(){

				console.log("onopen")
				{
					type:'loadGroup'
				}
				localWebSocket.send()
			}
			localWebSocket.onmessage = function(e){
				console.log(e)
			}
			localWebSocket.onclose = function(e){
				console.log(e)
			}
			localWebSocket.onerror = function(e){
				console.log(e)
			}
			return localWebSocket;
		}

	},
	created() {
		request({
			url: "/authenticate/info",
			method: "GET"
		}).then((response) => {
			this.$store.state.member = response.data
		});
		
		// 建立连接
		this.$store.state.localWebSocket = this.createLocalWebSocket("ws://localhost:9090/local");
	}
}
