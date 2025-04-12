import request from '@/utils/request';

// 查询折现曲线列表
export function listDiscountCurve(query) {
  return request({
    url: '/dur/discountCurve/list',
    method: 'get',
    params: query,
  });
}

// 查询折现曲线详细
export function getDiscountCurve(id) {
  return request({
    url: '/dur/discountCurve/' + id,
    method: 'get',
  });
}

// 新增折现曲线
export function addDiscountCurve(data) {
  return request({
    url: '/dur/discountCurve',
    method: 'post',
    data: data,
  });
}

// 修改折现曲线
export function updateDiscountCurve(data) {
  return request({
    url: '/dur/discountCurve',
    method: 'put',
    data: data,
  });
}

// 删除折现曲线
export function delDiscountCurve(id) {
  return request({
    url: '/dur/discountCurve/' + id,
    method: 'delete',
  });
}

// 导入折现曲线数据
export function importDiscountCurve(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request({
    url: '/dur/importDiscountCurve',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 导出折现曲线数据
export function exportDiscountCurve(query) {
  return request({
    url: '/dur/discountCurve/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  });
}
