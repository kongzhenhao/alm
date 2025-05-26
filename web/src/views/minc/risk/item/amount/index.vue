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
      <el-form-item label="项目" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
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
          v-hasPermi="['minc:risk:item:amount:add']"
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
          v-hasPermi="['minc:risk:item:amount:edit']"
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
          v-hasPermi="['minc:risk:item:amount:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['minc:risk:item:amount:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['minc:risk:item:amount:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="riskItemAmountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账期" align="center" prop="accountingPeriod" width="100" />
      <!-- 隐藏项目编码列 -->
      <el-table-column label="项目名称" align="center" prop="itemName" width="200" />
      <el-table-column label="S05-最低资本表的期末金额" align="center" prop="s05Amount" width="250">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.s05Amount) }}
        </template>
      </el-table-column>
      <el-table-column label="IR05-寿险业务保险风险表的期末金额" align="center" prop="ir05Amount" width="300">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.ir05Amount) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['minc:risk:item:amount:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['minc:risk:item:amount:remove']"
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

    <!-- 添加或修改风险项目金额对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账期" prop="accountingPeriod">
          <el-input v-model="form.accountingPeriod" placeholder="请输入账期，格式：YYYYMM" />
        </el-form-item>
        <el-form-item label="项目名称" prop="itemName">
          <el-select v-model="form.itemName" placeholder="请选择项目" style="width: 100%">
            <el-option
              v-for="item in itemNameOptions"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="S05金额" prop="s05Amount">
          <el-input v-model="form.s05Amount" placeholder="请输入S05-最低资本表的期末金额" style="width: 100%">
            <template slot="append">
              <el-tooltip content="支持输入最大30位数字，小数点后10位" placement="top">
                <i class="el-icon-info"></i>
              </el-tooltip>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="IR05金额" prop="ir05Amount">
          <el-input v-model="form.ir05Amount" placeholder="请输入IR05-寿险业务保险风险表的期末金额" style="width: 100%">
            <template slot="append">
              <el-tooltip content="支持输入最大30位数字，小数点后10位" placement="top">
                <i class="el-icon-info"></i>
              </el-tooltip>
            </template>
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
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div class="dialog-footer">
        <el-checkbox v-model="upload.updateSupport" />更新已有数据
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRiskItemAmount, getRiskItemAmount, addRiskItemAmount, updateRiskItemAmount, delRiskItemAmount, delRiskItemAmountByCondition, exportRiskItemAmount, exportRiskItemAmountTemplate } from "@/api/minc/risk/item/amount";
import { getToken } from "@/utils/auth";
import { saveAs } from 'file-saver';

export default {
  name: "RiskItemAmount",
  dicts: ['minc_risk_item'], // 使用字典数据
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
      // 风险项目金额表格数据
      riskItemAmountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 项目名称选项，将在created钩子中从字典数据加载
      itemNameOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountingPeriod: null,
        itemName: null,
        itemCode: null // 保留itemCode字段，但在前端不显示
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountingPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        itemName: [
          { required: true, message: "项目名称不能为空", trigger: "change" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入风险项目金额数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已有数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/minc/risk/item/amount/importData"
      }
    };
  },
  created() {
    this.getList();
    // 监听字典数据加载完成事件
    this.$nextTick(() => {
      // 从字典数据加载项目名称选项
      if (this.dict.type && this.dict.type.minc_risk_item) {
        this.itemNameOptions = this.dict.type.minc_risk_item.map(item => item.label);
      }
    });
  },
  methods: {
    /** 查询风险项目金额列表 */
    getList() {
      this.loading = true;

      // 处理查询参数，支持模糊搜索
      const params = {
        ...this.queryParams
      };

      listRiskItemAmount(params).then(response => {
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

        this.riskItemAmountList = data;
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
        itemCode: null, // 保留itemCode字段，但在表单中不显示
        s05Amount: 0,
        ir05Amount: 0
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
      this.title = "添加风险项目金额";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0]
      getRiskItemAmount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改风险项目金额";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 处理金额，确保是数字格式
          const processAmount = (amount) => {
            if (amount === null || amount === undefined || amount === '') {
              return 0;
            }

            try {
              // 移除可能的千分位分隔符和其他非数字字符（除了小数点和负号）
              const cleanedAmount = String(amount).replace(/[^\d.-]/g, '');

              // 检查是否是有效的数字格式
              if (!/^-?\d*\.?\d*$/.test(cleanedAmount)) {
                console.warn('无效的数字格式:', amount);
                return 0;
              }

              // 直接返回字符串形式的数字，避免JavaScript浮点数精度问题
              // 后端会正确处理字符串形式的数字
              return cleanedAmount === '' ? 0 : cleanedAmount;
            } catch (error) {
              console.error('处理金额出错:', error);
              return 0;
            }
          };

          // 根据项目名称获取项目编码，从字典数据中获取
          const getItemCodeByName = (itemName) => {
            // 从字典数据中查找项目编码
            if (this.dict.type && this.dict.type.minc_risk_item) {
              const dictItem = this.dict.type.minc_risk_item.find(item => item.label === itemName);
              if (dictItem) {
                return dictItem.value;
              }
            }
            return "UNKNOWN"; // 如果没有找到，返回未知项目编码
          };

          // 处理表单数据
          const data = {
            ...this.form,
            itemCode: getItemCodeByName(this.form.itemName), // 根据项目名称获取项目编码
            s05Amount: processAmount(this.form.s05Amount),
            ir05Amount: processAmount(this.form.ir05Amount)
          };

          // 显示加载中提示
          this.$modal.loading("正在处理中，请稍候...");

          if (this.form.id != null) {
            updateRiskItemAmount(data).then(response => {
              this.$modal.closeLoading();
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              this.$modal.closeLoading();
              this.$modal.msgError("修改失败：" + (error.message || error));
            });
          } else {
            addRiskItemAmount(data).then(response => {
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
          return delRiskItemAmount(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      } else {
        // 单条删除，使用条件删除，一次性删除所有相关记录
        this.$modal.confirm('是否确认删除"' + row.itemName + '"的所有数据？').then(function() {
          return delRiskItemAmountByCondition(row.accountingPeriod, row.itemCode);
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
      this.$modal.confirm('是否确认导出所有风险项目金额数据项？').then(() => {
        this.exportLoading = true;
        return exportRiskItemAmount(this.queryParams);
      }).then(response => {
        try {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          saveAs(blob, '风险项目金额数据.xlsx');
        } catch (error) {
          console.error("Excel导出失败", error);
          this.$modal.msgError("Excel导出失败：" + error.message);
        }
        this.exportLoading = false;
      }).catch(() => {
        this.exportLoading = false;
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      // 显示加载中提示
      this.$modal.loading("正在下载模板，请稍候...");

      exportRiskItemAmountTemplate().then(response => {
        this.$modal.closeLoading();
        // 使用saveAs直接下载
        try {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          saveAs(blob, '风险项目金额模板.xlsx');
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
      this.upload.isUploading = false;
      this.upload.open = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 格式化金额
    formatAmount(amount) {
      if (amount === null || amount === undefined) {
        return '0.00';
      }

      try {
        // 将数字转换为字符串
        let numStr = String(amount);

        // 处理科学计数法表示的数字
        if (numStr.includes('e') || numStr.includes('E')) {
          // 使用BigNumber或其他库处理科学计数法会更准确，这里使用简单方法
          const parts = numStr.split(/e/i);
          const base = parts[0];
          const exponent = parseInt(parts[1]);

          if (exponent > 0) {
            // 正指数，向右移动小数点
            numStr = base.replace('.', '').padEnd(base.replace('.', '').length + exponent, '0');
            if (base.includes('.')) {
              const decimalPlaces = base.split('.')[1].length;
              if (exponent < decimalPlaces) {
                numStr = numStr.slice(0, numStr.length - decimalPlaces + exponent) + '.' + numStr.slice(numStr.length - decimalPlaces + exponent);
              }
            }
          } else {
            // 负指数，向左移动小数点
            numStr = '0.' + '0'.repeat(Math.abs(exponent) - 1) + base.replace('.', '');
          }
        }

        // 分离整数部分和小数部分
        const parts = numStr.split('.');
        let integerPart = parts[0];
        const decimalPart = parts.length > 1 ? parts[1] : '';

        // 添加千分位分隔符到整数部分
        integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

        // 保留两位小数
        const formattedDecimal = decimalPart.length > 2 ? decimalPart.substring(0, 2) : decimalPart.padEnd(2, '0');

        // 组合整数部分和小数部分
        return integerPart + '.' + formattedDecimal;
      } catch (error) {
        console.error('格式化金额出错:', error);
        // 出错时返回原始值
        return amount;
      }
    }
  }
};
</script>
