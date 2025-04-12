<template>
  <div>
    <el-input
      type="textarea"
      placeholder="请输入内容"
      v-model="jsonContent"
      :autosize="{ minRows: 8, maxRows: 256 }"
      :disabled="mode === 'EXPORT'"
      size="small"
    ></el-input>

    <div style="margin-top: 20px">
      <el-button size="small" type="info" @click="onClickCancelButton">
        {{ $t('message.cancel') }}
      </el-button>
      <el-button size="small" type="primary" @click="onClickConfirmButton">
        {{ $t('message.confirm') }}
      </el-button>
    </div>
  </div>
</template>

<script>
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
  name: 'Exporter',
  // 数据传递
  props: [
    'type', // 类型，JOB 代表任务的导入导出，WORKFLOW 代表工作流的导入导出
    'mode', // EXPORT or INPUT
    'targetId', // export 模式有效，目标ID
  ],
  data() {
    return {
      jsonContent: undefined,
    };
  },
  methods: {
    notifyParent() {
      this.$emit('finished', 'ok');
    },

    fetchExportInfo(type, targetId) {
      let api = '/job/export?jobId=' + targetId;
      if (type === 'WORKFLOW') {
        api = '/workflow/export?workflowId=' + targetId;
      }
      let that = this;
      jobAxios.get(api).then((res) => {
        console.log('[Exporter] query export result: ' + JSON.stringify(res));
        that.jsonContent = JSON.stringify(res);
      });
    },
    input() {
      console.log('[Exporter] try to input by content: ' + this.jsonContent);
      if (this.jsonContent === undefined || this.jsonContent.length === 0) {
        return;
      }
      jobAxios.post('/job/save', JSON.parse(this.jsonContent)).then();
    },

    onClickCancelButton() {
      this.notifyParent();
    },

    onClickConfirmButton() {
      if (this.mode === 'INPUT') {
        this.input();
      }
      this.notifyParent();
    },
  },
  mounted() {
    console.log(
      '[Exporter] mounted Exporter with params, type=%s, mode=%s, targetId=%s',
      this.type,
      this.mode,
      this.targetId
    );
    if (this.mode === 'EXPORT') {
      this.fetchExportInfo(this.type, this.targetId);
    }
  },
};
</script>

<style scoped></style>
