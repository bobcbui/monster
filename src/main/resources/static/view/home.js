let template = // html
`
<div class='nav-head'>
	<router-link to="/">返回</router-link>
	<span style='float:right'>{{$route.query.url}}</span>
</div>
<div style='padding:10px'>
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
