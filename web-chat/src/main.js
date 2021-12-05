import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './index.scss'
import './permission'
import Varlet from '@varlet/ui'
import '@varlet/ui/es/style.js'


const app = createApp(App)
app.use(router).use(store).use(Varlet).mount('#app')
var ws = {};
window.onload = function () {
    if (!'WebSocket' in window) {
        return;
    }
    store.state.ws = webSocketInit();
    console.log('webSocketInit')
};

function webSocketInit() {
    ws = new WebSocket(import.meta.env.VITE_WS + "/tang/ws?jwt="+localStorage.getItem("token"));
    //成功建立连接
    ws.onopen = function () {
        ws.send(JSON.stringify({"text":"成功连接到服务器"}));
        console.log('成功连接到服务器')
    };
    //接收到消息
    ws.onmessage = function (event) {
        let messageList = JSON.parse(event.data)
        console.log(event.data)
        messageList.forEach((item,index) => {
            let to = item.to
            if(store.state.message[to] == undefined){
                store.state.message[to] = [item]
            }else{
                store.state.message[to].push(item)
            }
            store.state.newMessage[to] = item
        })
    };
    //连接发生错误
    ws.onerror = function () {
        console.log("WebSocket连接发生错误");
    };
    //连接关闭
    ws.onclose = function () {
        console.log("WebSocket连接关闭");
    };
    //监听窗口关闭事件，当窗口关闭时，主动关闭websocket连接
    window.onbeforeunload = function () {
        ws.close()
    };

    return ws;
}
if(window.location.pathname != '/login' && window.location.pathname != '/register'){
    if(localStorage.getItem("token") == null){
        window.location.href = '/login'
    }
}