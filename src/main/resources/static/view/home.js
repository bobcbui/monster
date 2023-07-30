let template = // html
`
<div style='height: calc(100% - 40px);border-bottom:1px solid black;background:var(--bottomColor);'>
	<router-view></router-view>
</div>
<div style='text-align: center;text-align: center;height: 40px;line-height: 40px;background:var(--topColor);'>
	<router-link :class='$route.name == "message" ? "n-active":""' :to="{name:'message'}">消息</router-link>&nbsp;&nbsp;
	<router-link :class='$route.name == "group" ? "n-active":""' :to="{name:'group'}">群组</router-link>&nbsp;&nbsp;
	<router-link :class='$route.name == "member" ? "n-active":""' :to="{name:'member'}">好友</router-link>&nbsp;&nbsp;
	<router-link :class='$route.name == "more" ? "n-active":""' :to="{name:'more'}">更多</router-link>&nbsp;&nbsp;
	<router-link :class='$route.name == "me" ? "n-active":""' :to="{name:'me'}">我的</router-link>
</div>
`
export default {
	template: template,
	data: () => {
		return {
		}
	},
	destroyed() {
	},
	methods: {
	},
	created() {
	},
	mounted() {
	}
}
