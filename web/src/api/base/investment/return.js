import request from '@/utils/request';

// 查询投资收益率假设列表
export function listReturn(query) {
  return request({
    url: '/base/investment/return/list',
    method: 'get',
    params: query
  });
}

// 查询投资收益率假设详细
export function getReturn(id) {
  return request({
    url: '/base/investment/return/' + id,
    method: 'get'
  });
}

// 新增投资收益率假设
export function addReturn(data) {
  return request({
    url: '/base/investment/return',
    method: 'post',
    data: data
  });
}

// 修改投资收益率假设
export function updateReturn(data) {
  return request({
    url: '/base/investment/return',
    method: 'put',
    data: data
  });
}

// 删除投资收益率假设
export function delReturn(id) {
  return request({
    url: '/base/investment/return/' + id,
    method: 'delete'
  });
}

// 导出投资收益率假设
export function exportReturn(query) {
  return request({
    url: '/base/investment/return/export',
    method: 'post',
    params: query
  });
}

// 获取投资收益率假设导入模板
export function importReturnTemplate() {
  return request({
    url: '/base/investment/return/importTemplate',
    method: 'post',
    responseType: 'blob'
  });
}
