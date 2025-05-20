<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="险种号" prop="polNo">
        <el-input
          v-model="queryParams.polNo"
          placeholder="请输入险种号"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="保单号" prop="contNo">
        <el-input
          v-model="queryParams.contNo"
          placeholder="请输入保单号"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="险种编码" prop="riskCode">
        <el-input
          v-model="queryParams.riskCode"
          placeholder="请输入险种编码"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="险种生效日期" prop="effectiveDate">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="业务类型编码" prop="busTypeCode">
        <el-input
          v-model="queryParams.busTypeCode"
          placeholder="请输入业务类型编码"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['insu:policy:detail:add']"
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
          v-hasPermi="['insu:policy:detail:edit']"
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
          v-hasPermi="['insu:policy:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['insu:policy:detail:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['insu:policy:detail:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="policyDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="险种号" align="center" prop="polNo" width="120" />
      <el-table-column label="保单号" align="center" prop="contNo" width="120" />
      <el-table-column label="险种生效日" align="center" prop="effectiveDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.effectiveDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="险种编码" align="center" prop="riskCode" width="120" />
      <el-table-column label="险种状态描述" align="center" prop="polStatusDesc" width="120" />
      <el-table-column label="险种状态变化时间" align="center" prop="polStatusChangedTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.polStatusChangedTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设计类型" align="center" prop="designType" width="120" />
      <el-table-column label="长短期标识" align="center" prop="termType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.insu_term_type" :value="scope.row.termType"/>
        </template>
      </el-table-column>
      <el-table-column label="业务类型编码" align="center" prop="busTypeCode" width="120" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['insu:policy:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['insu:policy:detail:remove']"
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

    <!-- 添加或修改保单数据明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="险种号" prop="polNo">
          <el-input v-model="form.polNo" placeholder="请输入险种号" />
        </el-form-item>
        <el-form-item label="保单号" prop="contNo">
          <el-input v-model="form.contNo" placeholder="请输入保单号" />
        </el-form-item>
        <el-form-item label="险种生效日" prop="effectiveDate">
          <el-date-picker
            v-model="form.effectiveDate"
            type="date"
            placeholder="选择险种生效日"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="险种编码" prop="riskCode">
          <el-input v-model="form.riskCode" placeholder="请输入险种编码" />
        </el-form-item>
        <el-form-item label="险种状态描述" prop="polStatusDesc">
          <el-input v-model="form.polStatusDesc" placeholder="请输入险种状态描述" />
        </el-form-item>
        <el-form-item label="险种状态变化时间" prop="polStatusChangedTime">
          <el-date-picker
            v-model="form.polStatusChangedTime"
            type="datetime"
            placeholder="选择险种状态变化时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="设计类型" prop="designType">
          <el-input v-model="form.designType" placeholder="请输入设计类型" />
        </el-form-item>
        <el-form-item label="长短期标识" prop="termType">
          <el-select v-model="form.termType" placeholder="请选择长短期标识">
            <el-option
              v-for="dict in dict.type.insu_term_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型编码" prop="busTypeCode">
          <el-input v-model="form.busTypeCode" placeholder="请输入业务类型编码" />
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
  listPolicyDetail,
  getPolicyDetail,
  addPolicyDetail,
  updatePolicyDetail,
  delPolicyDetail,
  exportPolicyDetail,
  importTemplatePolicyDetail
} from "@/api/insu/policyDetail";
import { getToken } from "@/utils/auth";

export default {
  name: "PolicyDetail",
  dicts: ['insu_term_type'],
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
      // 保单数据明细表格数据
      policyDetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        polNo: null,
        contNo: null,
        effectiveDate: null,
        riskCode: null,
        polStatusDesc: null,
        polStatusChangedTime: null,
        designType: null,
        termType: null,
        busTypeCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        polNo: [
          { required: true, message: "险种号不能为空", trigger: "blur" },
          { max: 20, message: "险种号长度不能超过20个字符", trigger: "blur" }
        ],
        contNo: [
          { required: true, message: "保单号不能为空", trigger: "blur" },
          { max: 20, message: "保单号长度不能超过20个字符", trigger: "blur" }
        ],
        effectiveDate: [
          { required: true, message: "险种生效日不能为空", trigger: "blur" }
        ],
        riskCode: [
          { required: true, message: "险种编码不能为空", trigger: "blur" },
          { max: 20, message: "险种编码长度不能超过20个字符", trigger: "blur" }
        ],
        polStatusDesc: [
          { required: true, message: "险种状态描述不能为空", trigger: "blur" },
          { max: 20, message: "险种状态描述长度不能超过20个字符", trigger: "blur" }
        ],
        polStatusChangedTime: [
          { required: true, message: "险种状态变化时间不能为空", trigger: "blur" }
        ],
        designType: [
          { required: true, message: "设计类型不能为空", trigger: "blur" },
          { max: 50, message: "设计类型长度不能超过50个字符", trigger: "blur" }
        ],
        termType: [
          { required: true, message: "长短期标识不能为空", trigger: "change" }
        ],
        busTypeCode: [
          { required: true, message: "业务类型编码不能为空", trigger: "blur" },
          { max: 20, message: "业务类型编码长度不能超过20个字符", trigger: "blur" }
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
        url: process.env.VUE_APP_BASE_API + "/insu/policy/detail/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询保单数据明细列表 */
    getList() {
      this.loading = true;
      this.queryParams.beginTime = this.dateRange && this.dateRange.length > 0 ? this.dateRange[0] : undefined;
      this.queryParams.endTime = this.dateRange && this.dateRange.length > 0 ? this.dateRange[1] : undefined;
      listPolicyDetail(this.queryParams).then(response => {
        this.policyDetailList = response.rows;
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
        polNo: null,
        contNo: null,
        effectiveDate: null,
        riskCode: null,
        polStatusDesc: null,
        polStatusChangedTime: null,
        designType: null,
        termType: null,
        busTypeCode: null
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
      this.dateRange = [];
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
      this.title = "添加保单数据明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPolicyDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改保单数据明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePolicyDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPolicyDetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除保单数据明细编号为"' + ids + '"的数据项？').then(() => {
        return delPolicyDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'insu/policy/detail/export',
        {
          ...this.queryParams
        },
        `policy_detail_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "保单数据明细导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplatePolicyDetail().then(response => {
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
    }
  }
};
</script>
