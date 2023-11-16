import request from '@/utils/request'

// 查询通道列表
export function listRoute(query) {
  return request({
    url: '/business/route/list',
    method: 'get',
    params: query
  })
}

// 查询通道详细
export function getRoute(id) {
  return request({
    url: '/business/route/' + id,
    method: 'get'
  })
}

// 新增通道
export function addRoute(data) {
  return request({
    url: '/business/route',
    method: 'post',
    data: data
  })
}

// 修改通道
export function updateRoute(data) {
  return request({
    url: '/business/route',
    method: 'put',
    data: data
  })
}

// 删除通道
export function delRoute(id) {
  return request({
    url: '/business/route/' + id,
    method: 'delete'
  })
}
