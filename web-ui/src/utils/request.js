import axios from 'axios'
import router from '@/router'
import {Loading, Message, MessageBox, Notification} from 'element-ui'
import store from '@/store'
import {getToken} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {blobValidate, isJsonText, tansParams} from "@/utils/util";
import cache from '@/plugins/cache'
import {saveAs} from 'file-saver'
import AES from '@/utils/aesSecret'
import RSA from "@/utils/jsencrypt";
import CryptoJS from 'crypto-js'
// import { ExitExe } from '@/views/dispatch/msgclient'


const enable = process.env.VUE_APP_ENABLE_ENCRYPT
const randomKey = AES.getKey()
let downloadLoadingInstance
const aesKeys = {}
// 是否显示重新登录
export let isRelogin = {show: false};

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 10000 * 6
})

// request拦截器
service.interceptors.request.use(config => {
  // 做请求覆盖
  if (config.method.toLowerCase() === 'put') {
    config.method = 'post'
    config.headers['X-HTTP-Method-Override'] = 'put'
  } else if (config.method.toLowerCase() === 'delete') {
    config.method = 'post'
    config.headers['X-HTTP-Method-Override'] = 'delete'
  }
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }

  config.headers['encryptInfo'] = RSA.encrypt(randomKey)
  const timestamp = new Date().getTime();
  config.headers['timestamp'] = timestamp
  config.headers['sign'] = CryptoJS.MD5(timestamp + randomKey)

  let enStr = undefined
  // 判断是否加密
  if (enable && enable === 'true') {
    const uuid = AES.getUuid()
    config.headers['uuid'] = uuid
    aesKeys[uuid] = randomKey

    // url参数加密
    if (config.params) {
      let params = tansParams(config.params)
      if (params) {
        params = params.slice(0, -1);
        enStr = AES.encrypt(params, randomKey)
      }
    }
  }

  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    // 拼接url加密参数
    let url = config.url;
    if (enStr) {
      const param = {requestParam: enStr}
      config.params = param
    } else {
      url = url + "?" + tansParams(config.params)
      url = url.slice(0, -1)
      config.params = null
    }
    config.url = url;
    // let url = config.url + '?' + tansParams(config.params);
    // url = url.slice(0, -1);
    // config.params = {};
    // config.url = url;
  }
  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    let requestObj = {};
    if (enable && enable === 'true' &&
      config.headers['Content-Type'] !== 'application/x-www-form-urlencoded'/* 下载接口不加密 */) {
      // 加密 请求体
      // const randomKey = AES.getKey()
      // const uuid = AES.getUuid()
      requestObj = {
        url: config.url,
        // 进行请求加密
        data: AES.encrypt(typeof config.data === 'object' ? JSON.stringify(config.data) : config.data, randomKey),
        time: timestamp
      }
      // 联调封装的请求头结构
      config.data = {requestData: requestObj.data}
    } else {
      requestObj = {
        url: config.url,
        // 进行请求加密
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: timestamp
      }
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;                  // 请求地址
      const s_data = sessionObj.data;                // 请求数据
      const s_time = sessionObj.time;                // 请求时间
      const interval = 1000;                         // 间隔时间(ms)，小于此时间视为重复提交
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = '数据正在处理，请勿重复提交';
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    // 响应解密
    let resData = res.data;
    if (res.responseData || resData.responseData) {
      if (enable && enable === 'true') {
        const uuid = res.headers['uuid']
        const key = aesKeys[uuid]
        if (resData.responseData) {
          resData = AES.decrypt(resData.responseData, key);
        } else {
          resData = AES.decrypt(res.responseData, key);
        }
        delete aesKeys[uuid]
        // 去除空格
        resData = resData.replaceAll('\0', '')
        if (isJsonText(resData)) {
          resData = JSON.parse(resData);
        }
      }
    }
    // 未设置状态码则默认成功状态
    const code = resData.code || 200;
    // 获取错误信息
    const msg = errorCode[code] || resData.msg || errorCode['default']
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return resData
    }
    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true;
        MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          isRelogin.show = false;
          store.dispatch('LogOut').then(() => {
            // location.href = '/index';
            router.push(`/login`)
            // let indexurl = this.$store.state.permission.indexPage
            // location.href = indexurl;
          })
        }).catch(() => {
          isRelogin.show = false;
        });
      }
      // ExitExe()
      return Promise.reject('会话失效或已过期，请重新登录。')
    } else if (code === 500) {
      Message({
        message: msg,
        type: 'error'
      })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      Notification.error({
        title: msg
      })
      return Promise.reject('error')
    } else {
      return resData
    }
  },
  error => {
    console.log('err' + error)
    let {message} = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

// 通用下载方法
export function download(url, params, filename) {
  downloadLoadingInstance = Loading.service({
    text: "正在下载数据，请稍候",
    spinner: "el-icon-loading",
    background: "rgba(0, 0, 0, 0.7)",
  })
  return service.post(url, params, {
    transformRequest: [(params) => {
      return tansParams(params)
    }],
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    responseType: 'blob'
  }).then(async (data) => {
    const isLogin = await blobValidate(data);
    if (isLogin) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      Message.error(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}

export default service
