<template>
  <div>
    <el-card class="box-card">
      <div v-for="res in nextNTriggerTime" :key="res" class="text item">
        {{ res }}
      </div>
    </el-card>
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
  name: 'TimeExpressionValidator',
  // 数据传递
  props: ['timeExpressionType', 'timeExpression'],
  data() {
    return {
      nextNTriggerTime: [],
    };
  },
  methods: {
    checkTimeExpression() {
      let that = this;
      let url =
        '/validate/timeExpression?timeExpressionType=' +
        this.timeExpressionType +
        '&timeExpression=' +
        encodeURIComponent(this.timeExpression);
      jobAxios.get(url).then((res) => (that.nextNTriggerTime = res));
    },
  },
  mounted() {
    console.log('type:' + this.timeExpressionType);
    console.log('expression:' + this.timeExpression);
    console.log('expressionAfterEncodeURIComponent: ' + this.timeExpression);
    this.checkTimeExpression();
  },
};
</script>

<style scoped></style>
