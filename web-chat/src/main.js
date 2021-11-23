import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//import './index.scss'
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
    websocket = new WebSocket("ws://192.168.0.108:9090/tang/ws?jwt="+localStorage.getItem("token"));
    //成功建立连接
    websocket.onopen = function () {
        websocket.send(JSON.stringify({"messageText":"成功连接到服务器"}));
        console.log('成功连接到服务器')
    };
    //接收到消息
    websocket.onmessage = function (event) {
        //store.message = event.data
        JSON.parse(event.data)
        
        if(store.state.message[JSON.parse(event.data).from] == undefined){
            store.state.message[JSON.parse(event.data).from] = [JSON.parse(event.data)]
        }else{
            store.state.message[JSON.parse(event.data).from].push(JSON.parse(event.data))
        }
        store.state.newMessage[JSON.parse(event.data).from] = JSON.parse(event.data)
        console.log(JSON.stringify(store.state.message))
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