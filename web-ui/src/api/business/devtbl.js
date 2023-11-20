import request from '@/utils/request'

// 1查询创建设备列表
export function listDevtbl(query) {
    return request({
        url: '/business/devtbl/list',
        method: 'get',
        params: query
    })
}

// 查询创建设备详细
export function getDevtbl(id) {
    return request({
        url: '/business/devtbl/detail/' + id,
        method: 'get'
    })
}

// 新增创建设备
export function addDevtbl(data) {
    return request({
        url: '/business/devtbl',
        method: 'post',
        data: data
    })
}

// 修改创建设备
export function updateDevtbl(data) {
    return request({
        url: '/business/devtbl',
        method: 'put',
        data: data
    })
}

// 删除创建设备
export function delDevtbl(ids) {
    return request({
        url: '/business/devtbl/del',
        method: 'delete',
        data: ids
    })
}
