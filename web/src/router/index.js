import Vue from 'vue'
import Router from 'vue-router'
import Mainpage from '../view/mainPage'
import Subpage from '../view/subPage'


Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
          path: '/',
          component: ()=>import('../view/newPage')
    },

    
  ]
})



export default router