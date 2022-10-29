let template = // html
	`
<div>
home
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
		request({
			url: "/api/other",
			method: "GET",
		}).then((response) => {

		});
	}
}
