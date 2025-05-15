import request from '@/utils/request';

// 查询万能平均结算利率列表
export function listSettlement(query) {
  return request({
    url: '/base/universal/settlement/list',
    method: 'get',
    params: query
  });
}

// 查询万能平均结算利率详细
export function getSettlement(id) {
  return request({
    url: '/base/universal/settlement/' + id,
    method: 'get'
  });
}

// 新增万能平均结算利率
export function addSettlement(data) {
  return request({
    url: '/base/universal/settlement',
    method: 'post',
    data: data
  });
}

// 修改万能平均结算利率
export function updateSettlement(data) {
  return request({
    url: '/base/universal/settlement',
    method: 'put',
    data: data
  });
}

// 删除万能平均结算利率
export function delSettlement(id) {
  return request({
    url: '/base/universal/settlement/' + id,
    method: 'delete'
  });
}

// 导出万能平均结算利率
export function exportSettlement(query) {
  return request({
    url: '/base/universal/settlement/export',
    method: 'post',
    params: query
  });
}

// 获取万能平均结算利率导入模板
export function importSettlementTemplate() {
  return request({
    url: '/base/universal/settlement/importTemplate',
    method: 'get',
    responseType: 'blob'
  });
}
