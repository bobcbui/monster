let template = // html
`
<div style='height:calc(100% - 100px)'>
    <div class='nav-head'>
        <router-link to="/">返回</router-link>
        <span style='float:right'>{{$route.query.url}}</span>
    </div>
    <div style='overflow-y: scroll;height:calc(100% - 40px)'>
        <p style='border: 1px solid black;
        margin: 5px;
        padding: 5px;
        border-radius: 5px;background: #efefef;' v-if='$store.state.messageMap[$route.query.url] != undefined' v-for='(item,index) in $store.state.messageMap[$route.query.url].message' :key='index'>
        <span style='font-weight: bold;color: #5f5fba;'>{{item.nickname}}</span><span style='float:right'>{{item.createTime}}</span>
        <br>
        {{item.text}}
        </p>
    </div>
</div>
<div style='height: 99px;'>
    <textarea  type='text' v-model='form.text' style='width: calc(100% - 100px);height:100%;border:0px;border-top:1px solid black;border-right:1px solid black;vertical-align: top;resize:none;'></textarea>
    <button @click="sendMessage()" style='width:100px;height:100%;border:0px;border-top:1px solid black;vertical-align: top;'>发送</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                type:"message-group",
                text:""
            }
        }
    },
    methods: {
        sendMessage(){
            let url = this.$route.query.url
            let socketGroupMap = this.$store.state.socketGroupMap
            socketGroupMap[url].send(JSON.stringify(this.form))
        }
    },
    created() {
		
	}
}
