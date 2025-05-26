import request from '@/utils/request'

// 获取项目定义列表（用于下拉选择）
export function getItemDefinitions() {
  return request({
    url: '/minc/correlation/coef/itemDefinitions',
    method: 'get'
  })
}

// 查询相关系数表列表
export function listCorrelationCoef(query) {
  return request({
    url: '/minc/correlation/coef/list',
    method: 'get',
    params: query
  })
}

// 查询相关系数表详细
export function getCorrelationCoef(id) {
  return request({
    url: '/minc/correlation/coef/' + id,
    method: 'get'
  })
}

// 新增相关系数表
export function addCorrelationCoef(data) {
  return request({
    url: '/minc/correlation/coef',
    method: 'post',
    data: data
  })
}

// 修改相关系数表
export function updateCorrelationCoef(data) {
  return request({
    url: '/minc/correlation/coef',
    method: 'put',
    data: data
  })
}

// 删除相关系数表
export function delCorrelationCoef(id) {
  return request({
    url: '/minc/correlation/coef/' + id,
    method: 'delete'
  })
}

// 导出相关系数表
export function exportCorrelationCoef(query) {
  return request({
    url: '/minc/correlation/coef/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 获取相关系数表导入模板
export function getImportTemplate() {
  return request({
    url: '/minc/correlation/coef/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入相关系数表
export function importCorrelationCoef(data) {
  return request({
    url: '/minc/correlation/coef/importData',
    method: 'post',
    data: data
  })
}
