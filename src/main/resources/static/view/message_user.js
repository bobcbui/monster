let template = // html
`
<div style='height:calc(100% - 100px)'>
    <div class='nav-head'>
        <router-link to="/">返回</router-link>
        <span style='float:right'>{{$route.query.url}}</span>
    </div>
    <div style='overflow-y: scroll;height:calc(100% - 40px)'>
       
    </div>
</div>
<div style='height: 99px;'>
    <input type='text' v-model='form.text' style='width: calc(100% - 100px);height:100%;border:0px;border-top:1px solid black;border-right:1px solid black;vertical-align: top;'>
    <button @click="sendMessage()" style='width:100px;height:100%;border:0px;border-top:1px solid black;vertical-align: top;'>发送</button>
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
            this.$store.state.socketMessage.send(JSON.stringify(this.form))
        },
        initMessageWebSocket(){
            try {
				this.$store.state.socketMessage = new WebSocket(this.$route.query.url + "?checkUrl=" + localStorage.getItem("checkUrl"));
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
