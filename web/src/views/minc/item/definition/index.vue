<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="项目编码" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入项目编码"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风险类型" prop="riskType">
        <el-input
          v-model="queryParams.riskType"
          placeholder="请输入风险类型"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="边际资本项目" prop="capitalItem">
        <el-input
          v-model="queryParams.capitalItem"
          placeholder="请输入边际资本项目名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="相关系数项目" prop="correlationItem">
        <el-input
          v-model="queryParams.correlationItem"
          placeholder="请输入相关系数项目名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 240px">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
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
          v-hasPermi="['minc:item:definition:add']"
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
          v-hasPermi="['minc:item:definition:edit']"
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
          v-hasPermi="['minc:item:definition:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:item:definition:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['minc:item:definition:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="itemDefinitionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目编码" align="center" prop="itemCode" />
      <el-table-column label="风险类型" align="center" prop="riskType" :show-overflow-tooltip="true" />
      <el-table-column label="边际最低资本贡献率表中的项目名称" align="center" prop="capitalItem" :show-overflow-tooltip="true" />
      <el-table-column label="相关系数表中的项目名称" align="center" prop="correlationItem" :show-overflow-tooltip="true" />
      <el-table-column label="上层级项目编码" align="center" prop="parentItemCode" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['minc:item:definition:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:item:definition:remove']"
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

    <!-- 添加或修改项目定义表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目编码" prop="itemCode">
              <el-input v-model="form.itemCode" placeholder="请输入项目编码，如：MR001" :disabled="form.id != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险类型" prop="riskType">
              <el-input v-model="form.riskType" placeholder="请输入风险类型，如：市场风险、信用风险" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="上层级项目编码" prop="parentItemCode">
              <el-input v-model="form.parentItemCode" placeholder="请输入上层级项目编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="边际最低资本贡献率项目" prop="capitalItem">
              <el-input v-model="form.capitalItem" placeholder="请输入边际最低资本贡献率表中的项目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相关系数表项目" prop="correlationItem">
              <el-input v-model="form.correlationItem" placeholder="请输入相关系数表中的项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="子风险最低资本因子公式" prop="subRiskFactorFormula">
              <el-input v-model="form.subRiskFactorFormula" type="textarea" placeholder="请输入子风险最低资本因子计算公式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="公司整体最低资本因子公式" prop="companyFactorFormula">
              <el-input v-model="form.companyFactorFormula" type="textarea" placeholder="请输入公司整体最低资本因子计算公式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="最低资本计算公式" prop="capitalCalculationFormula">
              <el-input v-model="form.capitalCalculationFormula" type="textarea" placeholder="请输入最低资本计算公式" />
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
import { listItemDefinition, getItemDefinition, addItemDefinition, updateItemDefinition, delItemDefinition, exportItemDefinition, getImportTemplate, importItemDefinition } from "@/api/minc/item/definition";
import { getToken } from "@/utils/auth";
import { saveAs } from 'file-saver';

export default {
  name: "ItemDefinition",
  dicts: ['sys_normal_disable'],
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
      // 项目定义表表格数据
      itemDefinitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemCode: null,
        riskType: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        itemCode: [
          { required: true, message: "项目编码不能为空", trigger: "blur" },
          { max: 20, message: "项目编码长度不能超过20个字符", trigger: "blur" }
        ],
        riskType: [
          { max: 50, message: "风险类型长度不能超过50个字符", trigger: "blur" }
        ],
        capitalItem: [
          { max: 100, message: "边际最低资本贡献率表中的项目名称长度不能超过100个字符", trigger: "blur" }
        ],
        correlationItem: [
          { max: 50, message: "相关系数表中的项目名称长度不能超过50个字符", trigger: "blur" }
        ],
        parentItemCode: [
          { max: 20, message: "上层级项目编码长度不能超过20个字符", trigger: "blur" }
        ],
        subRiskFactorFormula: [
          { max: 500, message: "子风险最低资本因子计算公式长度不能超过500个字符", trigger: "blur" }
        ],
        companyFactorFormula: [
          { max: 500, message: "公司整体最低资本因子计算公式长度不能超过500个字符", trigger: "blur" }
        ],
        capitalCalculationFormula: [
          { max: 500, message: "最低资本计算公式长度不能超过500个字符", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入项目定义表数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已有数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/minc/item/definition/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目定义表列表 */
    getList() {
      this.loading = true;
      listItemDefinition(this.queryParams).then(response => {
        let data = response.rows || [];

        // 按项目编码层级排序
        data.sort((a, b) => {
          const getItemCodeOrder = (itemCode) => {
            if (!itemCode) return 'ZZZZ_999_999_999_999';

            // 分析项目编码的层级结构
            const analyzeItemCode = (code) => {
              // 计算层级深度（通过下划线分隔符数量）
              const parts = code.split('_');
              const level = parts.length;

              // 提取前缀（风险类型）
              let prefix = '';
              const firstPart = parts[0] || '';
              const prefixMatch = firstPart.match(/^([A-Z]{2})/);
              if (prefixMatch) {
                prefix = prefixMatch[1];
              }

              // 风险类型排序
              const prefixOrder = {
                'NR': '01',  // 一般风险
                'MR': '02',  // 市场风险
                'CR': '03',  // 信用风险
                'IR': '04',  // 保险风险
                'OR': '05',  // 操作风险
                'LR': '06'   // 流动性风险
              };

              const orderPrefix = prefixOrder[prefix] || '99';

              // 构建排序键：层级_风险类型_各部分编号
              const paddedParts = parts.map(part => {
                // 提取数字部分并补零
                const numberMatch = part.match(/(\d+)/g);
                if (numberMatch) {
                  return numberMatch.map(num => num.padStart(3, '0')).join('_');
                }
                return part.padEnd(10, '0');
              });

              return `${String(level).padStart(2, '0')}_${orderPrefix}_${paddedParts.join('_')}`;
            };

            return analyzeItemCode(itemCode);
          };

          const aOrder = getItemCodeOrder(a.itemCode);
          const bOrder = getItemCodeOrder(b.itemCode);

          return aOrder.localeCompare(bOrder);
        });

        this.itemDefinitionList = data;
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
        itemCode: null,
        riskType: null,
        capitalItem: null,
        correlationItem: null,
        parentItemCode: null,
        subRiskFactorFormula: null,
        companyFactorFormula: null,
        capitalCalculationFormula: null,
        status: "1"
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
      this.title = "添加项目定义表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getItemDefinition(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目定义表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateItemDefinition(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addItemDefinition(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目定义表编号为"' + ids + '"的数据项？').then(function() {
        return delItemDefinition(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/item/definition/export',
        {
          ...this.queryParams,
        },
        `item_definition_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      // 显示加载中提示
      this.$modal.loading("正在下载模板，请稍候...");

      getImportTemplate().then(response => {
        this.$modal.closeLoading();
        // 使用saveAs直接下载
        try {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          saveAs(blob, '项目定义表模板.xlsx');
        } catch (error) {
          console.error("Excel下载失败", error);
          this.$modal.msgError("Excel下载失败：" + error.message);
        }
      }).catch(error => {
        this.$modal.closeLoading();
        this.$modal.msgError("下载模板失败：" + (error.message || error));
        console.error("下载模板失败", error);
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
