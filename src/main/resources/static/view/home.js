let template = // html
`
<div class='nav-head'>
	<router-link to="/">返回</router-link>
	<span class='float-right'>{{$route.query.url}}</span>
</div>
<div class='padding-10'>
	username : {{this.$store.state.user.username}}
	<br>
	ws : {{this.$store.state.user.ws}}
</div>
`
export default {
	template: template,
	data: function () {
		return {

		}
	},
	methods: {

	},
	created() {
	
	}
}
