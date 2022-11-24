let template = // html
	`
<section>
	<aside id="aside" v-bind:class="[$store.state.indexItem == 'aside' ? 'z-index-top':'']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<a class='padding-right-10' @click='selectTag("xx")'>消息</a>
			<a class='padding-right-10' @click='selectTag("hy")'>好友</a>
			<a class='padding-right-10' @click='selectTag("qz")'>群组</a>
			<router-link class='padding-right-10' to="/add">添加</router-link>
			<router-link class='float-right' to="/home">我的</router-link>
		</div>
		<div style="height:calc(100% - 70px); background-color:rgba(255, 190, 117, 0.3)">
			<ul v-if='type == "xx"' v-for="(item,index) in $store.state.messageMap">
				<li v-if='item.message.length > 0' style="border-bottom: 1px solid #a7a7a7;" @click='openMessage(item)'>
					💬{{item.name}}:{{item.message[0].text}}
				</li>
			</ul>
			<ul v-if='type == "qz"' v-for="item in list">
				<li v-if='type == "qz"'  style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({path:'/message-group', query:{url: item.ws}})">
					💬{{item.groupName}}
				</li>
			</ul>
			<ul v-if='type == "hy"' v-for="item in list">
				<li v-if='type == "hy"'  style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({path:'/message-user', query:{url: item.ws}})">
					💬{{item.nickname}}
				</li>
			</ul>
		</div>
		<div style="height:30px;line-height:30px;background-color:rgba(255, 190, 117, 0.11);padding: 0 10px;border-top: 1px solid #a7a7a7;">
			巴啦啦跨平台协议.关于
		</div>
	</aside>
		
	<main id="main" v-bind:class="[$store.state.indexItem == 'main' ? 'z-index-top' : '']">
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
		openMessage(item){
			if(item.type == 'message-group'){
				this.$router.push({path:'/message-group', query:{url: item.ws}})
			}
			if(item.type == 'message-user'){
				this.$router.push({path:'/message-user', query:{url: item.ws}})
			}
		},
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
				this.$store.state.socketLocal = new WebSocket("ws://"+window.location.host+"/local/" + localStorage.getItem("token"));
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
				type: "groupList"
			}))

			this.$store.state.socketLocal.send(JSON.stringify({
				type: "userList"
			}))

			this.$store.state.socketLocal.send(JSON.stringify({
				type: "messageList"
			}))
			
		},
		websocketonerror() {
			this.initWebSocket();
		},
		websocketonmessage(e) {
			let _this = this;
			let msg = JSON.parse(e.data);
			if (msg.type == 'groupList') {
				for (let index in msg.data) {
					this.all.group[msg.data[index]["groupName"]] = msg.data[index]
				}
				for (let index in this.all.group) {
					let group = this.all.group[index]
					this.$store.state.socketGroupMap[group.ws] = new WebSocket(group.ws + "?checkUrl=" + localStorage.getItem('checkUrl'));
					this.$store.state.socketGroupMap[group.ws].onmessage = function (e) {
						let msg = JSON.parse(e.data)
						if (msg.type == 'message-group') {
							_this.$store.state.messageMap[group.ws].message.push(msg);
						}
						if (msg.type == 'messageList') {
							_this.$store.state.messageMap[group.ws].message.push(...msg.data);
						}
					}
					this.$store.state.socketGroupMap[group.ws].onopen = function () {
						if (_this.$store.state.messageMap[group.ws] == undefined) {
							_this.$store.state.messageMap[group.ws] = { name: group.groupName, type: "message-group", ws: group.ws, message: [] }
						}
						_this.$store.state.socketGroupMap[group.ws].send(JSON.stringify({
							type: "messageList"
						}))
					}
					this.$store.state.socketGroupMap[group.ws].onerror = function (e) {

					}
					this.$store.state.socketGroupMap[group.ws].onclose = function () {

					}
					console.log(this.$store.state.socketGroupMap)
				}
			}
			if (msg.type == 'userList') {
				this.all.user = msg.data
			}
			if (msg.type == 'message-user') {
				if (_this.$store.state.messageMap[msg.ws] == undefined) {
					_this.$store.state.messageMap[msg.ws] = { name: msg.name, type: "message-user", ws: msg.ws, message: [] }
				}
				_this.$store.state.messageMap[msg.ws].message.push(msg);
			}
			if (msg.type == 'messageList') {
				let ml = msg.data;
				for(let i in ml){
					let m = ml[i]
					if (_this.$store.state.messageMap[m.ws] == undefined) {
						_this.$store.state.messageMap[m.ws] = { name: m.name, type: "message-user", ws: m.ws, message: [] }
					}
					_this.$store.state.messageMap[m.ws].message.push(m);
				}
			}
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
			localStorage.setItem("checkUrl", "http://"+window.location.host+"/check/" + response.data)
		});

		request({
			url: "/user",
			method: "GET"
		}).then((response) => {
			this.$store.state.user = response.data
		});

	}
}
