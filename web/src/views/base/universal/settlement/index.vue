<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否中短" prop="shortTermFlag">
        <el-select v-model="queryParams.shortTermFlag" placeholder="请选择是否中短" clearable>
          <el-option
            v-for="dict in dict.type.cost_short_term_flag"
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
          v-hasPermi="['base:universal:settlement:add']"
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
          v-hasPermi="['base:universal:settlement:edit']"
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
          v-hasPermi="['base:universal:settlement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:universal:settlement:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['base:universal:settlement:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settlementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="精算代码" align="center" prop="actuarialCode" width="120" />
      <el-table-column label="业务代码" align="center" prop="businessCode" width="120" />
      <el-table-column label="产品名称" align="center" prop="productName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="是否中短" align="center" prop="shortTermFlag" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_short_term_flag" :value="scope.row.shortTermFlag"/>
        </template>
      </el-table-column>
      <el-table-column label="定价保证成本率" align="center" prop="guaranteedCostRate" width="120" />
      <el-table-column label="平均结息利率T0" align="center" prop="avgRateT0" width="120" />
      <el-table-column label="平均结息利率T1" align="center" prop="avgRateT1" width="120" />
      <el-table-column label="平均结息利率T2" align="center" prop="avgRateT2" width="120" />
      <el-table-column label="平均结息利率T3" align="center" prop="avgRateT3" width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:universal:settlement:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:universal:settlement:remove']"
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

    <!-- 添加或修改万能平均结算利率对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期" />
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
            <el-form-item label="是否中短" prop="shortTermFlag">
              <el-select v-model="form.shortTermFlag" placeholder="请选择是否中短">
                <el-option
                  v-for="dict in dict.type.cost_short_term_flag"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="定价保证成本率" prop="guaranteedCostRate">
              <el-input-number v-model="form.guaranteedCostRate" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="平均结息利率T0" prop="avgRateT0">
              <el-input-number v-model="form.avgRateT0" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="平均结息利率T1" prop="avgRateT1">
              <el-input-number v-model="form.avgRateT1" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="平均结息利率T2" prop="avgRateT2">
              <el-input-number v-model="form.avgRateT2" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="平均结息利率T3" prop="avgRateT3">
              <el-input-number v-model="form.avgRateT3" :precision="6" :step="0.001" :min="0" :max="1" />
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
        <el-checkbox v-model="upload.updateSupport" />更新已存在数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  listSettlement, 
  getSettlement, 
  addSettlement, 
  updateSettlement, 
  delSettlement, 
  exportSettlement,
  importSettlementTemplate
} from "@/api/base/universal/settlement";
import { getToken } from "@/utils/auth";

export default {
  name: "UniversalAvgSettlementRate",
  dicts: ['cost_short_term_flag'],
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
      // 万能平均结算利率表格数据
      settlementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        actuarialCode: null,
        productName: null,
        shortTermFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式为6位数字，如202401", trigger: "blur" }
        ],
        actuarialCode: [
          { required: true, message: "精算代码不能为空", trigger: "blur" },
          { max: 20, message: "精算代码长度不能超过20个字符", trigger: "blur" }
        ],
        businessCode: [
          { required: true, message: "业务代码不能为空", trigger: "blur" },
          { max: 20, message: "业务代码长度不能超过20个字符", trigger: "blur" }
        ],
        productName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" },
          { max: 100, message: "产品名称长度不能超过100个字符", trigger: "blur" }
        ],
        shortTermFlag: [
          { required: true, message: "是否中短不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入万能平均结算利率数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已存在数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/base/universal/settlement/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询万能平均结算利率列表 */
    getList() {
      this.loading = true;
      listSettlement(this.queryParams).then(response => {
        this.settlementList = response.rows;
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
        businessCode: null,
        productName: null,
        shortTermFlag: 'N',
        guaranteedCostRate: 0,
        avgRateT0: 0,
        avgRateT1: 0,
        avgRateT2: 0,
        avgRateT3: 0,
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
      this.title = "添加万能平均结算利率";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getSettlement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改万能平均结算利率";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSettlement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSettlement(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除万能平均结算利率编号为"' + ids + '"的数据项？').then(() => {
        return delSettlement(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有万能平均结算利率数据项？').then(() => {
        this.download('base/universal/settlement/export', {
          ...this.queryParams
        }, `万能平均结算利率_${new Date().getTime()}.xlsx`);
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      // 直接下载模板文件
      window.open(process.env.VUE_APP_BASE_API + "/base/universal/settlement/importTemplate", "_blank");
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

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  
  .el-table {
    margin-top: 15px;
    
    &__header {
      font-weight: bold;
    }
  }
  
  .pagination-container {
    margin-top: 15px;
  }
  
  .el-dialog {
    &__body {
      padding: 20px 30px;
    }
  }
  
  .upload {
    &__tip {
      color: #606266;
      font-size: 12px;
      margin-top: 7px;
    }
  }
}
</style>
