import request from '@/utils/request';

// 查询续保率评估月份配置列表
export function listRenewalEvalMonthCfg(query) {
  return request({
    url: '/base/renewal/eval/month/cfg/list',
    method: 'get',
    params: query,
  });
}

// 查询续保率评估月份配置详细
export function getRenewalEvalMonthCfg(id) {
  return request({
    url: '/base/renewal/eval/month/cfg/' + id,
    method: 'get',
  });
}

// 新增续保率评估月份配置
export function addRenewalEvalMonthCfg(data) {
  return request({
    url: '/base/renewal/eval/month/cfg',
    method: 'post',
    data: data,
  });
}

// 修改续保率评估月份配置
export function updateRenewalEvalMonthCfg(data) {
  return request({
    url: '/base/renewal/eval/month/cfg',
    method: 'put',
    data: data,
  });
}

// 删除续保率评估月份配置
export function delRenewalEvalMonthCfg(id) {
  return request({
    url: '/base/renewal/eval/month/cfg/' + id,
    method: 'delete',
  });
}

// 导出续保率评估月份配置
export function exportRenewalEvalMonthCfg(query) {
  return request({
    url: '/base/renewal/eval/month/cfg/export',
    method: 'post',
    params: query,
  });
}

// 下载续保率评估月份配置模板
export function downloadTemplate() {
  return request({
    url: '/base/renewal/eval/month/cfg/exportTemplate',
    method: 'post',
    responseType: 'blob'
  });
}

// 导入续保率评估月份配置数据
export function importRenewalEvalMonthCfg(data) {
  return request({
    url: '/base/renewal/eval/month/cfg/importData',
    method: 'post',
    data: data,
  });
}
