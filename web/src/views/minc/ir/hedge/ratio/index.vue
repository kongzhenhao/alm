<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期，格式：YYYYMM"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入项目名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户编码" prop="accountCode">
        <el-select
          v-model="queryParams.accountCode"
          placeholder="请选择账户编码"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in dict.type.minc_account"
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
          v-hasPermi="['minc:ir:hedge:ratio:add']"
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
          v-hasPermi="['minc:ir:hedge:ratio:edit']"
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
          v-hasPermi="['minc:ir:hedge:ratio:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:ir:hedge:ratio:export']"
        >导出</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['minc:ir:hedge:ratio:import']"
        >导入</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="irHedgeRatioList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" />
      <el-table-column label="项目名称" align="center" prop="itemName" show-overflow-tooltip />
      <el-table-column label="账户编码" align="center" prop="accountCode">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.minc_account" :value="scope.row.accountCode"/>
        </template>
      </el-table-column>
      <el-table-column label="敏感度/对冲率" align="center" prop="sensitivityRate">
        <template slot-scope="scope">
          {{ formatPercentage(scope.row.sensitivityRate) }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['minc:ir:hedge:ratio:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:ir:hedge:ratio:remove']"
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

    <!-- 添加或修改利率风险对冲率表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="账期" prop="accountingPeriod">
          <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" />
        </el-form-item>
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="账户编码" prop="accountCode">
          <el-select v-model="form.accountCode" placeholder="请选择账户编码">
            <el-option
              v-for="dict in dict.type.minc_account"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="敏感度/对冲率" prop="sensitivityRateDisplay">
          <el-input v-model="form.sensitivityRateDisplay" placeholder="请输入敏感度或对冲率，如：7.93表示7.93%">
            <template slot="append">%</template>
          </el-input>
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
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <!-- <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link> -->
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listIrHedgeRatio, getIrHedgeRatio, delIrHedgeRatio, addIrHedgeRatio, updateIrHedgeRatio, importTemplateIrHedgeRatio, importDataIrHedgeRatio } from "@/api/minc/irHedgeRatio";
import { getToken } from "@/utils/auth";

export default {
  name: "IrHedgeRatio",
  dicts: ['minc_account'],
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
      // 利率风险对冲率表表格数据
      irHedgeRatioList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        itemName: null,
        accountCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式不正确，应为YYYYMM格式", trigger: "blur" }
        ],
        itemName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" },
          { max: 100, message: "项目名称长度不能超过100个字符", trigger: "blur" }
        ],
        accountCode: [
          { required: true, message: "账户编码不能为空", trigger: "change" }
        ],
        sensitivityRateDisplay: [
          { pattern: /^-?\d+(\.\d{1,2})?$/, message: "敏感度/对冲率格式不正确，最多2位小数", trigger: "blur" }
        ]
      },
      // 导入参数
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/minc/ir/hedge/ratio/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 格式化百分比显示 */
    formatPercentage(value) {
      if (value === null || value === undefined || value === '') {
        return '-';
      }
      // 将小数转换为百分比格式，保留2位小数
      const percentage = (parseFloat(value) * 100).toFixed(2);
      return percentage + '%';
    },
    /** 查询利率风险对冲率表列表 */
    getList() {
      this.loading = true;
      listIrHedgeRatio(this.queryParams).then(response => {
        this.irHedgeRatioList = response.rows;
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
        itemName: null,
        accountCode: null,
        sensitivityRate: null,
        sensitivityRateDisplay: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加利率风险对冲率表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIrHedgeRatio(id).then(response => {
        this.form = response.data;
        // 将数据库中的小数值转换为百分比显示
        if (this.form.sensitivityRate !== null && this.form.sensitivityRate !== undefined) {
          this.form.sensitivityRateDisplay = (parseFloat(this.form.sensitivityRate) * 100).toFixed(2);
        }
        this.open = true;
        this.title = "修改利率风险对冲率表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将百分比值转换为小数值
          if (this.form.sensitivityRateDisplay !== null && this.form.sensitivityRateDisplay !== undefined && this.form.sensitivityRateDisplay !== '') {
            this.form.sensitivityRate = (parseFloat(this.form.sensitivityRateDisplay) / 100).toString();
          } else {
            this.form.sensitivityRate = null;
          }

          if (this.form.id != null) {
            updateIrHedgeRatio(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIrHedgeRatio(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除利率风险对冲率表编号为"' + ids + '"的数据项？').then(function() {
        return delIrHedgeRatio(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/ir/hedge/ratio/export',
        {
          ...this.queryParams,
        },
        `ir_hedge_ratio_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "利率风险对冲率表导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('minc/ir/hedge/ratio/importTemplate', {}, `ir_hedge_ratio_template_${new Date().getTime()}.xlsx`)
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
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
