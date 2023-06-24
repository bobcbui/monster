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
            return this.$store.state.groupMap[this.$route.query.account]
        }
    },
    methods: {
        
    },
    created() {
        
    }
}
