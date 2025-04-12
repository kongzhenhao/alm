import request from '@/utils/request';

// 查询负债现金流列表
export function listCashFlow(query) {
  return request({
    url: '/dur/liabilityCashFlow/list',
    method: 'get',
    params: query,
  });
}

// 查询负债现金流详细
export function getCashFlow(id) {
  return request({
    url: '/dur/liabilityCashFlow/' + id,
    method: 'get',
  });
}

// 新增负债现金流
export function addCashFlow(data) {
  return request({
    url: '/dur/liabilityCashFlow',
    method: 'post',
    data: data,
  });
}

// 修改负债现金流
export function updateCashFlow(data) {
  return request({
    url: '/dur/liabilityCashFlow',
    method: 'put',
    data: data,
  });
}

// 删除负债现金流
export function delCashFlow(id) {
  return request({
    url: '/dur/liabilityCashFlow/' + id,
    method: 'delete',
  });
}

// 导入负债现金流数据
export function importCashFlow(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: '/dur/importLiabilityCashFlow',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 导出负债现金流数据
export function exportCashFlow(query) {
  return request({
    url: '/dur/liabilityCashFlow/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  });
}
