import request from '@/utils/request'

// 查询resgroupbindtbl3列表
export function listResgroupbindtbl3(query) {
    return request({
        url: '/business/resgroupbindtbl3/list',
        method: 'get',
        params: query
    })
}

// 查询resgroupbindtbl3详细
export function getResgroupbindtbl3(id) {
    return request({
        url: '/business/resgroupbindtbl3/detail/' + id,
        method: 'get'
    })
}

// 新增resgroupbindtbl3
export function addResgroupbindtbl3(data) {
    return request({
        url: '/business/resgroupbindtbl3',
        method: 'post',
        data: data
    })
}

// 修改resgroupbindtbl3
export function updateResgroupbindtbl3(data) {
    return request({
        url: '/business/resgroupbindtbl3',
        method: 'put',
        data: data
    })
}

// 删除resgroupbindtbl3
export function delResgroupbindtbl3(ids) {
    return request({
        url: '/business/resgroupbindtbl3/del',
        method: 'delete',
        data: ids
    })
}
