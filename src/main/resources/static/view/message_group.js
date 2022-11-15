let template = // html
`
<div style='height:calc(100% - 100px)'>
    <div class='nav-head'>
        <router-link to="/">返回</router-link>
        <span style='float:right'>{{$route.query.url}}</span>
    </div>
    <div style='overflow-y: scroll;height:calc(100% - 41px)'>
        <p v-for='(item,index) in $store.state.messageMap[$route.query.url].message' :key='index'>
        {{item.nickname}}：{{item.text}}
        </p>
    </div>
</div>
<div style='height: 100px;'>
    <input type='text' v-model='form.text' style='width: calc(100% - 100px);height:100%;border:0px;border-top:1px solid black;border-right:1px solid black'>
    <button @click="sendMessage()" style='width:100px;height:100%;border:0px;border-top:1px solid black;'>发送</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                type:"message",
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
