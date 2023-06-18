let template = // html
`
    <div v-if='thisGroup'>
        名称：{{thisGroup.alias}}<br/>
        群号：{{thisGroup.groupAccount}}<br/>
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
                if (item.groupAccount == this.$route.query.account) {
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
