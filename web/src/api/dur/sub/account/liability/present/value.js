import request from '@/utils/request';

// 查询分账户负债现金流现值汇总列表
export function listSubAccountLiabilityPresentValue(query) {
  return request({
    url: '/dur/sub/account/liability/present/value/list',
    method: 'get',
    params: query,
  });
}

// 查询分账户负债现金流现值汇总详细
export function getSubAccountLiabilityPresentValue(id) {
  return request({
    url: '/dur/sub/account/liability/present/value/' + id,
    method: 'get',
  });
}

// 根据条件查询分账户负债现金流现值汇总
export function getSubAccountLiabilityPresentValueByCondition(accountPeriod, cashFlowType, bpType, durationType, designType) {
  return request({
    url: '/dur/sub/account/liability/present/value/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      bpType,
      durationType,
      designType
    },
  });
}

// 新增分账户负债现金流现值汇总
export function addSubAccountLiabilityPresentValue(data) {
  return request({
    url: '/dur/sub/account/liability/present/value',
    method: 'post',
    data: data,
  });
}

// 批量新增分账户负债现金流现值汇总
export function batchAddSubAccountLiabilityPresentValue(data) {
  return request({
    url: '/dur/sub/account/liability/present/value/batchAdd',
    method: 'post',
    data: data,
  });
}

// 修改分账户负债现金流现值汇总
export function updateSubAccountLiabilityPresentValue(data) {
  return request({
    url: '/dur/sub/account/liability/present/value',
    method: 'put',
    data: data,
  });
}

// 删除分账户负债现金流现值汇总
export function delSubAccountLiabilityPresentValue(ids) {
  return request({
    url: '/dur/sub/account/liability/present/value/' + ids,
    method: 'delete',
  });
}

// 删除指定账期的分账户负债现金流现值汇总
export function delSubAccountLiabilityPresentValueByPeriod(accountPeriod) {
  return request({
    url: '/dur/sub/account/liability/present/value/period/' + accountPeriod,
    method: 'delete',
  });
}

// 导出分账户负债现金流现值汇总数据
export function exportSubAccountLiabilityPresentValue(query) {
  return request({
    url: '/dur/sub/account/liability/present/value/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 下载分账户负债现金流现值汇总模板
export function downloadTemplate() {
  return request({
    url: '/dur/sub/account/liability/present/value/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入分账户负债现金流现值汇总数据
export function importSubAccountLiabilityPresentValue(data) {
  return request({
    url: '/dur/sub/account/liability/present/value/importData',
    method: 'post',
    data: data
  });
}
