<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="精算代码" prop="actuarialCode">
        <el-input
          v-model="queryParams.actuarialCode"
          placeholder="请输入精算代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设计类型" prop="designType">
        <el-select v-model="queryParams.designType" placeholder="请选择设计类型" clearable>
          <el-option
            v-for="dict in dict.type.cost_design_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否中短" prop="shortTermFlag">
        <el-select v-model="queryParams.shortTermFlag" placeholder="请选择是否中短" clearable>
          <el-option label="是" value="Y" />
          <el-option label="否" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item label="业务代码" prop="businessCode">
        <el-input
          v-model="queryParams.businessCode"
          placeholder="请输入业务代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cost:product:effective:rate:add']"
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
          v-hasPermi="['cost:product:effective:rate:edit']"
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
          v-hasPermi="['cost:product:effective:rate:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cost:product:effective:rate:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['cost:product:effective:rate:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="精算代码" align="center" prop="actuarialCode" :show-overflow-tooltip="true" />
      <el-table-column label="设计类型" align="center" prop="designType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_design_type" :value="scope.row.designType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否中短" align="center" prop="shortTermFlag" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.shortTermFlag === 'Y' ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="业务代码" align="center" prop="businessCode" :show-overflow-tooltip="true" />
      <el-table-column label="产品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
      <el-table-column label="有效成本率" align="center" prop="effectiveCostRate" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ (scope.row.effectiveCostRate * 100).toFixed(6) + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现金流数据" align="center" prop="cashFlowSet" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="viewCashFlow(scope.row)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cost:product:effective:rate:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cost:product:effective:rate:remove']"
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

    <!-- 添加或修改分产品有效成本率对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="精算代码" prop="actuarialCode">
              <el-input v-model="form.actuarialCode" placeholder="请输入精算代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设计类型" prop="designType">
              <el-select v-model="form.designType" placeholder="请选择设计类型">
                <el-option
                  v-for="dict in dict.type.cost_design_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否中短" prop="shortTermFlag">
              <el-select v-model="form.shortTermFlag" placeholder="请选择是否中短">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="业务代码" prop="businessCode">
              <el-input v-model="form.businessCode" placeholder="请输入业务代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="有效成本率" prop="effectiveCostRate">
              <el-input-number v-model="form.effectiveCostRate" :precision="10" :step="0.000001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="现金流数据" prop="cashFlowSet">
              <el-input v-model="form.cashFlowSet" type="textarea" :rows="4" placeholder="请输入JSON格式的现金流数据" />
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

    <!-- 现金流查看对话框 -->
    <el-dialog title="现金流数据" :visible.sync="cashFlowOpen" width="600px" append-to-body>
      <el-table :data="cashFlowData" max-height="400">
        <el-table-column label="期数" align="center" prop="period" />
        <el-table-column label="日期" align="center" prop="date" />
        <el-table-column label="现金流值" align="center" prop="value" />
      </el-table>
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
  listProductEffectiveRate, 
  getProductEffectiveRate, 
  addProductEffectiveRate, 
  updateProductEffectiveRate, 
  delProductEffectiveRate, 
  exportProductEffectiveRate,
  importProductEffectiveRateTemplate,
  importProductEffectiveRate
} from "@/api/cost/productEffectiveRate";
import { getToken } from "@/utils/auth";

export default {
  name: "ProductEffectiveRate",
  dicts: ['cost_design_type'],
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
      // 分产品有效成本率表格数据
      rateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 现金流查看对话框
      cashFlowOpen: false,
      // 现金流数据
      cashFlowData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        actuarialCode: null,
        designType: null,
        shortTermFlag: null,
        businessCode: null,
        productName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式应为YYYYMM", trigger: "blur" }
        ],
        actuarialCode: [
          { required: true, message: "精算代码不能为空", trigger: "blur" }
        ],
        designType: [
          { required: true, message: "设计类型不能为空", trigger: "change" }
        ],
        shortTermFlag: [
          { required: true, message: "是否中短不能为空", trigger: "change" }
        ],
        businessCode: [
          { required: true, message: "业务代码不能为空", trigger: "blur" }
        ],
        productName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入分产品有效成本率数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/cost/product/effective/rate/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分产品有效成本率列表 */
    getList() {
      this.loading = true;
      listProductEffectiveRate(this.queryParams).then(response => {
        this.rateList = response.rows;
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
        actuarialCode: null,
        designType: null,
        shortTermFlag: "N",
        businessCode: null,
        productName: null,
        effectiveCostRate: 0,
        cashFlowSet: "{}",
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
      this.title = "添加分产品有效成本率";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getProductEffectiveRate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分产品有效成本率";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProductEffectiveRate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProductEffectiveRate(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除分产品有效成本率编号为"' + ids + '"的数据项？').then(function() {
        return delProductEffectiveRate(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有分产品有效成本率数据项？').then(() => {
        this.exportLoading = true;
        return exportProductEffectiveRate(this.queryParams);
      }).then(response => {
        this.$download.excel(response, '分产品有效成本率数据.xlsx');
        this.exportLoading = false;
      }).catch(() => {});
    },
    /** 查看现金流数据 */
    viewCashFlow(row) {
      try {
        const cashFlowObj = JSON.parse(row.cashFlowSet || '{}');
        this.cashFlowData = Object.keys(cashFlowObj).map(key => ({
          period: key,
          date: cashFlowObj[key].日期 || '',
          value: cashFlowObj[key].值 || cashFlowObj[key]
        }));
        this.cashFlowOpen = true;
      } catch (e) {
        this.$modal.msgError("现金流数据格式错误");
      }
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importProductEffectiveRateTemplate().then(response => {
        this.$download.excel(response, '分产品有效成本率数据模板.xlsx');
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
