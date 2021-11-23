import router from './router'
import store from './store'

// 路由守卫
router.beforeEach((to, from, next) => {
    next()
});
router.afterEach((to, from, next) => {
    if(localStorage.getItem("member") != null){
        store.state.login = JSON.parse(localStorage.getItem("member"))
    }else{
        store.state.login = ""
    }
})