import request from '@/utils/request';

// 查询保单续保率统计列表
export function listRenewalRateStats(query) {
  return request({
    url: '/insu/renewal/rate/stats/list',
    method: 'get',
    params: query,
  });
}

// 查询保单续保率统计详细
export function getRenewalRateStats(id) {
  return request({
    url: '/insu/renewal/rate/stats/' + id,
    method: 'get',
  });
}

// 新增保单续保率统计
export function addRenewalRateStats(data) {
  return request({
    url: '/insu/renewal/rate/stats',
    method: 'post',
    data: data,
  });
}

// 修改保单续保率统计
export function updateRenewalRateStats(data) {
  return request({
    url: '/insu/renewal/rate/stats',
    method: 'put',
    data: data,
  });
}

// 删除保单续保率统计
export function delRenewalRateStats(id) {
  return request({
    url: '/insu/renewal/rate/stats/' + id,
    method: 'delete',
  });
}

// 批量删除保单续保率统计
export function batchDelRenewalRateStats(ids) {
  return request({
    url: '/insu/renewal/rate/stats/' + ids,
    method: 'delete',
  });
}

// 删除指定账期的保单续保率统计
export function delRenewalRateStatsByPeriod(accountingPeriod) {
  return request({
    url: '/insu/renewal/rate/stats/period/' + accountingPeriod,
    method: 'delete',
  });
}

// 导出保单续保率统计
export function exportRenewalRateStats(query) {
  return request({
    url: '/insu/renewal/rate/stats/export',
    method: 'post',
    params: query,
  });
}

// 获取保单续保率统计导入模板
export function importTemplateRenewalRateStats() {
  return request({
    url: '/insu/renewal/rate/stats/importTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入保单续保率统计数据
export function importRenewalRateStats(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/insu/renewal/rate/stats/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 计算保单续保率统计
export function calcRenewalRateStats(accountingPeriod) {
  return request({
    url: '/insu/renewal/rate/stats/calc/' + accountingPeriod,
    method: 'post'
  });
}
