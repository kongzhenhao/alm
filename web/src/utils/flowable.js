import {
  listDefinition,
  definitionStart,
  flowableDetail,
} from '@/api/flowable/definition';
import { flowRecord } from '@/api/flowable/finished';
import router from '@/router';

export const startFlowable = async function (arg) {
  let { data, oldData, dataDict = {}, tenantId, flowableType } = arg;

  let { id: procDefId, deploymentId: deployId } = await listDefinition({
    pageNum: 1,
    pageSize: 10,
    name: flowableType,
    tenantId,
  }).then((res) => {
    let result = res.data.records.filter((item) => item.suspensionState == 1);
    return result[0];
  });

  let variables = null;
  variables = await flowRecord({ deployId, tenantId }).then((res) => {
    return res.data.formData;
  });
  variables.disabled = true;
  variables.formBtns = false;
  variables.data = data;
  variables.oldData = oldData;
  variables.dataDict = dataDict;

  return definitionStart({
    procDefId,
    data: JSON.stringify({ variables }),
    tenantId,
  });
};

export const reviewFlowable = function (id, tenantId) {
  flowableDetail(id, tenantId).then((res) => {
    router.push({
      path: '/task/flowable/task/record/index',
      query: {
        procInsId: res.data.procInsId,
        executionId: res.data.executionId,
        deployId: res.data.deployId,
        taskId: res.data.taskId,
        finished: false,
        tenantId,
      },
    });
  });
};

export const getUrlByTenantId = function (tenantId) {
  let url = '';
  switch (tenantId) {
    case '':
      url = '';
      break;
    default:
      url = '';
      break;
  }
  return url;
};
