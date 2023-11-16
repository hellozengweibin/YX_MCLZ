import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/system/menu/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function treeselect(query) {
  return request({
    url: '/system/menu/treeselect',
    method: 'get',
    params: query
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

// 复制菜单
export function copyMenu(platform,parentId,data) {
  return request({
    url: '/system/menu/copy/' + platform + '/' + parentId,
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeMenuStatus(menuId, status,orderNum) {
  const data = {
    menuId,
    status,
    orderNum
  }
  return request({
    url: '/system/menu/changeStatus',
    method: 'put',
    data: data
  })
}

// 修改菜单平台类型
export function updatePlatform(data) {
  return request({
    url: '/system/menu/updatePlatform',
    method: 'post',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/system/menu/' + menuId,
    method: 'delete'
  })
}
