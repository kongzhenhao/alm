import request from '@/utils/request'

// 查询关键久期折现因子列表
export function listKeyDurationDiscountFactors(query) {
  return request({
    url: '/dur/key/duration/discount/factors/list',
    method: 'get',
    params: query
  })
}

// 查询关键久期折现因子详细
export function getKeyDurationDiscountFactors(id) {
  return request({
    url: '/dur/key/duration/discount/factors/' + id,
    method: 'get'
  })
}

// 根据条件查询关键久期折现因子
export function getKeyDurationDiscountFactorsByCondition(accountPeriod, curveType, keyDuration, stressDirection, durationType) {
  return request({
    url: '/dur/key/duration/discount/factors/condition',
    method: 'get',
    params: {
      accountPeriod,
      curveType,
      keyDuration,
      stressDirection,
      durationType
    }
  })
}

// 新增关键久期折现因子
export function addKeyDurationDiscountFactors(data) {
  return request({
    url: '/dur/key/duration/discount/factors',
    method: 'post',
    data: data
  })
}

// 修改关键久期折现因子
export function updateKeyDurationDiscountFactors(data) {
  return request({
    url: '/dur/key/duration/discount/factors',
    method: 'put',
    data: data
  })
}

// 删除关键久期折现因子
export function delKeyDurationDiscountFactors(ids) {
  return request({
    url: '/dur/key/duration/discount/factors/' + ids,
    method: 'delete'
  })
}

// 删除指定账期的关键久期折现因子
export function delKeyDurationDiscountFactorsByPeriod(accountPeriod) {
  return request({
    url: '/dur/key/duration/discount/factors/period/' + accountPeriod,
    method: 'delete'
  })
}

// 导出关键久期折现因子
export function exportKeyDurationDiscountFactors(query) {
  return request({
    url: '/dur/key/duration/discount/factors/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 下载关键久期折现因子模板
export function exportKeyDurationDiscountFactorsTemplate() {
  return request({
    url: '/dur/key/duration/discount/factors/exportTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入关键久期折现因子
export function importKeyDurationDiscountFactors(file, updateSupport) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('updateSupport', updateSupport)
  return request({
    url: '/dur/key/duration/discount/factors/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
