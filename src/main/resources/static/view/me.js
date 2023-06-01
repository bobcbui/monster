let template = // html
`
<div v-if="member!=null">
用户名：{{member.name}}<br>
ws：{{member.ws}}<br>
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
