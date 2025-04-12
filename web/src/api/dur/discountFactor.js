import request from '@/utils/request';

// 查询折现因子列表
export function listDiscountFactor(query) {
  return request({
    url: '/dur/discountFactor/list',
    method: 'get',
    params: query,
  });
}

// 查询折现因子详细
export function getDiscountFactor(id) {
  return request({
    url: '/dur/discountFactor/' + id,
    method: 'get',
  });
}

// 新增折现因子
export function addDiscountFactor(data) {
  return request({
    url: '/dur/discountFactor',
    method: 'post',
    data: data,
  });
}

// 修改折现因子
export function updateDiscountFactor(data) {
  return request({
    url: '/dur/discountFactor',
    method: 'put',
    data: data,
  });
}

// 删除折现因子
export function delDiscountFactor(id) {
  return request({
    url: '/dur/discountFactor/' + id,
    method: 'delete',
  });
}

// 导入折现因子数据
export function importDiscountFactor(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: '/dur/importDiscountFactor',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 导出折现因子数据
export function exportDiscountFactor(query) {
  return request({
    url: '/dur/discountFactor/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  });
}
