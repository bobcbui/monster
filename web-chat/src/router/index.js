import { createRouter, createWebHistory } from "vue-router";

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import('../views/index.vue'),
        children: [
            {
                path: '/',
                name: 'home_index',
                component: () => import('../views/home.vue')
            },
            {
                path: '/group/:groupId',
                name: 'group_index',
                component: () => import('../views/group.vue')
            },
        ]
    },{
        path: '/login',
        name: 'login_index',
        component: () => import('../views/login.vue'),
    }
]
const router = createRouter({
    history: createWebHistory(), routes
});

export default router