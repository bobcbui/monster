let template = // html
`
<cNav title='🙂消息'>
	<cModal buttonName='验证消息'>
		<div v-for="(item,index) in $store.state.verifyList" class='m-b-10 b-r-5 b-1 p-5'>
			{{JSON.parse(item.data).username}} : {{JSON.parse(item.data).context}}
			<br>
			<span style='vertical-align: unset;'>
				<button v-if='item.state == 5' >申请被拒绝</button>
				<button v-if='item.state == 4' >申请被通过</button>
				<button v-if='item.state == 3' >等待验证</button>
				<button v-if='item.state == 2' >你已拒绝</button>
				<button v-if='item.state == 1' >你已通过</button>
				<button v-if='item.state == 0' @click='agreeVerify(item)'>同意</button>&nbsp;&nbsp;
				<button v-if='item.state == 0' @click='rejectVerify(item)'>拒绝</button>&nbsp;&nbsp;
			</span>
			<span style='float:right;vertical-align: unset;'>
				<span>{{toDate(item.createTime)}}</span>&nbsp;&nbsp;
				<button @click='deleteVerify(item)'>删除</button>
			</span>
		</div>
	</cModal>
</cNav>
<div class='p-10'>
	<div v-for="(item,index) in messageList" @click='openMessage(item)'>
		<div v-if='item && item.withAccount != null && memberMap[item.withAccount]' class='m-b-10 b-r-5 b-1 p-5' style='position: relative;'>
			<div style='width: 15px;text-align: center;font-size: 10px;height: 15px;border-radius: 10px;border:1px solid black;position: absolute;right: -5px;top: -5px;background: #ff5959;color: white;'>
				99
			</div>
			👤{{memberMap[item.withAccount].username}}  <span class='float-end'>{{toDate(item.createTime)}}</span>
			<br>
			{{item.content}}
		</div>
		<div v-if='item && item.withGroupAccount != null && groupMap[item.withGroupAccount]' class='m-b-10 b-r-5 b-1 p-5' style='position: relative;'>
			<div style='width: 15px;text-align: center;font-size: 10px;height: 15px;border-radius: 10px;border:1px solid black;position: absolute;right: -5px;top: -5px;background: #ff5959;color: white;'>
				1
			</div>
			👥{{groupMap[item.withGroupAccount].name}}  <span class='float-end'>{{toDate(item.createTime)}}</span>
			<br>
			{{item.content}}
		</div>
	</div>
</div>
`
// 
import cModal from '../component/modal.js';
import cNav from '../component/nav.js';
import { createMemberSocket } from '../core/app-socket.js';
export default {
	template: template,
	data: () => {
		return {
			messageList:[],
			show:false
		}
	},
	components: {
		cNav,cModal
	}
	,
	destroyed() {
		
	},
	watch: {
		"$store.state.memberMessageListMap":{
            handler(){
                this.loadMessageList();
            },
            deep: true,
            immediate: true,
        },
		"$store.state.groupMessageListMap":{
            handler(){
                this.loadMessageList();
            },
            deep: true,
            immediate: true,
        }
	},
	computed: {
		memberMessageListMap() {
			this.$store.state.memberMessageListMap;
		},
		memberMap(){
			return this.$store.state.memberMap;
		},
		groupMap(){
			return this.$store.state.groupMap;
		}
	},
	methods: {
		toDate(time){
			return toDate(time);
		},
		openMessage(item){
			// 路由跳转到member
			if(item.withAccount){
				this.$router.push({
					name: 'member-message', 
					query: {
						account: item.withAccount,
						routerName: this.$route.name
					}
				})
			}
			if(item.withGroupAccount){
				this.$router.push({
					name: 'group-message',
					query: {
						account: item.withGroupAccount,
						routerName: this.$route.name
					}
				})
			}
		},
		loadMessageList(val){
			let memberMessageListMap = this.$store.state.memberMessageListMap;
			let groupMessageListMap = this.$store.state.groupMessageListMap;
			this.messageList = []
			for (let memberAccount in memberMessageListMap){
				this.messageList.push(memberMessageListMap[memberAccount].slice(-1)[0])
			}
			for (let groupAccount in groupMessageListMap){
				this.messageList.push(groupMessageListMap[groupAccount].slice(-1)[0])
			}

			for (let memberAccount in memberMessageListMap){
				let i = 0;
				memberMessageListMap[memberAccount].forEach((item)=>{
					if(item.readTime > this.$store.state.memberMap[memberAccount].readTime){
						i++;
					}
				});
				this.$store.state.memberMap[memberAccount].unReadCount = i;
			}
			for (let groupAccount in groupMessageListMap){
				let i = 0;
				groupMessageListMap[groupAccount].forEach((item)=>{
					debugger
					if(item.readTime > this.$store.state.groupMap[groupAccount].readTime){
						i++;
					}
				});
				this.$store.state.groupMap[groupAccount].unReadCount = i;
			}
			// 通过createTime 排序，最新的在最下面
			this.messageList.sort((a,b)=>{
				return b.createTime - a.createTime
			})
			
		},
		agreeVerify(item){
			let that = this;
			let account = JSON.parse(item.data).account;
			createMemberSocket(account, (socket)=>{
				// 同意好友申请
				socket.send({
					type:"agree",
					account: that.$store.state.member.account,
					verifyId: item.id
				}, (data) => {
					console.log("同意了");
				});
				// 修改本地数据
				that.$store.state.socketLocal.send({
					type:"agreeVerify",
					verifyId: item.id
				}, (data) => {
					console.log("同意了 并修改本地数据");
					that.$store.state.socketLocal.send({type:"memberList"});
				});
			})
		},
		rejectVerify(item){
			let that = this;
			let account = JSON.parse(item.data).account;
			createMemberSocket(account, (socket)=>{
				// 拒绝好友申请
				socket.send({
					type:"reject",
					account: that.$store.state.member.account,
					verifyId: item.id
				}, (data) => {
					console.log("拒绝了");
				});
				// 修改本地数据
				that.$store.state.socketLocal.send({
					type:"rejectVerify",
					verifyId: item.id
				}, (data) => {
					console.log("拒绝了 并修改本地数据");
				});
			})
		},
		deleteVerify(item){
			this.$store.state.socketLocal.send({
				type:"deleteVerify",
				verifyId: item.id
			}, (data) => {
				this.$store.state.verifyList = this.$store.state.verifyList.filter((verify)=>{
					return verify.id != item.id
				});
			})
		}
		
	},
	created() {
		
	},
	mounted(){
		
	}
}
