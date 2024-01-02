//import functions and components
import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import HomePage from '@/components/Home.vue'
import ResultsPage from '@/components/Results.vue'
import ProductPage from '@/components/Product.vue'

//routes for the website
const routes = [
    { path: '/', component: HomePage },
    { path: '/results/:watchName', name: 'results', component: ResultsPage, props: true },
    { path: '/product/:ids', name: 'product', component: ProductPage, props: true }
]
//create router and history
const router = createRouter({
    history: createWebHistory(),
    routes
})
//create vue app and mount it to the app element
createApp(App).use(router).mount('#app')

export default router;
