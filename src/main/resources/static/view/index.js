let template = // html
`
<div style='text-align: center;padding-top:8px'>
	<router-link :to="{path:'/message-list'}"> 消息 </router-link>
	<router-link :to="{path:'/member-list'}"> 好友 </router-link>
	<router-link :to="{path:'/group-list'}"> 群 </router-link>
	<router-link :to="{path:'/group-list'}"> 创建群 </router-link>
	<router-link :to="{path:'/add'}"> 加群 </router-link>
	<router-link :to="{path:'/add'}"> 加好友 </router-link>
	<router-link :to="{path:'/add'}"> 我的 </router-link>
	<hr>
</div>
<router-view></router-view>
`

import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
		}
	},
	destroyed() {
		
	},
	methods: {

	},
	created() {
		request({
			url: "/authenticate/info",
			method: "GET"
		}).then((response) => {
			this.$store.state.member = response.data
		});
	}
}
