let template = // html
	`
<section>
	<aside id="aside" v-bind:class="[$store.state.indexItem == 'aside' ? 'z-index-top':'']">
		<div style="height:40px;background-color:azure;line-height:40px;padding:0 10px;border-bottom: 1px solid #a7a7a7;">
			<a style="padding-right:10px" @click='selectTag("xx")'>消息</a>
			<a style="padding-right:10px" @click='selectTag("hy")'>好友</a>
			<a style="padding-right:10px" @click='selectTag("qz")'>群组</a>
			<router-link style="padding-right:10px" to="/create">创建</router-link>
			<router-link style="padding-right:10px" to="/add">添加</router-link>
			<router-link  style="float:right" to="/home">我的</router-link>
		</div>
		<div style="height:calc(100% - 70px); background-color:rgba(255, 190, 117, 0.3)">
			<ul>
				<li v-for="item in list" style="border-bottom: 1px solid #a7a7a7;" @click="$router.push({name:'message',query: {type:item.type,receiveId:item.receiveId}})">
					💬{{item.username}}
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
			type: "message",
			list: [],
			allList: {
				messageList: [
					{ username: "为什么四道口附近",url:"asdf" }, 
					{ username: "df对方的" ,url:"dfdf"}, 
					{ username: "许昌许昌v" ,url:"cvcv"}
				],
				groupList: [
					{ username: "Java学习交流群" ,type:"groupMessage",receiveId:"1"},
					{ username: "游戏交流群" ,type:"groupMessage",receiveId:"2"}, 
					{ username: "社会主义交流群" ,type:"groupMessage",receiveId:"3"}
				],
				memberList: [
					{ username: "黄磊" ,url:"bb"}, 
					{ username: "黄行" ,url:"nnn"}, 
					{ username: "哈克" ,url:"jj"}
				]
			}
		}
	},
	destroyed() {
		this.$store.state.ws.close() //离开路由之后断开websocket连接
	},
	methods: {
		selectTag(tag) {
			if (tag == 'xx') {
				this.list = this.allList.messageList
			}
			if (tag == 'hy') {
				this.list = this.allList.memberList
			}
			if (tag == 'qz') {
				this.list = this.allList.groupList
			}
		},
		initWebSocket() {
			try {
				this.$store.state.ws = new WebSocket("ws://localhost:9090/socket/" + localStorage.getItem("token"));
				this.$store.state.ws.onmessage = this.websocketonmessage;
				this.$store.state.ws.onopen = this.websocketonopen;
				this.$store.state.ws.onerror = this.websocketonerror;
				this.$store.state.ws.onclose = this.websocketclose;
			} catch (e) {
				console.log(e)
			}
		},
		websocketonopen() {

		},
		websocketonerror() {
			this.initWebSocket();
		},
		websocketonmessage(e) {
			let data = JSON.parse(e.data)
			if(data.cmd == 'to_login'){
				location.href = '/login.html'
			}
			if(data.cmd == 'message'){
				this.$store.state.subscribe[data.subscribeId] = data
			}
		},
		websocketclose(e) {
			console.log("断开连接", e);
		},
	},
	created() {
		this.initWebSocket();
		this.list = this.allList.groupList
	}
}
