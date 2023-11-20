import request from '@/utils/request'

// 查询resgroupbindtbl2列表
export function listResgroupbindtbl2(query) {
    return request({
        url: '/business/resgroupbindtbl2/list',
        method: 'get',
        params: query
    })
}

// 查询resgroupbindtbl2详细
export function getResgroupbindtbl2(id) {
    return request({
        url: '/business/resgroupbindtbl2/detail/' + id,
        method: 'get'
    })
}

// 新增resgroupbindtbl2
export function addResgroupbindtbl2(data) {
    return request({
        url: '/business/resgroupbindtbl2',
        method: 'post',
        data: data
    })
}

// 修改resgroupbindtbl2
export function updateResgroupbindtbl2(data) {
    return request({
        url: '/business/resgroupbindtbl2',
        method: 'put',
        data: data
    })
}

// 删除resgroupbindtbl2
export function delResgroupbindtbl2(ids) {
    return request({
        url: '/business/resgroupbindtbl2/del',
        method: 'delete',
        data: ids
    })
}
