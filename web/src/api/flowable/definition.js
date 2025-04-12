import request from '@/utils/request';
import { getUrlByTenantId } from '@/utils/flowable';

// 查询流程定义列表
export function listDefinition(query) {
  let url = '';
  let hasChangedUrl = false;
  if (query.tenantId) {
    url = getUrlByTenantId(query.tenantId);
    delete query.tenantId;
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/list',
    method: 'get',
    params: query,
    headers: {
      hasChangedUrl,
    },
  });
}

// 部署流程实例
export function definitionStart(obj) {
  let url = '';
  let hasChangedUrl = false;
  if (obj.tenantId) {
    url = getUrlByTenantId(obj.tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/start/' + obj.procDefId,
    method: 'post',
    data: obj.data,
    headers: {
      hasChangedUrl,
    },
  });
}

// 获取流程变量
export function getProcessVariables(taskId) {
  return request({
    url: '/flowable/task/processVariables/' + taskId,
    method: 'get',
  });
}

// 激活/挂起流程
export function updateState(params, tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/updateState',
    method: 'put',
    params: params,
    headers: {
      hasChangedUrl,
    },
  });
}

// 指定流程办理人员列表
export function userList(tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/userList',
    method: 'get',
    headers: {
      hasChangedUrl,
    },
  });
}

// 指定流程办理组列表
export function roleList(tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/roleList',
    method: 'get',
    headers: {
      hasChangedUrl,
    },
  });
}

// 读取xml文件
export function readXml(deployId) {
  return request({
    url: '/flowable/definition/readXml/' + deployId,
    method: 'get',
  });
}
// 读取image文件
export function readImage(deployId) {
  return request({
    url: '/flowable/definition/readImage/' + deployId,
    method: 'get',
  });
}

// 读取image文件
export function getFlowViewer(procInsId, executionId) {
  return request({
    url: '/flowable/task/flowViewer/' + procInsId + '/' + executionId,
    method: 'get',
  });
}

// 读取xml文件
export function saveXml(data, tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/save',
    method: 'post',
    data: data,
    headers: {
      hasChangedUrl,
    },
  });
}

// 新增流程定义
export function addDeployment(data) {
  return request({
    url: '/system/deployment',
    method: 'post',
    data: data,
  });
}

// 修改流程定义
export function updateDeployment(data) {
  return request({
    url: '/system/deployment',
    method: 'put',
    data: data,
  });
}

// 删除流程定义
export function delDeployment(query, tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/' + query,
    method: 'delete',
    headers: {
      hasChangedUrl,
    },
  });
}

// 导出流程定义
export function exportDeployment(query) {
  return request({
    url: '/system/deployment/export',
    method: 'get',
    params: query,
  });
}

// 查询流程定义列表
export function flowableDetail(id, tenantId) {
  let url = '';
  let hasChangedUrl = false;
  if (tenantId) {
    url = getUrlByTenantId(tenantId);
    hasChangedUrl = true;
  }
  return request({
    url: url + '/flowable/definition/taskIdRelation/' + id,
    method: 'get',
    headers: {
      hasChangedUrl,
    },
  });
}
