import request from '@/utils/request';

// 查询会计准备金明细列表
export function listReserve(query) {
  return request({
    url: '/base/accounting/reserve/list',
    method: 'get',
    params: query
  });
}

// 查询会计准备金明细详细
export function getReserve(id) {
  return request({
    url: '/base/accounting/reserve/' + id,
    method: 'get'
  });
}

// 新增会计准备金明细
export function addReserve(data) {
  return request({
    url: '/base/accounting/reserve',
    method: 'post',
    data: data
  });
}

// 修改会计准备金明细
export function updateReserve(data) {
  return request({
    url: '/base/accounting/reserve',
    method: 'put',
    data: data
  });
}

// 删除会计准备金明细
export function delReserve(id) {
  return request({
    url: '/base/accounting/reserve/' + id,
    method: 'delete'
  });
}

// 导出会计准备金明细
export function exportReserve(query) {
  return request({
    url: '/base/accounting/reserve/export',
    method: 'post',
    params: query
  });
}

// 获取会计准备金明细导入模板
export function importReserveTemplate() {
  return request({
    url: '/base/accounting/reserve/importTemplate',
    method: 'post',
    responseType: 'blob'
  });
}
