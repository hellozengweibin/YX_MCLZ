import request from '@/utils/request'

// 查询创建资源组资源绑定列表
export function listResgroupbindtbl(query) {
    return request({
        url: '/business/resgroupbindtbl/list',
        method: 'get',
        params: query
    })
}

// 查询创建资源组资源绑定详细
export function getResgroupbindtbl(id) {
    return request({
        url: '/business/resgroupbindtbl/detail/' + id,
        method: 'get'
    })
}

// 新增创建资源组资源绑定
export function addResgroupbindtbl(data) {
    return request({
        url: '/business/resgroupbindtbl',
        method: 'post',
        data: data
    })
}

// 修改创建资源组资源绑定
export function updateResgroupbindtbl(data) {
    return request({
        url: '/business/resgroupbindtbl',
        method: 'put',
        data: data
    })
}

// 删除创建资源组资源绑定
export function delResgroupbindtbl(ids) {
    return request({
        url: '/business/resgroupbindtbl/del',
        method: 'delete',
        data: ids
    })
}
