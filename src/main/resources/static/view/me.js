let template = // html
`
<cNav title='我的'>
	<button @click='$refs["cModal"].show = true'>设置</button>
	<cModal ref='cModal'>
        sdfasdf
	</cModal>
</cNav>
<div v-if="member!=null" style='padding:10px'>
名称：{{member.username}}<br>
账号：{{member.account}}<br>
</div>
`
import cNav from '../component/nav.js'
import cModal from '../component/modal.js'
import request from '../lib/request.js'
export default {
	template: template,
	data: function () {
		return {
			
		}
	},
	components:{
		cNav, cModal
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
