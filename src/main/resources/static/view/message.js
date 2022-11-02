let template = // html
`
<div>
<input type='text' v-model='form.text'>
<button @click="sendMessage()">发送</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                text:""
            }
        }
    },
    methods: {
        sendMessage(){
            this.$store.state.socketMessage.send(JSON.stringify(this.form))
        },
        initMessageWebSocket(){
            try {
				this.$store.state.socketMessage = new WebSocket("ws://localhost:9090/socket/message/" + localStorage.getItem("checkToken"));
				this.$store.state.socketMessage.onmessage = this.websocketonmessage;
				this.$store.state.socketMessage.onopen = this.websocketonopen;
				this.$store.state.socketMessage.onerror = this.websocketonerror;
				this.$store.state.socketMessage.onclose = this.websocketclose;
			} catch (e) {
				console.log(e)
			}
        },
		websocketonopen() {

		},
		websocketonerror() {
			this.initWebSocket();
		},
		websocketonmessage(e) {
            console.log(e.data)
		},
		websocketclose(e) {
			console.log("断开连接", e);
		},
    },
    created() {
		this.initMessageWebSocket();
	}
}
