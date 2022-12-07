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
      path: '/:type/select/gu',
      component: ()=>import('../view/selectGuPage')
    },
    {
      path: '/suggestion/:gu/:binType',
      component: ()=>import('../view/binSuggestionPage')
    },
    
    {
      path: '/state/:gu/:conditionType',
      component: ()=>import('../view/binStatePage')
    },
    
  ]
})



export default router