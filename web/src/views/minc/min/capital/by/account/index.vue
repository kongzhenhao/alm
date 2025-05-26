<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountingPeriod">
        <el-input
          v-model="queryParams.accountingPeriod"
          placeholder="请输入账期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入项目名称"
          clearable
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
          v-hasPermi="['minc:min:capital:by:account:add']"
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
          v-hasPermi="['minc:min:capital:by:account:edit']"
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
          v-hasPermi="['minc:min:capital:by:account:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:min:capital:by:account:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="minCapitalByAccountList" @selection-change="handleSelectionChange">
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
      <el-table-column label="普通账户" align="center" prop="generalAmount" width="150">
        <template slot-scope="scope">
          {{ scope.row.generalAmount | formatNumber }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['minc:min:capital:by:account:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:min:capital:by:account:remove']"
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

    <!-- 添加或修改市场及信用最低资本表对话框 -->
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
                  key="市场风险最低资本"
                  label="市场风险最低资本"
                  value="市场风险最低资本"
                />
                <el-option
                  key="信用风险最低资本"
                  label="信用风险最低资本"
                  value="信用风险最低资本"
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
                  v-for="dict in filteredAccountList"
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
import { listMinCapitalByAccount, getMinCapitalByAccount, delMinCapitalByAccount, addMinCapitalByAccount, updateMinCapitalByAccount } from "@/api/minc/minCapitalByAccount";

export default {
  name: "MinCapitalByAccount",
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
      // 市场及信用最低资本表表格数据
      minCapitalByAccountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        itemCode: null,
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
  computed: {
    // 过滤账户列表，只显示传统、分红、万能、普通账户
    filteredAccountList() {
      if (!this.dict.type.minc_account) return [];
      return this.dict.type.minc_account.filter(account =>
        ['AC001', 'AC002', 'AC003', 'AC006'].includes(account.value)
      );
    }
  },
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
  created() {
    this.getList();
  },
  methods: {
    /** 查询市场及信用最低资本表列表 */
    getList() {
      this.loading = true;
      listMinCapitalByAccount(this.queryParams).then(response => {
        this.minCapitalByAccountList = response.rows;
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加市场及信用最低资本表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMinCapitalByAccount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改市场及信用最低资本表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 在新增时检查是否已存在相同的记录
          if (this.form.id == null) {
            // 检查当前列表中是否已有相同的记录
            const existRecord = this.minCapitalByAccountList.find(item =>
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
            updateMinCapitalByAccount(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              console.error("修改失败：", error);
              this.$modal.msgError("修改失败：" + (error.message || error));
            });
          } else {
            addMinCapitalByAccount(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除市场及信用最低资本表编号为"' + ids + '"的数据项？').then(function() {
        return delMinCapitalByAccount(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('minc/min/capital/by/account/export', {
        ...this.queryParams
      }, `市场及信用最低资本表_${new Date().getTime()}.xlsx`)
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

        const existRecord = this.minCapitalByAccountList.find(item =>
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