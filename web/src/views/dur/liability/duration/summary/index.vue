<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountPeriod">
        <el-input
          v-model="queryParams.accountPeriod"
          placeholder="请输入账期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="现金流类型" prop="cashFlowType">
        <el-select v-model="queryParams.cashFlowType" placeholder="请选择现金流类型" clearable>
          <el-option
            v-for="dict in dict.type.dur_cash_flow_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="久期类型" prop="durationType">
        <el-select v-model="queryParams.durationType" placeholder="请选择久期类型" clearable>
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
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否中短期" prop="isShortTerm">
        <el-select v-model="queryParams.isShortTerm" placeholder="请选择是否中短期" clearable>
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
          v-hasPermi="['dur:liability:duration:summary:add']"
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
          v-hasPermi="['dur:liability:duration:summary:edit']"
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
          v-hasPermi="['dur:liability:duration:summary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:liability:duration:summary:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="liabilityDurationSummaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" width="100" />
      <el-table-column label="现金流类型" align="center" prop="cashFlowType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_cash_flow_type" :value="scope.row.cashFlowType"/>
        </template>
      </el-table-column>

      <el-table-column label="久期类型" align="center" prop="durationType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_duration_type" :value="scope.row.durationType"/>
        </template>
      </el-table-column>
      <el-table-column label="设计类型" align="center" prop="designType" width="150" />
      <el-table-column label="是否中短期" align="center" prop="isShortTerm" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_is_short_term" :value="scope.row.isShortTerm"/>
        </template>
      </el-table-column>
      <el-table-column label="久期值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewDurationValSet(scope.row)"
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
            v-hasPermi="['dur:liability:duration:summary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:liability:duration:summary:remove']"
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

    <!-- 添加或修改负债久期汇总对话框 -->
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
            <el-form-item label="是否中短期" prop="isShortTerm">
              <el-select v-model="form.isShortTerm" placeholder="请选择是否中短期">
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
        <el-form-item label="久期值集" prop="durationValSet">
          <el-input v-model="form.durationValSet" type="textarea" placeholder="请输入久期值集" :rows="10" />
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

    <!-- 久期值集查看对话框 -->
    <el-dialog title="久期值集" :visible.sync="durationValSetDialogVisible" width="800px" append-to-body>
      <el-table :data="durationValSetTableData" height="400" border style="width: 100%">
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
  listLiabilityDurationSummary,
  getLiabilityDurationSummary,
  addLiabilityDurationSummary,
  updateLiabilityDurationSummary,
  delLiabilityDurationSummary,
  exportLiabilityDurationSummary,
  downloadTemplate
} from "@/api/dur/liability/duration/summary";
import { getToken } from "@/utils/auth";

export default {
  name: "LiabilityDurationSummary",
  dicts: ['dur_cash_flow_type', 'dur_duration_type', 'dur_is_short_term'],
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
      // 负债久期汇总表格数据
      liabilityDurationSummaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
        durationValSet: [
          { required: true, message: "久期值集不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入负债久期汇总数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/liability/duration/summary/importData"
      },
      // 久期值集对话框可见性
      durationValSetDialogVisible: false,
      // 久期值集表格数据
      durationValSetTableData: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询负债久期汇总列表 */
    getList() {
      this.loading = true;
      listLiabilityDurationSummary(this.queryParams).then(response => {
        this.liabilityDurationSummaryList = response.rows;
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
        isShortTerm: null,
        durationValSet: null
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
      this.title = "添加负债久期汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getLiabilityDurationSummary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改负债久期汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLiabilityDurationSummary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLiabilityDurationSummary(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除负债久期汇总编号为"' + ids + '"的数据项？').then(() => {
        return delLiabilityDurationSummary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/dur/liability/duration/summary/export', {
        ...this.queryParams
      }, `liability_duration_summary_${new Date().getTime()}.xlsx`);
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    downloadTemplate() {
      downloadTemplate().then(response => {
        this.downloadFile(response, `liability_duration_summary_template_${new Date().getTime()}.xlsx`);
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
    },
    /** 查看久期值集 */
    handleViewDurationValSet(row) {
      try {
        const durationValSet = JSON.parse(row.durationValSet || '{}');
        this.durationValSetTableData = Object.keys(durationValSet).map(key => {
          // 支持两种可能的字段名称：val 和 value
          const value = durationValSet[key].val !== undefined ? durationValSet[key].val : durationValSet[key].value;
          return {
            index: key,
            date: durationValSet[key].date,
            val: value
          };
        }).sort((a, b) => parseInt(a.index) - parseInt(b.index));
        this.durationValSetDialogVisible = true;
      } catch (e) {
        this.$message.error('解析久期值集失败，请检查数据格式');
        console.error('解析久期值集失败:', e);
      }
    }
  }
};
</script>
