<template>
  <div class="app-container" id="job_manager">
    <el-form
      :inline="true"
      :model="jobQueryContent"
      class="el-form--inline"
      size="small"
    >
      <el-form-item :label="$t('message.jobId')">
        <el-input
          v-model="jobQueryContent.jobId"
          type="number"
          :placeholder="$t('message.jobId')"
        />
      </el-form-item>
      <el-form-item :label="$t('message.keyword')">
        <el-input
          v-model="jobQueryContent.keyword"
          :placeholder="$t('message.keyword')"
        />
      </el-form-item>
    </el-form>

    <div class="search-btns">
      <el-button class="cancelBtn" @click="onClickReset">重置</el-button>
      <el-button class="confirmBtn" type="primary" @click="listJobInfos">
        查询
      </el-button>
    </div>

    <el-card class="table-wrap">
      <div class="title-wrap">
        <h4>任务配置</h4>
        <div class="btns">
          <el-button class="button export" @click="onClickJobInputButton">
            {{ $t('message.inputJob') }}
          </el-button>
          <el-button class="button add" @click="onClickNewJob">
            {{ $t('message.newJob') }}
          </el-button>
        </div>
      </div>

      <el-table :data="jobInfoPageResult.data" style="width: 100%">
        <el-table-column :label="$t('message.jobId')" prop="id" width="80" />
        <el-table-column :label="$t('message.jobName')" prop="jobName" />
        <el-table-column :label="$t('message.scheduleInfo')">
          <template slot-scope="scope">
            {{ scope.row.timeExpressionType }} {{ scope.row.timeExpression }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('message.executeType')">
          <template slot-scope="scope">
            {{ translateExecuteType(scope.row.executeType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('message.processorType')">
          <template slot-scope="scope">
            {{ translateProcessorType(scope.row.processorType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('message.status')" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="changeJobStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column :label="$t('message.operation')" width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="onClickModify(scope.row)"
            >
              {{ $t('message.edit') }}
            </el-button>
            <el-button size="mini" type="text" @click="onClickRun(scope.row)">
              {{ $t('message.run') }}
            </el-button>
            <el-dropdown trigger="click">
              <el-button size="mini" type="text">
                {{ $t('message.more') }}
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    @click="onClickRunByParameter(scope.row)"
                  >
                    {{ $t('message.runByParameter') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    @click="onClickRunHistory(scope.row)"
                  >
                    {{ $t('message.runHistory') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    @click="onClickCopyJob(scope.row)"
                  >
                    {{ $t('message.copy') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    @click="onClickJobExportButton(scope.row)"
                  >
                    {{ $t('message.export') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    size="mini"
                    type="text"
                    @click="onClickDeleteJob(scope.row)"
                  >
                    {{ $t('message.delete') }}
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-show="this.jobInfoPageResult.totalItems > 0"
        :page-size="this.jobInfoPageResult.pageSize"
        :total="this.jobInfoPageResult.totalItems"
        background
        layout="prev, pager, next"
        @current-change="onClickChangePage"
      />
    </el-card>

    <el-dialog
      :close-on-click-modal="true"
      :visible.sync="modifiedJobFormVisible"
      width="930px"
    >
      <el-form :model="modifiedJobForm" label-width="90px" size="small">
        <el-form-item :label="$t('message.jobName')">
          <el-input v-model="modifiedJobForm.jobName" />
        </el-form-item>
        <el-form-item :label="$t('message.jobDescription')">
          <el-input v-model="modifiedJobForm.jobDescription" />
        </el-form-item>
        <el-form-item :label="$t('message.jobParams')">
          <el-input v-model="modifiedJobForm.jobParams" type="textarea" />
        </el-form-item>
        <el-form-item :label="$t('message.scheduleInfo')">
          <el-select
            class="job_select"
            v-model="modifiedJobForm.timeExpressionType"
            :placeholder="$t('message.timeExpressionType')"
          >
            <el-option
              v-for="item in timeExpressionTypeOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>
          <el-input
            v-if="
              ['CRON', 'FIXED_DELAY', 'FIXED_RATE'].includes(
                modifiedJobForm.timeExpressionType
              )
            "
            class="job_input"
            v-model="modifiedJobForm.timeExpression"
            :placeholder="$t('message.timeExpressionPlaceHolder')"
          />
          <el-button
            v-if="
              ['DAILY_TIME_INTERVAL'].includes(
                modifiedJobForm.timeExpressionType
              )
            "
            type="primary"
            @click="onClickEditTimeExpression"
          >
            点击编辑
          </el-button>
          <el-button
            style="padding-left: 10px"
            type="text"
            @click="onClickValidateTimeExpression"
          >
            {{ $t('message.validateTimeExpression') }}
          </el-button>
        </el-form-item>
        <el-form-item :label="$t('message.lifeCycle')">
          <el-date-picker
            v-model="modifiedJobForm.lifeCycle"
            :end-placeholder="$t('message.finishedTime')"
            :start-placeholder="$t('message.startTime')"
            type="datetimerange"
            value-format="timestamp"
          ></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('message.executeConfig')">
          <el-select
            class="job_select"
            v-model="modifiedJobForm.executeType"
            :placeholder="$t('message.executeType')"
          >
            <el-option
              v-for="item in executeTypeOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>

          <el-select
            class="job_select"
            v-model="modifiedJobForm.processorType"
            :placeholder="$t('message.processorType')"
          >
            <el-option
              v-for="item in processorTypeOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>

          <el-input
            style="width: 350px"
            v-model="modifiedJobForm.processorInfo"
            :placeholder="verifyPlaceholder(modifiedJobForm.processorType)"
          />
        </el-form-item>
        <el-form-item :label="$t('message.runtimeConfig')">
          <el-select
            class="job_select"
            v-model="modifiedJobForm.dispatchStrategy"
            :placeholder="$t('message.dispatchStrategy')"
          >
            <el-option
              v-for="item in dispatchStrategy"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>
          <el-input
            v-model="modifiedJobForm.maxInstanceNum"
            :placeholder="$t('message.maxInstanceNum')"
            class="ruleContent job_number"
          >
            <template slot="prepend">
              {{ $t('message.maxInstanceNum') }}
            </template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.concurrency"
            :placeholder="$t('message.threadConcurrency')"
            class="ruleContent job_number"
          >
            <template slot="prepend">
              {{ $t('message.threadConcurrency') }}
            </template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.instanceTimeLimit"
            :placeholder="$t('message.timeout')"
            class="ruleContent job_number"
          >
            <template slot="prepend">{{ $t('message.timeout') }}</template>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('message.retryConfig')">
          <el-input
            v-model="modifiedJobForm.instanceRetryNum"
            :placeholder="$t('message.taskRetryTimes')"
            class="ruleContent job_number"
          >
            <template slot="prepend">
              {{ $t('message.taskRetryTimes') }}
            </template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.taskRetryNum"
            :placeholder="$t('message.subTaskRetryTimes')"
            class="ruleContent job_number"
          >
            <template slot="prepend">
              {{ $t('message.subTaskRetryTimes') }}
            </template>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('message.workerConfig')">
          <el-input
            v-model="modifiedJobForm.minCpuCores"
            :placeholder="$t('message.minCPU')"
            class="ruleContent job_number"
          >
            <template slot="prepend">{{ $t('message.minCPU') }}</template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.minMemorySpace"
            :placeholder="$t('message.minMemory')"
            class="ruleContent job_number"
          >
            <template slot="prepend">{{ $t('message.minMemory') }}</template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.minDiskSpace"
            :placeholder="$t('message.minDisk')"
            class="ruleContent job_number"
          >
            <template slot="prepend">{{ $t('message.minDisk') }}</template>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('message.clusterConfig')">
          <el-input
            v-model="modifiedJobForm.designatedWorkers"
            :placeholder="$t('message.designatedWorkerAddressPLH')"
            class="ruleContent"
            style="width: 360px"
          >
            <template slot="prepend">
              {{ $t('message.designatedWorkerAddress') }}
            </template>
          </el-input>
          <el-input
            v-model="modifiedJobForm.maxWorkerCount"
            :placeholder="$t('message.maxWorkerNumPLH')"
            class="ruleContent job_number"
          >
            <template slot="prepend">{{ $t('message.maxWorkerNum') }}</template>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('message.alarmConfig')">
          <el-select
            class="job_select"
            v-model="modifiedJobForm.notifyUserIds"
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
          <el-input
            class="job_number"
            v-model="modifiedJobForm.alarmConfig.alertThreshold"
          >
            <template slot="prepend">
              {{ $t('message.alertThreshold') }}
            </template>
          </el-input>
          <el-input
            class="job_number"
            v-model="modifiedJobForm.alarmConfig.statisticWindowLen"
          >
            <template slot="prepend">
              {{ $t('message.statisticWindow') + '(s)' }}
            </template>
          </el-input>
          <el-input
            class="job_number"
            v-model="modifiedJobForm.alarmConfig.silenceWindowLen"
          >
            <template slot="prepend">
              {{ $t('message.silenceWindow') + '(s)' }}
            </template>
          </el-input>
        </el-form-item>

        <el-form-item :label="$t('message.logConfig')">
          <el-select
            class="job_select"
            v-model="modifiedJobForm.logConfig.type"
            :placeholder="$t('message.logType')"
          >
            <el-option
              v-for="item in logType"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>
          <el-select
            class="job_select"
            v-model="modifiedJobForm.logConfig.level"
            :placeholder="$t('message.logLevel')"
          >
            <el-option
              v-for="item in logLevel"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            ></el-option>
          </el-select>
          <el-input
            v-if="[2, 4].includes(modifiedJobForm.logConfig.type)"
            v-model="modifiedJobForm.logConfig.loggerName"
            style="width: 360px"
          >
            <template slot="prepend">{{ $t('message.loggerName') }}</template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveJob">
            {{ $t('message.save') }}
          </el-button>
          <el-button @click="modifiedJobFormVisible = false">
            {{ $t('message.cancel') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog
      v-if="timeExpressionValidatorVisible"
      :close-on-click-modal="true"
      :visible.sync="timeExpressionValidatorVisible"
    >
      <TimeExpressionValidator
        :time-expression="modifiedJobForm.timeExpression"
        :time-expression-type="modifiedJobForm.timeExpressionType"
      />
    </el-dialog>

    <!-- 时间表达式编辑 -->
    <el-dialog
      v-if="timeExpressionEditorVisible"
      :close-on-click-modal="true"
      :visible.sync="timeExpressionEditorVisible"
    >
      <DailyTimeIntervalForm
        :timeExpression="modifiedJobForm.timeExpression"
        @contentChanged="eventFromDailyTimeIntervalExpress"
      ></DailyTimeIntervalForm>
    </el-dialog>

    <!-- 任务导入导出 -->
    <el-dialog
      v-if="jobExporterDialogVisible"
      :close-on-click-modal="true"
      :visible.sync="jobExporterDialogVisible"
    >
      <Exporter
        :mode="jobExporterMode"
        :target-id="jobExporterTargetId"
        type="JOB"
        @finished="eventFromExporter"
      ></Exporter>
    </el-dialog>

    <el-dialog
      :before-close="clsoeRunByParameter"
      :title="$t('message.runByParameter')"
      :visible="!!temporaryRowData"
      width="50%"
    >
      <el-input
        v-model="runParameter"
        :placeholder="$t('message.enteringParameter')"
        :rows="4"
        type="textarea"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="onClickRunCancel">
          {{ $t('message.cancel') }}
        </el-button>
        <el-button
          :loading="runLoading"
          size="mini"
          type="primary"
          @click="onClickRun(temporaryRowData)"
        >
          {{ $t('message.run') }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import TimeExpressionValidator from './components/TimeExpressionValidator';
import DailyTimeIntervalForm from './components/DailyTimeIntervalForm';
import Exporter from './components/Exporter';
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
export default {
  name: 'JobManager',
  components: { Exporter, TimeExpressionValidator, DailyTimeIntervalForm },
  data() {
    return {
      modifiedJobFormVisible: false,
      // 新建任务对象
      modifiedJobForm: {
        id: undefined,
        jobName: '',
        jobDescription: '',
        appId: this.$store.state.appId,
        jobParams: '',
        timeExpressionType: '',
        timeExpression: '',
        executeType: '',
        processorType: '',
        processorInfo: '',
        maxInstanceNum: 0,
        concurrency: 5,
        instanceTimeLimit: 0,
        instanceRetryNum: 0,
        taskRetryNum: 1,
        dispatchStrategy: undefined,

        minCpuCores: 0,
        minMemorySpace: 0,
        minDiskSpace: 0,

        enable: true,
        designatedWorkers: '',
        maxWorkerCount: 0,
        notifyUserIds: [],
        lifeCycle: null,
        alarmConfig: {
          alertThreshold: undefined,
          statisticWindowLen: undefined,
          silenceWindowLen: undefined,
        },
        logConfig: {
          type: 1,
          level: undefined,
          loggerName: undefined,
        },
      },
      // 任务查询请求对象
      jobQueryContent: {
        appId: this.$store.state.appId,
        index: 0,
        pageSize: 10,
        jobId: undefined,
        keyword: undefined,
      },
      // 任务列表（查询结果），包含index、pageSize、totalPages、totalItems、data（List类型）
      jobInfoPageResult: {
        index: 0,
        pageSize: 10,
        totalItems: 0,
        data: [],
      },
      // 时间表达式选择类型
      timeExpressionTypeOptions: [
        { key: 'API', label: 'API' },
        { key: 'CRON', label: 'CRON' },
        { key: 'FIXED_RATE', label: this.$t('message.fixRate') },
        { key: 'FIXED_DELAY', label: this.$t('message.fixDelay') },
        { key: 'WORKFLOW', label: this.$t('message.workflow') },
        {
          key: 'DAILY_TIME_INTERVAL',
          label: this.$t('message.dailyTimeInterval'),
        },
      ],
      // 处理器类型
      processorTypeOptions: [
        { key: 'BUILT_IN', label: this.$t('message.builtIn') },
        { key: 'EXTERNAL', label: this.$t('message.external') },
      ], // {key: "SHELL", label: "SHELL"}, {key: "PYTHON", label: "PYTHON"}
      // 执行方式类型
      executeTypeOptions: [
        { key: 'STANDALONE', label: this.$t('message.standalone') },
        { key: 'BROADCAST', label: this.$t('message.broadcast') },
        { key: 'MAP', label: this.$t('message.map') },
        { key: 'MAP_REDUCE', label: this.$t('message.mapReduce') },
      ],
      // 日志级别
      logLevel: [
        { key: 1, label: 'DEBUG' },
        { key: 2, label: 'INFO' },
        { key: 3, label: 'WARN' },
        { key: 4, label: 'ERROR' },
        { key: 99, label: 'OFF' },
      ],
      // 日志类型
      logType: [
        { key: 1, label: 'ONLINE' },
        { key: 2, label: 'LOCAL' },
        { key: 3, label: 'STDOUT' },
        { key: 4, label: 'LOCAL_AND_ONLINE' },
        { key: 999, label: 'NULL' },
      ],
      // 分发类型
      dispatchStrategy: [
        { key: 'HEALTH_FIRST', label: 'HEALTH_FIRST' },
        { key: 'RANDOM', label: 'RANDOM' },
      ],
      // 用户列表
      userList: [],
      // 时间表达式校验窗口
      timeExpressionValidatorVisible: false,
      // 时间表达式编辑窗口
      timeExpressionEditorVisible: false,
      // 临时存储的行数据
      temporaryRowData: null,
      // 运行参数
      runParameter: null,
      // 运行loading
      runLoading: false,

      // 任务导入导出相关功能
      jobExporterMode: undefined,
      jobExporterTargetId: undefined,
      jobExporterDialogVisible: false,
    };
  },
  methods: {
    clsoeRunByParameter() {
      this.onClickRunCancel();
    },
    // 保存变更，包括新增和修改
    async saveJob() {
      const { lifeCycle, alarmConfig } = this.modifiedJobForm;
      if (lifeCycle && Array.isArray(lifeCycle)) {
        const start = lifeCycle[0];
        const end = lifeCycle[1];
        this.modifiedJobForm.lifeCycle = {
          start,
          end,
        };
      }
      if (!alarmConfig.alertThreshold) {
        alarmConfig.alertThreshold = 0;
      }
      if (!alarmConfig.statisticWindowLen) {
        alarmConfig.statisticWindowLen = 0;
      }
      if (!alarmConfig.silenceWindowLen) {
        alarmConfig.silenceWindowLen = 0;
      }
      this.modifiedJobForm.alarmConfig = alarmConfig;
      await jobAxios.post('/job/save', this.modifiedJobForm);
      this.modifiedJobFormVisible = false;
      this.$message.success(this.$t('message.success'));
      this.listJobInfos();
    },
    // 列出符合当前搜索条件的任务
    listJobInfos() {
      const that = this;
      jobAxios.post('/job/list', this.jobQueryContent).then((res) => {
        console.log(res);
        if (res && res.data) {
          res.data = res.data.map((item) => {
            const lifeCycle = item.lifeCycle;
            if (lifeCycle && lifeCycle.start && lifeCycle.end) {
              item.lifeCycle = [lifeCycle.start, lifeCycle.end];
            } else {
              item.lifeCycle = null;
            }
            return item;
          });
        }

        that.jobInfoPageResult = res;
      });
    },
    // 修改任务状态
    changeJobStatus(data) {
      // switch 会自动更改 enable 的值
      let that = this;
      if (data.enable === false) {
        // 仅有，有特殊逻辑（关闭秒级任务），走单独接口
        jobAxios
          .get('/job/disable?jobId=' + data.id)
          .then(() => that.listJobInfos());
      } else {
        // 启用，则发起正常的保存操作
        this.modifiedJobForm = data;
        this.saveJob();
      }
    },
    // 新增任务，去除旧数据
    onClickNewJob() {
      this.modifiedJobForm.id = undefined;
      this.modifiedJobForm.jobName = undefined;
      this.modifiedJobForm.jobDescription = undefined;
      this.modifiedJobForm.jobParams = undefined;
      this.modifiedJobForm.timeExpression = undefined;
      this.modifiedJobForm.timeExpressionType = undefined;
      this.modifiedJobForm.processorInfo = undefined;
      this.modifiedJobForm.processorType = undefined;
      this.modifiedJobForm.executeType = undefined;
      this.modifiedJobForm.lifeCycle = null;
      this.modifiedJobForm.alarmConfig = {
        alertThreshold: undefined,
        statisticWindowLen: undefined,
        silenceWindowLen: undefined,
      };
      this.modifiedJobFormVisible = true;
    },
    // 点击 编辑按钮
    onClickModify(data) {
      // 修复点击编辑后再点击新增 行数据被清空 的问题
      if (!data.alarmConfig) {
        data.alarmConfig = {
          alertThreshold: undefined,
          statisticWindowLen: undefined,
          silenceWindowLen: undefined,
        };
      }
      if (!data.lifeCycle) {
        data.lifeCycle = null;
      }
      this.modifiedJobForm = JSON.parse(JSON.stringify(data));
      this.modifiedJobFormVisible = true;
    },
    // 点击 立即运行按钮
    onClickRun(data) {
      let that = this;
      let url =
        '/job/run?jobId=' + data.id + '&appId=' + this.$store.state.appId;
      if (this.temporaryRowData && this.runParameter) {
        url += `&instanceParams=${encodeURIComponent(this.runParameter)}`;
      }
      this.runLoading = true;
      jobAxios
        .get(url)
        .then(() => {
          that.$message.success(this.$t('message.success'));
          this.temporaryRowData = null;
          this.runLoading = false;
        })
        .catch(() => {
          this.runLoading = false;
        });
    },
    // 参数运行
    onClickRunByParameter(data) {
      this.temporaryRowData = data;
    },
    // 取消参数运行
    onClickRunCancel() {
      this.temporaryRowData = null;
      this.runParameter = null;
    },
    // 点击 删除任务
    onClickDeleteJob(data) {
      let that = this;
      let url = '/job/delete?jobId=' + data.id;
      jobAxios.get(url).then(() => {
        that.$message.success(this.$t('message.success'));
        that.listJobInfos();
      });
    },
    // 点击 复制任务
    onClickCopyJob(data) {
      let url = '/job/copy?jobId=' + data.id;
      let that = this;
      jobAxios.post(url).then((res) => {
        that.modifiedJobForm = res;
        that.modifiedJobFormVisible = true;
      });
    },
    // 点击 历史记录
    onClickRunHistory(data) {
      this.$router.push({
        name: 'InstanceManager',
        params: {
          jobId: data.id,
        },
      });
    },
    // 点击 换页
    onClickChangePage(index) {
      // 后端从0开始，前端从1开始
      this.jobQueryContent.index = index - 1;
      this.listJobInfos();
    },
    // 点击重置按钮
    onClickReset() {
      this.jobQueryContent.keyword = undefined;
      this.jobQueryContent.jobId = undefined;
      this.listJobInfos();
    },
    verifyPlaceholder(processorType) {
      let res;
      switch (processorType) {
        case 'BUILT_IN':
          res = this.$t('message.javaProcessorInfoPLH');
          break;
        case 'EXTERNAL':
          res = this.$t('message.containerProcessorInfoPLH');
          break;
        case 'SHELL':
          res = this.$t('message.shellProcessorInfoPLH');
          break;
        case 'PYTHON':
          res = this.$t('message.pythonProcessorInfoPLH');
      }
      return res;
    },
    // 翻译执行类型
    translateExecuteType(executeType) {
      switch (executeType) {
        case 'STANDALONE':
          return this.$t('message.standalone');
        case 'BROADCAST':
          return this.$t('message.broadcast');
        case 'MAP_REDUCE':
          return this.$t('message.mapReduce');
        case 'MAP':
          return this.$t('message.map');
        default:
          return 'UNKNOWN';
      }
    },
    // 翻译处理器类型
    translateProcessorType(processorType) {
      if (processorType === 'EXTERNAL') {
        return this.$t('message.external');
      }
      return this.$t('message.builtIn');
    },
    // 点击校验
    onClickValidateTimeExpression() {
      this.timeExpressionValidatorVisible = true;
    },
    // 点击编辑
    onClickEditTimeExpression() {
      this.timeExpressionEditorVisible = true;
    },
    // 每日固定间隔策略的组件回调
    eventFromDailyTimeIntervalExpress(content) {
      console.log('event from dailyTimeIntervalExpress: ' + content);
      this.modifiedJobForm.timeExpression = content;
      this.timeExpressionEditorVisible = false;
    },

    // 任务导出按钮
    onClickJobExportButton(row) {
      this.jobExporterMode = 'EXPORT';
      this.jobExporterTargetId = row.id;
      this.jobExporterDialogVisible = true;
    },
    // 任务导入按钮
    onClickJobInputButton() {
      this.jobExporterMode = 'INPUT';
      this.jobExporterTargetId = undefined;
      this.jobExporterDialogVisible = true;
    },
    // 任务导出组件的回调
    eventFromExporter(content) {
      console.log('receive callback from Exporter: ' + content);
      this.jobExporterDialogVisible = false;
      if (this.jobExporterMode === 'INPUT') {
        this.listJobInfos();
      }
    },
  },
  mounted() {
    let that = this;
    // 加载用户信息
    jobAxios.get('/user/list').then((res) => {
      const data = res || [];
      that.userList = data.map((item) => {
        return {
          ...item,
          id: `${item.id}`,
        };
      });
    });
    // 加载任务信息
    this.listJobInfos();
  },
};
</script>

<style scoped>
#job_manager {
  padding: 20px;
}

.job-editor-number {
  display: flex;
}

.job-input-number {
  background-color: #f5f7fa;
  color: #909399;
  /* vertical-align: middle; */
  /* display: table-cell; */
  position: relative;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 0 20px;
  /* width: 1px; */
  white-space: nowrap;
  display: block;
  border-top-right-radius: 0px;
  border-bottom-right-radius: 0px;
  line-height: 38px;
  width: auto;
}

.el-input-number {
  width: 100px;
}

.el-input-number .el-input {
  width: 1000px;
}

.job_input,
.job_select,
.job_number {
  min-width: 160px;
  width: 160px;
}

::v-deep .el-form-item__content .job_number:first-child {
  margin-left: 0;
}

::v-deep .el-form-item__content .job_number {
  margin-left: 10px;
}

::v-deep .job_number .el-input-group__prepend {
  min-width: 100px;
}

::v-deep .job_number .el-input__inner {
  width: 60px;
}
::v-deep .el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
