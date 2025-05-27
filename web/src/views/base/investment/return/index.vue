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
      <el-form-item label="基点类型" prop="bpType">
        <el-select v-model="queryParams.bpType" placeholder="请选择基点类型" clearable>
          <el-option
            v-for="dict in dict.type.cost_basis_point_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="久期类型" prop="durationType">
        <el-select v-model="queryParams.durationType" placeholder="请选择久期类型" clearable>
          <el-option
            v-for="dict in dict.type.cost_duration_type"
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
          v-hasPermi="['base:investment:return:add']"
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
          v-hasPermi="['base:investment:return:edit']"
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
          v-hasPermi="['base:investment:return:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:investment:return:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['base:investment:return:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="returnList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="情景名称" align="center" prop="scenarioName" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_scenario_name" :value="scope.row.scenarioName"/>
        </template>
      </el-table-column>
      <el-table-column label="基点类型" align="center" prop="bpType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_basis_point_type" :value="scope.row.bpType"/>
        </template>
      </el-table-column>
      <el-table-column label="久期类型" align="center" prop="durationType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cost_duration_type" :value="scope.row.durationType"/>
        </template>
      </el-table-column>
      <el-table-column label="投资收益率T0" align="center" prop="returnRateT0" width="120" />
      <el-table-column label="投资收益率T1" align="center" prop="returnRateT1" width="120" />
      <el-table-column label="投资收益率T2" align="center" prop="returnRateT2" width="120" />
      <el-table-column label="投资收益率T3" align="center" prop="returnRateT3" width="120" />
      <el-table-column label="投资收益率T4" align="center" prop="returnRateT4" width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:investment:return:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:investment:return:remove']"
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

    <!-- 添加或修改投资收益率假设对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="情景名称" prop="scenarioName">
              <el-select v-model="form.scenarioName" placeholder="请选择情景名称">
                <el-option
                  v-for="dict in dict.type.cost_scenario_name"
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
            <el-form-item label="基点类型" prop="bpType">
              <el-select v-model="form.bpType" placeholder="请选择基点类型">
                <el-option
                  v-for="dict in dict.type.cost_basis_point_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="久期类型" prop="durationType">
              <el-select v-model="form.durationType" placeholder="请选择久期类型">
                <el-option
                  v-for="dict in dict.type.cost_duration_type"
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
            <el-form-item label="投资收益率T0" prop="returnRateT0">
              <el-input-number v-model="form.returnRateT0" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资收益率T1" prop="returnRateT1">
              <el-input-number v-model="form.returnRateT1" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资收益率T2" prop="returnRateT2">
              <el-input-number v-model="form.returnRateT2" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资收益率T3" prop="returnRateT3">
              <el-input-number v-model="form.returnRateT3" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资收益率T4" prop="returnRateT4">
              <el-input-number v-model="form.returnRateT4" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资收益率T5" prop="returnRateT5">
              <el-input-number v-model="form.returnRateT5" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资收益率T6" prop="returnRateT6">
              <el-input-number v-model="form.returnRateT6" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资收益率T7" prop="returnRateT7">
              <el-input-number v-model="form.returnRateT7" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资收益率T8" prop="returnRateT8">
              <el-input-number v-model="form.returnRateT8" :precision="6" :step="0.001" :min="0" :max="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资收益率T9" prop="returnRateT9">
              <el-input-number v-model="form.returnRateT9" :precision="6" :step="0.001" :min="0" :max="1" />
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
  listReturn, 
  getReturn, 
  addReturn, 
  updateReturn, 
  delReturn, 
  exportReturn,
  importReturnTemplate
} from "@/api/base/investment/return";
import { getToken } from "@/utils/auth";

export default {
  name: "InvestmentReturnRate",
  dicts: ['cost_scenario_name', 'cost_basis_point_type', 'cost_duration_type'],
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
      // 投资收益率假设表格数据
      returnList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        scenarioName: null,
        bpType: null,
        durationType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式为6位数字，如202401", trigger: "blur" }
        ],
        scenarioName: [
          { required: true, message: "情景名称不能为空", trigger: "change" }
        ],
        bpType: [
          { required: true, message: "基点类型不能为空", trigger: "change" }
        ],
        durationType: [
          { required: true, message: "久期类型不能为空", trigger: "change" }
        ],
        returnRateT0: [
          { required: true, message: "投资收益率T0不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入投资收益率假设数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已存在数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/base/investment/return/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询投资收益率假设列表 */
    getList() {
      this.loading = true;
      listReturn(this.queryParams).then(response => {
        this.returnList = response.rows;
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
        scenarioName: null,
        bpType: null,
        durationType: null,
        returnRateT0: 0,
        returnRateT1: 0,
        returnRateT2: 0,
        returnRateT3: 0,
        returnRateT4: 0,
        returnRateT5: 0,
        returnRateT6: 0,
        returnRateT7: 0,
        returnRateT8: 0,
        returnRateT9: 0,
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
      this.title = "添加投资收益率假设";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getReturn(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改投资收益率假设";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReturn(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReturn(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除投资收益率假设编号为"' + ids + '"的数据项？').then(() => {
        return delReturn(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有投资收益率假设数据项？').then(() => {
        this.download('base/investment/return/export', {
          ...this.queryParams
        }, `投资收益率假设_${new Date().getTime()}.xlsx`);
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      // 直接下载模板文件
      window.open(process.env.VUE_APP_BASE_API + "/base/investment/return/importTemplate", "_blank");
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
