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
<button @click='searchGroup()'>搜索</button>
<button @click='joinGroup()'>搜索</button>

</div>

`
import request from '../lib/request.js'
export default {
    template: template,
    data: function(){
        return {
            ws:null
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
        searchGroup(){
            
            this.ws =  new WebSocket(this.groupUrl+"?=token="+localStorage.getItem("checkToken"))
            
            this.ws.onmessage = function(e){
                console.log(e.data)
                //this.ws.send(JSON.stringify({"cmd":"join","text":"申请加入"}))
            };

            this.ws.onopen = function(e){
                
            };

            this.ws.onerror = function(){
                
            };

            this.ws.onclose = function(){
                
            };
        },
        joinGroup(){
            this.ws.send(JSON.stringify({"cmd":"join","text":"申请加入"}))
        }
    }
}
