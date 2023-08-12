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
				// 获取群信息
				socket.send({ type: "info" }, (data) => {
					if (that.$store.state.groupMap == null) {
						that.$store.state.groupMap = {};
					}
					that.$store.state.groupMap[account] = data.data;
					// 获取群消息列表
					socket.send({ type: "messages" });
				});

			},(data) => {
				if(data.type == "groupSystemMessage"){
					that.$store.state.groupMessageListMap[account].push(data.data);
				}

				if(data.type == "messages"){
					that.$store.state.groupMessageListMap[account] = data.data;
					let i = 0;
					that.$store.state.groupMessageListMap[account].forEach((item)=>{
						
						if(item.createTime > that.$store.state.groupMap[account].readTime){
							i++;
						}
					});
					that.$store.state.unReadMessageCount[account] = i;
				}
				
				if(data.type == "message"){
					that.$store.state.groupMessageListMap[account].push(data.data);
					if(data.data.createTime > that.$store.state.groupMap[account].readTime){
						
						that.$store.state.unReadMessageCount[account]++;
					}
					that.$nextTick(() => { down(account); });
				}
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
					socket.send({ type: "groupList" });
					socket.send({ type: "loadMessage" });
					socket.send({ type: "loadVerify" });
					socket.send({ type: "recommendGroup" },(data) => {
						that.$store.state.recommendGroup = data.data;
					});
				}, (data, socket) => {
					if (data.type == "memberMap") {
						that.$store.state.memberMap = data.data;
						console.log(data.data);
					}

					if (data.type == "memberMessage") {
						console.log(data);
					}

					if (data.type == "groupList") {
						for (let item in data.data) {
							if(item != null && item != "null"){
								that.createGroupSocket(item);
							}
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
