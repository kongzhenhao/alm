import request from '@/utils/request'

// 查询负债现金流汇总列表
export function listLiabilityCashFlowSummary(query) {
  return request({
    url: '/dur/liability/cash/flow/summary/list',
    method: 'get',
    params: query
  })
}

// 查询负债现金流汇总详细
export function getLiabilityCashFlowSummary(id) {
  return request({
    url: '/dur/liability/cash/flow/summary/' + id,
    method: 'get'
  })
}

// 根据条件查询负债现金流汇总
export function getLiabilityCashFlowSummaryByCondition(accountPeriod, cashFlowType, durationType, designType, isShortTerm) {
  return request({
    url: '/dur/liability/cash/flow/summary/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      durationType,
      designType,
      isShortTerm
    }
  })
}

// 新增负债现金流汇总
export function addLiabilityCashFlowSummary(data) {
  return request({
    url: '/dur/liability/cash/flow/summary',
    method: 'post',
    data: data
  })
}

// 批量新增负债现金流汇总
export function batchAddLiabilityCashFlowSummary(data) {
  return request({
    url: '/dur/liability/cash/flow/summary/batchAdd',
    method: 'post',
    data: data
  })
}

// 修改负债现金流汇总
export function updateLiabilityCashFlowSummary(data) {
  return request({
    url: '/dur/liability/cash/flow/summary',
    method: 'put',
    data: data
  })
}

// 删除负债现金流汇总
export function delLiabilityCashFlowSummary(id) {
  return request({
    url: '/dur/liability/cash/flow/summary/' + id,
    method: 'delete'
  })
}

// 根据账期删除负债现金流汇总
export function delLiabilityCashFlowSummaryByPeriod(accountPeriod) {
  return request({
    url: '/dur/liability/cash/flow/summary/period/' + accountPeriod,
    method: 'delete'
  })
}

// 导出负债现金流汇总
export function exportLiabilityCashFlowSummary(query) {
  return request({
    url: '/dur/liability/cash/flow/summary/export',
    method: 'post',
    data: query
  })
}

// 获取负债现金流汇总导入模板
export function getImportTemplate() {
  return request({
    url: '/dur/liability/cash/flow/summary/exportTemplate',
    method: 'post'
  })
}
