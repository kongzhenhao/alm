import '@/assets/styles/index.scss';
import '@/assets/styles/lightning.scss';
import Vue from 'vue';
import Element from 'element-ui';
import './assets/styles/element-variables.scss';
// 全局设置 elementUI的$message的默认显示时间
const messages = ['success', 'warning', 'info', 'error'];
messages.forEach((type) => {
  Element.Message[type] = (options) => {
    if (typeof options === 'string') {
      options = {
        message: options,
      };
      options.duration = 1000;
      options.showClose = true;
    }
    options.type = type;
    return Element.Message(options);
  };
});
import Cookies from 'js-cookie';
Vue.use(Element, {
  size: Cookies.get('size') || 'medium',
});

// 自定义指令
import directive from './directive';
Vue.use(directive);
// 自定义插件
import plugins from './plugins';
Vue.use(plugins);
// 头部标签组件
import VueMeta from 'vue-meta';
Vue.use(VueMeta);

import './assets/icons';
import './permission';
import { getDicts } from '@/api/system/dict/data';
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  handleTree,
} from '@/utils/lightning';
import { startFlowable, reviewFlowable } from '@/utils/flowable.js';
import { download } from '@/utils/request';
// 全局方法挂载
Vue.prototype.getDicts = getDicts;
Vue.prototype.parseTime = parseTime;
Vue.prototype.resetForm = resetForm;
Vue.prototype.addDateRange = addDateRange;
Vue.prototype.selectDictLabel = selectDictLabel;
Vue.prototype.selectDictLabels = selectDictLabels;
Vue.prototype.handleTree = handleTree;
Vue.prototype.startFlowable = startFlowable;
Vue.prototype.reviewFlowable = reviewFlowable;
Vue.prototype.download = download;
// 分页组件
import Pagination from '@/components/Pagination';
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar';
// 富文本组件
import Editor from '@/components/Editor';
// 文件上传组件
import FileUpload from '@/components/FileUpload';
// 图片上传组件
import ImageUpload from '@/components/ImageUpload';
// 图片预览组件
import ImagePreview from '@/components/ImagePreview';
// 字典标签组件
import DictTag from '@/components/DictTag';
// 全局组件挂载
Vue.component('DictTag', DictTag);
Vue.component('Pagination', Pagination);
Vue.component('RightToolbar', RightToolbar);
Vue.component('Editor', Editor);
Vue.component('FileUpload', FileUpload);
Vue.component('ImageUpload', ImageUpload);
Vue.component('ImagePreview', ImagePreview);

// 字典数据组件
import DictData from '@/components/DictData';
DictData.install();

Vue.config.productionTip = false;

import App from './App';
import router from './router';
import store from './store';
import i18n from './i18n/i18n';
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: (h) => h(App),
});
