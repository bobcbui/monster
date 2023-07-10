let template = // html
`
<cNav title='消息'>
	<button @click='$refs["cModal"].show = true'>搜索</button>
	<cModal ref='cModal'>
		<input style='width:100%' placeholder='搜索'/>
	</cModal>
</cNav>
<div style='padding:10px'>
	<div v-for="(item,index) in messageList" style='border: 1px solid black; border-radius: 5px; margin-bottom: 10px;' @click='openMessage(item)'>
		<span v-if='item && item.withAccount != null'>
			{{memberMap[item.withAccount].username}} : {{item.content}} 。{{item.createTime}}
		</span>
		<span v-if='item && item.withGroupAccount != null && groupMap[item.withGroupAccount]'>
			{{groupMap[item.withGroupAccount].name}} : {{item.content}} 。{{item.createTime}}
		</span>
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
		openMessage(item){
			// 路由跳转到member
			this.$router.push({
				name: 'member-message', 
				query: {
					account: item.withAccount,
					routerName: this.$route.name
				}
			})
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
			
		}
		
	},
	created() {
		
	},
	mounted(){
		this.loadMessageList(this.$store.state.memberMessageList)
		
	}
}
