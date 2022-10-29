let template = // html
`
<div>
<input type='text' v-model='form.message'>
<button @click="sendMessage()">登录</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                message:"",
            }
        }
    },
    methods: {
        sendMessage(){
            this.form.url = this.$route.query.url
            this.$store.state.ws.send(JSON.stringify(this.form))
        }
    }
}
