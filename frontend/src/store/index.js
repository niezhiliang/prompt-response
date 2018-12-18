import Vue from 'vue'
import Vuex from 'vuex'
import request from '@/components/request.js'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    index: '/index',
    islogin: true
  },
  mutations: {
    changeHead: (state, payload) => {
      state.index = payload.index
    },
    login: (state, payload) => {
      state.islogin = payload.islogin
    }
  },
  actions: {
    changeHead (context, payload) {
      context.commit('changeHead', { index: payload.index })
    },
    login (context, user) {
      return request.post('unchk/login/index', user).then((res) => {
        if (res.data.code === 20) {
          context.commit('login', { islogin: true })
          return res.data
        } else {
          return res.data
        }
      })
    }
  }
})
