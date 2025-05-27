<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期，格式YYYYMM"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当季第几月" prop="monthSeqInCurrQuarter">
        <el-select
          v-model="queryParams.monthSeqInCurrQuarter"
          placeholder="请选择当季第几月"
          clearable
          style="width: 200px"
        >
          <el-option label="1" :value="1" />
          <el-option label="2" :value="2" />
          <el-option label="3" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['base:renewal:eval:month:cfg:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['base:renewal:eval:month:cfg:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['base:renewal:eval:month:cfg:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:renewal:eval:month:cfg:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['base:renewal:eval:month:cfg:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="renewalEvalMonthCfgList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="120" />
      <el-table-column label="当季第几月" align="center" prop="monthSeqInCurrQuarter" width="120" />
      <el-table-column label="评估时点月份" align="center" prop="evalPointMonth" width="120" />
      <el-table-column label="上年同比月份" align="center" prop="monthOfLastYear" width="120" />
      <el-table-column label="当季月份" align="center" prop="monthInCurrQuarter" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:renewal:eval:month:cfg:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:renewal:eval:month:cfg:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改续保率评估月份配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="账期" prop="accountingPeriod">
          <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="当季第几月" prop="monthSeqInCurrQuarter">
          <el-select v-model="form.monthSeqInCurrQuarter" placeholder="请选择当季第几月">
            <el-option label="1" :value="1" />
            <el-option label="2" :value="2" />
            <el-option label="3" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估时点月份" prop="evalPointMonth">
          <el-input v-model="form.evalPointMonth" placeholder="请输入评估时点月份，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="上年同比月份" prop="monthOfLastYear">
          <el-input v-model="form.monthOfLastYear" placeholder="请输入上年同比月份，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="当季月份" prop="monthInCurrQuarter">
          <el-input v-model="form.monthInCurrQuarter" placeholder="请输入当季月份，格式YYYYMM" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="downloadTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRenewalEvalMonthCfg,
  getRenewalEvalMonthCfg,
  addRenewalEvalMonthCfg,
  updateRenewalEvalMonthCfg,
  delRenewalEvalMonthCfg,
  exportRenewalEvalMonthCfg,
  downloadTemplate
} from "@/api/base/renewalEvalMonthCfg";
import { getToken } from "@/utils/auth";

export default {
  name: "RenewalEvalMonthCfg",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 续保率评估月份配置表格数据
      renewalEvalMonthCfgList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        monthSeqInCurrQuarter: null,
        evalPointMonth: null,
        monthOfLastYear: null,
        monthInCurrQuarter: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        monthSeqInCurrQuarter: [
          { required: true, message: "当季第几月不能为空", trigger: "change" }
        ],
        evalPointMonth: [
          { required: true, message: "评估时点月份不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "评估时点月份格式必须为YYYYMM", trigger: "blur" }
        ],
        monthOfLastYear: [
          { required: true, message: "上年同比月份不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "上年同比月份格式必须为YYYYMM", trigger: "blur" }
        ],
        monthInCurrQuarter: [
          { required: true, message: "当季月份不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "当季月份格式必须为YYYYMM", trigger: "blur" }
        ]
      },
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/base/renewal/eval/month/cfg/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询续保率评估月份配置列表 */
    getList() {
      this.loading = true;
      listRenewalEvalMonthCfg(this.queryParams).then(response => {
        this.renewalEvalMonthCfgList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        accountingPeriod: null,
        monthSeqInCurrQuarter: null,
        evalPointMonth: null,
        monthOfLastYear: null,
        monthInCurrQuarter: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加续保率评估月份配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getRenewalEvalMonthCfg(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改续保率评估月份配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRenewalEvalMonthCfg(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRenewalEvalMonthCfg(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除续保率评估月份配置编号为"' + ids + '"的数据项？').then(() => {
        return delRenewalEvalMonthCfg(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'base/renewal/eval/month/cfg/export',
        {
          ...this.queryParams
        },
        `renewal_eval_month_cfg_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "续保率评估月份配置导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    downloadTemplate() {
      downloadTemplate().then(response => {
        this.downloadFile(response, `renewal_eval_month_cfg_template_${new Date().getTime()}.xlsx`);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 下载文件
    downloadFile(response, fileName) {
      const blob = new Blob([response], { type: 'application/vnd.ms-excel' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      URL.revokeObjectURL(link.href);
    }
  }
};
</script>
