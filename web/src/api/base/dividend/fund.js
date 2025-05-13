import request from '@/utils/request';

// 查询分红方案列表
export function listFund(query) {
  return request({
    url: '/base/dividend/fund/list',
    method: 'get',
    params: query
  });
}

// 查询分红方案详细
export function getFund(id) {
  return request({
    url: '/base/dividend/fund/' + id,
    method: 'get'
  });
}

// 新增分红方案
export function addFund(data) {
  return request({
    url: '/base/dividend/fund',
    method: 'post',
    data: data
  });
}

// 修改分红方案
export function updateFund(data) {
  return request({
    url: '/base/dividend/fund',
    method: 'put',
    data: data
  });
}

// 删除分红方案
export function delFund(id) {
  return request({
    url: '/base/dividend/fund/' + id,
    method: 'delete'
  });
}

// 导出分红方案
export function exportFund(query) {
  return request({
    url: '/base/dividend/fund/export',
    method: 'post',
    params: query
  });
}

// 获取分红方案导入模板
export function importFundTemplate() {
  return request({
    url: '/base/dividend/fund/importTemplate',
    method: 'post',
    responseType: 'blob'
  });
}
