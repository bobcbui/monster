let template = // html
`
<cNav title='群组'>
	<cModal buttonName='创建'>
		<input class='w-100 m-b-5' placeholder='群号' v-model='createGroupForm.name'>
		<input class='w-100 m-b-5' placeholder='群名称' v-model='createGroupForm.nickname'>
		<button class='w-100 m-b-5' @click='create'>创建</button>
	</cModal>&nbsp;
	<cModal buttonName='加群'>
		{{joinGroupForm.name}}
		<input class='w-100 m-b-5' placeholder='群号' v-model='joinGroupForm.account' >
		<button class='w-100 m-b-5' @click='search'>查询</button>
		<button class='w-100 m-b-5' v-if='joinGroupForm.account != null' @click='join'>加入群</button>
	</cModal>
</cNav>
<div class='p-10'>
	<ul class='m-0'>
		<li style='border:1px solid black;margin:0px 0px 10px 0px;border-radius:5px;padding:5px;' v-for='(item,index) in groupMap' :key='index' @click='toGroupMessage(item)'>
		{{item.name}}</li>
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
			this.$store.state.socketLocal.send({ type: "createGroup", data: this.createGroupForm})
		},
		search(){
			createGroupSocket(this.joinGroupForm.account, (socket) => {
				let that = this;
				that.$store.state.socketGroup[this.joinGroupForm.account] = socket;
				socket.send({ type: "info"}, (data, socket) => {
					that.joinGroupForm = data.data;
				})
			}, (data, socket) => {
				console.log("other message");
			});
		
		},
		join(){
			this.joinGroupForm.memberAccount = this.$store.state.member.account;
			this.$store.state.socketGroup[this.joinGroupForm.account].send({ type: "join", data: this.joinGroupForm})
			this.$store.state.socketLocal.send({ type: "join", data: this.joinGroupForm})
		}
	},
	created() {
	
	}
}
