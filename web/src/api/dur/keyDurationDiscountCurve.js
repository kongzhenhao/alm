import request from '@/utils/request';

// 查询关键久期折现曲线列表
export function listKeyDurationDiscountCurve(query) {
  return request({
    url: '/dur/key/duration/discount/curve/list',
    method: 'get',
    params: query
  });
}

// 查询关键久期折现曲线详细
export function getKeyDurationDiscountCurve(id) {
  return request({
    url: '/dur/key/duration/discount/curve/' + id,
    method: 'get'
  });
}

// 根据条件查询关键久期折现曲线
export function getKeyDurationDiscountCurveByCondition(accountPeriod, curveType, keyDuration, stressDirection, durationType) {
  return request({
    url: '/dur/key/duration/discount/curve/condition',
    method: 'get',
    params: {
      accountPeriod,
      curveType,
      keyDuration,
      stressDirection,
      durationType
    }
  });
}

// 新增关键久期折现曲线
export function addKeyDurationDiscountCurve(data) {
  return request({
    url: '/dur/key/duration/discount/curve',
    method: 'post',
    data: data
  });
}

// 批量新增关键久期折现曲线
export function batchAddKeyDurationDiscountCurve(data) {
  return request({
    url: '/dur/key/duration/discount/curve/batchAdd',
    method: 'post',
    data: data
  });
}

// 修改关键久期折现曲线
export function updateKeyDurationDiscountCurve(data) {
  return request({
    url: '/dur/key/duration/discount/curve',
    method: 'put',
    data: data
  });
}

// 删除关键久期折现曲线
export function delKeyDurationDiscountCurve(ids) {
  return request({
    url: '/dur/key/duration/discount/curve/' + ids,
    method: 'delete'
  });
}

// 删除指定账期的关键久期折现曲线
export function delKeyDurationDiscountCurveByPeriod(accountPeriod) {
  return request({
    url: '/dur/key/duration/discount/curve/period/' + accountPeriod,
    method: 'delete'
  });
}

// 导出关键久期折现曲线
export function exportKeyDurationDiscountCurve(query) {
  return request({
    url: '/dur/key/duration/discount/curve/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 获取关键久期折现曲线导入模板
export function exportKeyDurationDiscountCurveTemplate() {
  return request({
    url: '/dur/key/duration/discount/curve/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入关键久期折现曲线数据
export function importKeyDurationDiscountCurve(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/key/duration/discount/curve/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
