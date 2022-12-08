import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import axios from 'axios'
import store from './store'

Vue.config.productionTip = false
Vue.prototype.$axios=axios
axios.defaults.baseURL = process.env.VUE_APP_URL;

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
