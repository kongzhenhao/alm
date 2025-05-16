import request from '@/utils/request'

// 查询分账户负债基点价值DV10列表
export function listAccountLiabilityDv10(query) {
  return request({
    url: '/dur/account/liability/dv10/list',
    method: 'get',
    params: query
  })
}

// 查询分账户负债基点价值DV10详细
export function getAccountLiabilityDv10(id) {
  return request({
    url: '/dur/account/liability/dv10/' + id,
    method: 'get'
  })
}

// 根据条件查询分账户负债基点价值DV10
export function getAccountLiabilityDv10ByCondition(accountPeriod, cashFlowType, designType, valueType) {
  return request({
    url: '/dur/account/liability/dv10/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      designType,
      valueType
    }
  })
}

// 新增分账户负债基点价值DV10
export function addAccountLiabilityDv10(data) {
  return request({
    url: '/dur/account/liability/dv10',
    method: 'post',
    data: data
  })
}

// 修改分账户负债基点价值DV10
export function updateAccountLiabilityDv10(data) {
  return request({
    url: '/dur/account/liability/dv10',
    method: 'put',
    data: data
  })
}

// 删除分账户负债基点价值DV10
export function delAccountLiabilityDv10(ids) {
  return request({
    url: '/dur/account/liability/dv10/' + ids,
    method: 'delete'
  })
}

// 删除指定账期的分账户负债基点价值DV10
export function delAccountLiabilityDv10ByPeriod(accountPeriod) {
  return request({
    url: '/dur/account/liability/dv10/period/' + accountPeriod,
    method: 'delete'
  })
}

// 导出分账户负债基点价值DV10
export function exportAccountLiabilityDv10(query) {
  return request({
    url: '/dur/account/liability/dv10/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 下载分账户负债基点价值DV10模板
export function downloadTemplate() {
  return request({
    url: '/dur/account/liability/dv10/exportTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
