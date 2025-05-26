# 最低资本模块批处理功能

## 概述

本模块实现了最低资本相关的批处理功能，对应设计文档中的最低资本模块(MD0001)。

## 功能列表

### UC0006 - 计算最低资本明细汇总数据

**功能描述：** 根据分部门最低资本明细表数据计算汇总结果

**处理流程：**
1. **数据准备**：从TB0001表(分部门最低资本明细表)读取指定账期的数据
2. **风险类型过滤**：根据项目定义表中的风险类型过滤，只计算风险类型为"市场风险"和"信用风险"的项目
3. **汇总计算**：按账期、项目编码、账户编码分组，汇总amount值
4. **数据入表**：将汇总结果写入TB0007表(最低资本明细汇总表)

### UC0007 - 计算边际最低资本贡献率数据

**功能描述：** 计算子风险层面和公司层面边际最低资本贡献因子

**处理流程：**
1. **数据准备**：从TB0002表(风险项目金额表)读取数据，获取再保后金额
2. **初始化数据**：将再保后金额写入TB0006表(边际最低资本贡献率表)
3. **子风险层面计算**：使用Aviator表达式引擎执行项目定义表中的sub_risk_factor_formula
4. **子风险结果更新**：将计算结果更新到TB0006表的sub_risk_marginal_factor字段
5. **公司层面计算**：使用Aviator表达式引擎执行项目定义表中的company_factor_formula
6. **公司层面结果更新**：将计算结果更新到TB0006表的company_marginal_factor字段

**相关文件：**
- Task: `MarginalCapitalCalculationTask.java`
- Service: `MarginalCapitalCalculationService.java` / `MarginalCapitalCalculationServiceImpl.java`
- Mapper: `MarginalCapitalCalculationMapper.java` / `MarginalCapitalCalculationMapper.xml`
- Entity: `MarginalCapitalCalculationEntity.java`
- Processor: `MarginalCapitalCalculationProcessor.java`
- Util: `AviatorFormulaUtil.java`

## 数据表说明

### UC0006相关表
- **输入表**：TB0001 分部门最低资本明细表 (t_minc_dept_mincap_detail)
- **关联表**：项目定义表 (t_minc_item_definition)
- **输出表**：TB0007 最低资本明细汇总表 (t_minc_min_capital_summary)

### UC0007相关表
- **输入表1**：TB0002 风险项目金额表 (t_minc_risk_item_amount)
- **输入表2**：TB0004 相关系数表 (t_minc_correlation_coef)
- **输入表3**：TB0005 项目定义表 (t_minc_item_definition)
- **输出表**：TB0006 边际最低资本贡献率表 (t_minc_marginal_capital)

### UC0008相关表

- **输入表1**：TB0006 边际最低资本贡献率表 (t_minc_marginal_capital)
- **输入表2**：TB0007 最低资本明细汇总表 (t_minc_min_capital_summary)
- **输入表3**：TB0005 项目定义表 (t_minc_item_definition)
- **输出表**：TB0008 市场及信用最低资本表 (t_minc_min_capital_by_account)

## 公式示例

### 子风险层面边际最低资本贡献因子公式
```
(IR001 * IR001_IR001_CORR + NR001 * IR001_NR001_CORR + MR001 * IR001_MR001_CORR + CR001 * IR001_CR001_CORR) / (IR001 + NR001 + MR001 + CR001 - QR003)
```

其中：
- `IR001`、`NR001`、`MR001`、`CR001`：项目编码，代表金额变量
- `IR001_IR001_CORR`：相关系数变量，表示IR001与IR001的相关系数
- `QR003`：量化风险分散效应

### 公司层面边际最低资本贡献因子公式

```
IR001_01_SUB * IR001_SUB
```

其中：
- `IR001_01_SUB`：IR001_01项目的子风险层面边际最低资本贡献因子
- `IR001_SUB`：IR001项目的子风险层面边际最低资本贡献因子

**多级项目示例**：
```
IR001_01_04_01_SUB * IR001_01_04_SUB * IR001_01_SUB * IR001_SUB
```

表示四级项目的公司层面因子 = 自身子风险因子 × 上级子风险因子 × 上上级子风险因子 × 上上上级子风险因子

### 市场及信用最低资本计算公式

```
MR001_01 * MR001_01_COMPANY
```

其中：
- `MR001_01`：利率风险在指定账户下的金额（来自TB0007表）
- `MR001_01_COMPANY`：利率风险的公司层面边际最低资本贡献因子（来自TB0006表）

**计算逻辑**：最低资本金额 = 账户金额 × 公司层面边际最低资本贡献因子

### UC0008 - 计算市场及信用最低资本数据

**功能描述：** 计算各账户下的市场风险和信用风险最低资本

**处理流程：**

1. **数据准备**：从TB0006表和TB0007表读取数据
2. **公式执行**：使用Aviator表达式引擎执行项目定义表中的capital_calculation_formula
3. **按项目计算**：为传统、分红、万能账户分别计算各项目的最低资本
4. **按风险类型汇总**：同一风险类型的所有项目金额累加
5. **普通账户汇总**：普通账户 = 传统账户 + 分红账户 + 万能账户
6. **结果入表**：将汇总结果写入TB0008表(市场及信用最低资本表)

**计算逻辑：**
- **项目级计算**：每个项目按公式计算各账户的最低资本
- **风险类型汇总**：同一风险类型的所有项目金额累加
- **最终结果**：只有两条记录
  - 市场风险最低资本（所有市场风险项目的汇总）
  - 信用风险最低资本（所有信用风险项目的汇总）

**账户范围：**
- **计算账户**：传统账户(AC001)、分红账户(AC002)、万能账户(AC003)
- **汇总账户**：普通账户(AC006) = 传统 + 分红 + 万能

**相关文件：**
- Task: `MarketCreditCapitalTask.java`
- Service: `MarketCreditCapitalService.java` / `MarketCreditCapitalServiceImpl.java`
- Mapper: `MarketCreditCapitalMapper.java` / `MarketCreditCapitalMapper.xml`
- Entity: `MarketCreditCapitalEntity.java`
- Processor: `MarketCreditCapitalProcessor.java`

## 使用方式

### 1. 通过PowerJob调度

```json
{
  "accountPeriod": "202412"
}
```

### 2. 直接调用Task

```java
// UC0006
@Autowired
private MinCapitalSummaryCalculationTask summaryTask;
boolean result = summaryTask.execute("202412");

// UC0007
@Autowired
private MarginalCapitalCalculationTask marginalTask;
boolean result = marginalTask.executeSubRiskCalculation("202412");

// UC0008
@Autowired
private MarketCreditCapitalTask marketCreditTask;
boolean result = marketCreditTask.executeCalculation("202412");
```

### 3. 本地测试

在Processor类中取消注释`@PostConstruct`方法即可进行本地测试。

## 核心特性

- ✅ **完整的事务处理**：确保数据一致性
- ✅ **Aviator表达式引擎**：支持复杂的公式计算
- ✅ **风险类型过滤**：只计算指定风险类型的项目
- ✅ **详细的日志记录**：便于问题排查
- ✅ **严格的参数校验**：对输入参数进行校验
- ✅ **错误处理和异常回滚**：保证数据完整性
- ✅ **支持PowerJob调度**：可集成到调度系统

## 注意事项

1. **计算顺序**：必须先执行UC0006，再执行UC0007，最后执行UC0008
2. **公式配置**：项目定义表中的sub_risk_factor_formula、company_factor_formula、capital_calculation_formula字段必须正确配置
3. **数据依赖**：确保TB0001、TB0002、TB0004、TB0005表中有对应的数据
4. **账期格式**：账期必须为YYYYMM格式（如202412）
5. **风险类型**：UC0008只计算风险类型为"市场风险"和"信用风险"的项目

## 错误处理

- 参数为空或格式错误时返回false
- 数据库操作异常时会回滚事务并抛出异常
- 没有源数据时不算失败，返回true
- 公式计算失败时记录错误日志，继续处理其他项目
