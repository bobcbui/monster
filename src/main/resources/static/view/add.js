let template = // html
`
<div style='padding:10px'>
<input>&nbsp;<button>添加</button>

</div>

创建群
<div style='padding:10px'>
群名：<input v-model='groupName'>
<br>
<button @click='create()'>创建</button>

<br>
加入群
<div style='padding:10px'>
群路径：<input v-model='groupUrl'>
<br>
<button @click='create()'>创建</button>

</div>
`
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
