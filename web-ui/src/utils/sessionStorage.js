// vue里面使用sessionStorage
// https://github.com/chaiyining007/libs

const _is_session_storage = !!window.sessionStorage
let _data_session_dom, _dataSessionDom_name = window.location.hostname || 'sessionUserData'
if (!_is_session_storage) {
    try {
        _data_session_dom = document.createElement('input')
        _data_session_dom.type = 'hidden'
        _data_session_dom.style.display = 'none'
        _data_session_dom.addBehavior('#default#userData') //userData的语法
        document.body.appendChild(_data_session_dom)
        _data_session_dom.expires = new Date($.date().addDays(365)).toUTCString() //设定过期时间
        //加载userdata
        _data_session_dom.load(_dataSessionDom_name)
    } catch (e) {
        _data_session_dom = null
        console.log('userData初始化失败！')
    }
}
export const session_storage = (key, value) => {
    let _return_value
    try {
        if (_is_session_storage) {
            if (value != undefined) {
                sessionStorage.setItem(encodeURIComponent(key), encodeURIComponent(value))
            } else {
                _return_value = sessionStorage.getItem(encodeURIComponent(key))
                return _return_value ? decodeURIComponent(_return_value) : null
            }
        } else if (_data_session_dom) {
            if (value != undefined) {
                _data_session_dom.setAttribute(key, value)
                _data_session_dom.save(_dataSessionDom_name)
            } else {
                _return_value = _data_session_dom.getAttribute(key)
                return _return_value ? decodeURIComponent(_return_value) : null
            }
        }
    } catch (e) {
        return false
    }
}
export const remove_session_storage_item = (key) => {
    if (_is_session_storage) {
        sessionStorage.removeItem(encodeURIComponent(key))
    } else if (_data_session_dom) {
        _data_session_dom.removeAttribute(encodeURIComponent(key))
        _data_session_dom.save(_dataSessionDom_name)
    }
}
export const clear_session_storage = () => {
    if (_is_session_storage) {
        sessionStorage.clear()
    } else {
        _data_session_dom.remove()
    }
}