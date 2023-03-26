let template = // html
`
<div>
	<ul>
		<li v-for="(item,index) in memberList">
			{{item}}
		</li>
	</ul>
</div>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			memberList: [{username:"askdjfk",nickname:"testes",ws:"ws:11110111"},{username:"askdjfk",nickname:"testes",ws:"ws:11110111"}],
			
		}
	},
	destroyed() {
		// this.$store.state.socketLocal.close() 
	},
	methods: {
		
	},
	created() {
	
	}
}
