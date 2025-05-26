import request from '@/utils/request'

// 查询项目定义表列表
export function listItemDefinition(query) {
  return request({
    url: '/minc/item/definition/list',
    method: 'get',
    params: query
  })
}

// 查询项目定义表详细
export function getItemDefinition(id) {
  return request({
    url: '/minc/item/definition/' + id,
    method: 'get'
  })
}

// 新增项目定义表
export function addItemDefinition(data) {
  return request({
    url: '/minc/item/definition',
    method: 'post',
    data: data
  })
}

// 批量新增项目定义表
export function batchAddItemDefinition(data) {
  return request({
    url: '/minc/item/definition/batchAdd',
    method: 'post',
    data: data
  })
}

// 修改项目定义表
export function updateItemDefinition(data) {
  return request({
    url: '/minc/item/definition',
    method: 'put',
    data: data
  })
}

// 删除项目定义表
export function delItemDefinition(id) {
  return request({
    url: '/minc/item/definition/' + id,
    method: 'delete'
  })
}

// 导出项目定义表
export function exportItemDefinition(query) {
  return request({
    url: '/minc/item/definition/export',
    method: 'post',
    params: query
  })
}

// 获取项目定义表导入模板
export function getImportTemplate() {
  return request({
    url: '/minc/item/definition/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入项目定义表数据
export function importItemDefinition(data) {
  return request({
    url: '/minc/item/definition/importData',
    method: 'post',
    data: data
  })
}
