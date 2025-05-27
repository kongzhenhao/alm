import request from '@/utils/request';

// 查询分账户汇总列表
export function listAccountSummary(query) {
  return request({
    url: '/cost/account/summary/list',
    method: 'get',
    params: query,
  });
}

// 查询分账户汇总详细
export function getAccountSummary(id) {
  return request({
    url: '/cost/account/summary/' + id,
    method: 'get',
  });
}

// 新增分账户汇总
export function addAccountSummary(data) {
  return request({
    url: '/cost/account/summary',
    method: 'post',
    data: data,
  });
}

// 修改分账户汇总
export function updateAccountSummary(data) {
  return request({
    url: '/cost/account/summary',
    method: 'put',
    data: data,
  });
}

// 删除分账户汇总
export function delAccountSummary(id) {
  return request({
    url: '/cost/account/summary/' + id,
    method: 'delete',
  });
}

// 导出分账户汇总
export function exportAccountSummary(query) {
  return request({
    url: '/cost/account/summary/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取分账户汇总导入模板
export function importAccountSummaryTemplate() {
  return request({
    url: '/cost/account/summary/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}

// 导入分账户汇总数据
export function importAccountSummary(data) {
  return request({
    url: '/cost/account/summary/importData',
    method: 'post',
    data: data
  });
}
