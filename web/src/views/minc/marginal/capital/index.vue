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
      <el-form-item label="项目" prop="itemCode">
        <el-input
          v-model="queryParams.itemCode"
          placeholder="请输入项目"
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
          v-hasPermi="['minc:marginal:capital:add']"
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
          v-hasPermi="['minc:marginal:capital:edit']"
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
          v-hasPermi="['minc:marginal:capital:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:marginal:capital:export']"
        >导出</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="marginalCapitalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <el-table-column label="项目" align="center" width="200">
        <template slot-scope="scope">
          <div>
            <div style="font-weight: bold; color: #409EFF; font-size: 14px;">{{ scope.row.itemName || scope.row.itemCode }}</div>
            <div style="font-size: 11px; color: #999;">{{ scope.row.itemCode }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="再保后金额" align="center" prop="reinsuAfterAmount" width="150">
        <template slot-scope="scope">
          <span>{{ formatNumber(scope.row.reinsuAfterAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="子风险边际因子" align="center" prop="subRiskMarginalFactor" width="140">
        <template slot-scope="scope">
          <span>{{ formatPercent(scope.row.subRiskMarginalFactor) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="公司边际因子" align="center" prop="companyMarginalFactor" width="140">
        <template slot-scope="scope">
          <span>{{ formatPercent(scope.row.companyMarginalFactor) }}</span>
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
            v-hasPermi="['minc:marginal:capital:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:marginal:capital:remove']"
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

    <!-- 添加或修改边际最低资本贡献率表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账期" prop="accountingPeriod">
              <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" :disabled="form.id != null" />
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
          <el-col :span="24">
            <el-form-item label="再保后金额" prop="reinsuAfterAmount">
              <el-input-number
                v-model="form.reinsuAfterAmount"
                placeholder="请输入再保后金额"
                :min="0"
                :precision="2"
                :step="1000"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="子风险边际因子" prop="subRiskMarginalFactor">
              <el-input-number
                v-model="form.subRiskMarginalFactor"
                placeholder="请输入子风险边际因子"
                :min="0"
                :max="1"
                :precision="4"
                :step="0.0001"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司边际因子" prop="companyMarginalFactor">
              <el-input-number
                v-model="form.companyMarginalFactor"
                placeholder="请输入公司边际因子"
                :min="0"
                :max="1"
                :precision="4"
                :step="0.0001"
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
import { listMarginalCapital, getMarginalCapital, addMarginalCapital, updateMarginalCapital, delMarginalCapital, exportMarginalCapital, getItemDefinitions } from "@/api/minc/marginal/capital";

export default {
  name: "MarginalCapital",
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
      // 边际最低资本贡献率表表格数据
      marginalCapitalList: [],
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
        reinsuAfterAmount: [
          { required: true, message: "再保后金额不能为空", trigger: "blur" },
          { type: 'number', min: 0, message: "再保后金额不能小于0", trigger: "blur" }
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
    /** 查询边际最低资本贡献率表列表 */
    getList() {
      this.loading = true;
      listMarginalCapital(this.queryParams).then(response => {
        let data = response.rows || [];

        // 按项目编码层级深度排序
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

        this.marginalCapitalList = data;
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
        reinsuAfterAmount: null,
        subRiskMarginalFactor: null,
        companyMarginalFactor: null
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
      this.title = "添加边际最低资本贡献率表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getMarginalCapital(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改边际最低资本贡献率表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMarginalCapital(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMarginalCapital(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除边际最低资本贡献率表编号为"' + ids + '"的数据项？').then(function() {
        return delMarginalCapital(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'minc/marginal/capital/export',
        {
          ...this.queryParams,
        },
        `marginal_capital_${new Date().getTime()}.xlsx`
      );
    },

    // 格式化数字
    formatNumber(value) {
      if (value == null || value === '') return '';
      return Number(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      });
    },
    // 格式化百分比
    formatPercent(value) {
      if (value == null || value === '') return '';
      return (Number(value) * 100).toFixed(2) + '%';
    }
  }
};
</script>
