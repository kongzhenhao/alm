import request from '@/utils/request';

// 查询负债现金流列表
export function listLiabilityCashFlow(query) {
  return request({
    url: '/dur/liability/cash/flow/list',
    method: 'get',
    params: query,
  });
}

// 查询负债现金流详细
export function getLiabilityCashFlow(id) {
  return request({
    url: '/dur/liability/cash/flow/' + id,
    method: 'get',
  });
}

// 根据条件查询负债现金流
export function getLiabilityCashFlowByCondition(accountPeriod, cashFlowType, bpType, durationType, designType, isShortTerm, actuarialCode) {
  return request({
    url: '/dur/liability/cash/flow/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      bpType,
      durationType,
      designType,
      isShortTerm,
      actuarialCode
    },
  });
}

// 新增负债现金流
export function addLiabilityCashFlow(data) {
  return request({
    url: '/dur/liability/cash/flow',
    method: 'post',
    data: data,
  });
}

// 批量新增负债现金流
export function batchAddLiabilityCashFlow(data) {
  return request({
    url: '/dur/liability/cash/flow/batchAdd',
    method: 'post',
    data: data,
  });
}

// 修改负债现金流
export function updateLiabilityCashFlow(data) {
  return request({
    url: '/dur/liability/cash/flow',
    method: 'put',
    data: data,
  });
}

// 删除负债现金流
export function delLiabilityCashFlow(id) {
  return request({
    url: '/dur/liability/cash/flow/batch/' + id,
    method: 'delete',
  });
}

// 批量删除负债现金流
export function batchDelLiabilityCashFlow(ids) {
  return request({
    url: '/dur/liability/cash/flow/batch/' + ids,
    method: 'delete',
  });
}

// 删除指定账期的负债现金流
export function delLiabilityCashFlowByPeriod(accountPeriod) {
  return request({
    url: '/dur/liability/cash/flow/period/' + accountPeriod,
    method: 'delete',
  });
}

// 导出负债现金流数据
export function exportLiabilityCashFlow(query) {
  return request({
    url: '/dur/liability/cash/flow/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 导入负债现金流数据
export function importLiabilityCashFlow(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/liability/cash/flow/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 下载负债现金流模板
export function downloadTemplate() {
  return request({
    url: '/dur/liability/cash/flow/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}
