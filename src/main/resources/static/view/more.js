let template = // html
`
<cNav title='🔍更多'>
	<cModal buttonName='添加'>
        sdfasdf
	</cModal>
</cNav>
<div class='p-10'>

</div>
`
import cModal from '../component/modal.js';
import cNav from '../component/nav.js';
export default {
    template: template,
	data: () => {
        return {
			
        }
	},
    components: {
        cNav,cModal
    },
	destroyed() {
		
	},
	watch: {
		
	},
	computed: {

    },
    methods: {
		
	},
	created() {
		
	},
	mounted(){

    }
}
