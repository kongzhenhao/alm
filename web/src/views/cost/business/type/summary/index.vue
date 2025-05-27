<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="情景名称" prop="scenarioName">
        <el-select v-model="queryParams.scenarioName" placeholder="请选择情景名称" clearable>
          <el-option
            v-for="dict in dict.type.cost_scenario_name"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="业务类型" prop="businessType">
        <el-select v-model="queryParams.businessType" placeholder="请选择业务类型" clearable>
          <el-option label="有效业务" value="有效业务" />
          <el-option label="新业务" value="新业务" />
        </el-select>
      </el-form-item>
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设计类型" prop="designType">
        <el-select v-model="queryParams.designType" placeholder="请选择设计类型" clearable>
          <el-option label="传统险" value="传统险" />
          <el-option label="分红险" value="分红险" />
          <el-option label="万能险" value="万能险" />
          <el-option label="投连险" value="投连险" />
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
          v-hasPermi="['cost:business:type:summary:add']"
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
          v-hasPermi="['cost:business:type:summary:edit']"
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
          v-hasPermi="['cost:business:type:summary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cost:business:type:summary:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['cost:business:type:summary:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="summaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="情景名称" align="center" prop="scenarioName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_scenario_name" :value="scope.row.scenarioName"/>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" align="center" prop="businessType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_business_type" :value="scope.row.businessType"/>
        </template>
      </el-table-column>
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="设计类型" align="center" prop="designType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_design_type" :value="scope.row.designType"/>
        </template>
      </el-table-column>
      <el-table-column label="法定准备金T0" align="center" prop="statutoryReserveT0" :show-overflow-tooltip="true" />
      <el-table-column label="法定准备金T1" align="center" prop="statutoryReserveT1" :show-overflow-tooltip="true" />
      <el-table-column label="资金成本率T0" align="center" prop="fundCostRateT0" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ (scope.row.fundCostRateT0 * 100).toFixed(2) + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资金成本率T1" align="center" prop="fundCostRateT1" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ (scope.row.fundCostRateT1 * 100).toFixed(2) + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保证成本率T0" align="center" prop="guaranteedCostRateT0" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ (scope.row.guaranteedCostRateT0 * 100).toFixed(2) + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保证成本率T1" align="center" prop="guaranteedCostRateT1" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ (scope.row.guaranteedCostRateT1 * 100).toFixed(2) + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cost:business:type:summary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cost:business:type:summary:remove']"
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

    <!-- 添加或修改分业务类型汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="情景名称" prop="scenarioName">
              <el-select v-model="form.scenarioName" placeholder="请选择情景名称">
                <el-option
                  v-for="dict in dict.type.cost_scenario_name"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="form.businessType" placeholder="请选择业务类型">
                <el-option label="有效业务" value="有效业务" />
                <el-option label="新业务" value="新业务" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设计类型" prop="designType">
              <el-select v-model="form.designType" placeholder="请选择设计类型">
                <el-option label="传统险" value="传统险" />
                <el-option label="分红险" value="分红险" />
                <el-option label="万能险" value="万能险" />
                <el-option label="投连险" value="投连险" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">准备金数据</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="法定准备金T0" prop="statutoryReserveT0">
              <el-input-number v-model="form.statutoryReserveT0" :precision="2" :step="1000" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法定准备金T1" prop="statutoryReserveT1">
              <el-input-number v-model="form.statutoryReserveT1" :precision="2" :step="1000" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="法定准备金T2" prop="statutoryReserveT2">
              <el-input-number v-model="form.statutoryReserveT2" :precision="2" :step="1000" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法定准备金T3" prop="statutoryReserveT3">
              <el-input-number v-model="form.statutoryReserveT3" :precision="2" :step="1000" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">成本率数据</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="资金成本率T0" prop="fundCostRateT0">
              <el-input-number v-model="form.fundCostRateT0" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资金成本率T1" prop="fundCostRateT1">
              <el-input-number v-model="form.fundCostRateT1" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="资金成本率T2" prop="fundCostRateT2">
              <el-input-number v-model="form.fundCostRateT2" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资金成本率T3" prop="fundCostRateT3">
              <el-input-number v-model="form.fundCostRateT3" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保证成本率T0" prop="guaranteedCostRateT0">
              <el-input-number v-model="form.guaranteedCostRateT0" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保证成本率T1" prop="guaranteedCostRateT1">
              <el-input-number v-model="form.guaranteedCostRateT1" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保证成本率T2" prop="guaranteedCostRateT2">
              <el-input-number v-model="form.guaranteedCostRateT2" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保证成本率T3" prop="guaranteedCostRateT3">
              <el-input-number v-model="form.guaranteedCostRateT3" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
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
        <el-checkbox v-model="upload.updateSupport" />更新已经存在的数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listBusinessTypeSummary,
  getBusinessTypeSummary,
  addBusinessTypeSummary,
  updateBusinessTypeSummary,
  delBusinessTypeSummary,
  exportBusinessTypeSummary,
  importBusinessTypeSummaryTemplate,
  importBusinessTypeSummary
} from "@/api/cost/businessTypeSummary";
import { getToken } from "@/utils/auth";

export default {
  name: "BusinessTypeSummary",
  dicts: ['cost_business_type', 'cost_design_type', 'cost_scenario_name'],
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
      // 分业务类型汇总表格数据
      summaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        scenarioName: null,
        businessType: null,
        accountingPeriod: null,
        designType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        scenarioName: [
          { required: true, message: "情景名称不能为空", trigger: "blur" }
        ],
        businessType: [
          { required: true, message: "业务类型不能为空", trigger: "change" }
        ],
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式应为YYYYMM", trigger: "blur" }
        ],
        designType: [
          { required: true, message: "设计类型不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入分业务类型汇总数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/cost/business/type/summary/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分业务类型汇总列表 */
    getList() {
      this.loading = true;
      listBusinessTypeSummary(this.queryParams).then(response => {
        this.summaryList = response.rows;
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
        scenarioName: null,
        businessType: null,
        accountingPeriod: null,
        designType: null,
        statutoryReserveT0: 0,
        statutoryReserveT1: 0,
        statutoryReserveT2: 0,
        statutoryReserveT3: 0,
        fundCostRateT0: 0,
        fundCostRateT1: 0,
        fundCostRateT2: 0,
        fundCostRateT3: 0,
        guaranteedCostRateT0: 0,
        guaranteedCostRateT1: 0,
        guaranteedCostRateT2: 0,
        guaranteedCostRateT3: 0,
        remark: null
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
      this.title = "添加分业务类型汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getBusinessTypeSummary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分业务类型汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBusinessTypeSummary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBusinessTypeSummary(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除分业务类型汇总编号为"' + ids + '"的数据项？').then(function() {
        return delBusinessTypeSummary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有分业务类型汇总数据项？').then(() => {
        this.exportLoading = true;
        return exportBusinessTypeSummary(this.queryParams);
      }).then(response => {
        this.$download.excel(response, '分业务类型汇总数据.xlsx');
        this.exportLoading = false;
      }).catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importBusinessTypeSummaryTemplate().then(response => {
        this.$download.excel(response, '分业务类型汇总数据模板.xlsx');
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
    }
  }
};
</script>
