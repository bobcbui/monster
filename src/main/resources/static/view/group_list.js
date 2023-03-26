let template = // html
`
<div>
	<ul>
		<li v-for="(item,index) in groupList">
			{{item.groupName}}：{{item.nickname}}：<a>进入</a>
		</li>
	</ul>
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
	destroyed() {

	},
	methods: {
		
	},
	created() {
	
	}
}
