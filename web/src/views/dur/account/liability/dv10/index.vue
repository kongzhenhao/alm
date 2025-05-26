<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账期" prop="accountPeriod">
        <el-input
          v-model="queryParams.accountPeriod"
          placeholder="请输入账期，格式YYYYMM"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="现金流类型" prop="cashFlowType">
        <el-select
          v-model="queryParams.cashFlowType"
          placeholder="请选择现金流类型"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in dict.type.dur_cash_flow_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设计类型" prop="designType">
        <el-select
          v-model="queryParams.designType"
          placeholder="请选择设计类型"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="(label, value) in designTypeOptions"
            :key="value"
            :label="label"
            :value="value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="现值类型" prop="valueType">
        <el-select
          v-model="queryParams.valueType"
          placeholder="请选择现值类型"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in dict.type.dur_value_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          v-hasPermi="['dur:account:liability:dv10:add']"
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
          v-hasPermi="['dur:account:liability:dv10:edit']"
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
          v-hasPermi="['dur:account:liability:dv10:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:account:liability:dv10:export']"
        >导出</el-button>
      </el-col>
      <!-- 导入按钮已隐藏 -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountLiabilityDv10List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" width="100" />
      <el-table-column label="现金流类型" align="center" prop="cashFlowType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_cash_flow_type" :value="scope.row.cashFlowType"/>
        </template>
      </el-table-column>
      <el-table-column label="设计类型" align="center" prop="designType" width="100">
        <template slot-scope="scope">
          <span>{{ getDesignTypeLabel(scope.row.designType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现值类型" align="center" prop="valueType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_value_type" :value="scope.row.valueType"/>
        </template>
      </el-table-column>
      <el-table-column label="期限点值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewTermValues(scope.row)"
          >查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['dur:account:liability:dv10:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:account:liability:dv10:remove']"
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

    <!-- 添加或修改分账户负债基点价值DV10对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountPeriod">
              <el-input v-model="form.accountPeriod" placeholder="请输入账期，格式YYYYMM" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="现金流类型" prop="cashFlowType">
              <el-select v-model="form.cashFlowType" placeholder="请选择现金流类型">
                <el-option
                  v-for="dict in dict.type.dur_cash_flow_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设计类型" prop="designType">
              <el-select v-model="form.designType" placeholder="请选择设计类型">
                <el-option
                  v-for="(label, value) in designTypeOptions"
                  :key="value"
                  :label="label"
                  :value="value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="现值类型" prop="valueType">
              <el-select v-model="form.valueType" placeholder="请选择现值类型">
                <el-option
                  v-for="dict in dict.type.dur_value_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">期限点值</el-divider>
        <el-row>
          <el-col :span="8">
            <el-form-item label="0年期限点" prop="term0">
              <el-input v-model="form.term0" placeholder="请输入0年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="0.5年期限点" prop="term05">
              <el-input v-model="form.term05" placeholder="请输入0.5年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="1年期限点" prop="term1">
              <el-input v-model="form.term1" placeholder="请输入1年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="2年期限点" prop="term2">
              <el-input v-model="form.term2" placeholder="请输入2年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="3年期限点" prop="term3">
              <el-input v-model="form.term3" placeholder="请输入3年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="4年期限点" prop="term4">
              <el-input v-model="form.term4" placeholder="请输入4年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="5年期限点" prop="term5">
              <el-input v-model="form.term5" placeholder="请输入5年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="6年期限点" prop="term6">
              <el-input v-model="form.term6" placeholder="请输入6年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="7年期限点" prop="term7">
              <el-input v-model="form.term7" placeholder="请输入7年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="8年期限点" prop="term8">
              <el-input v-model="form.term8" placeholder="请输入8年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="10年期限点" prop="term10">
              <el-input v-model="form.term10" placeholder="请输入10年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="12年期限点" prop="term12">
              <el-input v-model="form.term12" placeholder="请输入12年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="15年期限点" prop="term15">
              <el-input v-model="form.term15" placeholder="请输入15年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="20年期限点" prop="term20">
              <el-input v-model="form.term20" placeholder="请输入20年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="25年期限点" prop="term25">
              <el-input v-model="form.term25" placeholder="请输入25年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="30年期限点" prop="term30">
              <el-input v-model="form.term30" placeholder="请输入30年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="35年期限点" prop="term35">
              <el-input v-model="form.term35" placeholder="请输入35年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="40年期限点" prop="term40">
              <el-input v-model="form.term40" placeholder="请输入40年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="45年期限点" prop="term45">
              <el-input v-model="form.term45" placeholder="请输入45年期限点值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="50年期限点" prop="term50">
              <el-input v-model="form.term50" placeholder="请输入50年期限点值" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 期限点值集查看对话框 -->
    <el-dialog title="期限点值集" :visible.sync="termValuesDialogVisible" width="800px" append-to-body>
      <el-table :data="termValuesTableData" height="400" border style="width: 100%">
        <el-table-column prop="term" label="期限点" width="120" align="center"></el-table-column>
        <el-table-column label="值" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.value }}</span>
          </template>
        </el-table-column>
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
        <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAccountLiabilityDv10,
  getAccountLiabilityDv10,
  delAccountLiabilityDv10,
  addAccountLiabilityDv10,
  updateAccountLiabilityDv10,
  downloadTemplate
} from "@/api/dur/accountLiabilityDv10";
import { getToken } from "@/utils/auth";

export default {
  name: "AccountLiabilityDv10",
  dicts: ['dur_cash_flow_type', 'dur_value_type'],
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 分账户负债基点价值DV10表格数据
      accountLiabilityDv10List: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 期限点值集对话框可见性
      termValuesDialogVisible: false,
      // 期限点值集表格数据
      termValuesTableData: [],
      // 设计类型选项
      designTypeOptions: {
        '01': '传统险',
        '02': '分红险',
        '03': '万能险',
        '04': '投连险'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountPeriod: null,
        cashFlowType: null,
        designType: null,
        valueType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        cashFlowType: [
          { required: true, message: "现金流类型不能为空", trigger: "change" }
        ],
        designType: [
          { required: true, message: "设计类型不能为空", trigger: "blur" }
        ],
        valueType: [
          { required: true, message: "现值类型不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入分账户负债基点价值DV10数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/account/liability/dv10/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分账户负债基点价值DV10列表 */
    getList() {
      this.loading = true;
      listAccountLiabilityDv10(this.queryParams).then(response => {
        this.accountLiabilityDv10List = response.rows;
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
        accountPeriod: null,
        cashFlowType: null,
        designType: null,
        valueType: null,
        term0: null,
        term05: null,
        term1: null,
        term2: null,
        term3: null,
        term4: null,
        term5: null,
        term6: null,
        term7: null,
        term8: null,
        term10: null,
        term12: null,
        term15: null,
        term20: null,
        term25: null,
        term30: null,
        term35: null,
        term40: null,
        term45: null,
        term50: null
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
      this.title = "添加分账户负债基点价值DV10";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getAccountLiabilityDv10(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分账户负债基点价值DV10";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccountLiabilityDv10(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAccountLiabilityDv10(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除分账户负债基点价值DV10编号为"' + ids + '"的数据项？').then(() => {
        return delAccountLiabilityDv10(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'dur/account/liability/dv10/export',
        {
          ...this.queryParams
        },
        `account_liability_dv10_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "分账户负债基点价值DV10导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      downloadTemplate().then(response => {
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

    /** 获取设计类型标签 */
    getDesignTypeLabel(designType) {
      const designTypeMap = {
        '01': '传统险',
        '02': '分红险',
        '03': '万能险',
        '04': '投连险'
      };
      return designTypeMap[designType] || designType;
    },

    /** 查看期限点值 */
    handleViewTermValues(row) {
      try {
        // 构建期限点值数据
        this.termValuesTableData = [
          { term: "0年", value: row.term0 },
          { term: "0.5年", value: row.term05 },
          { term: "1年", value: row.term1 },
          { term: "2年", value: row.term2 },
          { term: "3年", value: row.term3 },
          { term: "4年", value: row.term4 },
          { term: "5年", value: row.term5 },
          { term: "6年", value: row.term6 },
          { term: "7年", value: row.term7 },
          { term: "8年", value: row.term8 },
          { term: "10年", value: row.term10 },
          { term: "12年", value: row.term12 },
          { term: "15年", value: row.term15 },
          { term: "20年", value: row.term20 },
          { term: "25年", value: row.term25 },
          { term: "30年", value: row.term30 },
          { term: "35年", value: row.term35 },
          { term: "40年", value: row.term40 },
          { term: "45年", value: row.term45 },
          { term: "50年", value: row.term50 }
        ];
        // 显示对话框
        this.termValuesDialogVisible = true;
      } catch (e) {
        this.$message.error('解析期限点值失败，请检查数据格式');
        console.error('解析期限点值失败:', e);
      }
    }
  }
};
</script>
