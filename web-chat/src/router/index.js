import { createRouter, createWebHistory } from "vue-router";

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import('../views/index.vue'),
        children: [
            {
                path: '/',
                name: 'home',
                component: () => import('../views/home.vue'),
                children:[
                    {
                        path: '/gang/:gangId',
                        name: 'gang_message',
                        component: () => import('../views/gang_message.vue')
                    }, {
                        path: '/',
                        name: 'plaza',
                        component: () => import('../views/plaza.vue')
                    },{
                        path: '/setting',
                        name: 'setting',
                        component: () => import('../views/setting.vue')
                    },
                ]
            },
            
           
            
        ]
    },{
        path: '/login',
        name: 'login',
        component: () => import('../views/login.vue'),
    },{
        path: '/demo',
        name: 'demo',
        component: () => import('../views/demo.vue'),
    },{
        path: '/register',
        name: 'register',
        component: () => import('../views/register.vue'),
    }
]
const router = createRouter({
    history: createWebHistory(), routes
});

export default router