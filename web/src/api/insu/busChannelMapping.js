import request from '@/utils/request';

// 查询渠道码映射配置列表
export function listBusChannelMapping(query) {
  return request({
    url: '/insu/bus/channel/mapping/list',
    method: 'get',
    params: query,
  });
}

// 查询渠道码映射配置详细
export function getBusChannelMapping(id) {
  return request({
    url: '/insu/bus/channel/mapping/' + id,
    method: 'get',
  });
}

// 新增渠道码映射配置
export function addBusChannelMapping(data) {
  return request({
    url: '/insu/bus/channel/mapping',
    method: 'post',
    data: data,
  });
}

// 修改渠道码映射配置
export function updateBusChannelMapping(data) {
  return request({
    url: '/insu/bus/channel/mapping',
    method: 'put',
    data: data,
  });
}

// 删除渠道码映射配置
export function delBusChannelMapping(id) {
  return request({
    url: '/insu/bus/channel/mapping/' + id,
    method: 'delete',
  });
}

// 导出渠道码映射配置
export function exportBusChannelMapping(query) {
  return request({
    url: '/insu/bus/channel/mapping/export',
    method: 'post',
    params: query,
  });
}

// 获取渠道码映射配置导入模板
export function importTemplateBusChannelMapping() {
  return request({
    url: '/insu/bus/channel/mapping/importTemplate',
    method: 'post',
  });
}

// 导入渠道码映射配置数据
export function importBusChannelMapping(data) {
  return request({
    url: '/insu/bus/channel/mapping/importData',
    method: 'post',
    data: data,
  });
}
