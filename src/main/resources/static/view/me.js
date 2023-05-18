let template = // html
`
<div v-if="member!=null">
用户名：{{member.name}}
</div>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			groupList: [
				{groupName:"askdjfk",nickname:"testes",ws:"ws:11110111"},
				{groupName:"askdjfk",nickname:"testes",ws:"ws:11110111"}
			],
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
