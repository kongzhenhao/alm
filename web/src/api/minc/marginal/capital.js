import request from '@/utils/request'

// 获取项目定义列表（用于下拉选择）
export function getItemDefinitions() {
  return request({
    url: '/minc/marginal/capital/itemDefinitions',
    method: 'get'
  })
}

// 查询边际最低资本贡献率表列表
export function listMarginalCapital(query) {
  return request({
    url: '/minc/marginal/capital/list',
    method: 'get',
    params: query
  })
}

// 查询边际最低资本贡献率表详细
export function getMarginalCapital(id) {
  return request({
    url: '/minc/marginal/capital/' + id,
    method: 'get'
  })
}

// 新增边际最低资本贡献率表
export function addMarginalCapital(data) {
  return request({
    url: '/minc/marginal/capital',
    method: 'post',
    data: data
  })
}

// 修改边际最低资本贡献率表
export function updateMarginalCapital(data) {
  return request({
    url: '/minc/marginal/capital',
    method: 'put',
    data: data
  })
}

// 删除边际最低资本贡献率表
export function delMarginalCapital(id) {
  return request({
    url: '/minc/marginal/capital/' + id,
    method: 'delete'
  })
}

// 导出边际最低资本贡献率表
export function exportMarginalCapital(query) {
  return request({
    url: '/minc/marginal/capital/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}


