import request from '@/utils/request';

// 查询分产品有效成本率列表
export function listProductEffectiveRate(query) {
  return request({
    url: '/cost/product/effective/rate/list',
    method: 'get',
    params: query,
  });
}

// 查询分产品有效成本率详细
export function getProductEffectiveRate(id) {
  return request({
    url: '/cost/product/effective/rate/' + id,
    method: 'get',
  });
}

// 新增分产品有效成本率
export function addProductEffectiveRate(data) {
  return request({
    url: '/cost/product/effective/rate',
    method: 'post',
    data: data,
  });
}

// 修改分产品有效成本率
export function updateProductEffectiveRate(data) {
  return request({
    url: '/cost/product/effective/rate',
    method: 'put',
    data: data,
  });
}

// 删除分产品有效成本率
export function delProductEffectiveRate(id) {
  return request({
    url: '/cost/product/effective/rate/' + id,
    method: 'delete',
  });
}

// 导出分产品有效成本率
export function exportProductEffectiveRate(query) {
  return request({
    url: '/cost/product/effective/rate/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取分产品有效成本率导入模板
export function importProductEffectiveRateTemplate() {
  return request({
    url: '/cost/product/effective/rate/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}

// 导入分产品有效成本率数据
export function importProductEffectiveRate(data) {
  return request({
    url: '/cost/product/effective/rate/importData',
    method: 'post',
    data: data
  });
}
