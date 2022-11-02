let template = // html
`
<div style='padding:10px'>
    
创建群
<br>
群名：<input v-model='groupName'>
<br>
<button @click='create()'>创建</button>
<br>
加入群
<br>
群路径：
<br>
<input v-model='groupUrl'>
<br>
<button @click='join()'>创建</button>

</div>

`
import request from '../lib/request.js'
export default {
    template: template,
    data: function(){
        return {

        }
    },
    methods: {
        create(){
            request({
                url: "/group/create",
                method: "POST",
                data:{groupName:this.groupName}
            }).then((response) => {
                alert(JSON.stringify(response.data))
            });
        },
        join(){
            request({
                url: "/group/join",
                method: "POST",
                data:{groupUrl:this.groupUrl}
            }).then((response) => {
                alert(JSON.stringify(response.data))
            });
        }
    }
}
