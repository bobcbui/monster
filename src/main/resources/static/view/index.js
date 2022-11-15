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
				<li v-if='type == "xx"' v-for="(item,index) in $store.state.messageMap" style="border-bottom: 1px solid #a7a7a7;">
					💬{{item.name}}<span v-if='item.message.length > 0'>:{{item.message[0].text}}</span>
				</li>
				<li v-if='type == "qz"' v-for="item in list" style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({path:'/message-group', query:{url: item.ws}})">
					💬{{item.groupName}}
				</li>
				<li v-if='type == "hy"' v-for="item in list" style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({path:'/message-user', query:{url: item.ws}})">
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
					this.all.group[msg.data[index]["groupName"]] = msg.data[index]
				}
				for(let index in this.all.group){
					let group = this.all.group[index]
					this.$store.state.socketGroupMap[group.ws] = new WebSocket(group.ws+"?checkUrl="+localStorage.getItem('checkUrl'));
					this.$store.state.socketGroupMap[group.ws].onmessage = function(e){
						let msg = JSON.parse(e.data)
						if(_this.$store.state.messageMap[group.ws] == undefined){
							_this.$store.state.messageMap[group.ws] = {name:msg.groupName,type:"message-group",ws:msg.ws,message:[]}
						}
						if(msg.type == 'message'){
							_this.$store.state.messageMap[group.ws].message.push(msg);
						}
					}
					this.$store.state.socketGroupMap[group.ws].onopen = function(){
						
					}
					this.$store.state.socketGroupMap[group.ws].onerror = function(e){
						
					}
					this.$store.state.socketGroupMap[group.ws].onclose = function(){
						
					}
					console.log(this.$store.state.socketGroupMap)
				}
			}
			if(msg.type == 'userList'){
				this.all.user = msg.data
			}
			if(msg.type == 'message'){
				if(msg.type == 'message'){
					if(_this.$store.state.messageMap[msg.ws] == undefined){
						_this.$store.state.messageMap[msg.ws] = {name:msg.name,type:"message-user",ws:msg.ws , message:[]}
					}
					_this.$store.state.messageMap[msg.ws].message.push(msg);
				}
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
			localStorage.setItem("checkUrl","http://localhost:9090/check/"+response.data)
		});

		request({
			url: "/user",
			method: "GET"
		}).then((response) => {
			this.$store.state.user = response.data
		});

		

		
	}
}
