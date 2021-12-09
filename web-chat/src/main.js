import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './index.scss'
import './permission'

const app = createApp(App)
app.use(router).use(store).mount('#app')
if(window.location.pathname != '/login' && window.location.pathname != '/register'){
    if(localStorage.getItem("token") == null){
        window.location.href = '/login'
    }
}