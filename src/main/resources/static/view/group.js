let template = // html
`
<div style='padding:10px;padding-top:0px;'>
	<button style='width:100%;margin-bottom:5px;'>创建群</button>
	<button style='width:100%;margin-bottom:5px;'>加入群</button>
</div>
<ul style='margin:0px'>
	<li style='padding:0px 10px;border:1px solid black;margin:0px 10px 10px 10px;border-radius:5px;' v-for='(item,index) in groupList' :key='index'>{{item.groupName}}</li>
</ul>
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
