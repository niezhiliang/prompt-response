import axios from 'axios'
import ele from 'element-ui'
import vc from 'vue-cookie'
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  return config
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error)
})

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error)
})

export default {

  post (url, data) {
    return axios({
      method: 'post',
      baseURL: 'http://localhost:8080/',
      url,
      data: data,
      timeout: 10000,
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/json; charset=UTF-8'
      }
    })
      .then(
        (response) => {
          if (response && (response.status === 200 || response.status === 304 || response.status === 400)) {
            return response
            // 如果不需要除了data之外的数据，可以直接 return response.data
          }
          ele.Message('网络异常')
        }
      )
  }
}
