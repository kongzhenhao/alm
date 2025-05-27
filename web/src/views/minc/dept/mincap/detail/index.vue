<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期，格式：YYYYMM"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门名称" prop="department">
        <el-input
          v-model="queryParams.department"
          placeholder="请输入部门名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入项目"
          clearable
          style="width: 240px"
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
          v-hasPermi="['minc:dept:mincap:detail:add']"
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
          v-hasPermi="['minc:dept:mincap:detail:edit']"
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
          v-hasPermi="['minc:dept:mincap:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:dept:mincap:detail:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['minc:dept:mincap:detail:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deptMincapDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="部门名称" align="left" prop="department" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="项目" align="left" prop="itemName" :show-overflow-tooltip="true" />
      <el-table-column label="传统保险账户" align="left" prop="traditionalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.traditionalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="分红保险账户" align="left" prop="dividendAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.dividendAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="万能保险账户" align="left" prop="universalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.universalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="独立账户" align="left" prop="independentAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.independentAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="资本补充债账户" align="left" prop="companyTotalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.companyTotalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['minc:dept:mincap:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:dept:mincap:detail:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[10, 20, 30, 50]"
      @pagination="getList"
    />

    <!-- 添加或修改分部门最低资本明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="账期" prop="accountingPeriod">
          <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" />
        </el-form-item>
        <el-form-item label="部门名称" prop="department">
          <el-input v-model="form.department" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="项目" prop="itemCode">
          <el-select v-model="form.itemCode" placeholder="请选择项目" style="width: 100%">
            <el-option
              v-for="dict in dict.type.minc_risk_item"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-divider content-position="center">账户金额</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="传统保险账户" prop="traditionalAmount">
              <el-input v-model="form.traditionalAmount" placeholder="请输入金额" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分红保险账户" prop="dividendAmount">
              <el-input v-model="form.dividendAmount" placeholder="请输入金额" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="万能保险账户" prop="universalAmount">
              <el-input v-model="form.universalAmount" placeholder="请输入金额" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="独立账户" prop="independentAmount">
              <el-input v-model="form.independentAmount" placeholder="请输入金额" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资本补充债账户" prop="companyTotalAmount">
              <el-input v-model="form.companyTotalAmount" placeholder="请输入金额" style="width: 100%" />
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
      <el-form :model="upload" label-width="120px">
        <el-form-item label="是否更新已有数据">
          <el-radio-group v-model="upload.updateSupport">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDeptMincapDetail,
  getDeptMincapDetail,
  addDeptMincapDetail,
  batchAddDeptMincapDetail,
  updateDeptMincapDetail,
  delDeptMincapDetail,
  delDeptMincapDetailByCondition,
  exportDeptMincapDetail
} from "@/api/minc/dept/mincap/detail";
import { getToken } from "@/utils/auth";

export default {
  name: "DeptMincapDetail",
  dicts: ['minc_risk_item', 'minc_account'],
  filters: {
    // 数字格式化过滤器，添加千分位分隔符
    formatNumber(value) {
      if (!value) return '0.00';
      // 将数字转换为字符串，并保留两位小数
      const num = parseFloat(value).toFixed(2);
      // 添加千分位分隔符
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
  },
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
      // 分部门最低资本明细表格数据
      deptMincapDetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10, // 默认每页显示10条数据
        accountingPeriod: null,
        department: null,
        itemCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        department: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
          { max: 100, message: "部门名称长度不能超过100个字符", trigger: "blur" }
        ],
        itemCode: [
          { required: true, message: "项目不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入分部门最低资本明细数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已有数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/minc/dept/mincap/detail/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分部门最低资本明细列表 */
    getList() {
      this.loading = true;

      // 修改查询参数，获取所有数据
      const params = {
        ...this.queryParams,
        pageNum: 1,
        pageSize: 500, // 设置一个足够大的值，获取所有数据，但不要太大以避免性能问题
        isDel: 0 // 确保只查询未删除的记录
      };

      listDeptMincapDetail(params).then(response => {
        // 获取所有数据
        let allData = response.rows || [];

        // 按部门和项目编码层级排序
        allData.sort((a, b) => {
          // 首先按部门排序
          const aDepartment = a.department || '';
          const bDepartment = b.department || '';

          if (aDepartment !== bDepartment) {
            return aDepartment.localeCompare(bDepartment);
          }

          // 部门相同时，按项目编码层级排序
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

        this.total = allData.length;

        // 在前端进行分页
        const pageSize = this.queryParams.pageSize;
        const pageNum = this.queryParams.pageNum;
        const startIndex = (pageNum - 1) * pageSize;
        const endIndex = startIndex + pageSize;

        // 获取当前页的数据
        this.deptMincapDetailList = allData.slice(startIndex, endIndex);

        // 打印调试信息
        console.log("总数据条数：" + this.total);
        console.log("当前页数据条数：" + this.deptMincapDetailList.length);
        console.log("数据示例：", this.deptMincapDetailList.length > 0 ? this.deptMincapDetailList[0] : "无数据");

        this.loading = false;
      }).catch(error => {
        console.error("获取数据失败：", error);
        this.loading = false;
        this.$modal.msgError("获取数据失败：" + (error.message || error));
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
        department: null,
        itemCode: null,
        traditionalAmount: 0,
        dividendAmount: 0,
        universalAmount: 0,
        independentAmount: 0,
        companyTotalAmount: 0
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
      this.title = "添加分部门最低资本明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];

      // 显示加载中提示
      this.$modal.loading("正在加载数据，请稍候...");

      // 查询条件
      const query = {
        accountingPeriod: row.accountingPeriod,
        department: row.department,
        itemCode: row.itemCode,
        isDel: 0 // 确保只查询未删除的记录
      };

      // 获取所有相关记录
      listDeptMincapDetail(query).then(response => {
        const records = response.rows || [];

        // 关闭加载提示
        this.$modal.closeLoading();

        // 初始化表单数据
        this.form = {
          id: id,
          accountingPeriod: row.accountingPeriod,
          department: row.department,
          itemCode: row.itemCode,
          itemName: row.itemName,
          traditionalAmount: row.traditionalAmount || 0,
          dividendAmount: row.dividendAmount || 0,
          universalAmount: row.universalAmount || 0,
          independentAmount: row.independentAmount || 0,
          companyTotalAmount: row.companyTotalAmount || 0
        };

        // 填充各账户类型的金额
        if (records && records.length > 0) {
          for (const record of records) {
            if (record.accountCode === 'AC001') {
              this.form.traditionalAmount = record.amount !== null ? record.amount : 0;
            } else if (record.accountCode === 'AC002') {
              this.form.dividendAmount = record.amount !== null ? record.amount : 0;
            } else if (record.accountCode === 'AC003') {
              this.form.universalAmount = record.amount !== null ? record.amount : 0;
            } else if (record.accountCode === 'AC004') {
              this.form.independentAmount = record.amount !== null ? record.amount : 0;
            } else if (record.accountCode === 'AC005') {
              this.form.companyTotalAmount = record.amount !== null ? record.amount : 0;
            }
          }
        }

        // 打印调试信息
        console.log("修改表单数据:", this.form);

        // 延迟打开对话框，确保数据已经正确渲染
        setTimeout(() => {
          this.open = true;
          this.title = "修改分部门最低资本明细";
        }, 100);
      }).catch(error => {
        this.$modal.closeLoading();
        this.$modal.msgError("加载数据失败：" + (error.message || error));
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 获取当前账期
          const currentPeriod = this.form.accountingPeriod;

          // 如果账期为空，则使用当前日期作为账期
          if (!currentPeriod) {
            // 获取当前日期，格式为YYYYMM
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            this.form.accountingPeriod = `${year}${month}`;
          }

          // 处理金额，确保是数字格式
          const processAmount = (amount) => {
            if (amount === null || amount === undefined || amount === '') {
              return 0;
            }
            // 移除可能的千分位分隔符和其他非数字字符（除了小数点和负号）
            const cleanedAmount = String(amount).replace(/[^\d.-]/g, '');
            // 转换为数字
            return parseFloat(cleanedAmount) || 0;
          };

          // 准备单条提交的数据
          const data = {
            accountingPeriod: this.form.accountingPeriod,
            department: this.form.department,
            itemCode: this.form.itemCode,
            itemName: this.form.itemName,
            // 添加一个默认的账户编码，避免后端验证失败
            accountCode: "DEFAULT",
            traditionalAmount: processAmount(this.form.traditionalAmount),
            dividendAmount: processAmount(this.form.dividendAmount),
            universalAmount: processAmount(this.form.universalAmount),
            independentAmount: processAmount(this.form.independentAmount),
            companyTotalAmount: processAmount(this.form.companyTotalAmount)
          };

          console.log("提交数据:", data);

          // 如果是修改操作，添加ID
          if (this.form.id != null) {
            data.id = this.form.id;
          }

          // 显示加载中提示
          this.$modal.loading("正在处理中，请稍候...");

          if (this.form.id != null) {
            // 修改操作
            updateDeptMincapDetail(data).then(response => {
              this.$modal.closeLoading();
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              this.$modal.closeLoading();
              this.$modal.msgError("修改失败：" + (error.message || error));
            });
          } else {
            // 新增操作
            addDeptMincapDetail(data).then(response => {
              this.$modal.closeLoading();
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              this.$modal.closeLoading();
              this.$modal.msgError("新增失败：" + (error.message || error));
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      // 如果是批量删除
      if (this.ids && this.ids.length > 0) {
        const ids = this.ids;
        this.$modal.confirm('是否确认删除选中的' + ids.length + '条数据项？').then(function() {
          return delDeptMincapDetail(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      } else {
        // 单条删除，使用条件删除，一次性删除所有相关记录
        this.$modal.confirm('是否确认删除"' + row.department + ' - ' + row.itemName + '"的所有账户数据？').then(function() {
          return delDeptMincapDetailByCondition(row.accountingPeriod, row.department, row.itemCode);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch((error) => {
          this.$modal.msgError("删除失败：" + (error.message || error));
        });
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/dept/mincap/detail/export',
        {
          ...this.queryParams
        },
        `dept_mincap_detail_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('minc/dept/mincap/detail/exportTemplate', {}, `dept_mincap_detail_template_${new Date().getTime()}.xlsx`);
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

  }
};
</script>
