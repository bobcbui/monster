let template = // html
`
<cNav title='消息'>
	<cModal buttonName='验证消息'>
		<div v-for="(item,index) in $store.state.verifyList" style='border:1px solid black;padding:5px;border-radius:5px'>
			{{JSON.parse(item.context).username}} : {{JSON.parse(item.context).context}} （{{item.state}}）
			<br>
			<button @click='agreeVerify(item)'>同意</button>&nbsp;&nbsp;<button @click='rejectVerify(item)'>拒绝</button>&nbsp;&nbsp;<button @click='deleteVerify(item)'>删除</button>
		</div>
	</cModal>
</cNav>
<div class='p-10'>
	<div v-for="(item,index) in messageList" @click='openMessage(item)'>
		<div v-if='item && item.withAccount != null && memberMap[item.withAccount]' style='border: 1px solid black; border-radius: 5px; margin-bottom: 10px;'>
			{{memberMap[item.withAccount].username}}  <span class='float-end'>{{toDate(item.createTime)}}</span>
			<br>
			{{item.content}}
		</div>
		<div v-if='item && item.withGroupAccount != null && groupMap[item.withGroupAccount]' style='border: 1px solid black; border-radius: 5px; margin-bottom: 10px;'>
			{{groupMap[item.withGroupAccount].name}}  <span class='float-end'>{{toDate(item.createTime)}}</span>
			<br>
			{{item.content}}
		</div>
	</div>
</div>
`
// 
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
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
		"$store.state.memberMessageList":{
            handler(){
                this.loadMessageList()
            },
            deep: true,
            immediate: true,
        },
		"$store.state.groupMessageList":{
            handler(){
                this.loadMessageList()
            },
            deep: true,
            immediate: true,
        }
	},
	computed: {
		memberMessageList() {
			this.$store.state.memberMessageList
		},
		memberMap(){
			return this.$store.state.memberMap
		},
		groupMap(){
			return this.$store.state.groupMap
		}
	},
	methods: {
		toDate(time){
			// 时间戳转距离当前有多久，分钟，小时，天，月，年
			let now = new Date().getTime();
			let distance = now - time;
			if(distance < 60 * 1000){
				return "刚刚"
			}
			if(distance < 60 * 60 * 1000){
				return Math.floor(distance / (60 * 1000)) + "分钟前"
			}
			if(distance < 24 * 60 * 60 * 1000){
				return Math.floor(distance / (60 * 60 * 1000)) + "小时前"
			}
			if(distance < 30 * 24 * 60 * 60 * 1000){
				return Math.floor(distance / (24 * 60 * 60 * 1000)) + "天前"
			}
			if(distance < 12 * 30 * 24 * 60 * 60 * 1000){
				return Math.floor(distance / (30 * 24 * 60 * 60 * 1000)) + "月前"
			}
			return Math.floor(distance / (12 * 30 * 24 * 60 * 60 * 1000)) + "年前"
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
			let valMember = this.$store.state.memberMessageList;
			let valGroup = this.$store.state.groupMessageList;
			this.messageList = []
			for (let m in valMember){
				// val 最后一条消息
				this.messageList.push(valMember[m][valMember[m].length - 1])
			}
			for (let m in valGroup){
				// val 最后一条消息
				this.messageList.push(valGroup[m][valGroup[m].length - 1])
			}
			// 通过createTime 排序，最新的在最下面
			this.messageList.sort((a,b)=>{
				return b.createTime - a.createTime
			})
			
		},
		agreeVerify(item){
			request({
                method: 'get',
                url: '/one-token',
            }).then(response => {
				let ws = decodeWsAccount(JSON.parse(item.context).account);
                let socket = new WebSocket(ws + "?checkUrl=" + document.location.origin + "/check/" + response.data);
                this.$store.state.socketMember = socket;
                let that = this;
                socket.onopen = function(e){
                    socket.send(JSON.stringify({
						type:"agree",
						account: that.$store.state.member.account,
						verifyId: item.id
					}))
					
                };
                socket.onmessage = function(e){
					if(e.data == "agree"){
						if(e.code == 200){

						}
						// 同意了
						socket.close()
					}
                };
                socket.onclose = function(e){
                    
                };
                socket.onerror = function(e){
                    
                };
			})
		}
		
	},
	created() {
		
	},
	mounted(){
		this.loadMessageList(this.$store.state.memberMessageList)
		
	}
}
