import request from '@/utils/request';

// 查询负债久期汇总列表
export function listLiabilityDurationSummary(query) {
  return request({
    url: '/dur/liability/duration/summary/list',
    method: 'get',
    params: query,
  });
}

// 查询负债久期汇总详细
export function getLiabilityDurationSummary(id) {
  return request({
    url: '/dur/liability/duration/summary/' + id,
    method: 'get',
  });
}

// 根据条件查询负债久期汇总
export function getLiabilityDurationSummaryByCondition(accountPeriod, cashFlowType, bpType, durationType, designType, isShortTerm) {
  return request({
    url: '/dur/liability/duration/summary/condition',
    method: 'get',
    params: {
      accountPeriod,
      cashFlowType,
      bpType,
      durationType,
      designType,
      isShortTerm
    },
  });
}

// 新增负债久期汇总
export function addLiabilityDurationSummary(data) {
  return request({
    url: '/dur/liability/duration/summary',
    method: 'post',
    data: data,
  });
}

// 批量新增负债久期汇总
export function batchAddLiabilityDurationSummary(data) {
  return request({
    url: '/dur/liability/duration/summary/batchAdd',
    method: 'post',
    data: data,
  });
}

// 修改负债久期汇总
export function updateLiabilityDurationSummary(data) {
  return request({
    url: '/dur/liability/duration/summary',
    method: 'put',
    data: data,
  });
}

// 删除负债久期汇总
export function delLiabilityDurationSummary(id) {
  return request({
    url: '/dur/liability/duration/summary/' + id,
    method: 'delete',
  });
}

// 删除指定账期的负债久期汇总
export function delLiabilityDurationSummaryByPeriod(accountPeriod) {
  return request({
    url: '/dur/liability/duration/summary/period/' + accountPeriod,
    method: 'delete',
  });
}

// 导出负债久期汇总数据
export function exportLiabilityDurationSummary(query) {
  return request({
    url: '/dur/liability/duration/summary/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 下载负债久期汇总模板
export function downloadTemplate() {
  return request({
    url: '/dur/liability/duration/summary/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入负债久期汇总数据
export function importLiabilityDurationSummary(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/liability/duration/summary/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
