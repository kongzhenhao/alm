import request from '@/utils/request'

// 查询风险项目金额列表
export function listRiskItemAmount(query) {
  return request({
    url: '/minc/risk/item/amount/list',
    method: 'get',
    params: query
  })
}

// 查询风险项目金额详细
export function getRiskItemAmount(id) {
  return request({
    url: '/minc/risk/item/amount/' + id,
    method: 'get'
  })
}

// 新增风险项目金额
export function addRiskItemAmount(data) {
  return request({
    url: '/minc/risk/item/amount',
    method: 'post',
    data: data
  })
}

// 批量新增风险项目金额
export function batchAddRiskItemAmount(data) {
  return request({
    url: '/minc/risk/item/amount/batchAdd',
    method: 'post',
    data: data
  })
}

// 修改风险项目金额
export function updateRiskItemAmount(data) {
  return request({
    url: '/minc/risk/item/amount',
    method: 'put',
    data: data
  })
}

// 删除风险项目金额
export function delRiskItemAmount(id) {
  return request({
    url: '/minc/risk/item/amount/' + id,
    method: 'delete'
  })
}

// 根据账期删除风险项目金额
export function delRiskItemAmountByPeriod(accountingPeriod) {
  return request({
    url: '/minc/risk/item/amount/period/' + accountingPeriod,
    method: 'delete'
  })
}

// 根据条件删除风险项目金额
export function delRiskItemAmountByCondition(accountingPeriod, itemCode) {
  return request({
    url: '/minc/risk/item/amount/condition',
    method: 'delete',
    params: {
      accountingPeriod,
      itemCode
    }
  })
}

// 导出风险项目金额
export function exportRiskItemAmount(query) {
  return request({
    url: '/minc/risk/item/amount/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 导出风险项目金额模板
export function exportRiskItemAmountTemplate() {
  return request({
    url: '/minc/risk/item/amount/exportTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
