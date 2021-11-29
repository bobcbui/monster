import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './index.scss'
import './permission'
import { provide } from "vue";

const app = createApp(App)

app.use(router).use(store).mount('#app')

var websocket = {};

window.onload = function () {
    if (!'WebSocket' in window) {
        return;
    }
    webSocketInit();
    console.log('webSocketInit')
    app.provide('ws',websocket)
};


function webSocketInit() {
    websocket = new WebSocket(import.meta.env.VITE_WS + "/tang/ws?jwt="+localStorage.getItem("token"));
    //成功建立连接
    websocket.onopen = function () {
        websocket.send(JSON.stringify({"messageText":"成功连接到服务器"}));
        console.log('成功连接到服务器')
    };
    //接收到消息
    websocket.onmessage = function (event) {
        let messageList = JSON.parse(event.data)
        console.log(event.data)
        messageList.forEach((item,index) => {
            let to = item.to
            if(store.state.message[to] == undefined){
                store.state.message[to] = [item]
            }else{
                store.state.message[to].push(item)
            }
        })
    };
    //连接发生错误
    websocket.onerror = function () {
        console.log("WebSocket连接发生错误");
    };
    //连接关闭
    websocket.onclose = function () {
        console.log("WebSocket连接关闭");
    };
    //监听窗口关闭事件，当窗口关闭时，主动关闭websocket连接
    window.onbeforeunload = function () {
        websocket.close()
    };
}
app.config.globalProperties.$ws = websocket