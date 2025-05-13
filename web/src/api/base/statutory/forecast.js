import request from '@/utils/request';

// 查询法定准备金预测列表
export function listForecast(query) {
  return request({
    url: '/base/statutory/forecast/list',
    method: 'get',
    params: query
  });
}

// 查询法定准备金预测详细
export function getForecast(id) {
  return request({
    url: '/base/statutory/forecast/' + id,
    method: 'get'
  });
}

// 新增法定准备金预测
export function addForecast(data) {
  return request({
    url: '/base/statutory/forecast',
    method: 'post',
    data: data
  });
}

// 修改法定准备金预测
export function updateForecast(data) {
  return request({
    url: '/base/statutory/forecast',
    method: 'put',
    data: data
  });
}

// 删除法定准备金预测
export function delForecast(id) {
  return request({
    url: '/base/statutory/forecast/' + id,
    method: 'delete'
  });
}

// 导出法定准备金预测
export function exportForecast(query) {
  return request({
    url: '/base/statutory/forecast/export',
    method: 'post',
    params: query
  });
}

// 获取法定准备金预测导入模板
export function importForecastTemplate() {
  return request({
    url: '/base/statutory/forecast/importTemplate',
    method: 'post',
    responseType: 'blob'
  });
}
