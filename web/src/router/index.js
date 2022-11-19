import Vue from 'vue'
import Router from 'vue-router'



Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
          path: '/',
          component: ()=>import('../view/mainPage')
    },
    {
      path: '/state/select/gu',
      component: ()=>import('../view/selectGuPage')
    },
    {
      path: '/state/:gu/:conditionType',
      component: ()=>import('../view/binStatePage')
    },
    
  ]
})



export default router