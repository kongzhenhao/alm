<template>
  <div class="duration-value-set-container">
    <div class="operation-bar">
      <el-button size="mini" type="primary" @click="showChartView">图表视图</el-button>
      <el-button size="mini" type="primary" @click="showTableView">表格视图</el-button>
      <el-button size="mini" type="success" @click="handleSave">保存</el-button>
    </div>
    
    <!-- 图表视图 -->
    <div v-show="viewMode === 'chart'" class="chart-container">
      <div ref="chartRef" style="width: 100%; height: 400px;"></div>
    </div>
    
    <!-- 表格视图 -->
    <div v-show="viewMode === 'table'" class="table-container">
      <el-table
        :data="tableData"
        height="400"
        border
        style="width: 100%">
        <el-table-column
          prop="index"
          label="序号"
          width="80">
        </el-table-column>
        <el-table-column
          prop="date"
          label="日期"
          width="120">
        </el-table-column>
        <el-table-column
          prop="val"
          label="值">
          <template slot-scope="scope">
            <el-input v-model="scope.row.val" size="mini" @change="handleValueChange(scope.row)"></el-input>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalItems">
        </el-pagination>
      </div>
    </div>
    
    <!-- 图表弹窗 -->
    <el-dialog title="久期值集图表" :visible.sync="chartDialogVisible" width="80%" append-to-body>
      <div ref="fullChartRef" style="width: 100%; height: 600px;"></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "DurationValueSet",
  props: {
    value: {
      type: String,
      default: '{}'
    },
    readonly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      viewMode: 'table', // 'table' or 'chart'
      parsedData: {},
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      chartInstance: null,
      fullChartInstance: null,
      chartDialogVisible: false
    };
  },
  watch: {
    value: {
      handler(newVal) {
        this.parseValueSet(newVal);
        this.updateTableData();
        this.updateChart();
      },
      immediate: true
    }
  },
  mounted() {
    this.parseValueSet(this.value);
    this.updateTableData();
    this.$nextTick(() => {
      this.initChart();
    });
  },
  methods: {
    parseValueSet(valueSetStr) {
      try {
        this.parsedData = JSON.parse(valueSetStr || '{}');
        this.totalItems = Object.keys(this.parsedData).length;
      } catch (e) {
        console.error('解析久期值集失败:', e);
        this.parsedData = {};
        this.totalItems = 0;
      }
    },
    
    updateTableData() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      
      const keys = Object.keys(this.parsedData).sort((a, b) => parseInt(a) - parseInt(b));
      const slicedKeys = keys.slice(startIndex, endIndex);
      
      this.tableData = slicedKeys.map(key => {
        const item = this.parsedData[key];
        return {
          index: key,
          date: item.date,
          val: item.val
        };
      });
    },
    
    handleSizeChange(size) {
      this.pageSize = size;
      this.updateTableData();
    },
    
    handleCurrentChange(page) {
      this.currentPage = page;
      this.updateTableData();
    },
    
    handleValueChange(row) {
      if (this.parsedData[row.index]) {
        this.parsedData[row.index].val = row.val;
        this.updateChart();
      }
    },
    
    handleSave() {
      this.$emit('input', JSON.stringify(this.parsedData));
      this.$emit('change', JSON.stringify(this.parsedData));
    },
    
    showChartView() {
      this.viewMode = 'chart';
      this.$nextTick(() => {
        this.updateChart();
      });
    },
    
    showTableView() {
      this.viewMode = 'table';
    },
    
    initChart() {
      if (this.$refs.chartRef) {
        this.chartInstance = echarts.init(this.$refs.chartRef);
        this.updateChart();
      }
    },
    
    updateChart() {
      if (!this.chartInstance) return;
      
      const data = [];
      const keys = Object.keys(this.parsedData).sort((a, b) => {
        const dateA = new Date(this.parsedData[a].date);
        const dateB = new Date(this.parsedData[b].date);
        return dateA - dateB;
      });
      
      keys.forEach(key => {
        const item = this.parsedData[key];
        data.push([item.date, parseFloat(item.val)]);
      });
      
      const option = {
        title: {
          text: '久期值集图表'
        },
        tooltip: {
          trigger: 'axis',
          formatter: function(params) {
            const param = params[0];
            return `${param.name}<br/>${param.value[1]}`;
          }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item[0])
        },
        yAxis: {
          type: 'value'
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 10
          },
          {
            start: 0,
            end: 10
          }
        ],
        series: [
          {
            data: data.map(item => item[1]),
            type: 'line',
            smooth: true
          }
        ]
      };
      
      this.chartInstance.setOption(option);
    },
    
    showChartDialog() {
      this.chartDialogVisible = true;
      this.$nextTick(() => {
        if (!this.fullChartInstance && this.$refs.fullChartRef) {
          this.fullChartInstance = echarts.init(this.$refs.fullChartRef);
        }
        
        if (this.fullChartInstance) {
          const data = [];
          const keys = Object.keys(this.parsedData).sort((a, b) => {
            const dateA = new Date(this.parsedData[a].date);
            const dateB = new Date(this.parsedData[b].date);
            return dateA - dateB;
          });
          
          keys.forEach(key => {
            const item = this.parsedData[key];
            data.push([item.date, parseFloat(item.val)]);
          });
          
          const option = {
            title: {
              text: '久期值集图表'
            },
            tooltip: {
              trigger: 'axis',
              formatter: function(params) {
                const param = params[0];
                return `${param.name}<br/>${param.value[1]}`;
              }
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item[0])
            },
            yAxis: {
              type: 'value'
            },
            dataZoom: [
              {
                type: 'inside',
                start: 0,
                end: 100
              },
              {
                start: 0,
                end: 100
              }
            ],
            series: [
              {
                data: data.map(item => item[1]),
                type: 'line',
                smooth: true
              }
            ]
          };
          
          this.fullChartInstance.setOption(option);
        }
      });
    }
  }
};
</script>

<style scoped>
.duration-value-set-container {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
}

.operation-bar {
  margin-bottom: 10px;
}

.chart-container {
  margin-top: 20px;
}

.table-container {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 10px;
  text-align: right;
}
</style>
