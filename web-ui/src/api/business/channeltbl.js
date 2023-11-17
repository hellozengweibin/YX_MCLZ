import request from '@/utils/request'

// 查询创建二级设备列表
export function listChanneltbl(query) {
    return request({
        url: '/business/channeltbl/list',
        method: 'get',
        params: query
    })
}

// 查询创建二级设备详细
export function getChanneltbl(id) {
    return request({
        url: '/business/channeltbl/detail/' + id,
        method: 'get'
    })
}

// 新增创建二级设备
export function addChanneltbl(data) {
    return request({
        url: '/business/channeltbl',
        method: 'post',
        data: data
    })
}

// 修改创建二级设备
export function updateChanneltbl(data) {
    return request({
        url: '/business/channeltbl',
        method: 'put',
        data: data
    })
}

// 删除创建二级设备
export function delChanneltbl(ids) {
    return request({
        url: '/business/channeltbl/del',
        method: 'delete',
        data: ids
    })
}
