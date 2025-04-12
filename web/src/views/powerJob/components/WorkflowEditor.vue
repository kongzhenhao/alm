<template>
  <div class="edit">
    <el-row style="margin-left: 0px; margin-bottom: 20px; margin-right: 25px">
      <el-col :span="1">
        <el-button type="primary" @click="back">
          {{ $t('message.back') }}
        </el-button>
      </el-col>
      <el-col :offset="22" :span="1">
        <el-button :loading="saveLoading" type="success" @click="saveWorkflow">
          {{ $t('message.save') }}
        </el-button>
      </el-col>
    </el-row>

    <el-row>
      <el-form ref="form" :model="workflowInfo" label-width="100px">
        <el-form-item :label="$t('message.wfName')">
          <el-input v-model="workflowInfo.wfName" />
        </el-form-item>
        <el-form-item :label="$t('message.wfDescription')">
          <el-input v-model="workflowInfo.wfDescription" />
        </el-form-item>

        <el-form-item :label="$t('message.scheduleInfo')">
          <el-row>
            <el-col :span="6">
              <el-select
                v-model="workflowInfo.timeExpressionType"
                :placeholder="$t('message.timeExpressionType')"
              >
                <el-option
                  v-for="item in timeExpressionTypeOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.key"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <el-input
                v-model="workflowInfo.timeExpression"
                :placeholder="$t('message.wfTimeExpressionPLH')"
              />
            </el-col>
            <el-col :span="4">
              <el-button type="text" @click="onClickValidateTimeExpression">
                {{ $t('message.validateTimeExpression') }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item :label="$t('message.lifeCycle')">
          <el-date-picker
            v-model="workflowInfo.lifeCycle"
            :end-placeholder="$t('message.finishedTime')"
            :start-placeholder="$t('message.startTime')"
            type="datetimerange"
            value-format="timestamp"
          ></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('message.maxInstanceNum')">
          <el-input-number v-model="workflowInfo.maxWfInstanceNum" />
        </el-form-item>

        <el-form-item :label="$t('message.alarmConfig')">
          <el-select
            v-model="workflowInfo.notifyUserIds"
            :placeholder="$t('message.alarmSelectorPLH')"
            filterable
            multiple
          >
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.username"
              :value="user.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('message.flowParam')">
          <el-input v-model="workflowInfo.flowParam" />
        </el-form-item>
      </el-form>
    </el-row>

    <el-row>
      <!-- v-if="workflowInfo.id" -->
      <div class="power-flow">
        <div id="fullInc" class="power-dag">
          <PowerWorkflow
            :defaultWidthInc="234"
            :edges="peworkflowDAG.edges"
            :nodes="peworkflowDAG.nodes"
            :onClickImportNode="onClickImportNode"
            :onClickImportSpecialNode="onClickImportSpecialNode"
            :selectNode="selectNode"
            fullInc="fullInc"
            @getDag="getDag"
            @onSelectedNode="handleSelectedNode"
          >
            <div v-if="selectNode !== null" class="job-panl">
              <el-form ref="form" :model="nodeInfo">
                <el-form-item
                  v-if="nodeInfo.type != '2'"
                  :label="$t('message.jobName')"
                >
                  <el-select
                    v-model="nodeInfo.jobId"
                    :loading="taskLoading"
                    :remote-method="remoteTaskData"
                    :style="{ width: 'calc(100% - 90px)' }"
                    filterable
                    placeholder="请输入关键词"
                    remote
                    reserve-keyword
                    @change="handleWaitTaskChange"
                    @focus="handleWaitFocus"
                  >
                    <el-option
                      v-for="item in waitTaskList"
                      :key="item.id"
                      :label="item.jobName || item.wfName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('message.nodeName')">
                  <el-input
                    v-model="nodeInfo.nodeName"
                    :style="{ width: 'calc(100% - 90px)' }"
                    @input="handleNodeName"
                  />
                </el-form-item>
                <el-form-item
                  v-if="nodeInfo.type != '2'"
                  :label="$t('message.nodeParams')"
                >
                  <el-input
                    v-model="nodeInfo.nodeParams"
                    :style="{ width: 'calc(100% - 90px)' }"
                  />
                </el-form-item>
                <el-form-item
                  v-if="nodeInfo.type != '2'"
                  :label="$t('message.enable')"
                >
                  <el-switch v-model="nodeInfo.enable"></el-switch>
                  <img
                    v-if="nodeInfo.enable"
                    alt
                    class="job-panl-icon"
                    height="18"
                    src="../assets/start.svg"
                    width="18"
                  />
                </el-form-item>
                <el-form-item
                  v-if="nodeInfo.type != '2'"
                  :label="$t('message.skipWhenFailed')"
                >
                  <el-switch v-model="nodeInfo.skipWhenFailed"></el-switch>
                  <img
                    v-if="nodeInfo.skipWhenFailed"
                    alt
                    class="job-panl-icon"
                    height="18"
                    src="../assets/skip.svg"
                    width="18"
                  />
                </el-form-item>
              </el-form>
              <!-- <MonacoEditor
                    :code="nodeInfo.nodeParams"
                    key="nodeParams"
                    theme="vs"
                    :height="100"
                    :editorOptions="options"
                    @mounted="onMounted"
                    @codeChange="onCodeChange"
                  >
                  </MonacoEditor> -->
              <div v-if="nodeInfo.type == '2'" class="judge-message-params">
                <p>{{ $t('message.nodeParams') }}</p>
                <JSEditor
                  key="nodeParams"
                  :code="nodeInfo.nodeParams"
                  @onCodeChange="onCodeChange"
                ></JSEditor>
              </div>

              <div class="job-panl-btn">
                <el-button type="success" @click="handleNodeSave">
                  {{ $t('message.save') }}
                </el-button>
              </div>
            </div>
          </PowerWorkflow>
          <el-drawer
            :title="$t('message.importJobTitle')"
            :visible.sync="importDrawerVisible"
            direction="rtl"
            size="60%"
          >
            <div class="power-import-body">
              <el-row>
                <el-form
                  :inline="true"
                  :model="jobQueryContent"
                  class="el-form--inline"
                  size="small"
                >
                  <el-form-item :label="$t('message.jobId')">
                    <el-input
                      v-model="jobQueryContent.jobId"
                      :placeholder="$t('message.jobId')"
                    />
                  </el-form-item>
                  <el-form-item :label="$t('message.keyword')">
                    <el-input
                      v-model="jobQueryContent.keyword"
                      :placeholder="$t('message.keyword')"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="listJobInfos">
                      {{ $t('message.query') }}
                    </el-button>
                    <el-button type="cancel" @click="onClickReset">
                      {{ $t('message.reset') }}
                    </el-button>
                    <el-button type="cancel" @click="onBulkImport">
                      {{ $t('message.bulkImport') }}
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-row>
              <el-table
                :data="jobInfoPageResult.data"
                class="power-import-table"
                size="small"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="55" />
                <el-table-column :label="$t('message.jobId')" property="id" />
                <el-table-column
                  :label="$t('message.jobName')"
                  property="jobName"
                />
                <el-table-column :label="$t('message.operation')">
                  <template slot-scope="scope">
                    <el-button size="small" @click="importTask([scope.row])">
                      {{ $t('message.import') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-row style="margin-top: 20px">
                <el-pagination
                  :page-size="this.jobInfoPageResult.pageSize"
                  :total="this.jobInfoPageResult.totalItems"
                  layout="prev, pager, next"
                  @current-change="onClickChangePage"
                />
              </el-row>
            </div>
          </el-drawer>
        </div>
      </div>
    </el-row>
    <el-dialog
      v-if="timeExpressionValidatorVisible"
      :visible.sync="timeExpressionValidatorVisible"
    >
      <TimeExpressionValidator
        :time-expression="workflowInfo.timeExpression"
        :time-expression-type="workflowInfo.timeExpressionType"
      />
    </el-dialog>
    <el-drawer
      :title="$t('message.importWorkflowTitle')"
      :visible.sync="workflowVisible"
      direction="rtl"
      size="60%"
    >
      <WorkflowManager
        :isWorkflow="true"
        @onImportNode="onImportChildWorkflowNode"
      ></WorkflowManager>
    </el-drawer>
  </div>
</template>

<script>
import JSEditor from './JSEditor.vue';
import TimeExpressionValidator from './TimeExpressionValidator';
import PowerWorkflow from './PowerWorkflow';
import WorkflowManager from '../WorkflowManager';
import axios from 'axios';
import { Message } from 'element-ui';

let jobAxios = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
});

jobAxios.interceptors.response.use(
  (response) => {
    if (response.data.success === true) {
      return response.data.data;
    }
    Message.warning('ERROR：' + response.data.message);
    return Promise.reject(response.data.msg);
  },
  (error) => {
    Message.error(error.toString());
    return Promise.reject(error);
  }
);

function nodeInfoChange(icon, index) {
  return function (value) {
    if (!this.selectNode) return;
    const group = this.selectNode.getContainer();
    const current = group.getChildByIndex(index);
    if (value) {
      current.attr({ img: icon });
    } else {
      current.attr({ img: '' });
    }
  };
}

/** 节点类型映射 */
const nodeType = {
  1: (item) => {
    return {
      type: 'flow-node',
      size: [240, 70],
      leftText: item.jobId,
      titleText: item.nodeName,
      icon1: item.enable ? require('../assets/start.svg') : '',
      icon2: item.skipWhenFailed ? require('../assets/skip.svg') : '',
    };
  },
  2: (item) => {
    return {
      type: 'max-diamond-node',
      text: !item.nodeName ? '判断' : item.nodeName,
      style: {
        sideLength: 80,
        textStyle: {
          // fill: "#FFFFFF",
        },
        // fill: "#FE9201",
        // stroke: "#D45547",
      },
    };
  },
  3: (item) => {
    return {
      type: 'flow-child-node',
      size: [240, 70],
      leftText: item.jobId,
      titleText: item.nodeName,
      icon1: item.enable ? require('../assets/start.svg') : '',
      icon2: item.skipWhenFailed ? require('../assets/skip.svg') : '',
    };
  },
};

export default {
  name: 'WorkflowEditor',
  components: {
    TimeExpressionValidator,
    PowerWorkflow,
    JSEditor,
    WorkflowManager,
  },
  data() {
    return {
      workflowInfo: {
        id: '',
        appId: this.$store.state.appId,
        enable: true,
        maxWfInstanceNum: 1,
        notifyUserIds: [],
        timeExpression: undefined,
        timeExpressionType: undefined,
        wfDescription: undefined,
        wfName: undefined,
        lifeCycle: null,
        flowParam: undefined,
      },
      nodeInfo: {
        id: null,
        jobId: null,
        nodeName: '',
        nodeParams: '',
        enable: true,
        skipWhenFailed: true,
      },
      timeExpressionTypeOptions: [
        { key: 'API', label: 'API' },
        { key: 'CRON', label: 'CRON' },
      ],
      userList: [],

      // 导入任务相关
      importDrawerVisible: false,
      jobQueryContent: {
        appId: this.$store.state.appId,
        index: 0,
        pageSize: 8,
        jobId: undefined,
        keyword: undefined,
      },
      jobInfoPageResult: {
        pageSize: 20,
        totalItems: 0,
        data: [],
      },

      // 事件（1：新增起点，2：新增终点，3：删除节点；4：删除边）
      event: undefined,
      from: undefined,

      // 时间表达式校验窗口
      timeExpressionValidatorVisible: false,
      /** DAG信息 */
      peworkflowDAG: {
        nodes: [],
        edges: [],
      },
      /** 保存按钮loading */
      saveLoading: false,
      /** 流程图实例 */
      powerFlow: null,
      /** 选中的数据 */
      multipleSelection: [],
      /** 当前的节点信息 */
      taskList: [],
      /** 当前选中的节点 */
      selectNode: null,
      /** 重置节点方法 */
      resetNodes: null,
      /** 待选任务列表 */
      waitTaskList: [],
      /** 任务搜索loading */
      taskLoading: false,
      /** 任务节流 */
      taskTimeout: null,
      /** 工作流引入显隐控制 */
      workflowVisible: false,
    };
  },
  methods: {
    // 返回上一页
    back: function () {
      this.$router.go(-1);
    },
    /* ******** 导入任务相关，所有函数与 JobManager 重复，需要前端大佬重构！ ******** */
    // 点击重置按钮
    onClickReset() {
      this.jobQueryContent.keyword = undefined;
      this.jobQueryContent.jobId = undefined;
      this.listJobInfos();
    },
    // 列出符合当前搜索条件的任务
    listJobInfos() {
      const that = this;
      jobAxios.post('/job/list', this.jobQueryContent).then((res) => {
        that.jobInfoPageResult = res;
      });
    },
    // 点击 换页
    onClickChangePage(index) {
      // 后端从0开始，前端从1开始
      this.jobQueryContent.index = index - 1;
      this.listJobInfos();
    },
    onClickImportNode() {
      this.listJobInfos();
      this.importDrawerVisible = true;
    },
    /** 引入其他类型节点, 判断，工作流 */
    onClickImportSpecialNode(data) {
      const { type } = data;
      console.log(type);
      if (type === 3) {
        this.workflowVisible = true;
      } else {
        this.importTask([
          {
            appId: this.workflowInfo.appId,
            jobParams: 'true',
            type: type,
            workflowId: this.workflowInfo.id,
            jobName: '',
          },
        ]);
      }
    },
    /** 引入嵌套工作流节点 */
    onImportChildWorkflowNode(data) {
      this.importTask([
        {
          appId: this.workflowInfo.appId,
          nodeName: data.wfName,
          enable: data.enable,
          id: data.id,
          nodeParams: '',
          type: 3,
          workflowId: this.workflowInfo.id,
          jobName: data.wfName,
        },
      ]);
    },
    onClickValidateTimeExpression() {
      this.timeExpressionValidatorVisible = true;
    },
    /** 选中节点 */
    handleSelectedNode(item) {
      this.selectNode = item;

      // 从节点列表找到节点
      let index = this.getNodeIndexById(item.get('model').nodeId);

      let node = this.taskList[index];
      // if (node.type === "condition") return false;
      this.remoteTaskData(null, node.jobId);
      console.log(node);
      this.nodeInfo = {
        type: node.nodeType,
        jobId: node.jobId,
        nodeName: node.nodeName
          ? node.nodeName
          : node.nodeType == 2
          ? '判断'
          : node.nodeName,
        nodeParams: node.nodeParams,
        enable: node.enable,
        skipWhenFailed: node.skipWhenFailed,
        id: item.get('model').nodeId || item.get('model').id,
      };
    },
    /** 多选节点 */
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /** 修改节点名称 */
    handleNodeName(value) {
      const nodeItem = this.powerFlow.graph.get('selectedItem');
      const group = nodeItem.getContainer();
      const current = group.getChildByIndex(2);
      current.attr('text', value);
    },
    /** 获取工作流程图实例 */
    getDag(powerFlow, { resetNodes }) {
      this.powerFlow = powerFlow;
      this.resetNodes = resetNodes;
    },
    /** 根据nodeId找任务节点索引 */
    getNodeIndexById(nodeId) {
      return this.taskList.findIndex((item) => item.nodeId == nodeId);
    },
    /** 获取工作流信息 */
    async getWorkflowInfo(fit) {
      const res = await jobAxios.get('/workflow/fetch', {
        params: {
          workflowId: this.workflowInfo.id,
          appId: this.workflowInfo.appId,
        },
      });
      console.log(res, 'res===');
      this.workflowInfo = { ...this.workflowInfo, ...res };
      if (res.peworkflowDAG) {
        this.taskList = res.peworkflowDAG.nodes;
        this.peworkflowDAG = res.peworkflowDAG;
        this.$nextTick(() => {
          this.resetNodes();
          if (fit) {
            // this.powerFlow.graph.fitView(20);
            // 改为layout适配会对节点少的时候友好一点
            this.powerFlow.graph.layout();
          }
        });
      }
    },
    /** 保存工作流全局信息 */
    async saveWorkflow() {
      // 改为不需要dag信息
      const flowData = this.powerFlow.graph.save();
      console.log(flowData);
      let dagInfo = {
        nodes: flowData.nodes.map((item) => ({ nodeId: item.id })),
        edges: flowData.edges.map((item) => {
          const property = {};
          if (item.label) {
            property.property = item.label === 'Y' ? 'true' : 'false';
          }
          return {
            from: item.source,
            to: item.target,
            ...property,
          };
        }),
      };
      const { lifeCycle } = this.workflowInfo;
      if (lifeCycle && Array.isArray(lifeCycle)) {
        const start = lifeCycle[0];
        const end = lifeCycle[1];
        this.workflowInfo.lifeCycle = {
          start,
          end,
        };
      }
      const res = await jobAxios.post('/workflow/save', {
        ...this.workflowInfo,
        dag: dagInfo,
      });
      this.$message.success(this.$t('message.success'));
      if (!this.workflowInfo.id) this.workflowInfo.id = res;
    },
    /** 导入任务节点数据 */
    async importTask(taskList) {
      if (taskList.length === 0) {
        return;
      }

      let type = '';

      let data = taskList.map((item) => {
        type = item.type ? Number(item.type) : 1;
        return {
          appId: item.appId,
          enable: item.enable,
          skipWhenFailed: item.skipWhenFailed,
          nodeName: item.jobName,
          jobId: item.id,
          nodeParams: item.jobParams,
          workflowId: this.workflowInfo.id,
          // type: "JOB"
          type: type,
        };
      });
      let res = await jobAxios.post('/workflow/saveNode', data);
      console.log(res);
      // 先移动视口一个节点点的位置
      // 获取缩放比例
      const zoom = this.powerFlow.graph.getZoom();
      this.powerFlow.graph.translate(260 * zoom, 0);
      const viewPointEnd = this.powerFlow.graph.getPointByCanvas(0, 0);
      res.forEach((item, index) => {
        const nodeText = nodeType[type](item);
        this.powerFlow.graph.add('node', {
          ...item,

          id: `${item.id}`,
          nodeId: `${item.id}`,
          nodeType: `${item.type}`,
          // type: nodeType[item.type],
          size: [240, 70],
          x: viewPointEnd.x + 20,
          y: viewPointEnd.y + 70 * index + 20 + index * 10,
          ...nodeText,
          // leftText: item.jobId,
          // titleText: item.nodeName,
          // icon1: item.enable ? require("../assets/start.svg") : "",
          // icon2: item.skipWhenFailed ? require("../assets/skip.svg") : "",
        });
      });
      this.taskList = [
        ...this.taskList,
        ...res.map((item) => ({
          ...item,
          nodeType: item.type,
          nodeParams: item.nodeParams,
          nodeId: item.id,
        })),
      ];
    },
    /** 保存单个节点 */
    async handleNodeSave(value = {}) {
      let data = [
        {
          ...this.nodeInfo,
          appId: this.workflowInfo.appId,
          workflowId: this.workflowInfo.id,
          ...value,
        },
      ];
      await jobAxios.post('/workflow/saveNode', data);

      let index = this.getNodeIndexById(this.nodeInfo.id);

      this.taskList[index] = {
        ...this.taskList[index],
        nodeName: this.nodeInfo.nodeName,
        nodeParams: this.nodeInfo.nodeParams,
        enable: this.nodeInfo.enable,
        skipWhenFailed: this.nodeInfo.skipWhenFailed,
      };

      this.$message.success(this.$t('message.success'));
    },
    /** 批量导入工作流 */
    async onBulkImport() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning(this.$t('message.noSelect'));
        return;
      }
      await this.importTask(this.multipleSelection);
    },
    /** 远程加载任务列表数据 */
    async remoteTaskData(value, jobId) {
      clearTimeout(this.taskTimeout);
      this.taskTimeout = setTimeout(() => {
        this.taskLoading = true;
        console.log(this.nodeInfo);
        let url = '/job/list';
        if (this.nodeInfo.type === 3) {
          url = '/workflow/list';
        }
        jobAxios
          .post(url, {
            ...this.jobQueryContent,
            index: 0,
            keyword: value,
            jobId: jobId,
          })
          .then((res) => {
            this.waitTaskList = res.data;
            this.taskLoading = false;
          });
      }, 100);
    },
    /** 选中任务时 */
    handleWaitTaskChange(value) {
      // 找到节点信息
      let current = this.waitTaskList.find((item) => item.id === value);

      let currentShape = this.selectNode.getContainer().getChildByIndex(1);

      currentShape.attr({ text: current.id });
      let index = this.getNodeIndexById(this.selectNode.get('model').nodeId);
      this.powerFlow.graph.updateItem(this.selectNode, {
        leftText: current.id,
        jobId: current.id,
      });
      this.taskList[index] = {
        ...this.taskList[index],
        jobId: current.id,
      };
      this.nodeInfo.jobId = value;
    },
    /** 节点外点击时单独处理 */
    handleWaitFocus() {
      this.powerFlow.graph.set('noKeyDown', true);
    },
    /** 判断节点参数改变 */
    onCodeChange(code) {
      this.nodeInfo.nodeParams = code;
    },
  },
  mounted() {
    // 加载用户信息
    let that = this;
    jobAxios.get('/user/list').then((res) => (that.userList = res));

    // 读取传递数据，如果是修改，需要先将数据绘制上去
    let modify = this.$route.params.modify;
    if (modify) {
      this.workflowInfo = this.$route.params.workflowInfo;
      if (this.workflowInfo.lifeCycle) {
        const { start, end } = this.workflowInfo.lifeCycle;
        this.workflowInfo.lifeCycle = [start, end];
      } else {
        this.workflowInfo.lifeCycle = null;
      }
      this.workflowInfo.appId = this.$store.state.appId;
      this.getWorkflowInfo(true);
    }
  },
  watch: {
    'nodeInfo.enable': {
      handler: nodeInfoChange(require('../assets/start.svg'), 3),
    },
    'nodeInfo.skipWhenFailed': {
      handler: nodeInfoChange(require('../assets/skip.svg'), 4),
    },
  },
};
</script>

<style scoped>
.edit {
  padding: 20px !important;
  border: none !important;
}

.el-input {
  width: 80%;
}

.job-panl-icon {
  vertical-align: middle;
}

.title {
  display: inline-block;
  margin: 5px 0;
  font-size: 16px;
  font-weight: bold;
}

.power-dag {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.job-panl {
  /* border: 1px solid red; */
  flex: 1;
  position: relative;
  border-radius: 10px;
  box-shadow: 0 10px 10px 1px #c0c0c0;
  border-top-right-radius: 0px;
  box-sizing: border-box;
  margin: 0 10px;
  padding: 10px;
  height: 100%;
}

.job-panl-btn {
  /* position: absolute;
        bottom: 0; */
  display: flex;
  justify-content: flex-end;
  width: 100%;
  /* margin-right: 12px; */
  box-sizing: border-box;
  padding: 12px;
  border-top: 1px solid #f0f0f0;
}

.job-tools {
  width: calc(100% - 10px);
  box-sizing: border-box;
  border: 1px solid #d0d0d0;
  /* box-shadow: 0 0 10px 1px #c0c0c0; */
  border-radius: 10px;
  margin-right: 10px;
  border-bottom-left-radius: 0px;
  border-bottom-right-radius: 0px;
  display: flex;
  padding: 8px 10px;
}

.job-tools > div {
  box-sizing: border-box;
  height: 30px;
  width: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.job-tools div:hover {
  border: 1px solid #f0f0f0;
}

.job-tools > div + div {
  margin-left: 24px;
}

.job-tools i {
  font-size: 20px;
  /* color: #aaaaaa; */
}

.power-import-body {
  padding: 0px 20px;
}

/* .el-drawer__body {
  padding: 0 20px;
} */
</style>
<!-- can't use scope, or dag will be the black block, maybe this is the bug of d3.js -->
<style>
svg {
  font-size: 16px;
  /* border: 1px solid red; */
}

.power-flow {
  background: #fff;
}

.node rect {
  stroke: #999;
  fill: #fff;
  stroke-width: 1.5px;
}

.edgePath path {
  stroke: #333;
  stroke-width: 1px;
}

.power-import-table .el-table-column--selection > .cell {
  padding-left: 15px;
}

.judge-message-params {
  font-size: 14px;
  color: #606266;
}

.judge-message-params p {
  margin-bottom: 4px;
}
</style>
