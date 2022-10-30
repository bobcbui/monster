let template = // html
`
<div>
<input type='text' v-model='form.text'>
<button @click="sendMessage()">登录</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{}
        }
    },
    methods: {
        sendMessage(){
            this.form.type = this.$route.query.type
            this.form.receiveId = this.$route.query.receiveId
            this.$store.state.ws.send(JSON.stringify(this.form))
        }
    }
}
