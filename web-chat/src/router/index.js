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
                component: () => import('../views/home.vue')
            },
            {
                path: '/group/:groupId',
                name: 'group_message',
                component: () => import('../views/group_message.vue')
            },
        ]
    },{
        path: '/login',
        name: 'login',
        component: () => import('../views/login.vue'),
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