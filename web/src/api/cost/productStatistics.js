import request from '@/utils/request';

// 查询分产品统计列表
export function listProductStatistics(query) {
  return request({
    url: '/cost/product/statistics/list',
    method: 'get',
    params: query,
  });
}

// 查询分产品统计详细
export function getProductStatistics(id) {
  return request({
    url: '/cost/product/statistics/' + id,
    method: 'get',
  });
}

// 新增分产品统计
export function addProductStatistics(data) {
  return request({
    url: '/cost/product/statistics',
    method: 'post',
    data: data,
  });
}

// 修改分产品统计
export function updateProductStatistics(data) {
  return request({
    url: '/cost/product/statistics',
    method: 'put',
    data: data,
  });
}

// 删除分产品统计
export function delProductStatistics(id) {
  return request({
    url: '/cost/product/statistics/' + id,
    method: 'delete',
  });
}

// 导出分产品统计
export function exportProductStatistics(query) {
  return request({
    url: '/cost/product/statistics/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取分产品统计导入模板
export function importProductStatisticsTemplate() {
  return request({
    url: '/cost/product/statistics/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}

// 导入分产品统计数据
export function importProductStatistics(data) {
  return request({
    url: '/cost/product/statistics/importData',
    method: 'post',
    data: data
  });
}
