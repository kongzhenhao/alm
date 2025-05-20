<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
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
        <el-input
          v-model="queryParams.monthSeqInCurrQuarter"
          placeholder="请输入当季第几月"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="渠道类型编码" prop="channelTypeCode">
        <el-input
          v-model="queryParams.channelTypeCode"
          placeholder="请输入渠道类型编码"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上年同比月份" prop="monthOfLastYear">
        <el-input
          v-model="queryParams.monthOfLastYear"
          placeholder="请输入上年同比月份，格式YYYYMM"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['insu:renewal:rate:stats:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleCalc"
          v-hasPermi="['insu:renewal:rate:stats:calc']"
        >统计计算</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="renewalRateStatsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="当季第几月" align="center" prop="monthSeqInCurrQuarter" width="100" />
      <el-table-column label="渠道类型编码" align="center" prop="channelTypeCode" width="120" />
      <el-table-column label="上年同比月份" align="center" prop="monthOfLastYear" width="120" />
      <el-table-column label="上年度有效保单数量" align="center" prop="validPolicyCntLastYear" width="150" />
      <el-table-column label="本年度有效保单数量" align="center" prop="validPolicyCntCurrYear" width="150" />
      <el-table-column label="保单续保率" align="center" prop="policyRenewalRate" width="120">
        <template slot-scope="scope">
          <span>{{ (scope.row.policyRenewalRate * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
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
            v-hasPermi="['insu:renewal:rate:stats:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['insu:renewal:rate:stats:remove']"
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

    <!-- 添加或修改保单续保率统计对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="账期" prop="accountingPeriod">
          <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="当季第几月" prop="monthSeqInCurrQuarter">
          <el-input-number v-model="form.monthSeqInCurrQuarter" :min="1" :max="12" :precision="0" />
        </el-form-item>
        <el-form-item label="渠道类型编码" prop="channelTypeCode">
          <el-input v-model="form.channelTypeCode" placeholder="请输入渠道类型编码" />
        </el-form-item>
        <el-form-item label="上年同比月份" prop="monthOfLastYear">
          <el-input v-model="form.monthOfLastYear" placeholder="请输入上年同比月份，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="上年度有效保单数量" prop="validPolicyCntLastYear">
          <el-input-number v-model="form.validPolicyCntLastYear" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item label="本年度有效保单数量" prop="validPolicyCntCurrYear">
          <el-input-number v-model="form.validPolicyCntCurrYear" :min="0" :precision="0" />
        </el-form-item>
        <el-form-item label="保单续保率" prop="policyRenewalRate">
          <el-input-number v-model="form.policyRenewalRate" :precision="4" :step="0.01" :min="0" :max="1" />
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
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
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
  listRenewalRateStats,
  getRenewalRateStats,
  addRenewalRateStats,
  updateRenewalRateStats,
  delRenewalRateStats,
  exportRenewalRateStats,
  importTemplateRenewalRateStats,
  calcRenewalRateStats
} from "@/api/insu/renewalRateStats";
import { getToken } from "@/utils/auth";

export default {
  name: "RenewalRateStats",
  dicts: [],
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
      // 保单续保率统计表格数据
      renewalRateStatsList: [],
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
        channelTypeCode: null,
        monthOfLastYear: null,
        validPolicyCntLastYear: null,
        validPolicyCntCurrYear: null,
        policyRenewalRate: null
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
          { required: true, message: "当季第几月不能为空", trigger: "blur" }
        ],
        channelTypeCode: [
          { required: true, message: "渠道类型编码不能为空", trigger: "blur" },
          { max: 20, message: "渠道类型编码长度不能超过20个字符", trigger: "blur" }
        ],
        monthOfLastYear: [
          { required: true, message: "上年同比月份不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "上年同比月份格式必须为YYYYMM", trigger: "blur" }
        ],
        validPolicyCntLastYear: [
          { required: true, message: "上年度有效保单数量不能为空", trigger: "blur" }
        ],
        validPolicyCntCurrYear: [
          { required: true, message: "本年度有效保单数量不能为空", trigger: "blur" }
        ],
        policyRenewalRate: [
          { required: true, message: "保单续保率不能为空", trigger: "blur" }
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
        url: process.env.VUE_APP_BASE_API + "/insu/renewal/rate/stats/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询保单续保率统计列表 */
    getList() {
      this.loading = true;
      listRenewalRateStats(this.queryParams).then(response => {
        this.renewalRateStatsList = response.rows;
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
        channelTypeCode: null,
        monthOfLastYear: null,
        validPolicyCntLastYear: null,
        validPolicyCntCurrYear: null,
        policyRenewalRate: null
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
      this.title = "添加保单续保率统计";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getRenewalRateStats(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改保单续保率统计";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRenewalRateStats(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRenewalRateStats(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除保单续保率统计编号为"' + ids + '"的数据项？').then(() => {
        return delRenewalRateStats(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'insu/renewal/rate/stats/export',
        {
          ...this.queryParams
        },
        `renewal_rate_stats_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "保单续保率统计导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplateRenewalRateStats().then(response => {
        this.download(response.msg);
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
    /** 统计计算按钮操作 */
    handleCalc() {
      if (!this.queryParams.accountingPeriod) {
        this.$modal.msgError("请先输入账期");
        return;
      }
      this.$modal.confirm('是否确认对账期"' + this.queryParams.accountingPeriod + '"进行续保率统计计算？').then(() => {
        this.loading = true;
        return calcRenewalRateStats(this.queryParams.accountingPeriod);
      }).then(response => {
        this.getList();
        this.$modal.msgSuccess("统计计算成功");
      }).catch(() => {
        this.loading = false;
      });
    }
  }
};
</script>
