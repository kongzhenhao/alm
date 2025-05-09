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
      <el-form-item label="曲线类型" prop="curveType">
        <el-select v-model="queryParams.curveType" placeholder="请选择曲线类型" clearable>
          <el-option
            v-for="dict in dict.type.dur_curve_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
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
          ></el-option>
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
          v-hasPermi="['dur:discount:curve:add']"
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
          v-hasPermi="['dur:discount:curve:edit']"
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
          v-hasPermi="['dur:discount:curve:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:discount:curve:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['dur:discount:curve:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="discountCurveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" />
      <el-table-column label="曲线类型" align="center" prop="curveType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.dur_curve_type" :value="scope.row.curveType"/>
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
      <el-table-column label="曲线值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewCurveValSet(scope.row)"
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
            v-hasPermi="['dur:discount:curve:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:discount:curve:remove']"
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

    <!-- 添加或修改折现曲线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="账期" prop="accountPeriod">
              <el-input v-model="form.accountPeriod" placeholder="请输入账期，格式YYYYMM" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="曲线类型" prop="curveType">
              <el-select v-model="form.curveType" placeholder="请选择曲线类型">
                <el-option
                  v-for="dict in dict.type.dur_curve_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
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
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="曲线值集" prop="curveValSet">
              <el-input v-model="form.curveValSet" type="textarea" placeholder="请输入曲线值集" />
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
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="downloadTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 曲线值集查看对话框 -->
    <el-dialog :title="'曲线值集查看 - ' + curveValSetInfo.title" :visible.sync="curveValSetInfo.open" width="600px" append-to-body>
      <el-table :data="curveValSetInfo.tableData" height="400" border>
        <el-table-column prop="index" label="序号" width="80" align="center" />
        <el-table-column prop="date" label="日期" width="180" align="center" />
        <el-table-column prop="value" label="值" align="center" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="curveValSetInfo.open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDiscountCurve,
  getDiscountCurve,
  addDiscountCurve,
  updateDiscountCurve,
  delDiscountCurve,
  batchDelDiscountCurve,
  exportDiscountCurve,
  downloadTemplate
} from "@/api/dur/discountCurve";
import { getToken } from "@/utils/auth";

export default {
  name: "DiscountCurve",
  dicts: ['dur_curve_type', 'dur_duration_type', 'dur_bp_type'],
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
        curveType: [
          { required: true, message: "曲线类型不能为空", trigger: "change" }
        ],
        bpType: [
          { required: true, message: "基点类型不能为空", trigger: "change" }
        ],
        durationType: [
          { required: true, message: "久期类型不能为空", trigger: "change" }
        ],
        curveValSet: [
          { required: true, message: "曲线值集不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "导入折现曲线数据",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/discount/curve/importData"
      },
      // 曲线值集查看参数
      curveValSetInfo: {
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
    /** 查询折现曲线列表 */
    getList() {
      this.loading = true;
      listDiscountCurve(this.queryParams).then(response => {
        this.discountCurveList = response.rows;
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
        curveType: null,
        bpType: null,
        durationType: null,
        curveValSet: null
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
      this.title = "添加折现曲线";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getDiscountCurve(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改折现曲线";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDiscountCurve(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDiscountCurve(this.form).then(response => {
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
      if (row && row.id) {
        // 单个删除
        const id = row.id;
        this.$modal.confirm('是否确认删除折现曲线编号为"' + id + '"的数据项？').then(function() {
          return delDiscountCurve(id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      } else {
        // 批量删除
        const ids = this.ids;
        this.$modal.confirm('是否确认批量删除选中的' + ids.length + '条折现曲线数据？').then(function() {
          return batchDelDiscountCurve(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("批量删除成功");
        }).catch(() => {});
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/dur/discount/curve/export', {
        ...this.queryParams
      }, `discount_curve_${new Date().getTime()}.xlsx`);
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    /** 下载模板操作 */
    downloadTemplate() {
      downloadTemplate().then(response => {
        this.downloadFile(response, `discount_curve_template_${new Date().getTime()}.xlsx`);
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
    // 下载文件
    downloadFile(response, fileName) {
      const blob = new Blob([response], { type: 'application/vnd.ms-excel' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      URL.revokeObjectURL(link.href);
    },
    /** 查看曲线值集按钮操作 */
    handleViewCurveValSet(row) {
      try {
        // 清空之前的数据
        this.curveValSetInfo.tableData = [];

        // 设置标题
        this.curveValSetInfo.title = row.curveType ? this.dict.type.dur_curve_type.find(item => item.value === row.curveType)?.label || '' : '';

        // 解析JSON数据
        if (row.curveValSet) {
          // 先尝试直接解析
          try {
            const curveValSetData = JSON.parse(row.curveValSet);
            this.processValSetData(curveValSetData);
          } catch (parseError) {
            console.warn("初次解析失败，尝试直接提取数据", parseError);

            // 如果解析失败，直接使用正则表达式提取数据
            this.extractDataWithRegex(row.curveValSet);
          }
        } else {
          this.$message.warning("曲线值集数据为空");
        }

        // 打开对话框
        this.curveValSetInfo.open = true;
      } catch (error) {
        this.$modal.msgError("解析曲线值集数据失败：" + error.message);
        console.error("解析曲线值集数据失败", error);
      }
    },

    /**
     * 处理值集数据
     */
    processValSetData(curveValSetData) {
      const tableData = [];

      // 遍历并格式化数据
      Object.keys(curveValSetData).forEach(key => {
        const item = curveValSetData[key];
        if (item && typeof item === 'object') {
          tableData.push({
            index: key,
            date: item.date || '',
            value: item.val || item.value || 0
          });
        }
      });

      // 按序号排序
      this.curveValSetInfo.tableData = tableData.sort((a, b) => parseInt(a.index) - parseInt(b.index));
    },

    /**
     * 使用正则表达式提取数据
     */
    extractDataWithRegex(jsonString) {
      try {
        // 尝试使用多种正则表达式提取数据
        const tableData = [];

        // 尝试不同的正则表达式模式
        const patterns = [
          // 模式1: 标准格式 {"key":{"date":"value","val":value}}
          /["']?(\d+)["']?\s*:\s*{\s*["']?date["']?\s*:\s*["']([^"']+)["']\s*,\s*["'](?:val|value)["']\s*:\s*([\d\.]+)/g,

          // 模式2: 可能的变体格式 {"key":{"date":"value",value:value}}
          /["']?(\d+)["']?\s*:\s*{\s*["']?date["']?\s*:\s*["']([^"']+)["']\s*,\s*(?:val|value)\s*:\s*([\d\.]+)/g,

          // 模式3: 可能的变体格式 {key:{date:"value","val":value}}
          /(\d+)\s*:\s*{\s*date\s*:\s*["']([^"']+)["']\s*,\s*["']?(?:val|value)["']?\s*:\s*([\d\.]+)/g,

          // 模式4: 更宽松的模式，尝试捕获数字和日期格式
          /["']?(\d+)["']?\s*:\s*{[^}]*?["']?date["']?\s*:\s*["']([^"']+)["'][^}]*?(?:val|value)["']?\s*:\s*([\d\.]+)/g
        ];

        // 尝试每个模式
        for (const regex of patterns) {
          let match;
          const patternData = [];

          // 重置正则表达式的lastIndex
          regex.lastIndex = 0;

          while ((match = regex.exec(jsonString)) !== null) {
            patternData.push({
              index: match[1],
              date: match[2],
              value: parseFloat(match[3])
            });
          }

          // 如果这个模式有匹配，就使用这个模式的结果
          if (patternData.length > 0) {
            tableData.push(...patternData);
            break;
          }
        }

        // 如果没有模式匹配，尝试最后的方法：直接提取日期和数字
        if (tableData.length === 0) {
          // 提取所有日期格式的字符串
          const dateMatches = jsonString.match(/(\d{4}-\d{2}-\d{2})/g) || [];
          // 提取所有数字
          const valueMatches = jsonString.match(/"(?:val|value)"\s*:\s*([\d\.]+)/g) || [];
          const valueExtracted = valueMatches.map(v => parseFloat(v.replace(/"(?:val|value)"\s*:\s*/, '')));

          // 如果日期和值的数量相同，则可以配对
          if (dateMatches.length > 0 && dateMatches.length === valueExtracted.length) {
            for (let i = 0; i < dateMatches.length; i++) {
              tableData.push({
                index: i + 1,
                date: dateMatches[i],
                value: valueExtracted[i]
              });
            }
          }
        }

        if (tableData.length > 0) {
          // 去重，有时候不同模式可能匹配到相同的数据
          const uniqueData = [];
          const seen = new Set();

          tableData.forEach(item => {
            const key = `${item.index}-${item.date}`;
            if (!seen.has(key)) {
              seen.add(key);
              uniqueData.push(item);
            }
          });

          // 按序号排序
          this.curveValSetInfo.tableData = uniqueData.sort((a, b) => parseInt(a.index) - parseInt(b.index));
          this.$message.warning("曲线值集数据格式有误，已尝试提取并显示数据");
          return;
        }

        // 如果所有方法都失败，尝试显示原始数据
        if (jsonString.length > 0) {
          this.$message.warning("无法解析曲线值集数据，显示原始数据");
          this.curveValSetInfo.tableData = [{
            index: 1,
            date: '原始数据',
            value: jsonString.substring(0, 100) + (jsonString.length > 100 ? '...' : '')
          }];
          return;
        }

        throw new Error("无法提取数据");
      } catch (error) {
        console.error("提取数据失败", error);
        // 显示错误信息但不抛出异常，这样可以继续显示对话框
        this.$message.error("无法解析曲线值集数据，请检查数据格式");
        this.curveValSetInfo.tableData = [{
          index: 1,
          date: '错误信息',
          value: error.message
        }];
      }
    }
  }
};
</script>
