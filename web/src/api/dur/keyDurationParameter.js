import request from '@/utils/request';

// 查询关键久期参数列表
export function listKeyDurationParameter(query) {
  return request({
    url: '/dur/key/duration/parameter/list',
    method: 'get',
    params: query
  });
}

// 查询关键久期参数详细（不包含参数值集）
export function getKeyDurationParameter(id) {
  return request({
    url: '/dur/key/duration/parameter/' + id,
    method: 'get'
  });
}

// 查询关键久期参数详细（包含参数值集）
export function getKeyDurationParameterWithValSet(id) {
  return request({
    url: '/dur/key/duration/parameter/withValSet/' + id,
    method: 'get'
  });
}

// 根据条件查询关键久期参数
export function getKeyDurationParameterByCondition(accountPeriod, keyDuration) {
  return request({
    url: '/dur/key/duration/parameter/condition',
    method: 'get',
    params: {
      accountPeriod,
      keyDuration
    }
  });
}

// 新增关键久期参数
export function addKeyDurationParameter(data) {
  return request({
    url: '/dur/key/duration/parameter',
    method: 'post',
    data: data
  });
}

// 批量新增关键久期参数
export function batchAddKeyDurationParameter(data) {
  return request({
    url: '/dur/key/duration/parameter/batchAdd',
    method: 'post',
    data: data
  });
}

// 修改关键久期参数
export function updateKeyDurationParameter(data) {
  return request({
    url: '/dur/key/duration/parameter',
    method: 'put',
    data: data
  });
}

// 删除关键久期参数
export function delKeyDurationParameter(ids) {
  return request({
    url: '/dur/key/duration/parameter/' + ids,
    method: 'delete'
  });
}

// 删除指定账期的关键久期参数
export function delKeyDurationParameterByPeriod(accountPeriod) {
  return request({
    url: '/dur/key/duration/parameter/period/' + accountPeriod,
    method: 'delete'
  });
}

// 导出关键久期参数
export function exportKeyDurationParameter(query) {
  return request({
    url: '/dur/key/duration/parameter/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
}

// 下载关键久期参数模板
export function downloadTemplate() {
  return request({
    url: '/dur/key/duration/parameter/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入关键久期参数数据
export function importKeyDurationParameter(file, updateSupport) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('updateSupport', updateSupport);
  return request({
    url: '/dur/key/duration/parameter/importData',
    method: 'post',
    data: formData,
    timeout: 300000, // 设置较长的超时时间，因为导入可能需要较长时间
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
