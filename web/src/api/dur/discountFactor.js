import request from '@/utils/request'

// 查询折现因子列表
export function listDiscountFactor(query) {
  return request({
    url: '/dur/discount/factor/list',
    method: 'get',
    params: query
  })
}

// 查询折现因子详细
export function getDiscountFactor(id) {
  return request({
    url: '/dur/discount/factor/' + id,
    method: 'get'
  })
}

// 新增折现因子
export function addDiscountFactor(data) {
  return request({
    url: '/dur/discount/factor',
    method: 'post',
    data: data
  })
}

// 批量新增折现因子
export function batchAddDiscountFactor(data) {
  return request({
    url: '/dur/discount/factor/batchAdd',
    method: 'post',
    data: data
  })
}

// 修改折现因子
export function updateDiscountFactor(data) {
  return request({
    url: '/dur/discount/factor',
    method: 'put',
    data: data
  })
}

// 删除折现因子
export function delDiscountFactor(id) {
  return request({
    url: '/dur/discount/factor/' + id,
    method: 'delete'
  })
}

// 删除指定账期的折现因子
export function delDiscountFactorByPeriod(accountPeriod) {
  return request({
    url: '/dur/discount/factor/period/' + accountPeriod,
    method: 'delete'
  })
}

// 导出折现因子数据
export function exportDiscountFactor(query) {
  return request({
    url: '/dur/discount/factor/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 下载折现因子模板
export function downloadTemplate() {
  return request({
    url: '/dur/discount/factor/exportTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入折现因子数据
export function importDiscountFactor(file, updateSupport) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('updateSupport', updateSupport)
  return request({
    url: '/dur/discount/factor/importData',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
