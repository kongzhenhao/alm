import request from '@/utils/request';

// 查询折现曲线列表
export function listDiscountCurve(query) {
  return request({
    url: '/dur/discount/curve/list',
    method: 'get',
    params: query,
  });
}

// 查询折现曲线详细
export function getDiscountCurve(id) {
  return request({
    url: '/dur/discount/curve/' + id,
    method: 'get',
  });
}

// 根据条件查询折现曲线
export function getDiscountCurveByCondition(accountPeriod, curveType, bpType, durationType) {
  return request({
    url: '/dur/discount/curve/condition',
    method: 'get',
    params: {
      accountPeriod,
      curveType,
      bpType,
      durationType
    },
  });
}

// 新增折现曲线
export function addDiscountCurve(data) {
  return request({
    url: '/dur/discount/curve',
    method: 'post',
    data: data,
  });
}

// 批量新增折现曲线
export function batchAddDiscountCurve(data) {
  return request({
    url: '/dur/discount/curve/batchAdd',
    method: 'post',
    data: data,
  });
}

// 修改折现曲线
export function updateDiscountCurve(data) {
  return request({
    url: '/dur/discount/curve',
    method: 'put',
    data: data,
  });
}

// 删除折现曲线
export function delDiscountCurve(id) {
  return request({
    url: '/dur/discount/curve/' + id,
    method: 'delete',
  });
}

// 批量删除折现曲线
export function batchDelDiscountCurve(ids) {
  return request({
    url: '/dur/discount/curve/batch/' + ids,
    method: 'delete',
  });
}

// 删除指定账期的折现曲线
export function delDiscountCurveByPeriod(accountPeriod) {
  return request({
    url: '/dur/discount/curve/period/' + accountPeriod,
    method: 'delete',
  });
}

// 导出折现曲线数据
export function exportDiscountCurve(query) {
  return request({
    url: '/dur/discount/curve/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 下载折现曲线模板
export function downloadTemplate() {
  return request({
    url: '/dur/discount/curve/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入折现曲线数据
export function importDiscountCurve(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/discount/curve/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
