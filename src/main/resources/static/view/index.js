let template = // html
	`
<router-view></router-view>
`

import request from '../lib/request.js';
import MySocket from '../core/my-socket.js';
export default {
	template: template,
	data: function () {
		return {
		}
	},
	destroyed() {

	},
	methods: {
		createGroupWebSocket(account) {
			let that = this;
			// one-token
			request({
				method: 'get',
				url: '/one-token',
			}).then(response => {
				let ws = decodeWsAccount(account);
				let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
				that.$store.state.socketGroup[account] = socket;
				socket.onopen = function (e) {
					socket.send(JSON.stringify({ type: "messages" }))
					socket.send(JSON.stringify({ type: "info" }))
				};
				socket.onmessage = function (e) {
					let data = JSON.parse(e.data);
					if (data.type == "message") {
						if (that.$store.state.groupMessageList[account] == null) {
							that.$store.state.groupMessageList[account] = []
						}
						that.$store.state.groupMessageList[account].push(data.data)
						//渲染完毕执行
						that.$nextTick(function () {
							down(account)
						})
					}
					if (data.type == "messages") {
						that.$store.state.groupMessageList[account] = data.data
						//渲染完毕执行
						that.$nextTick(function () {
							down(account)
						})
					}

					if (data.type == "info") {
						if (that.$store.state.groupMap == null) {
							that.$store.state.groupMap = {}
						}
						that.$store.state.groupMap[account] = data.data

					}
				};
				socket.onclose = function (e) {

				};
				socket.onerror = function (e) {

				};
			}).catch(function (error) {

			});

		},
		createLocalWebSocket() {
			let _this = this;
			new MySocket("ws://localhost:9090/local?token=" + localStorage.getItem("token"),
				(that) => {
					// 加载成功获取member 和 group 列表
					_this.$store.state.socketLocal = that;
					that.send({ type: "memberMap" })
					that.send({ type: "groupList" })
					that.send({ type: "loadMessage" })
					that.send({ type: "loadVerify" })
					that.send({ type: "asdfasdf" }, (e, that) => {

					})
				},
				(data, that) => {
					if (data.type == "memberMap") {
						_this.$store.state.memberMap = data.data
						console.log(data.data)
					}

					if (data.type == "memberMessage") {
						console.log(data)
					}

					if (data.type == "groupList") {
						let groupList = data.data;
						groupList.forEach((item) => {
							_this.createGroupWebSocket(item)
						})
					}

					if (data.type == "messages") {
						//that.$store.state.group1List = data
						console.log(data.data)
					}

					if (data.type == "message") {
						data.data.state = true;
						if (data.data.account != data.data.withAccount) {
							_this.$store.state.memberMessageList[data.data.withAccount].push(data.data);
							_this.$nextTick(function () {
								down(data.data.withAccount)
							});
						}
					}

					// 记载所有消息
					if (data.type == "loadMessage") {
						let account;
						for (let item of data.data) {
							_this.$store.state.memberMessageList[item.account] = item.data
							account = item.account;
						}
						down(account)
					}

					if ("loadVerify" == data.type) {
						_this.$store.state.verifyList = data.data
					}
				}
			);
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
			this.createLocalWebSocket()
		});
	}
}
