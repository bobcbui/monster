let template = // html
`
<div style='text-align: center;padding-top:8px'>
	<router-link :to="{name:'message'}"> 消息 </router-link>
	<router-link :to="{name:'group'}"> 群组 </router-link>
	<router-link :to="{name:'member'}"> 好友 </router-link>
	<router-link :to="{name:'me'}"> 我的 </router-link>
	<hr>
</div>
<router-view></router-view>
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
			let that = this;
			socket.onopen = function(e){
				that.$store.state.socketGroup[url] = socket;
			};
			socket.onmessage = function(e){
				
			};
			socket.onclose = function(e){
				
			};
			socket.onerror = function(e){
				
			};
		},
		createMemberWebSocket(url){
			let socket = new WebSocket(url + "?checkUrl=" + localStorage.getItem("checkUrl"));
			let that = this;
			socket.onopen = function(e){
				that.$store.state.socketMember = socket;
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
			let that = this;
			socket.onopen = function(e){
				that.$store.state.socketLocal = socket;
			};
			socket.onmessage = function(e){
				
			};
			socket.onclose = function(e){
				
			};
			socket.onerror = function(e){
				
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
	}
}
