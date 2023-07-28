let template = // html
	`
<router-view></router-view>
`

import request from '../lib/request.js';
import { createLocalSocket, createGroupSocket } from '../core/app-socket.js';
export default {
	template: template,
	data: () => {
		return {
		}
	},
	destroyed() {

	},
	methods: {
		createGroupSocket(account) {
			let that = this;
			createGroupSocket(account, (appSocket) => {
				// 获取群消息列表
				appSocket.send({ type: "messages" }, (data) => {
					that.$store.state.groupMessageList[account] = data.data
					//渲染完毕执行
					that.$nextTick(() => {
						down(account)
					})
				});

				// 获取群信息
				appSocket.send({ type: "info" }, (data) => {
					if (that.$store.state.groupMap == null) {
						that.$store.state.groupMap = {}
					}
					that.$store.state.groupMap[account] = data.data
				});

				// 保存socket到state在
				that.$store.state.socketGroup[account] = appSocket;
			})

		},
		createLocalWebSocket() {
			let that = this;
			createLocalSocket(
				"ws://localhost:9090/local?token=" + localStorage.getItem("token"),
				(appSocket) => {
					// 加载成功获取member 和 group 列表
					that.$store.state.socketLocal = appSocket;
					appSocket.send({ type: "memberMap" })
					appSocket.send({ type: "groupList" })
					appSocket.send({ type: "loadMessage" })
					appSocket.send({ type: "loadVerify" })
					appSocket.send({ type: "asdfasdf" }, (data, socket) => { console.log(data); })
				}, (data, socket) => {
					if (data.type == "memberMap") {
						that.$store.state.memberMap = data.data
						console.log(data.data)
					}

					if (data.type == "memberMessage") {
						console.log(data)
					}

					if (data.type == "groupList") {
						let groupList = data.data;
						groupList.forEach((item) => {
							that.createGroupSocket(item)
						})
					}

					if (data.type == "message") {
						data.data.state = true;
						if (data.data.account != data.data.withAccount) {
							that.$store.state.memberMessageList[data.data.withAccount].push(data.data);
							that.$nextTick(() => { down(data.data.withAccount); });
						}
					}

					// 记载所有消息
					if (data.type == "loadMessage") {
						let account;
						for (let item of data.data) {
							that.$store.state.memberMessageList[item.account] = item.data
							account = item.account;
						}
						down(account)
					}

					if ("loadVerify" == data.type) {
						that.$store.state.verifyList = data.data
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
			method: "GET",
			async: false
		}).then((response) => {
			this.$store.state.member = response.data
			this.createLocalWebSocket()
		});
	}
}
