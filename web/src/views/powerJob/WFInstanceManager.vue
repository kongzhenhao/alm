<template>
  <div class="app-container" id="wf_instance_manager">
    <el-form
      :inline="true"
      :model="wfInstanceQueryContent"
      class="el-form--inline"
      size="small"
    >
      <el-form-item :label="$t('message.wfId')">
        <el-input
          v-model="wfInstanceQueryContent.workflowId"
          type="number"
          :placeholder="$t('message.wfId')"
        />
      </el-form-item>
      <el-form-item :label="$t('message.wfInstanceId')">
        <el-input
          v-model="wfInstanceQueryContent.wfInstanceId"
          type="number"
          :placeholder="$t('message.wfInstanceId')"
        />
      </el-form-item>

      <el-form-item :label="$t('message.status')">
        <el-select
          v-model="wfInstanceQueryContent.status"
          :placeholder="$t('message.status')"
        >
          <el-option
            v-for="item in wfInstanceStatusOptions"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <div class="search-btns">
      <el-button class="cancelBtn" @click="onClickRest">
        {{ $t('message.reset') }}
      </el-button>
      <el-button class="confirmBtn" type="primary" @click="listWfInstances">
        {{ $t('message.query') }}
      </el-button>
    </div>

    <el-card class="table-wrap">
      <div class="title-wrap">
        <h4>工作流记录</h4>
      </div>
      <el-table
        :data="wfInstancePageResult.data"
        :row-class-name="wfInstanceTableRowClassName"
        size="small"
        style="width: 100%"
      >
        <el-table-column
          :label="$t('message.wfId')"
          :show-overflow-tooltip="true"
          prop="workflowId"
          width="110"
        />
        <el-table-column
          :label="$t('message.wfName')"
          :show-overflow-tooltip="true"
          prop="workflowName"
        />
        <el-table-column
          :label="$t('message.wfInstanceId')"
          :show-overflow-tooltip="true"
          prop="wfInstanceId"
        />
        <el-table-column
          :label="$t('message.status')"
          :show-overflow-tooltip="true"
          prop="status"
          width="160"
        >
          <template slot-scope="scope">
            {{ fetchWFStatus(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('message.triggerTime')"
          :show-overflow-tooltip="true"
          prop="actualTriggerTime"
        />
        <el-table-column
          :label="$t('message.finishedTime')"
          :show-overflow-tooltip="true"
          prop="finishedTime"
        />

        <el-table-column
          :label="$t('message.operation')"
          :show-overflow-tooltip="true"
          width="225"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="onClickShowDetail(scope.row)"
            >
              {{ $t('message.detail') }}
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="onClickStop(scope.row)"
            >
              {{ $t('message.stop') }}
            </el-button>
            <el-button size="mini" type="warning" @click="restart(scope.row)">
              {{ $t('message.reRun') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :page-size="this.wfInstancePageResult.pageSize"
        :total="this.wfInstancePageResult.totalItems"
        background
        layout="prev, pager, next"
        @current-change="onClickChangeInstancePage"
      />
    </el-card>
  </div>
</template>

<script>
import common from '@/utils/common';
import { Message } from 'element-ui';
import axios from 'axios';

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
  name: 'WFInstanceManager',
  data() {
    return {
      // 查询条件
      wfInstanceQueryContent: {
        appId: this.$store.state.appId,
        index: 0,
        pageSize: 10,
        wfInstanceId: undefined,
        workflowId: undefined,
        status: '',
      },
      // 查询结果
      wfInstancePageResult: {
        pageSize: 10,
        totalItems: 0,
        data: [],
      },
      // 工作流实例状态选择
      wfInstanceStatusOptions: [
        {
          key: '',
          label: this.$t('message.all'),
        },
        {
          key: 'WAITING',
          label: this.$t('message.waitingDispatch'),
        },
        {
          key: 'RUNNING',
          label: this.$t('message.running'),
        },
        {
          key: 'FAILED',
          label: this.$t('message.failed'),
        },
        {
          key: 'SUCCEED',
          label: this.$t('message.success'),
        },
        {
          key: 'STOPPED',
          label: this.$t('message.stopped'),
        },
      ],
    };
  },
  methods: {
    listWfInstances() {
      let that = this;
      jobAxios
        .post('/wfInstance/list', this.wfInstanceQueryContent)
        .then((res) => (that.wfInstancePageResult = res));
    },
    // 重置搜索条件
    onClickRest() {
      this.wfInstanceQueryContent.wfInstanceId = undefined;
      this.wfInstanceQueryContent.workflowId = undefined;
      this.wfInstanceQueryContent.status = '';
      this.listWfInstances();
    },
    // 查看工作流详情
    onClickShowDetail(data) {
      this.$router.push({
        path: '/task/job/wfInstanceDetail',
        query: {
          wfInstanceId: data.wfInstanceId,
        },
      });
      // this.$router.push({path:'/data/2/wfInstanceDetail'})
    },

    // 停止工作流
    onClickStop(data) {
      let that = this;
      let url =
        '/wfInstance/stop?wfInstanceId=' +
        data.wfInstanceId +
        '&appId=' +
        this.$store.state.appId;
      jobAxios.get(url).then(() => {
        that.$message.success(this.$t('message.success'));
        // 重新加载列表
        that.listInstanceInfos();
      });
    },
    // 换页
    onClickChangeInstancePage(index) {
      // 后端从0开始，前端从1开始
      this.wfInstanceQueryContent.index = index - 1;
      this.listWfInstances();
    },
    // 表单颜色
    wfInstanceTableRowClassName({ row }) {
      switch (row.status) {
        // 失败
        case 3:
          return 'error-row';
        // 成功
        case 4:
          return 'success-row';
        case 10:
          return 'warning-row';
      }
    },
    fetchWFStatus(status) {
      return common.translateWfInstanceStatus(status);
    },
    // 重试
    async restart(row) {
      const data = {
        appId: this.wfInstanceQueryContent.appId,
        wfInstanceId: row.wfInstanceId,
      };
      await jobAxios.get('/wfInstance/retry', {
        params: data,
      });
      this.listWfInstances();
    },
  },
  mounted() {
    this.listWfInstances();
  },
};
</script>

<style>
#wf_instance_manager {
  padding: 20px;
}

/* svg {
  font-size: 10px;
  border: 1px solid red;
} */

text {
  font-weight: 300;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 14px;
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
</style>
<style scoped>
::v-deep .el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
