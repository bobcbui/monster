<template>
  <router-view />
</template>

<script>
export default {
  name: "index",
  data() {
    return {
    };
  },
  created() {
      this.initWebSocket();
  },
  destroyed() {
    this.$store.state.ws.close() //离开路由之后断开websocket连接
  },
  methods: {
    initWebSocket() {
      this.$store.state.ws = new WebSocket(import.meta.env.VITE_WS + "/tang/ws?jwt="+localStorage.getItem("token"));
      this.$store.state.ws.onmessage = this.websocketonmessage;
      this.$store.state.ws.onopen = this.websocketonopen;
      this.$store.state.ws.onerror = this.websocketonerror;
      this.$store.state.ws.onclose = this.websocketclose;
    },
    websocketonopen() {
      //连接建立之后执行send方法发送数据
      let actions = { test: "12345" };
      this.websocketsend(JSON.stringify(actions));
    },
    websocketonerror() {
      //连接建立失败重连
      this.initWebSocket();
    },
    websocketonmessage(e) {
      let messageList = JSON.parse(e.data)
        console.log(e.data)
        messageList.forEach((item,index) => {
            let to = item.to
            if(this.$store.state.message[to] == undefined){
                this.$store.state.message[to] = [item]
            }else{
                this.$store.state.message[to].push(item)
            }
            this.$store.state.newMessage[to] = item
        })
    },
    websocketsend(Data) {
      //数据发送
      this.$store.state.ws.send(Data);
    },
    websocketclose(e) {
      //关闭
      console.log("断开连接", e);
    },
  },
};
</script>

<style scoped>
.head-table {
  border: 1px solid black;
  border-bottom: 0px;
  margin: auto;
  width: 100%;
  max-width: 800px;
  text-align: center;
  border-top: 0;
}
</style>