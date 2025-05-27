import request from '@/utils/request';

// 查询法定准备金明细列表
export function listReserve(query) {
  return request({
    url: '/base/statutory/reserve/list',
    method: 'get',
    params: query
  });
}

// 查询法定准备金明细详细
export function getReserve(id) {
  return request({
    url: '/base/statutory/reserve/' + id,
    method: 'get'
  });
}

// 新增法定准备金明细
export function addReserve(data) {
  return request({
    url: '/base/statutory/reserve',
    method: 'post',
    data: data
  });
}

// 修改法定准备金明细
export function updateReserve(data) {
  return request({
    url: '/base/statutory/reserve',
    method: 'put',
    data: data
  });
}

// 删除法定准备金明细
export function delReserve(id) {
  return request({
    url: '/base/statutory/reserve/' + id,
    method: 'delete'
  });
}

// 导出法定准备金明细
export function exportReserve(query) {
  return request({
    url: '/base/statutory/reserve/export',
    method: 'post',
    params: query
  });
}

// 获取法定准备金明细导入模板
export function importReserveTemplate() {
  return request({
    url: '/base/statutory/reserve/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}
