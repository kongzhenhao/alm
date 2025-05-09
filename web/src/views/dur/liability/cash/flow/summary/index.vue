<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountPeriod">
        <el-input
          v-model="queryParams.accountPeriod"
          placeholder="请输入账期"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="现金流类型" prop="cashFlowType">
        <el-select
          v-model="queryParams.cashFlowType"
          placeholder="请选择现金流类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.dur_cash_flow_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="基点类型" prop="bpType">
        <el-select
          v-model="queryParams.bpType"
          placeholder="请选择基点类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.dur_bp_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="久期类型" prop="durationType">
        <el-select
          v-model="queryParams.durationType"
          placeholder="请选择久期类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.dur_duration_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设计类型" prop="designType">
        <el-input
          v-model="queryParams.designType"
          placeholder="请输入设计类型"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="中短期险种" prop="isShortTerm">
        <el-select
          v-model="queryParams.isShortTerm"
          placeholder="请选择是否中短期险种"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.dur_is_short_term"
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
          v-hasPermi="['dur:liability:cash:flow:summary:add']"
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
          v-hasPermi="['dur:liability:cash:flow:summary:edit']"
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
          v-hasPermi="['dur:liability:cash:flow:summary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:liability:cash:flow:summary:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="liabilityCashFlowSummaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" />
      <el-table-column label="现金流类型" align="center" prop="cashFlowType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_cash_flow_type" :value="scope.row.cashFlowType"/>
        </template>
      </el-table-column>
      <el-table-column label="基点类型" align="center" prop="bpType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_bp_type" :value="scope.row.bpType"/>
        </template>
      </el-table-column>
      <el-table-column label="久期类型" align="center" prop="durationType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_duration_type" :value="scope.row.durationType"/>
        </template>
      </el-table-column>
      <el-table-column label="设计类型" align="center" prop="designType" />
      <el-table-column label="中短期险种" align="center" prop="isShortTerm">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_is_short_term" :value="scope.row.isShortTerm"/>
        </template>
      </el-table-column>
      <el-table-column label="现金流值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewCashValSet(scope.row)"
          >查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="现金流现值值集" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewPresentCashValSet(scope.row)"
          >查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['dur:liability:cash:flow:summary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:liability:cash:flow:summary:remove']"
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

    <!-- 添加或修改负债现金流汇总对话框 -->
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
            <el-form-item label="基点类型" prop="bpType">
              <el-select v-model="form.bpType" placeholder="请选择基点类型">
                <el-option
                  v-for="dict in dict.type.dur_bp_type"
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
                  v-for="dict in dict.type.dur_duration_type"
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
              <el-input v-model="form.designType" placeholder="请输入设计类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中短期险种" prop="isShortTerm">
              <el-select v-model="form.isShortTerm" placeholder="请选择是否中短期险种">
                <el-option
                  v-for="dict in dict.type.dur_is_short_term"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="现金流值集" prop="cashValSet">
          <el-input v-model="form.cashValSet" type="textarea" placeholder="请输入现金流值集" :rows="5" />
        </el-form-item>
        <el-form-item label="现金流现值值集" prop="presentCashValSet">
          <el-input v-model="form.presentCashValSet" type="textarea" placeholder="请输入现金流现值值集" :rows="5" />
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

    <!-- 现金流值集查看对话框 -->
    <el-dialog title="现金流值集" :visible.sync="cashValSetDialogVisible" width="800px" append-to-body>
      <el-table :data="cashValSetTableData" height="400" border style="width: 100%">
        <el-table-column prop="index" label="序号" width="80" align="center"></el-table-column>
        <el-table-column prop="date" label="日期" width="120" align="center"></el-table-column>
        <el-table-column label="值" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.val }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 现金流现值值集查看对话框 -->
    <el-dialog title="现金流现值值集" :visible.sync="presentCashValSetDialogVisible" width="800px" append-to-body>
      <el-table :data="presentCashValSetTableData" height="400" border style="width: 100%">
        <el-table-column prop="index" label="序号" width="80" align="center"></el-table-column>
        <el-table-column prop="date" label="日期" width="120" align="center"></el-table-column>
        <el-table-column label="值" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.val }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {
  listLiabilityCashFlowSummary,
  getLiabilityCashFlowSummary,
  addLiabilityCashFlowSummary,
  updateLiabilityCashFlowSummary,
  delLiabilityCashFlowSummary,
  exportLiabilityCashFlowSummary,
  getImportTemplate
} from "@/api/dur/liability/cash/flow/summary";
import { getToken } from "@/utils/auth";

export default {
  name: "LiabilityCashFlowSummary",
  dicts: ['dur_cash_flow_type', 'dur_bp_type', 'dur_duration_type', 'dur_is_short_term'],
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
      // 负债现金流汇总表格数据
      liabilityCashFlowSummaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 现金流值集对话框可见性
      cashValSetDialogVisible: false,
      // 现金流现值值集对话框可见性
      presentCashValSetDialogVisible: false,
      // 现金流值集表格数据
      cashValSetTableData: [],
      // 现金流现值值集表格数据
      presentCashValSetTableData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountPeriod: null,
        cashFlowType: null,
        bpType: null,
        durationType: null,
        designType: null,
        isShortTerm: null
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
        bpType: [
          { required: true, message: "基点类型不能为空", trigger: "change" }
        ],
        durationType: [
          { required: true, message: "久期类型不能为空", trigger: "change" }
        ],
        designType: [
          { required: true, message: "设计类型不能为空", trigger: "blur" },
          { max: 50, message: "设计类型长度不能超过50个字符", trigger: "blur" }
        ],
        isShortTerm: [
          { required: true, message: "是否为中短期险种不能为空", trigger: "change" }
        ],
        cashValSet: [
          { required: true, message: "现金流值集不能为空", trigger: "blur" }
        ],
        presentCashValSet: [
          { required: true, message: "现金流现值值集不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入负债现金流汇总数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/liability/cash/flow/summary/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询负债现金流汇总列表 */
    getList() {
      this.loading = true;
      listLiabilityCashFlowSummary(this.queryParams).then(response => {
        this.liabilityCashFlowSummaryList = response.rows;
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
        bpType: null,
        durationType: null,
        designType: null,
        isShortTerm: 'N',
        cashValSet: null,
        presentCashValSet: null
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
      this.title = "添加负债现金流汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getLiabilityCashFlowSummary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改负债现金流汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLiabilityCashFlowSummary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLiabilityCashFlowSummary(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除负债现金流汇总编号为"' + ids + '"的数据项？').then(() => {
        return delLiabilityCashFlowSummary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'dur/liability/cash/flow/summary/export',
        {
          ...this.queryParams,
        },
        `liability_cash_flow_summary_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      getImportTemplate().then(response => {
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
    /** 查看现金流值集 */
    handleViewCashValSet(row) {
      try {
        const cashValSet = JSON.parse(row.cashValSet || '{}');
        this.cashValSetTableData = Object.keys(cashValSet).map(key => {
          // 支持两种可能的字段名称：val 和 value
          const value = cashValSet[key].val !== undefined ? cashValSet[key].val : cashValSet[key].value;
          // 保持原始值，不进行转换，确保小数位完整展示
          return {
            index: key,
            date: cashValSet[key].date,
            val: value.toString()
          };
        }).sort((a, b) => parseInt(a.index) - parseInt(b.index));
        this.cashValSetDialogVisible = true;
      } catch (e) {
        this.$message.error('解析现金流值集失败，请检查数据格式');
        console.error('解析现金流值集失败:', e);
      }
    },
    /** 查看现金流现值值集 */
    handleViewPresentCashValSet(row) {
      try {
        const presentCashValSet = JSON.parse(row.presentCashValSet || '{}');
        this.presentCashValSetTableData = Object.keys(presentCashValSet).map(key => {
          // 支持两种可能的字段名称：val 和 value
          const value = presentCashValSet[key].val !== undefined ? presentCashValSet[key].val : presentCashValSet[key].value;
          // 保持原始值，不进行转换，确保小数位完整展示
          return {
            index: key,
            date: presentCashValSet[key].date,
            val: value.toString()
          };
        }).sort((a, b) => parseInt(a.index) - parseInt(b.index));
        this.presentCashValSetDialogVisible = true;
      } catch (e) {
        this.$message.error('解析现金流现值值集失败，请检查数据格式');
        console.error('解析现金流现值值集失败:', e);
      }
    }
  }
};
</script>
