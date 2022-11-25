let template = // html
    `
<div style='height:calc(100% - 100px)'>
    <div class='nav-head'>
        <router-link to="/">返回</router-link>
        <span style='float:right'>{{$route.query.url}}</span>
    </div>
    <div style='overflow-y: scroll;height:calc(100% - 40px)'>
        <div style='border: 1px solid black;word-break:break-all;margin: 5px;padding: 5px;border-radius: 5px;background: #efefef;' 
        v-if='$store.state.messageMap[$route.query.url] != undefined' v-for='(item,index) in $store.state.messageMap[$route.query.url].message' :key='index'>
        <span style='font-weight: bold;color: #5f5fba;'>{{item.nickname}}</span><span style='float:right'>{{item.createTime}}</span>
        <br>
        {{item.text}}
        </div>
    </div>
</div>
<div style='height: 98px;'>
<textarea  type='text' v-model='form.text' style='width: calc(100% - 100px);height:100%;border:0px;border-top:1px solid black;border-right:1px solid black;vertical-align: top;resize:none;'></textarea>
    <button @click="sendMessage()" style='width:100px;height:100%;border:0px;border-top:1px solid black;vertical-align: top;'>发送</button>
</div>
`
export default {
    template: template,
    data: function () {
        return {
            form: {
                type: "message-user",
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
            if (msg.type == 'message-user') {
                if (this.$store.state.messageMap[this.$route.query.url] == undefined) {
                    this.$store.state.messageMap[this.$route.query.url] = { name: msg.name, type: "message-user", ws: this.$route.query.url, message: [] }
                }
                this.$store.state.socketLocal.send(JSON.stringify(msg))
                this.$store.state.messageMap[this.$route.query.url].message.push(msg);
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
