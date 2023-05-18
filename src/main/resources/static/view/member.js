let template = // html
`
member
`
export default {
    template: template,
    data: function () {
        return {
            form: {
                type: "message-member",
                text: "",
            }
        }
    },
    wathc:{
    },
    methods: {
        sendMessage() {
            this.$store.state.socketMessage.send(JSON.stringify(this.form))
        },
        initMessageWebSocket() {
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
            let msg = JSON.parse(e.data)
            if (msg.type == 'message-member') {
                if (this.$store.state.messageMap[this.$route.query.url] == undefined) {
                    this.$store.state.messageMap[this.$route.query.url] = { name: msg.name, type: "message-member", ws: this.$route.query.url, message: [] }
                }
                this.$store.state.messageMap[this.$route.query.url].message.push(msg);
                msg.ws = this.$route.query.url
                this.$store.state.socketLocal.send(JSON.stringify(msg))
            }
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
