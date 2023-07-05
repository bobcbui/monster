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
		<span v-if='item'>
			{{memberMap[item.withAccount].username}} : {{item.content}}
		</span>
	</div>
</div>
`
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
            handler(val){
                this.loadMemberMessageList(val)
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
		}
	},
	methods: {
		openMessage(item){
			// 路由跳转到member
			this.$router.push({ path: '/member-message', query: { account: item.withAccount }})
		},
		loadMemberMessageList(val){
			console.log("memberMessageList change",val)
				this.messageList = []
				for (let m in val){
					// val 最后一条消息
					debugger
					this.messageList.push(val[m][val[m].length - 1])
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
		this.loadMemberMessageList(this.$store.state.memberMessageList)
	}
}
