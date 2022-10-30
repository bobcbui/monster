let template = // html
`
<div style='padding:10px'>
群名称：<input v-model='from.nickname'>
<br>
<button @click='create()'>创建</button>

</div>
`

import request from '../lib/request.js'
export default {
    template: template,
    data: function(){
        return {
            from:{

            }
        }
    },
    methods: {
        create(){
            request({
                url: "/group/create",
                method: "POST",
                data:this.from
            }).then((response) => {
    
            });
        }
    }
}
