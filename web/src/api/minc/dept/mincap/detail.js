import request from '@/utils/request'

// 查询分部门最低资本明细列表
export function listDeptMincapDetail(query) {
  return request({
    url: '/minc/dept/mincap/detail/list',
    method: 'get',
    params: query
  })
}

// 查询分部门最低资本明细详细
export function getDeptMincapDetail(id) {
  return request({
    url: '/minc/dept/mincap/detail/' + id,
    method: 'get'
  })
}

// 新增分部门最低资本明细
export function addDeptMincapDetail(data) {
  return request({
    url: '/minc/dept/mincap/detail',
    method: 'post',
    data: data
  })
}

// 批量新增分部门最低资本明细
export function batchAddDeptMincapDetail(data) {
  return request({
    url: '/minc/dept/mincap/detail/batchAdd',
    method: 'post',
    data: data
  })
}

// 修改分部门最低资本明细
export function updateDeptMincapDetail(data) {
  return request({
    url: '/minc/dept/mincap/detail',
    method: 'put',
    data: data
  })
}

// 删除分部门最低资本明细
export function delDeptMincapDetail(id) {
  return request({
    url: '/minc/dept/mincap/detail/' + id,
    method: 'delete'
  })
}

// 根据账期删除分部门最低资本明细
export function delDeptMincapDetailByPeriod(accountingPeriod) {
  return request({
    url: '/minc/dept/mincap/detail/period/' + accountingPeriod,
    method: 'delete'
  })
}

// 根据条件删除分部门最低资本明细
export function delDeptMincapDetailByCondition(accountingPeriod, department, itemCode) {
  return request({
    url: '/minc/dept/mincap/detail/condition',
    method: 'delete',
    params: {
      accountingPeriod,
      department,
      itemCode
    }
  })
}

// 导出分部门最低资本明细
export function exportDeptMincapDetail(query) {
  return request({
    url: '/minc/dept/mincap/detail/export',
    method: 'post',
    data: query
  })
}

// 导出分部门最低资本明细模板
export function exportDeptMincapDetailTemplate() {
  return request({
    url: '/minc/dept/mincap/detail/exportTemplate',
    method: 'post'
  })
}
