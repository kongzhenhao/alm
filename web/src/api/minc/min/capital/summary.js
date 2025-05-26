import request from '@/utils/request'

// 获取项目定义列表（用于下拉选择）
export function getItemDefinitions() {
  return request({
    url: '/minc/min/capital/summary/itemDefinitions',
    method: 'get'
  })
}

// 查询最低资本明细汇总表列表
export function listMinCapitalSummary(query) {
  return request({
    url: '/minc/min/capital/summary/list',
    method: 'get',
    params: query
  })
}

// 查询最低资本明细汇总表详细
export function getMinCapitalSummary(id) {
  return request({
    url: '/minc/min/capital/summary/' + id,
    method: 'get'
  })
}

// 新增最低资本明细汇总表
export function addMinCapitalSummary(data) {
  return request({
    url: '/minc/min/capital/summary',
    method: 'post',
    data: data
  })
}

// 修改最低资本明细汇总表
export function updateMinCapitalSummary(data) {
  return request({
    url: '/minc/min/capital/summary',
    method: 'put',
    data: data
  })
}

// 删除最低资本明细汇总表
export function delMinCapitalSummary(id) {
  return request({
    url: '/minc/min/capital/summary/' + id,
    method: 'delete'
  })
}

// 导出最低资本明细汇总表
export function exportMinCapitalSummary(query) {
  return request({
    url: '/minc/min/capital/summary/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
