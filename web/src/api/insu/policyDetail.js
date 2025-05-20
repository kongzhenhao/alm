import request from '@/utils/request';

// 查询保单数据明细列表
export function listPolicyDetail(query) {
  return request({
    url: '/insu/policy/detail/list',
    method: 'get',
    params: query,
  });
}

// 查询保单数据明细详细
export function getPolicyDetail(id) {
  return request({
    url: '/insu/policy/detail/' + id,
    method: 'get',
  });
}

// 新增保单数据明细
export function addPolicyDetail(data) {
  return request({
    url: '/insu/policy/detail',
    method: 'post',
    data: data,
  });
}

// 修改保单数据明细
export function updatePolicyDetail(data) {
  return request({
    url: '/insu/policy/detail',
    method: 'put',
    data: data,
  });
}

// 删除保单数据明细
export function delPolicyDetail(id) {
  return request({
    url: '/insu/policy/detail/' + id,
    method: 'delete',
  });
}

// 导出保单数据明细
export function exportPolicyDetail(query) {
  return request({
    url: '/insu/policy/detail/export',
    method: 'post',
    params: query,
  });
}

// 获取保单数据明细导入模板
export function importTemplatePolicyDetail() {
  return request({
    url: '/insu/policy/detail/importTemplate',
    method: 'post',
  });
}

// 导入保单数据明细数据
export function importPolicyDetail(data) {
  return request({
    url: '/insu/policy/detail/importData',
    method: 'post',
    data: data,
  });
}
