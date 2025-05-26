<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期，格式：YYYYMM"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目X" prop="itemCodeX">
        <el-input
          v-model="queryParams.itemCodeX"
          placeholder="请输入第一个风险项目"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目Y" prop="itemCodeY">
        <el-input
          v-model="queryParams.itemCodeY"
          placeholder="请输入第二个风险项目"
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
          v-hasPermi="['minc:correlation:coef:add']"
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
          v-hasPermi="['minc:correlation:coef:edit']"
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
          v-hasPermi="['minc:correlation:coef:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:correlation:coef:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['minc:correlation:coef:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="correlationCoefList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="项目X" align="center" width="180">
        <template slot-scope="scope">
          <div>
            <div style="font-weight: bold; color: #409EFF; font-size: 14px;">{{ scope.row.itemNameX || scope.row.itemCodeX }}</div>
            <div style="font-size: 11px; color: #999;">{{ scope.row.itemCodeX }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="项目Y" align="center" width="180">
        <template slot-scope="scope">
          <div>
            <div style="font-weight: bold; color: #409EFF; font-size: 14px;">{{ scope.row.itemNameY || scope.row.itemCodeY }}</div>
            <div style="font-size: 11px; color: #999;">{{ scope.row.itemCodeY }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="相关系数" align="center" prop="correlationValue" width="120" />
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
            v-hasPermi="['minc:correlation:coef:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:correlation:coef:remove']"
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

    <!-- 添加或修改相关系数表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" :disabled="form.id != null" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目X" prop="itemCodeX">
              <el-select
                v-model="form.itemCodeX"
                placeholder="请选择第一个风险项目"
                clearable
                filterable
                style="width: 100%"
                :disabled="form.id != null"
              >
                <el-option
                  v-for="item in itemDefinitionList"
                  :key="item.itemCode"
                  :label="item.correlationItem"
                  :value="item.itemCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目Y" prop="itemCodeY">
              <el-select
                v-model="form.itemCodeY"
                placeholder="请选择第二个风险项目"
                clearable
                filterable
                style="width: 100%"
                :disabled="form.id != null"
              >
                <el-option
                  v-for="item in itemDefinitionList"
                  :key="item.itemCode"
                  :label="item.correlationItem"
                  :value="item.itemCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="相关系数" prop="correlationValue">
              <el-input-number
                v-model="form.correlationValue"
                placeholder="请输入相关系数，范围[-1,1]"
                :min="-1"
                :max="1"
                :precision="2"
                :step="0.01"
                style="width: 100%"
              />
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
import { listCorrelationCoef, getCorrelationCoef, addCorrelationCoef, updateCorrelationCoef, delCorrelationCoef, exportCorrelationCoef, getImportTemplate, importCorrelationCoef, getItemDefinitions } from "@/api/minc/correlation/coef";
import { getToken } from "@/utils/auth";
import { saveAs } from 'file-saver';

export default {
  name: "CorrelationCoef",
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
      // 相关系数表表格数据
      correlationCoefList: [],
      // 项目定义列表数据
      itemDefinitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        itemCodeX: null,
        itemCodeY: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式不正确，应为YYYYMM格式", trigger: "blur" }
        ],
        itemCodeX: [
          { required: true, message: "第一个风险项目编码不能为空", trigger: "blur" },
          { max: 20, message: "第一个风险项目编码长度不能超过20个字符", trigger: "blur" }
        ],
        itemCodeY: [
          { required: true, message: "第二个风险项目编码不能为空", trigger: "blur" },
          { max: 20, message: "第二个风险项目编码长度不能超过20个字符", trigger: "blur" }
        ],
        correlationValue: [
          { required: true, message: "相关系数不能为空", trigger: "blur" },
          { type: 'number', min: -1, max: 1, message: "相关系数范围应在-1到1之间", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入相关系数表数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已有数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/minc/correlation/coef/importData"
      }
    };
  },
  created() {
    this.getList();
    this.getItemDefinitionList();
  },
  methods: {
    /** 获取项目定义列表 */
    getItemDefinitionList() {
      console.log("开始获取项目定义列表...");
      getItemDefinitions().then(response => {
        console.log("项目定义列表响应:", response);
        this.itemDefinitionList = response.data || [];
        console.log("设置的项目定义列表数量:", this.itemDefinitionList.length);
        if (this.itemDefinitionList.length > 0) {
          console.log("第一条数据示例:", this.itemDefinitionList[0]);
        }
      }).catch(error => {
        console.error("获取项目定义列表失败", error);
        this.itemDefinitionList = [];
      });
    },
    /** 查询相关系数表列表 */
    getList() {
      this.loading = true;
      listCorrelationCoef(this.queryParams).then(response => {
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

          // 首先按项目X排序
          const aOrderX = getItemCodeOrder(a.itemCodeX);
          const bOrderX = getItemCodeOrder(b.itemCodeX);

          if (aOrderX !== bOrderX) {
            return aOrderX.localeCompare(bOrderX);
          }

          // 项目X相同时，按项目Y排序
          const aOrderY = getItemCodeOrder(a.itemCodeY);
          const bOrderY = getItemCodeOrder(b.itemCodeY);

          return aOrderY.localeCompare(bOrderY);
        });

        this.correlationCoefList = data;
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
        itemCodeX: null,
        itemCodeY: null,
        correlationValue: null
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
      this.title = "添加相关系数表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getCorrelationCoef(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改相关系数表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCorrelationCoef(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCorrelationCoef(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除相关系数表编号为"' + ids + '"的数据项？').then(function() {
        return delCorrelationCoef(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/correlation/coef/export',
        {
          ...this.queryParams,
        },
        `correlation_coef_${new Date().getTime()}.xlsx`
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
          saveAs(blob, '相关系数表模板.xlsx');
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
