let template = // html
`
<cNav title='👥群组'>
	<cModal buttonName='推荐群'>
		<ul class='m-0'>
			<li class='m-b-10 b-r-5 b-1 p-5' v-for='(item, index) in  $store.state.recommendGroupList'>
				{{item.name}}
				<span class='m-b-5 float-end' @click='joinGroupForm.account = item.account , join()' style='color:blue'>加入</span>
			</li>
		</ul>
	</cModal>&nbsp;
	<cModal buttonName='创建'>
		<input class='w-100 m-b-5' placeholder='群号' v-model='createGroupForm.name'>
		<input class='w-100 m-b-5' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button class='w-100 m-b-5' @click='create'>创建</button>
	</cModal>&nbsp;
	<cModal buttonName='加群'>
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
		}
	},
	methods: {
		toGroupMessage(item){
			this.$router.push({ path: '/group-message', query: { account: item.account } });
		},
		create(){
			this.$store.state.socketLocal.send({ type: "createGroup", data: this.createGroupForm});
		},
		search(){
			let that = this;
			createGroupSocket(that.joinGroupForm.account, (socket) => {
				that.$store.state.socketGroup[that.joinGroupForm.account] = socket;
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
				socket.send({ type: "join", data: that.joinGroupForm});
				that.$store.state.socketLocal.send({ type: "join", data: that.joinGroupForm},(data) => {
					that.$store.state.socketLocal.send({ type: "groupList"});
				});
			});
		}
	},
	created() {
	
	}
}
