import request from '@/utils/request';

// 查询分业务类型汇总列表
export function listBusinessTypeSummary(query) {
  return request({
    url: '/cost/business/type/summary/list',
    method: 'get',
    params: query,
  });
}

// 查询分业务类型汇总详细
export function getBusinessTypeSummary(id) {
  return request({
    url: '/cost/business/type/summary/' + id,
    method: 'get',
  });
}

// 新增分业务类型汇总
export function addBusinessTypeSummary(data) {
  return request({
    url: '/cost/business/type/summary',
    method: 'post',
    data: data,
  });
}

// 修改分业务类型汇总
export function updateBusinessTypeSummary(data) {
  return request({
    url: '/cost/business/type/summary',
    method: 'put',
    data: data,
  });
}

// 删除分业务类型汇总
export function delBusinessTypeSummary(id) {
  return request({
    url: '/cost/business/type/summary/' + id,
    method: 'delete',
  });
}

// 导出分业务类型汇总
export function exportBusinessTypeSummary(query) {
  return request({
    url: '/cost/business/type/summary/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取分业务类型汇总导入模板
export function importBusinessTypeSummaryTemplate() {
  return request({
    url: '/cost/business/type/summary/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}

// 导入分业务类型汇总数据
export function importBusinessTypeSummary(data) {
  return request({
    url: '/cost/business/type/summary/importData',
    method: 'post',
    data: data
  });
}
