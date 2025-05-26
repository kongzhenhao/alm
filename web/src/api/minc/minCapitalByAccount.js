import request from '@/utils/request'

// 查询市场及信用最低资本表列表
export function listMinCapitalByAccount(query) {
  return request({
    url: '/minc/min/capital/by/account/list',
    method: 'get',
    params: query
  })
}

// 查询市场及信用最低资本表详细
export function getMinCapitalByAccount(id) {
  return request({
    url: '/minc/min/capital/by/account/' + id,
    method: 'get'
  })
}

// 新增市场及信用最低资本表
export function addMinCapitalByAccount(data) {
  return request({
    url: '/minc/min/capital/by/account',
    method: 'post',
    data: data
  })
}

// 修改市场及信用最低资本表
export function updateMinCapitalByAccount(data) {
  return request({
    url: '/minc/min/capital/by/account',
    method: 'put',
    data: data
  })
}

// 删除市场及信用最低资本表
export function delMinCapitalByAccount(id) {
  return request({
    url: '/minc/min/capital/by/account/' + id,
    method: 'delete'
  })
}

// 获取项目定义列表（用于下拉选择）
export function getItemDefinitions() {
  return request({
    url: '/minc/min/capital/by/account/itemDefinitions',
    method: 'get'
  })
}
