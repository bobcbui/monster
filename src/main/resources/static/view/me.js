let template = // html
`
<cNav title='我的'>
	<cModal buttonName='设置'>
        <button @click="logout" class='w-100'>退出登录</button>
	</cModal>
</cNav>
<div v-if="member!=null" class='p-10'>
名称：{{member.username}}<br>
账号：{{member.account}}<br>
</div>
`
import cModal from '../component/modal.js'
import cNav from '../component/nav.js'
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
		logout(){
			localStorage.removeItem('token');
			location.href = '/'
		}
		
	},
	created() {
	
	}
}
