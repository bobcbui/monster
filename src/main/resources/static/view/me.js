let template = // html
`
<cNav title='😊我的'>
	<cModal buttonName='设置'>
		<div class='m-b-10'>
			主题颜色：
			<div class='m-b-10'>
				<label>粉红色<input type='radio' value='light' @click='click(1)'></label>&nbsp;&nbsp;
				<label>蓝色<input type='radio' value='light' @click='click(2)'></label>
			</div>
		</div>
		<div  class='m-b-10'>
			<button @click="logout" class='w-100'>退出登录</button>
		</div>
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
	data: () => {
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
			location.href = '/';
		},
		click(item){
			if(item == 1){
				const root = document.documentElement;
				root.style.setProperty("--topColor", "rgb(255, 222, 252)");
				root.style.setProperty("--bottomColor", "rgb(255, 248, 248)");
				localStorage.setItem("--topColor", "rgb(255, 222, 252)");
				localStorage.setItem("--bottomColor", "rgb(255, 248, 248)");
			}
			if(item == 2){
				const root = document.documentElement;
				root.style.setProperty("--topColor", "#00a9ff");
				root.style.setProperty("--bottomColor", "#ceeeff");
				localStorage.setItem("--topColor", "#00a9ff");
				localStorage.setItem("--bottomColor", "#ceeeff");
			}
			
		}
		
	},
	created() {
	
	}
}
