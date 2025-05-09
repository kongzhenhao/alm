import request from '@/utils/request';

// 查询分账户负债久期汇总列表
export function listSubAccountLiabilityDuration(query) {
  return request({
    url: '/dur/sub/account/liability/duration/list',
    method: 'get',
    params: query,
  });
}

// 查询分账户负债久期汇总详细
export function getSubAccountLiabilityDuration(id) {
  return request({
    url: '/dur/sub/account/liability/duration/' + id,
    method: 'get',
  });
}

// 根据条件查询分账户负债久期汇总
export function getSubAccountLiabilityDurationByCondition(accountPeriod, cashFlowType, bpType, durationType, designType) {
  return request({
    url: '/dur/sub/account/liability/duration/condition',
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

// 新增分账户负债久期汇总
export function addSubAccountLiabilityDuration(data) {
  return request({
    url: '/dur/sub/account/liability/duration',
    method: 'post',
    data: data,
  });
}

// 批量新增分账户负债久期汇总
export function batchAddSubAccountLiabilityDuration(data) {
  return request({
    url: '/dur/sub/account/liability/duration/batchAdd',
    method: 'post',
    data: data,
  });
}

// 修改分账户负债久期汇总
export function updateSubAccountLiabilityDuration(data) {
  return request({
    url: '/dur/sub/account/liability/duration',
    method: 'put',
    data: data,
  });
}

// 删除分账户负债久期汇总
export function delSubAccountLiabilityDuration(ids) {
  return request({
    url: '/dur/sub/account/liability/duration/' + ids,
    method: 'delete',
  });
}

// 删除指定账期的分账户负债久期汇总
export function delSubAccountLiabilityDurationByPeriod(accountPeriod) {
  return request({
    url: '/dur/sub/account/liability/duration/period/' + accountPeriod,
    method: 'delete',
  });
}

// 导出分账户负债久期汇总数据
export function exportSubAccountLiabilityDuration(query) {
  return request({
    url: '/dur/sub/account/liability/duration/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 下载分账户负债久期汇总模板
export function downloadTemplate() {
  return request({
    url: '/dur/sub/account/liability/duration/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入分账户负债久期汇总数据
export function importSubAccountLiabilityDuration(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/sub/account/liability/duration/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
