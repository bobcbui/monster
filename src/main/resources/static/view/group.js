let template = // html
`
group
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
	destroyed() {

	},
	methods: {
		
	},
	created() {
	
	}
}
