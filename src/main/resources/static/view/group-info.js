let template = // html
`
    <div v-if='thisGroup'>
        名称：{{thisGroup.name}}<br/>
        群号：{{thisGroup.account}}<br/>
    </div>
`
import request from '../lib/request.js';
export default {
    template: template,
    data: function () {
        return {
            
        }
    },
    wathc: {

    },
    computed: {
        thisGroup() {
            let _thisGroup;
            this.$store.state.groupList.filter(item => {
                if (item.account == this.$route.query.account) {
                    _thisGroup = item; 
                }
            })
            return _thisGroup;
        }
    },
    methods: {
        
    },
    created() {
        
    }
}
