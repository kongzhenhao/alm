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
      <el-form-item label="因子类型" prop="factorType">
        <el-select v-model="queryParams.factorType" placeholder="请选择因子类型" clearable>
          <el-option
            v-for="dict in dict.type.dur_factor_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="基点类型" prop="bpType">
        <el-select v-model="queryParams.bpType" placeholder="请选择基点类型" clearable>
          <el-option
            v-for="dict in dict.type.dur_bp_type"
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
          v-hasPermi="['dur:discount:factor:add']"
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
          v-hasPermi="['dur:discount:factor:edit']"
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
          v-hasPermi="['dur:discount:factor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:discount:factor:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['dur:discount:factor:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="discountFactorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" />
      <el-table-column label="因子类型" align="center" prop="factorType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_factor_type" :value="scope.row.factorType"/>
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
      <el-table-column label="因子值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewFactorValSet(scope.row)"
          >查看</el-button>
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
            v-hasPermi="['dur:discount:factor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:discount:factor:remove']"
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

    <!-- 添加或修改折现因子对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账期" prop="accountPeriod">
          <el-input v-model="form.accountPeriod" placeholder="请输入账期，格式YYYYMM" />
        </el-form-item>
        <el-form-item label="因子类型" prop="factorType">
          <el-select v-model="form.factorType" placeholder="请选择因子类型">
            <el-option
              v-for="dict in dict.type.dur_factor_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
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
        <el-form-item label="因子值集" prop="factorValSet">
          <el-input v-model="form.factorValSet" type="textarea" placeholder="请输入因子值集" />
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
        <el-checkbox v-model="upload.updateSupport" />更新已经存在的数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 因子值集查看对话框 -->
    <el-dialog :title="'因子值集查看 - ' + factorValSetInfo.title" :visible.sync="factorValSetInfo.open" width="600px" append-to-body>
      <el-table :data="factorValSetInfo.tableData" height="400" border>
        <el-table-column prop="index" label="序号" width="80" align="center" />
        <el-table-column prop="date" label="日期" width="180" align="center" />
        <el-table-column prop="value" label="值" align="center" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="factorValSetInfo.open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDiscountFactor,
  getDiscountFactor,
  addDiscountFactor,
  updateDiscountFactor,
  delDiscountFactor,
  downloadTemplate,
  importDiscountFactor
} from "@/api/dur/discountFactor";
import { getToken } from "@/utils/auth";

export default {
  name: "DiscountFactor",
  dicts: ['dur_factor_type', 'dur_bp_type', 'dur_duration_type'],
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
      // 折现因子表格数据
      discountFactorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountPeriod: null,
        factorType: null,
        bpType: null,
        durationType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        factorType: [
          { required: true, message: "因子类型不能为空", trigger: "change" }
        ],
        bpType: [
          { required: true, message: "基点类型不能为空", trigger: "change" }
        ],
        durationType: [
          { required: true, message: "久期类型不能为空", trigger: "change" }
        ],
        factorValSet: [
          { required: true, message: "因子值集不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入折现因子数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/discount/factor/importData"
      },
      // 因子值集查看参数
      factorValSetInfo: {
        open: false,
        title: "",
        tableData: []
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询折现因子列表 */
    getList() {
      this.loading = true;
      listDiscountFactor(this.queryParams).then(response => {
        this.discountFactorList = response.rows;
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
        factorType: null,
        bpType: null,
        durationType: null,
        factorValSet: null
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
      this.title = "添加折现因子";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getDiscountFactor(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改折现因子";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDiscountFactor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDiscountFactor(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除折现因子编号为"' + ids + '"的数据项？').then(() => {
        return delDiscountFactor(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/dur/discount/factor/export', {
        ...this.queryParams
      }, `discount_factor_${new Date().getTime()}.xlsx`);
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    downloadTemplate() {
      downloadTemplate().then(response => {
        this.downloadFile(response, `discount_factor_template_${new Date().getTime()}.xlsx`);
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
    /** 查看因子值集按钮操作 */
    handleViewFactorValSet(row) {
      try {
        // 清空之前的数据
        this.factorValSetInfo.tableData = [];

        // 设置标题
        this.factorValSetInfo.title = row.factorType ? this.dict.type.dur_factor_type.find(item => item.value === row.factorType)?.label || '' : '';

        // 解析JSON数据
        if (row.factorValSet) {
          const factorValSetData = JSON.parse(row.factorValSet);
          const tableData = [];

          // 遍历并格式化数据
          Object.keys(factorValSetData).forEach(key => {
            const item = factorValSetData[key];
            tableData.push({
              index: key,
              date: item.date || '',
              value: item.val || item.value || 0
            });
          });

          // 按序号排序
          this.factorValSetInfo.tableData = tableData.sort((a, b) => parseInt(a.index) - parseInt(b.index));
        }

        // 打开对话框
        this.factorValSetInfo.open = true;
      } catch (error) {
        this.$modal.msgError("解析因子值集数据失败：" + error.message);
        console.error("解析因子值集数据失败", error);
      }
    }
  }
};
</script>
