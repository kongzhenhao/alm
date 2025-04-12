<template>
  <div class="app-container" id="workflow_manager">
    <el-form
      :inline="true"
      :model="workflowQueryContent"
      class="el-form--inline"
      size="small"
    >
      <el-form-item :label="$t('message.wfId')">
        <el-input
          v-model="workflowQueryContent.workflowId"
          type="number"
          :placeholder="$t('message.wfId')"
        />
      </el-form-item>
      <el-form-item :label="$t('message.keyword')">
        <el-input
          v-model="workflowQueryContent.keyword"
          :placeholder="$t('message.keyword')"
        />
      </el-form-item>
    </el-form>

    <div class="search-btns">
      <el-button class="cancelBtn" @click="onClickReset">
        {{ $t('message.reset') }}
      </el-button>
      <el-button class="confirmBtn" type="primary" @click="listWorkflow">
        {{ $t('message.query') }}
      </el-button>
    </div>

    <!--第二行，工作流数据表格-->
    <el-card class="table-wrap">
      <div class="title-wrap">
        <h4>工作流配置</h4>
        <div class="btns">
          <el-button
            class="button add"
            size="small"
            type="primary"
            @click="onClickNewWorkflow"
          >
            {{ $t('message.newWorkflow') }}
          </el-button>
        </div>
      </div>
      <el-table
        :data="workflowPageResult.data"
        :type="isWorkflow ? 'selection' : null"
        size="”small“"
        style="width: 100%"
      >
        <el-table-column
          :label="$t('message.wfId')"
          :show-overflow-tooltip="true"
          prop="id"
          width="120"
        />
        <el-table-column
          :label="$t('message.wfName')"
          :show-overflow-tooltip="true"
          prop="wfName"
        />
        <el-table-column
          :label="$t('message.scheduleInfo')"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            {{ scope.row.timeExpressionType }} {{ scope.row.timeExpression }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="!isWorkflow"
          :label="$t('message.status')"
          :show-overflow-tooltip="true"
          width="80"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="switchWorkflow(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('message.operation')"
          :show-overflow-tooltip="true"
          :width="isWorkflow ? 100 : 300"
        >
          <template slot-scope="scope">
            <div v-if="!isWorkflow">
              <el-button size="mini" @click="onClickModifyWorkflow(scope.row)">
                {{ $t('message.edit') }}
              </el-button>
              <el-button
                :loading="copyLoading"
                size="mini"
                @click="onClickCopy(scope.row)"
              >
                {{ $t('message.copy') }}
              </el-button>
              <el-dropdown>
                <el-button
                  :style="{ marginRight: '10px', marginLeft: '10px' }"
                  size="mini"
                  @click="onClickRunWorkflow(scope.row)"
                >
                  {{ $t('message.run') }}
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
                </el-dropdown-menu>
              </el-dropdown>
              <el-button
                size="mini"
                type="danger"
                @click="onClickDeleteWorkflow(scope.row)"
              >
                {{ $t('message.delete') }}
              </el-button>
            </div>
            <div v-if="isWorkflow">
              <el-button size="mini" @click="onImportNode(scope.row)">
                引入
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :hide-on-single-page="true"
        :page-size="this.workflowPageResult.pageSize"
        :total="this.workflowPageResult.totalItems"
        background
        layout="prev, pager, next"
        @current-change="onClickChangePage"
      />
    </el-card>

    <el-dialog
      :before-close="optionClose"
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
        <el-button size="small" @click="onClickRunCancel">
          {{ $t('message.cancel') }}
        </el-button>
        <el-button
          :loading="runLoading"
          size="small"
          type="primary"
          @click="onClickRunWorkflow(temporaryRowData)"
        >
          {{ $t('message.run') }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
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
  name: 'WorkflowManager',
  props: ['isWorkflow'],
  data() {
    return {
      // 查询条件
      workflowQueryContent: {
        appId: this.$store.state.appId,
        index: 0,
        pageSize: 10,
        workflowId: undefined,
        keyword: undefined,
      },
      // 工作流查询结果
      workflowPageResult: {
        pageSize: 10,
        totalItems: 0,
        data: [],
      },
      // 复制loading
      copyLoading: false,
      // 新建工作流对象
      workflowObj: {},
      temporaryRowData: null,
      // 运行参数
      runParameter: null,
      // 运行loading
      runLoading: false,
    };
  },
  methods: {
    // 查询工作流
    listWorkflow() {
      const that = this;
      jobAxios.post('/workflow/list', this.workflowQueryContent).then((res) => {
        that.workflowPageResult = res;
      });
    },
    /** 引入嵌套工作流节点 */
    onImportNode(data) {
      this.$emit('onImportNode', data);
    },
    // 点击重置
    onClickReset() {
      this.workflowQueryContent.workflowId = undefined;
      this.workflowQueryContent.keyword = undefined;
      this.listWorkflow();
    },
    // 开关工作流
    switchWorkflow(data) {
      let that = this;
      let path = data.enable ? 'enable' : 'disable';
      let url =
        '/workflow/' +
        path +
        '?appId=' +
        this.$store.state.appId +
        '&workflowId=' +
        data.id;
      jobAxios.get(url, (res) => {
        console.log(res);
        that.listWorkflow();
      });
    },
    // 编辑工作流
    onClickModifyWorkflow(data) {
      this.$router.push({
        name: 'WorkflowEditor',
        params: {
          modify: true,
          workflowInfo: data,
        },
      });
    },
    // 立即运行工作流
    onClickRunWorkflow(data) {
      let that = this;
      let url =
        '/workflow/run?appId=' +
        this.$store.state.appId +
        '&workflowId=' +
        data.id;
      if (this.temporaryRowData && this.runParameter) {
        url += `&initParams=${encodeURIComponent(this.runParameter)}`;
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
    optionClose() {
      this.onClickRunCancel();
    },
    // 取消参数运行
    onClickRunCancel() {
      this.temporaryRowData = null;
      this.runParameter = null;
    },
    // 删除工作流
    onClickDeleteWorkflow(data) {
      let that = this;
      let url =
        '/workflow/delete?appId=' +
        this.$store.state.appId +
        '&workflowId=' +
        data.id;
      jobAxios.get(url).then(() => {
        that.$message.success(this.$t('message.success'));
        that.listWorkflow();
      });
    },
    // 新建工作流
    onClickNewWorkflow() {
      this.$router.push({
        name: 'WorkflowEditor',
        params: {
          modify: false,
        },
      });
    },
    // 点击换页
    onClickChangePage(index) {
      // 后端从0开始，前端从1开始
      this.workflowQueryContent.index = index - 1;
      this.listWorkflow();
    },
    /** 复制工作流 */
    onClickCopy(data) {
      this.copyLoading = true;
      jobAxios
        .post(
          `/workflow/copy?workflowId=${data.id}&appId=${this.workflowQueryContent.appId}`
        )
        .then((res) => {
          this.$router.push({
            name: 'WorkflowEditor',
            params: {
              modify: true,
              workflowInfo: {
                ...data,
                id: res,
              },
            },
          });
          this.copyLoading = false;
          this.$message.success(this.$t('message.success'));
        })
        .catch(() => {
          this.copyLoading = false;
        });
    },
  },
  mounted() {
    this.listWorkflow();
  },
};
</script>

<style scoped>
#workflow_manager {
  padding: 20px;
}
::v-deep .el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
