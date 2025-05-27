import request from '@/utils/request'

// 查询利率风险对冲率表列表
export function listIrHedgeRatio(query) {
  return request({
    url: '/minc/ir/hedge/ratio/list',
    method: 'get',
    params: query
  })
}

// 查询利率风险对冲率表详细
export function getIrHedgeRatio(id) {
  return request({
    url: '/minc/ir/hedge/ratio/' + id,
    method: 'get'
  })
}

// 新增利率风险对冲率表
export function addIrHedgeRatio(data) {
  return request({
    url: '/minc/ir/hedge/ratio',
    method: 'post',
    data: data
  })
}

// 修改利率风险对冲率表
export function updateIrHedgeRatio(data) {
  return request({
    url: '/minc/ir/hedge/ratio',
    method: 'put',
    data: data
  })
}

// 删除利率风险对冲率表
export function delIrHedgeRatio(id) {
  return request({
    url: '/minc/ir/hedge/ratio/' + id,
    method: 'delete'
  })
}

// 根据账期删除利率风险对冲率表
export function delIrHedgeRatioByPeriod(accountingPeriod) {
  return request({
    url: '/minc/ir/hedge/ratio/period/' + accountingPeriod,
    method: 'delete'
  })
}

// 导出利率风险对冲率表
export function exportIrHedgeRatio(query) {
  return request({
    url: '/minc/ir/hedge/ratio/export',
    method: 'post',
    params: query
  })
}

// 获取利率风险对冲率表导入模板
export function importTemplateIrHedgeRatio() {
  return request({
    url: '/minc/ir/hedge/ratio/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入利率风险对冲率表数据
export function importDataIrHedgeRatio(data) {
  return request({
    url: '/minc/ir/hedge/ratio/importData',
    method: 'post',
    data: data
  })
}
