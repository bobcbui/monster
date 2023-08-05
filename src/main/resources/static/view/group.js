let template = // html
`
<cNav title='👥群组'>
	<cModal buttonName='推荐群' ref='recommendGroupRef'>
		<ul class='m-0'>
			<li class='m-b-10 b-r-5 b-1 p-5' v-for='(item, index) in  $store.state.recommendGroup'>
				{{item.name}}
				<span class='m-b-5 float-end' @click='joinGroupForm.account = item.account , join()' style='color:blue'>加入</span>
			</li>
		</ul>
	</cModal>&nbsp;
	<cModal buttonName='创建' ref='createGroupRef'>
		<input class='w-100 m-b-5' placeholder='群号' v-model='createGroupForm.name'>
		<input class='w-100 m-b-5' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button class='w-100 m-b-5' @click='create'>创建</button>
	</cModal>&nbsp;
	<cModal buttonName='加群' ref='joinGroupRef'>
		{{joinGroupForm.name}}
		<input class='w-100 m-b-5' placeholder='群号' v-model='joinGroupForm.account' >
		<button class='w-100 m-b-5' @click='search'>查询</button>
		<button class='w-100 m-b-5' v-if='joinGroupForm.name != null' @click='join'>加入群</button>
	</cModal>
</cNav>
<div class='p-10'>
	<ul class='m-0'>
		<li class='m-b-10 b-r-5 b-1 p-5' v-for='(item,index) in groupMap' :key='index' @click='toGroupMessage(item)'>
		👥{{item.name}}
		</li>
	</ul>
</div>
`
import cModal from '../component/modal.js'
import cNav from '../component/nav.js'
import { createGroupSocket } from '../core/app-socket.js'
export default {
	template: template,
	data: () => {
		return {
			showCreateGroup: false,
			createGroupForm: {
				name: "",
				nickname: "",
			},
			showJoinGroup: false,
			joinGroupForm: {
				
			}
		}
	},
	components: {
		cNav,cModal
	},
	destroyed() {

	},
	// 计算属性
	computed: {
		groupMap(){
			return this.$store.state.groupMap;
		},
		socketLocal(){
			return this.$store.state.socketLocal;
		},
		socketGroup(){
			return this.$store.state.socketGroup;
		}
	},
	methods: {
		toGroupMessage(item){
			this.$router.push({ path: '/group-message', query: { account: item.account } });
		},
		create(){
			let that = this;
			this.socketLocal.send({ type: "createGroup", data: this.createGroupForm},(data) => {
				that.socketLocal.send({ type: "groupMap"});
				that.showCreateGroup = false;
				alert("创建成功！");
				this.$refs.createGroupRef.close();
			});
		},
		search(){
			let that = this;
			createGroupSocket(that.joinGroupForm.account, (socket) => {
				that.socketGroup[that.joinGroupForm.account] = socket;
				socket.send({ type: "info"}, (data, socket) => {
					that.joinGroupForm = data.data;
				})
			}, (data, socket) => {
				console.log("other message");
			});
		
		},
		join(){
			let that = this;
			createGroupSocket(that.joinGroupForm.account, (socket) => {
				that.joinGroupForm.memberAccount = that.$store.state.member.account;
				socket.send({ type: "join", data: that.joinGroupForm},(data, socket) => {
					socket.send({ type: "messages"}, (data, socket) => {
						that.$store.state.groupMessageListMap[that.joinGroupForm.account] = data.data;
					});
					alert("加入成功")
					this.$refs.joinGroupRef.close();
					this.$refs.recommendGroupRef.close();
					that.socketLocal.send({ type: "joinGroup", data: data.data});
					that.groupMap[that.joinGroupForm.account] = data.data;
					that.socketGroup[that.joinGroupForm.account] = socket;
					that.$router.push({ name: 'group-message', query: { account: that.joinGroupForm.account,time:new Date().getTime() } });
					
				});
				
			});
		}
	},
	created() {
	
	}
}
