import request from '@/utils/request'

// 查询分中短负债基点价值DV10列表
export function listLiabilityDv10ByDuration(query) {
  return request({
    url: '/dur/liability/dv10/by/duration/list',
    method: 'get',
    params: query
  })
}

// 查询分中短负债基点价值DV10详细
export function getLiabilityDv10ByDuration(id) {
  return request({
    url: '/dur/liability/dv10/by/duration/' + id,
    method: 'get'
  })
}

// 根据条件查询分中短负债基点价值DV10
export function getLiabilityDv10ByDurationByCondition(accountPeriod, cashFlowType, designType, shortTermFlag, valueType) {
  return request({
    url: '/dur/liability/dv10/by/duration/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      designType,
      shortTermFlag,
      valueType
    }
  })
}

// 新增分中短负债基点价值DV10
export function addLiabilityDv10ByDuration(data) {
  return request({
    url: '/dur/liability/dv10/by/duration',
    method: 'post',
    data: data
  })
}

// 修改分中短负债基点价值DV10
export function updateLiabilityDv10ByDuration(data) {
  return request({
    url: '/dur/liability/dv10/by/duration',
    method: 'put',
    data: data
  })
}

// 删除分中短负债基点价值DV10
export function delLiabilityDv10ByDuration(ids) {
  return request({
    url: '/dur/liability/dv10/by/duration/' + ids,
    method: 'delete'
  })
}

// 删除指定账期的分中短负债基点价值DV10
export function delLiabilityDv10ByDurationByPeriod(accountPeriod) {
  return request({
    url: '/dur/liability/dv10/by/duration/period/' + accountPeriod,
    method: 'delete'
  })
}

// 导出分中短负债基点价值DV10
export function exportLiabilityDv10ByDuration(query) {
  return request({
    url: '/dur/liability/dv10/by/duration/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 下载分中短负债基点价值DV10模板
export function downloadTemplate() {
  return request({
    url: '/dur/liability/dv10/by/duration/exportTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
