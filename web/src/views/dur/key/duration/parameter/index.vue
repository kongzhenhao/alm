<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账期" prop="accountPeriod">
        <el-input
          v-model="queryParams.accountPeriod"
          placeholder="请输入账期"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关键期限点" prop="keyDuration">
        <el-input
          v-model="queryParams.keyDuration"
          placeholder="请输入关键期限点"
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
          v-hasPermi="['dur:key:duration:parameter:add']"
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
          v-hasPermi="['dur:key:duration:parameter:edit']"
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
          v-hasPermi="['dur:key:duration:parameter:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dur:key:duration:parameter:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['dur:key:duration:parameter:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="keyDurationParameterList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="false" />
      <el-table-column label="账期" align="center" prop="accountPeriod" width="120" />
      <el-table-column label="关键期限点" align="center" prop="keyDuration" width="120" />
      <el-table-column label="参数值集" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewParameterValSet(scope.row)"
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
            v-hasPermi="['dur:key:duration:parameter:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dur:key:duration:parameter:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrapper">
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="handlePagination"
      />
    </div>

    <!-- 添加或修改关键久期参数对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="账期" prop="accountPeriod">
          <el-input v-model="form.accountPeriod" placeholder="请输入账期，格式YYYYMM" maxlength="6" />
        </el-form-item>
        <el-form-item label="关键期限点" prop="keyDuration">
          <el-input v-model="form.keyDuration" placeholder="请输入关键期限点，可以是整数、小数或其他数值" />
          <span class="el-form-item__description">关键期限点可以是整数、小数或其他数值，如：1、2.5、0.001、10000等</span>
        </el-form-item>
        <el-form-item label="关键久期参数值集" prop="parameterValSet">
          <el-input v-model="form.parameterValSet" type="textarea" placeholder="请输入关键久期参数值集" :rows="10" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 参数值集查看对话框 -->
    <el-dialog :title="'参数值集查看 - ' + parameterValSetInfo.title" :visible.sync="parameterValSetInfo.open" width="600px" append-to-body :close-on-click-modal="!parameterValSetInfo.loading">
      <div v-loading="parameterValSetInfo.loading" element-loading-text="正在加载参数值集数据，请稍候...">
        <el-table :data="parameterValSetInfo.tableData" height="400" border v-if="!parameterValSetInfo.loading || parameterValSetInfo.tableData.length > 0">
          <el-table-column prop="index" label="序号" width="80" align="center" />
          <el-table-column prop="date" label="日期" width="180" align="center" />
          <el-table-column prop="value" label="值" align="center" />
        </el-table>
        <div v-if="!parameterValSetInfo.loading && parameterValSetInfo.tableData.length === 0" class="empty-data">
          <i class="el-icon-warning"></i>
          <p>暂无数据</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="parameterValSetInfo.open = false" :disabled="parameterValSetInfo.loading">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body :close-on-click-modal="false" :close-on-press-escape="false">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :on-error="handleFileError"
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
      <div v-if="upload.isUploading" class="upload-loading">
        <el-progress :percentage="upload.percentage" :stroke-width="15" status="warning"></el-progress>
        <div class="upload-loading-text">正在导入数据，请耐心等待...</div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading" :disabled="upload.isUploading">确 定</el-button>
        <el-button @click="cancelImport" :disabled="upload.isUploading">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listKeyDurationParameter,
  getKeyDurationParameter,
  getKeyDurationParameterWithValSet,
  addKeyDurationParameter,
  updateKeyDurationParameter,
  delKeyDurationParameter,
  exportKeyDurationParameter,
  downloadTemplate,
  importKeyDurationParameter
} from "@/api/dur/keyDurationParameter";
import { getToken } from "@/utils/auth";

export default {
  name: "KeyDurationParameter",
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
      // 关键久期参数表格数据
      keyDurationParameterList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 参数值集查看参数
      parameterValSetInfo: {
        open: false,
        title: "",
        tableData: [],
        loading: false
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountPeriod: null,
        keyDuration: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^\d{6}$/, message: "账期格式必须为YYYYMM", trigger: "blur" }
        ],
        keyDuration: [
          { required: true, message: "关键期限点不能为空", trigger: "blur" }
        ],
        parameterValSet: [
          { required: true, message: "关键久期参数值集不能为空", trigger: "blur" }
        ]
      },
      // 上传参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 上传进度
        percentage: 0,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/dur/key/duration/parameter/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询关键久期参数列表 */
    getList() {
      this.loading = true;

      // 修改查询参数，获取所有数据
      const params = {
        ...this.queryParams,
        pageNum: 1,
        pageSize: 1000 // 设置一个足够大的值，获取所有数据
      };

      listKeyDurationParameter(params).then(response => {
        // 获取数据
        let allRows = response.rows;

        // 定义关键期限点的自定义排序顺序
        const customOrder = [
          "0", "0.5", "1", "2", "3", "4", "5", "6", "7", "8", "10",
          "12", "15", "20", "25", "30", "35", "40", "45", "50"
        ];

        // 创建一个映射，将关键期限点映射到其在自定义顺序中的索引
        const orderMap = {};
        customOrder.forEach((value, index) => {
          orderMap[value] = index;
        });

        // 按照自定义顺序排序
        allRows.sort((a, b) => {
          // 提取关键期限点的数值部分
          const extractNumber = (str) => {
            if (!str) return "";
            // 如果是纯数字，直接返回
            if (!isNaN(parseFloat(str)) && isFinite(str)) {
              return parseFloat(str).toString();
            }

            // 尝试提取数字部分
            const match = str.match(/^(\d*\.?\d+)/);
            if (match && match[1]) {
              return parseFloat(match[1]).toString();
            }

            return str;
          };

          const keyA = extractNumber(a.keyDuration);
          const keyB = extractNumber(b.keyDuration);

          // 如果两个关键期限点都在自定义顺序中，按照自定义顺序排序
          if (keyA in orderMap && keyB in orderMap) {
            return orderMap[keyA] - orderMap[keyB];
          }

          // 如果只有一个关键期限点在自定义顺序中，将其排在前面
          if (keyA in orderMap) return -1;
          if (keyB in orderMap) return 1;

          // 如果都不在自定义顺序中，尝试数值排序
          const numA = parseFloat(keyA);
          const numB = parseFloat(keyB);
          if (!isNaN(numA) && !isNaN(numB)) {
            return numA - numB;
          }

          // 最后使用字符串排序
          return keyA.localeCompare(keyB);
        });

        // 在前端进行分页
        const pageSize = this.queryParams.pageSize;
        const pageNum = this.queryParams.pageNum;
        const startIndex = (pageNum - 1) * pageSize;
        const endIndex = startIndex + pageSize;

        // 获取当前页的数据
        this.keyDurationParameterList = allRows.slice(startIndex, endIndex);
        this.total = allRows.length;
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
        keyDuration: null,
        parameterValSet: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 分页操作 */
    handlePagination(val) {
      this.queryParams.pageNum = val.page;
      this.queryParams.pageSize = val.limit;
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
      this.title = "添加关键久期参数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getKeyDurationParameter(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改关键久期参数";
      });
    },
    /** 查看参数值集按钮操作 */
    handleViewParameterValSet(row) {
      try {
        // 清空之前的数据
        this.parameterValSetInfo.tableData = [];

        // 设置标题
        this.parameterValSetInfo.title = row.keyDuration || '';

        // 显示加载中
        this.parameterValSetInfo.open = true;
        this.parameterValSetInfo.loading = true;

        // 使用新的API方法获取包含参数值集的数据
        getKeyDurationParameterWithValSet(row.id).then(response => {
          if (response.code === 200 && response.data) {
            const data = response.data;

            // 解析JSON数据
            if (data.parameterValSet) {
              // 先尝试直接解析
              try {
                const parameterValSetData = JSON.parse(data.parameterValSet);
                this.processValSetData(parameterValSetData);
              } catch (parseError) {
                console.warn("初次解析失败，尝试直接提取数据", parseError);

                // 如果解析失败，直接使用正则表达式提取数据
                this.extractDataWithRegex(data.parameterValSet);
              }
            } else {
              this.$message.warning("参数值集数据为空");
            }
          } else {
            this.$message.error("获取参数值集数据失败");
          }

          // 关闭加载中
          this.parameterValSetInfo.loading = false;
        }).catch(error => {
          console.error("获取参数值集数据失败", error);
          this.$message.error("获取参数值集数据失败：" + error.message);
          this.parameterValSetInfo.loading = false;
        });
      } catch (error) {
        this.$modal.msgError("解析参数值集数据失败：" + error.message);
        console.error("解析参数值集数据失败", error);
        this.parameterValSetInfo.loading = false;
      }
    },

    /**
     * 处理值集数据
     */
    processValSetData(parameterValSetData) {
      const tableData = [];

      // 遍历并格式化数据
      Object.keys(parameterValSetData).forEach(key => {
        const item = parameterValSetData[key];
        if (item && typeof item === 'object') {
          tableData.push({
            index: key,
            date: item.date || '',
            value: item.val || item.value || 0
          });
        }
      });

      // 按序号排序
      this.parameterValSetInfo.tableData = tableData.sort((a, b) => parseInt(a.index) - parseInt(b.index));
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
          this.parameterValSetInfo.tableData = uniqueData.sort((a, b) => parseInt(a.index) - parseInt(b.index));
          this.$message.warning("参数值集数据格式有误，已尝试提取并显示数据");
          return;
        }

        // 如果所有方法都失败，尝试显示原始数据
        if (jsonString.length > 0) {
          this.$message.warning("无法解析参数值集数据，显示原始数据");
          this.parameterValSetInfo.tableData = [{
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
        this.$message.error("无法解析参数值集数据，请检查数据格式");
        this.parameterValSetInfo.tableData = [{
          index: 1,
          date: '错误信息',
          value: error.message
        }];
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateKeyDurationParameter(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addKeyDurationParameter(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除关键久期参数编号为"' + ids + '"的数据项？').then(function() {
        return delKeyDurationParameter(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        'dur/key/duration/parameter/export',
        {
          ...this.queryParams
        },
        `key_duration_parameter_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "关键久期参数导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    downloadTemplate() {
      this.download(
        'dur/key/duration/parameter/exportTemplate',
        {},
        `key_duration_parameter_template_${new Date().getTime()}.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
      // 计算上传进度
      if (event.total > 0) {
        this.upload.percentage = Math.round((event.loaded / event.total) * 100);
      }
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.upload.percentage = 0;
      this.$refs.upload.clearFiles();

      // 检查响应状态
      if (response.code === 200) {
        this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      } else {
        this.$alert(response.msg, "导入失败", { dangerouslyUseHTMLString: true, type: 'error' });
      }
    },
    // 文件上传失败处理
    handleFileError(error, file, fileList) {
      this.upload.isUploading = false;
      this.upload.percentage = 0;

      let errorMsg = "导入失败";
      if (error && error.message) {
        errorMsg = error.message;
      } else if (error && error.response && error.response.data) {
        errorMsg = error.response.data.msg || "导入失败，请检查文件格式或网络连接";
      }

      this.$alert(errorMsg, "导入失败", { type: 'error' });
    },
    // 取消导入
    cancelImport() {
      if (this.upload.isUploading) {
        this.$confirm('正在导入数据，确定要取消吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 取消上传
          this.$refs.upload.abort();
          this.upload.isUploading = false;
          this.upload.percentage = 0;
          this.upload.open = false;
        }).catch(() => {});
      } else {
        this.upload.open = false;
      }
    },
    // 提交上传文件
    submitFileForm() {
      const fileObj = this.$refs.upload.uploadFiles[0];
      if (fileObj) {
        this.upload.isUploading = true;
        importKeyDurationParameter(fileObj.raw, this.upload.updateSupport).then(response => {
          this.$modal.msgSuccess(response.msg);
          this.upload.open = false;
          this.upload.isUploading = false;
          this.$refs.upload.clearFiles();
          this.getList();
        }).catch(error => {
          this.upload.isUploading = false;
          console.error("导入失败", error);
        });
      } else {
        this.$message.warning("请先选择需要导入的文件");
      }
    }
  }
};
</script>

<style scoped>
pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-y: auto;
  max-height: 500px;
}

.upload-loading {
  margin: 15px 0;
  padding: 10px;
  background-color: #f8f8f8;
  border-radius: 4px;
}

.upload-loading-text {
  margin-top: 10px;
  text-align: center;
  color: #e6a23c;
  font-size: 14px;
}

.el-form-item__description {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  display: block;
}

.pagination-wrapper {
  background: #fff;
  padding: 15px 0;
  margin-top: 5px;
}

.empty-data {
  text-align: center;
  padding: 30px 0;
  color: #909399;
}

.empty-data i {
  font-size: 30px;
  margin-bottom: 10px;
}

.empty-data p {
  margin: 0;
  font-size: 14px;
}
</style>
