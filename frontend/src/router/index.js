import Vue from 'vue'
import Router from 'vue-router'
import App from '@/App'
import Qrcode from '@/pages/qrcode'
import Phone from '@/pages/phonedraw'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: App,
      children: [
        {
          path: '/',
          component: Qrcode,
          name: 'qrcode'
        },
        {
          path: '/qrcode',
          component: Qrcode,
          name: 'qrcode'
        },
        {
          path: '/phone',
          component: Phone,
          name: 'phone'
        }
      ]
    }
  ]
})
