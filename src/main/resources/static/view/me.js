let template = // html
`
<div v-if="member!=null">
名称：{{member.username}}<br>
账号：{{member.account}}<br>
</div>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			
		}
	},
	computed:{
		member(){
			return this.$store.state.member;
		}
	},
	destroyed() {

	},
	methods: {
		
	},
	created() {
	
	}
}
