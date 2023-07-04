let template = // html
`
<div v-for="(item,index) in messageList">
{{memberMap[item.withAccount].username}}:{{item.content}}
</div>
`
import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			messageList:[]
		}
	},
	destroyed() {
		
	},
	watch: {
		memberMessageList(val,oldVal){
			this.loadMemberMessageList(val)
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
		loadMemberMessageList(val){
			console.log("memberMessageList change",val)
				this.messageList = []
				for (let m in val){
					this.messageList.push(val[m][0])
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
