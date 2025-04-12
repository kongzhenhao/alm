<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账期" prop="accountPeriod">
        <el-input
          v-model="queryParams.accountPeriod"
          placeholder="请输入账期"
          clearable
          style="width: 160px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="曲线类型" prop="curveType">
        <el-select v-model="queryParams.curveType" placeholder="请选择曲线类型" clearable style="width: 160px">
          <el-option
            v-for="dict in curveTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="久期类型" prop="durationType">
        <el-select v-model="queryParams.durationType" placeholder="请选择久期类型" clearable style="width: 160px">
          <el-option
            v-for="dict in durationTypeOptions"
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
          v-hasPermi="['dur:discountCurve:add']"
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
          v-hasPermi="['dur:discountCurve:edit']"
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
          v-hasPermi="['dur:discountCurve:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:discountCurve:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['dur:discountCurve:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleDownloadTemplate"
        >下载模板</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="discountCurveList"
      @selection-change="handleSelectionChange"
      height="500"
      border
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账期" align="center" prop="accountPeriod" />
      <el-table-column label="曲线类型" align="center" prop="curveType">
        <template slot-scope="scope">
          <span>{{ getCurveTypeLabel(scope.row.curveType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="久期类型" align="center" prop="durationType">
        <template slot-scope="scope">
          <span>{{ getDurationTypeLabel(scope.row.durationType) }}</span>
        </template>
      </el-table-column>
      
      <!-- 动态列，根据数据动态生成 -->
      <el-table-column
        v-for="(item, index) in curveColumns"
        :key="index"
        :label="item.label"
        align="center"
        :prop="item.prop"
        :formatter="formatCurveValue"
      />
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['dur:discountCurve:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:discountCurve:remove']"
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

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
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
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="handleDownloadTemplate">下载模板</el-link>
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
import { listDiscountCurve, getDiscountCurve, delDiscountCurve, addDiscountCurve, updateDiscountCurve, importDiscountCurve, exportDiscountCurve } from "@/api/dur/discountCurve";
import { getToken } from "@/utils/auth";

export default {
  name: "DiscountCurve",
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
      // 折现曲线表格数据
      discountCurveList: [],
      // 曲线动态列
      curveColumns: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountPeriod: null,
        curveType: null,
        durationType: null
      },
      // 曲线类型选项
      curveTypeOptions: [
        { value: "01", label: "中档" },
        { value: "02", label: "低档" }
      ],
      // 久期类型选项
      durationTypeOptions: [
        { value: "01", label: "修正久期" },
        { value: "02", label: "有效久期" }
      ],
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入折现曲线数据",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/importDiscountCurve"
      }
    };
  },
  created() {
    this.getList();
    // 初始化动态列
    this.initDynamicColumns();
  },
  methods: {
    /** 查询折现曲线列表 */
    getList() {
      this.loading = true;
      listDiscountCurve(this.queryParams).then(response => {
        this.discountCurveList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 初始化动态列 */
    initDynamicColumns() {
      // 这里模拟生成1272个列，实际应该根据后端返回的数据动态生成
      // 为了性能考虑，这里只显示前10个列作为示例
      this.curveColumns = [];
      for (let i = 1; i <= 10; i++) {
        this.curveColumns.push({
          label: i.toString(),
          prop: 'curveValSet.' + i + '.val'
        });
      }
    },
    // 曲线值格式化
    formatCurveValue(row, column) {
      const prop = column.property;
      if (prop && prop.startsWith('curveValSet.')) {
        const parts = prop.split('.');
        if (parts.length === 3 && row.curveValSet) {
          const index = parts[1];
          const field = parts[2];
          if (row.curveValSet[index] && row.curveValSet[index][field] !== undefined) {
            // 格式化数值，添加千位分隔符
            return this.formatNumber(row.curveValSet[index][field]);
          }
        }
      }
      return '';
    },
    // 格式化数字，添加千位分隔符
    formatNumber(num) {
      if (num === undefined || num === null) return '';
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },
    // 获取曲线类型标签
    getCurveTypeLabel(value) {
      const found = this.curveTypeOptions.find(item => item.value === value);
      return found ? found.label : value;
    },
    // 获取久期类型标签
    getDurationTypeLabel(value) {
      const found = this.durationTypeOptions.find(item => item.value === value);
      return found ? found.label : value;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        accountPeriod: null,
        curveType: null,
        durationType: null,
        curveValSet: {}
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
      this.$router.push({ path: '/dur/discountCurve/add' });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids[0];
      this.$router.push({ path: '/dur/discountCurve/edit', query: { id: id } });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除折现曲线数据？').then(function() {
        return delDiscountCurve(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('dur/discountCurve/export', {
        ...this.queryParams
      }, `折现曲线_${new Date().getTime()}.xlsx`);
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    handleDownloadTemplate() {
      this.download('dur/discountCurve/template', {}, `折现曲线模板.xlsx`);
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
      this.$alert(response.msg || '导入成功', "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
