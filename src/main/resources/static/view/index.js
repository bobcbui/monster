let template = // html
	`
<section>
	<aside id="aside" v-bind:class="[$store.state.indexItem == 'aside' ? 'z-index-top':'']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<a style="padding-right:10px" @click='selectTag("xx")'>消息</a>
			<a style="padding-right:10px" @click='selectTag("hy")'>好友</a>
			<a style="padding-right:10px" @click='selectTag("qz")'>群组</a>
			<router-link style="padding-right:10px" to="/add">添加</router-link>
			<router-link  style="float:right" to="/home">我的</router-link>
		</div>
		<div style="height:calc(100% - 70px); background-color:rgba(255, 190, 117, 0.3)">
			<ul>
				<li v-if='type == "xx"' v-for="item in list" style="border-bottom: 1px solid #a7a7a7;">
					💬{{item.messageName}}
				</li>
				<li v-if='type == "qz"' v-for="item in list" style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({path:'/group-message/'+item.groupName})">
					💬{{item.groupName}}
				</li>
			</ul>
		</div>
		<div style="height:30px;line-height:30px;background-color:rgba(255, 190, 117, 0.11);padding: 0 10px;border-top: 1px solid #a7a7a7;">
			巴啦啦跨平台协议.关于
		</div>
	</aside>
		
	<main id="main" v-bind:class="[$store.state.indexItem == 'main' ? 'z-index-top' : '']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<router-link to="/">返回</router-link>
		</div>
		<router-view></router-view>
	</main>
</section>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			type: "xx",
			list: [],
			all: {
				message: {
					
				},
				group: {
					
				},
				user: {

				}
			}
		}
	},
	destroyed() {
		this.$store.state.socketLocal.close() //离开路由之后断开websocket连接
	},
	methods: {
		selectTag(tag) {
			this.type = tag
			if (tag == 'xx') {
				this.list = this.all.message
			}
			if (tag == 'hy') {
				this.list = this.all.user
			}
			if (tag == 'qz') {
				this.list = this.all.group
			}
		},
		initWebSocket() {
			try {
				this.$store.state.socketLocal = new WebSocket("ws://localhost:9090/local/" + localStorage.getItem("token"));
				this.$store.state.socketLocal.onmessage = this.websocketonmessage;
				this.$store.state.socketLocal.onopen = this.websocketonopen;
				this.$store.state.socketLocal.onerror = this.websocketonerror;
				this.$store.state.socketLocal.onclose = this.websocketclose;
			} catch (e) {
				console.log(e)
			}
		},
		websocketonopen() {
			this.$store.state.socketLocal.send(JSON.stringify({
				type:"groupList"
			}))
	
			this.$store.state.socketLocal.send(JSON.stringify({
				type:"userList"
			}))

		},
		websocketonerror() {
			this.initWebSocket();
		},
		websocketonmessage(e) {
			let _this = this;
			let msg = JSON.parse(e.data);
			if(msg.type == 'groupList'){
				for(let index in msg.data){
					console.log(msg.data[index])
					this.all.group[msg.data[index]["groupName"]] = msg.data[index]
				}
				for(let index in this.all.group){
					let group = this.all.group[index]
					this.$store.state.socketGroupMap[group.groupName] = new WebSocket("ws://localhost:9090/group/" + group.groupName);
					this.$store.state.socketGroupMap[group.groupName].onmessage = function(e){
						
					}
					this.$store.state.socketGroupMap[group.groupName].onopen = function(){
						
					}
					this.$store.state.socketGroupMap[group.groupName].onerror = function(e){
						
					}
					this.$store.state.socketGroupMap[group.groupName].onclose = function(){
						
					}
					console.log(this.$store.state.socketGroupMap)
				}
			}
			if(msg.type == 'userList'){

			}
			if(msg.type == 'message'){
				
			}

			console.log(e)
		},
		websocketclose(e) {
			console.log("断开连接", e);
		},
	},
	created() {
		
		this.initWebSocket();

		request({
			url: "/token",
			method: "POST"
		}).then((response) => {
			localStorage.setItem("checkToken", response.data)
		});

		request({
			url: "/user",
			method: "GET"
		}).then((response) => {
			this.$store.state.user = response.data
		});

		

		
	}
}
