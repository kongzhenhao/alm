import request from '@/utils/request';

// 查询分账户有效成本率列表
export function listAccountEffectiveRate(query) {
  return request({
    url: '/cost/account/effective/rate/list',
    method: 'get',
    params: query,
  });
}

// 查询分账户有效成本率详细
export function getAccountEffectiveRate(id) {
  return request({
    url: '/cost/account/effective/rate/' + id,
    method: 'get',
  });
}

// 新增分账户有效成本率
export function addAccountEffectiveRate(data) {
  return request({
    url: '/cost/account/effective/rate',
    method: 'post',
    data: data,
  });
}

// 修改分账户有效成本率
export function updateAccountEffectiveRate(data) {
  return request({
    url: '/cost/account/effective/rate',
    method: 'put',
    data: data,
  });
}

// 删除分账户有效成本率
export function delAccountEffectiveRate(id) {
  return request({
    url: '/cost/account/effective/rate/' + id,
    method: 'delete',
  });
}

// 导出分账户有效成本率
export function exportAccountEffectiveRate(query) {
  return request({
    url: '/cost/account/effective/rate/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取分账户有效成本率导入模板
export function importAccountEffectiveRateTemplate() {
  return request({
    url: '/cost/account/effective/rate/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}

// 导入分账户有效成本率数据
export function importAccountEffectiveRate(data) {
  return request({
    url: '/cost/account/effective/rate/importData',
    method: 'post',
    data: data
  });
}
