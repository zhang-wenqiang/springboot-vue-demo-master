import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
//引入全局样式
import './assets/css/global.css'
//引入字体图标
import './assets/fonts/iconfont.css'
//引入组件
import TreeTable from 'vue-table-with-tree-grid'
// 引入富文本编辑器
import VueQuillEditor from 'vue-quill-editor'
// require styles 导入富文本编辑器样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
// 引入加载进度条
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
// 引入axios请求
import axios from 'axios'
//配置请求根路径
//axios.defaults.baseURL = ''
// 在reques拦截器，展示进图条
// axios.interceptors.request.use(config => {
//   NProgress.start()
//   config.headers.Authorization = window.sessionStorage.getItem('token')
//   return config
// })
// 在response拦截器，隐藏进度条
// axios.interceptors.response.use(config => {
//   NProgress.done()
//   return config
// })

Vue.prototype.$http = axios


Vue.config.productionTip = false

Vue.component('tree-table', TreeTable)
// 富文本编辑器注册为全局组件
Vue.use(VueQuillEditor)
// 进度条注册全局组件
// Vue.use(nProgress)

Vue.filter('dateFormat', function (originVal) {
  const dt = new Date(originVal)

  const y = dt.getFullYear()
  const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  const d = (dt.getDate() + '').padStart(2, '0')

  const hh = (dt.getHours() + '').padStart(2, '0')
  const mm = (dt.getMinutes() + '').padStart(2, '0')
  const ss = (dt.getSeconds() + '').padStart(2, '0')

  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
