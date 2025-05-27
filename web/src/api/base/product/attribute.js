import request from '@/utils/request';

// 查询产品属性列表
export function listProductAttribute(query) {
  return request({
    url: '/base/product/attribute/list',
    method: 'get',
    params: query
  });
}

// 查询产品属性详细
export function getProductAttribute(id) {
  return request({
    url: '/base/product/attribute/' + id,
    method: 'get'
  });
}

// 新增产品属性
export function addProductAttribute(data) {
  return request({
    url: '/base/product/attribute',
    method: 'post',
    data: data
  });
}

// 修改产品属性
export function updateProductAttribute(data) {
  return request({
    url: '/base/product/attribute',
    method: 'put',
    data: data
  });
}

// 删除产品属性
export function delProductAttribute(id) {
  return request({
    url: '/base/product/attribute/' + id,
    method: 'delete'
  });
}

// 导出产品属性
export function exportProductAttribute(query) {
  return request({
    url: '/base/product/attribute/export',
    method: 'post',
    params: query
  });
}

// 获取产品属性导入模板
export function importTemplate() {
  return request({
    url: '/base/product/attribute/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}
