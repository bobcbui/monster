let template = // html
`
sdsd
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
