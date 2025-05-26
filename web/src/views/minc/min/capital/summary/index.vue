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
          v-hasPermi="['minc:min:capital:summary:add']"
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
          v-hasPermi="['minc:min:capital:summary:edit']"
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
          v-hasPermi="['minc:min:capital:summary:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:min:capital:summary:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="minCapitalSummaryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="项目" align="center" prop="itemName" :show-overflow-tooltip="true" />
      <el-table-column label="传统保险账户" align="center" prop="traditionalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.traditionalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="分红保险账户" align="center" prop="dividendAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.dividendAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="万能保险账户" align="center" prop="universalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.universalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="独立账户" align="center" prop="independentAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.independentAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="资本补充债账户" align="center" prop="companyTotalAmount" width="150">
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
            v-hasPermi="['minc:min:capital:summary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:min:capital:summary:remove']"
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

    <!-- 添加或修改最低资本明细汇总表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input
                v-model="form.accountingPeriod"
                placeholder="请输入账期，格式：YYYYMM"
                :disabled="form.id != null"
                @blur="checkUniqueKey"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目" prop="itemCode">
              <el-select
                v-model="form.itemCode"
                placeholder="请选择项目"
                clearable
                filterable
                style="width: 100%"
                :disabled="form.id != null"
                @change="checkUniqueKey"
              >
                <el-option
                  v-for="item in itemDefinitionList"
                  :key="item.itemCode"
                  :label="item.capitalItem"
                  :value="item.itemCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="账户" prop="accountCode">
              <el-select
                v-model="form.accountCode"
                placeholder="请选择账户"
                clearable
                filterable
                style="width: 100%"
                :disabled="form.id != null"
                @change="checkUniqueKey"
              >
                <el-option
                  v-for="dict in dict.type.minc_account"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户金额" prop="amount">
              <el-input-number
                v-model="form.amount"
                placeholder="请输入账户金额"
                :precision="10"
                :step="1000"
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
  </div>
</template>

<script>
import { listMinCapitalSummary, getMinCapitalSummary, addMinCapitalSummary, updateMinCapitalSummary, delMinCapitalSummary, exportMinCapitalSummary, getItemDefinitions } from "@/api/minc/min/capital/summary";

export default {
  name: "MinCapitalSummary",
  dicts: ['minc_account'],
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
      // 最低资本明细汇总表表格数据
      minCapitalSummaryList: [],
      // 项目定义列表数据
      itemDefinitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10, // 默认每页显示10条数据
        accountingPeriod: null,
        itemCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式不正确，应为YYYYMM格式", trigger: "blur" }
        ],
        itemCode: [
          { required: true, message: "项目不能为空", trigger: "change" }
        ],
        accountCode: [
          { required: true, message: "账户不能为空", trigger: "change" }
        ],
        amount: [
          { type: 'number', message: "账户金额必须为数字", trigger: "blur" }
        ]
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
      getItemDefinitions().then(response => {
        this.itemDefinitionList = response.data || [];
      }).catch(error => {
        console.error("获取项目定义列表失败", error);
        this.itemDefinitionList = [];
      });
    },

    /** 查询最低资本明细汇总表列表 */
    getList() {
      this.loading = true;

      // 修改查询参数，获取所有数据
      const params = {
        ...this.queryParams,
        pageNum: 1,
        pageSize: 500, // 设置一个足够大的值，获取所有数据，但不要太大以避免性能问题
        isDel: 0 // 确保只查询未删除的记录
      };

      listMinCapitalSummary(params).then(response => {
        // 获取所有数据
        let allData = response.rows || [];

        // 前端排序：按项目类别智能分组
        allData.sort((a, b) => {
          // 提取项目类别（风险类型或项目名称的前缀）
          const getProjectCategory = (item) => {
            // 优先使用风险类型
            if (item.riskType) {
              return item.riskType;
            }
            // 如果没有风险类型，尝试从项目名称中提取类别
            const itemName = item.itemName || '';
            // 提取项目名称中的关键词作为分类依据
            if (itemName.includes('市场风险') || itemName.includes('市场')) {
              return '市场风险';
            } else if (itemName.includes('信用风险') || itemName.includes('信用')) {
              return '信用风险';
            } else if (itemName.includes('保险风险') || itemName.includes('保险')) {
              return '保险风险';
            } else if (itemName.includes('操作风险') || itemName.includes('操作')) {
              return '操作风险';
            }
            // 如果都不匹配，使用项目名称的第一部分作为分类
            const parts = itemName.split(/[-_\s]/);
            return parts[0] || '其他';
          };

          const aCategoryKey = getProjectCategory(a);
          const bCategoryKey = getProjectCategory(b);

          // 首先按类别排序（字母顺序）
          if (aCategoryKey !== bCategoryKey) {
            return aCategoryKey.localeCompare(bCategoryKey);
          }

          // 同一类别内按项目名称排序
          if (a.itemName !== b.itemName) {
            return (a.itemName || '').localeCompare(b.itemName || '');
          }

          // 同一项目内按账户编码排序
          return (a.accountCode || '').localeCompare(b.accountCode || '');
        });

        this.total = allData.length;

        // 在前端进行分页
        const pageSize = this.queryParams.pageSize;
        const pageNum = this.queryParams.pageNum;
        const startIndex = (pageNum - 1) * pageSize;
        const endIndex = startIndex + pageSize;

        // 获取当前页的数据
        this.minCapitalSummaryList = allData.slice(startIndex, endIndex);

        // 打印调试信息
        console.log("总数据条数：" + this.total);
        console.log("当前页数据条数：" + this.minCapitalSummaryList.length);
        console.log("数据示例：", this.minCapitalSummaryList.length > 0 ? this.minCapitalSummaryList[0] : "无数据");

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
        itemCode: null,
        accountCode: null,
        amount: null
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
      this.title = "添加最低资本明细汇总表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getMinCapitalSummary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改最低资本明细汇总表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 在新增时检查是否已存在相同的记录
          if (this.form.id == null) {
            // 检查当前列表中是否已有相同的记录
            const existRecord = this.minCapitalSummaryList.find(item =>
              item.accountingPeriod === this.form.accountingPeriod &&
              item.itemCode === this.form.itemCode &&
              item.accountCode === this.form.accountCode
            );

            if (existRecord) {
              this.$modal.msgError("该账期、项目和账户的记录已存在，不能重复添加");
              return;
            }
          }

          if (this.form.id != null) {
            updateMinCapitalSummary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              console.error("修改失败：", error);
              this.$modal.msgError("修改失败：" + (error.message || error));
            });
          } else {
            addMinCapitalSummary(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              console.error("新增失败：", error);
              this.$modal.msgError("新增失败：" + (error.message || error));
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除最低资本明细汇总表编号为"' + ids + '"的数据项？').then(function() {
        return delMinCapitalSummary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/min/capital/summary/export',
        {
          ...this.queryParams,
        },
        `min_capital_summary_${new Date().getTime()}.xlsx`
      );
    },
    // 格式化数字
    formatNumber(value) {
      if (value == null || value === '') return '';
      return Number(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 10
      });
    },
    /** 检查唯一键是否重复 */
    checkUniqueKey() {
      // 只在新增时检查，且三个字段都有值时才检查
      if (this.form.id == null &&
          this.form.accountingPeriod &&
          this.form.itemCode &&
          this.form.accountCode) {

        const existRecord = this.minCapitalSummaryList.find(item =>
          item.accountingPeriod === this.form.accountingPeriod &&
          item.itemCode === this.form.itemCode &&
          item.accountCode === this.form.accountCode
        );

        if (existRecord) {
          this.$message.warning("该账期、项目和账户的记录已存在");
        }
      }
    }
  }
};
</script>
