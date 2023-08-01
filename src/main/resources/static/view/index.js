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
		async createGroupSocket(account) {
			let that = this;
			createGroupSocket(account, (socket) => {
				that.$store.state.socketGroup[account] = socket;
				// 获取群消息列表
				socket.send({ type: "messages" }, (data) => {
					that.$store.state.groupMessageListMap[account] = data.data;
					//渲染完毕执行
					that.$nextTick(() => {
						down(account);
					});
				});
				// 获取群信息
				socket.send({ type: "info" }, (data) => {
					if (that.$store.state.groupMap == null) {
						that.$store.state.groupMap = {};
					}
					that.$store.state.groupMap[account] = data.data;
				});
			})
		},
		createLocalWebSocket() {
			let that = this;
			createLocalSocket(
				"ws://" + window.location.host + "/local?token=" + localStorage.getItem("token"),
				(socket) => {
					that.$store.state.socketLocal = socket;
					// 加载成功获取member 和 group 列表
					socket.send({ type: "memberMap" });
					socket.send({ type: "groupMap" });
					socket.send({ type: "loadMessage" });
					socket.send({ type: "loadVerify" });
				}, (data, socket) => {
					if (data.type == "memberMap") {
						that.$store.state.memberMap = data.data;
						console.log(data.data);
					}

					if (data.type == "memberMessage") {
						console.log(data);
					}

					if (data.type == "groupMap") {
						let groupMap = data.data;
						for (let item in groupMap) {
							that.createGroupSocket(item);
						}
					}

					if (data.type == "message") {
						data.data.state = true;
						if (data.data.account != data.data.withAccount) {
							that.$store.state.memberMessageListMap[data.data.withAccount].push(data.data);
							that.$nextTick(() => { down(data.data.withAccount); });
						}
					}

					// 记载所有消息
					if (data.type == "loadMessage") {
						let account;
						for (let item of data.data) {
							that.$store.state.memberMessageListMap[item.account] = item.data;
							account = item.account;
						}
						down(account)
					}

					if ("loadVerify" == data.type) {
						that.$store.state.verifyList = data.data;
					}
				}
			);
		},
	},
	created() {
		
	},
	mounted() {
		request({
			url: "/authenticate/info",
			method: "GET",
		}).then((response) => {
			this.$store.state.member = response.data
			this.createLocalWebSocket()
		});
	}
}
