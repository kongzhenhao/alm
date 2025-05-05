# 需求规格说明书模板

## 文档信息

| 项目名称 | 【需求名称】 |
| -------- | ------------ |
| 文档版本 | V1.0         |
| 作者     | 孔镇浩       |
| 创建日期 | 2025-04-10   |
| 状态     | 已确认       |

## 变更履历

| 版本 | 日期       | 变更描述 | 修订人 | 审核 |
| ---- | ---------- | -------- | ------ | ---- |
| V1.0 | 2025-04-10 | 初次编写 | 孔镇浩 | 通过 |

## 1. 需求概述

### 1.1 需求背景

先忽略

### 1.2 需求目标

先忽略

### 1.3 需求范围

先忽略

### 1.4 相关干系人

| 角色       | 部门 | 姓名 | 职责 |
| ---------- | ---- | ---- | ---- |
| 产品负责人 |      |      |      |
| 业务负责人 |      |      |      |
| 技术负责人 |      |      |      |

## 2. 业务架构

### 2.1 业务模块关系图

无

### 2.2 模块列表

| 模块编号 | 模块名称 | 模块英文名 | 英文缩写 |
| -------- | -------- | ---------- | -------- |
| MD0001   | 久期     | duration   | dur      |
| MD0002   | 负债     | liability  | liab     |

### 2.3 数据模型

#### 2.3.1 久期模块
##### 2.3.1.1 表间关系
```mermaid
erDiagram
折现曲线表
折现因子表
负债现金流表 }|--|| 负债现金流汇总表 : 汇总
负债现金流汇总表 ||--|| 负债久期汇总表 : 1v1
负债现金流汇总表 }|--|| 分账户负债现金流现值汇总表 : 汇总
负债久期汇总表 }|--|| 分账户负债久期汇总表 : 汇总


```
##### 2.3.1.2 表名字典
| 表编号 | 表中文名 | 表英文名 | 备注 |
| ---- | ------ | ---- | ------ |
| TB0001 | 负债现金流表 | t_dur_liability_cash_flow  |  |
| TB0002 | 折现曲线表 | t_dur_discount_curve  |  |
| TB0003 | 折现因子表 | t_dur_discount_factor  |  |
| TB0005 | 负债现金流汇总表 | t_dur_liability_cash_flow_summary  |  |
| TB0007 | 负债久期汇总表 | t_dur_liability_duration_summary  |  |
| TB0008 | 分账户负债现金流现值汇总表 | t_dur_sub_account_liability_present_value  |  |
| TB0009 | 分账户负债久期汇总表 | t_dur_sub_account_liability_duration  |  |

##### 2.3.1.3 表集
**（1）TB0001**

| 字段名                 | 数据类型 | 长度  | 允许空 | 唯一索引 | 默认值 | 说明                                                         |
|---------------------| -------- | ----- | ------ | ------- | ------ | ------------------------------------------------------------ |
| **account_period**  | varchar  | 6     | 否     | 是       | 无     | 账期,格式YYYYMM                                             |
| **cash_flow_type**  | char     | 2     | 否     | 是       | 无     | 现金流类型,01:流入,02:流出         |
| **bp_type**         | char     | 2     | 否     | 是       | 无     | 基点类型,01:+50bp,02:-50bp,03:0bp  |
| **design_type**     | varchar  | 50    | 否     | 是       | 无     | 设计类型                                                     |
| **is_short_term**   | char     | 1     | 否     | 是       | 'N'    | 是否为中短期险种                                             |
| **actuarial_code**  | varchar  | 20    | 否     | 是       | 无     | 精算代码                                                     |
| business_code       | varchar  | 20    | 否     | 否       | 无     | 业务代码                                                     |
| product_name        | varchar  | 50    | 否     | 否       | 无     | 产品名称                                                     |
| insurance_main_type | varchar  | 50    | 否     | 否       | 无     | 险种主类                                                     |
| insurance_sub_type  | varchar  | 50    | 否     | 否       | 无     | 险种细类                                                     |
| cash_val_set        | varchar  | 65535 | 否     | 否       | 无     | 现金流值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（2）TB0002**

| 字段名             | 数据类型 | 长度  | 允许空 | 唯一索引 | 默认值 | 说明                                                         |
| ------------------ | -------- | ----- | ------ | -------- | ------ | ------------------------------------------------------------ |
| **account_period** | varchar  | 6     | 否     | 是       | 无     | 账期,格式YYYYMM                                             |
| **curve_type**     | char     | 2     | 否     | 是       | 无     | 曲线类型,02:中档,01:低档                                     |
| **bp_type**         | char     | 2     | 否     | 是       | 无     | 基点类型,01:+50bp,02:-50bp,03:0bp  |
| **duration_type**  | char     | 2     | 否     | 是       | 无     | 久期类型,01:修正久期,02:有效久期                             |
| curve_val_set      | varchar  | 65535 | 否     | 否       | 无     | 曲线值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（3）TB0003**

| 字段名                | 数据类型 | 长度 | 允许空 | 唯一索引 | 默认值            | 说明                     |
| --------------------- | -------- | ---- | ------ | ---- | ----------------- | ------------------------ |
| **account_period**    | varchar  | 6    | 否     | 是  | 无                | 账期,格式YYYYMM         |
| **factor_type**     | char  | 2  | 否     | 是  | 无                | 因子类型,02:中档,01:低档 |
| **bp_type**         | char     | 2     | 否     | 是       | 无     | 基点类型,01:+50bp,02:-50bp,03:0bp  |
| **duration_type** | char | 2 | 否 | 是 | 无 | 久期类型,01:修正久期,02:有效久期 |
| factor_val_set | varchar | 65535 | 否     | 否   | 无                | 因子值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（3）TB0005**

| 字段名          | 数据类型 | 长度 | 允许空 | 唯一索引 | 默认值            | 说明                     |
| --------------- | -------- | ---- | ------ | ---- | ----------------- | ------------------------ |
| **account_period** | varchar  | 6    | 否     | 是  | 无                | 账期,格式YYYYMM         |
| **cash_flow_type** | char     | 2     | 否     | 是       | 无     | 现金流类型,01:流入,02:流出    |
| **bp_type**         | char     | 2     | 否     | 是       | 无     | 基点类型,01:+50bp,02:-50bp,03:0bp  |
| **duration_type**  | char     | 2     | 否     | 是       | 无     | 久期类型,01:修正久期,02:有效久期                             |
| **design_type** | varchar  | 50   | 否     | 是 | 无                | 设计类型                 |
| **is_short_term**  | char     | 1     | 否     | 是      | 'N'    | 是否为中短期险种                                             |
| cash_val_set       | varchar  | 65535 | 否     | 否       | 无     | 现金流值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |
| present_cash_val_set | varchar | 65535 | 否 | 否 | 无 | 现金流现值值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（4）TB0007**

| 字段名          | 数据类型 | 长度 | 允许空 | 唯一索引 | 默认值            | 说明                     |
| --------------- | -------- | ---- | ------ | ---- | ----------------- | ------------------------ |
| **account_period** | varchar  | 6    | 否     | 是 | 无                | 账期,格式YYYYMM         |
| **cash_flow_type** | char     | 2     | 否     | 是       | 无     | 现金流类型,01:流入,02:流出  |
| **duration_type**  | char     | 2     | 否     | 是       | 无     | 久期类型,01:修正久期,02:有效久期                             |
| **design_type** | varchar  | 50   | 否     | 是  | 无                | 设计类型                 |
| **is_short_term**  | char     | 1     | 否     | 是      | 'N'    | 是否为中短期险种                                             |
| duration_val_set   | varchar  | 65535 | 否     | 否       | 无     | 久期值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（5）TB0008**

| 字段名          | 数据类型 | 长度 | 允许空 | 唯一索引 | 默认值            | 说明                     |
| --------------- | -------- | ---- | ------ | ---- | ----------------- | ------------------------ |
| **account_period** | varchar  | 6    | 否     | 是  | 无                | 账期,格式YYYYMM         |
| **cash_flow_type** | char     | 2     | 否     | 是       | 无     | 现金流类型,01:流入,02:流出    |
| **bp_type**         | char     | 2     | 否     | 是       | 无     | 基点类型,01:+50bp,02:-50bp,03:0bp  |
| **duration_type**  | char     | 2     | 否     | 是       | 无     | 久期类型,01:修正久期,02:有效久期                             |
| **design_type** | varchar  | 50   | 否     | 是  | 无                | 设计类型                 |
| present_cash_val_set | varchar  | 65535 | 否     | 否       | 无     | 分账户负债现金流现值值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

**（6）TB0009**

| 字段名          | 数据类型 | 长度 | 允许空 | 唯一索引 | 默认值            | 说明                     |
| --------------- | -------- | ---- | ------ | ---- | ----------------- | ------------------------ |
| **account_period** | varchar  | 6    | 否     | 是  | 无                | 账期,格式YYYYMM         |
| **cash_flow_type** | char     | 2     | 否     | 是       | 无     | 现金流类型,01:流入,02:流出    |
| **duration_type**  | char     | 2     | 否     | 是       | 无     | 久期类型,01:修正久期,02:有效久期                             |
| **design_type** | varchar  | 50   | 否     | 是  | 无                | 设计类型                 |
| duration_val_set   | varchar  | 65535 | 否     | 否       | 无     | 久期值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}} |

### 2.4 用例列表

| 用例编号   | 用例名称             | 用例描述 | 模块编号 |
|--------|------------------| - | ---- |
| UC0001 | 导入负债现金流数据        |  | MD0001 |
| UC0002 | 导入折现曲线数据         |  | MD0001 |
| UC0003 | 导入折现因子数据         |  | MD0001 |
| UC0005 | 负债现金流汇总及现值计算     |  | MD0001 |
| UC0006 | 负债久期计算        |  | MD0001 |
| UC0007 | 分账户负债现金流现值汇总        |  | MD0001 |
| UC0008 | 分账户负债久期汇总        |  | MD0001 |

### 2.5 接口清单
| 接口编号   | 接口名称        | 接口描述 | 模块编号 |
| ------ |-------------| ---- | ---- |
| IF0001 | 导入负债现金流数据   |  | MD0001 |
| IF0002 | 导入折现曲线数据    |  | MD0001 |

## 3. 业务概念与术语

| 术语      | 定义   | 业务含义   | 备注   |
| ------- | ---- | ------ | ---- |
| 【通用术语1】 | 【定义】 | 【业务含义】 | 【备注】 |
| 【通用术语2】 | 【定义】 | 【业务含义】 | 【备注】 |

## 4. 功能需求

### 4.1 久期模块

#### 4.1.1 原型图

#### 4.1.1.1 负债现金流列表页

##### 4.1.1.1.1 原型图

![image-20250410001806017](C:\Users\86135\AppData\Roaming\Typora\typora-user-images\image-20250410001806017.png)

##### 4.1.1.1.2 查询条件
| 字段名 | 字段类型 | 格式 | 备注 |
| ------ | -------- | ------ | ---- |
| 账期 | 输入框 | 长度20 | 日期选择器,格式为YYYYMM |
| 现金流类型 | 下拉框 | 长度20 | 可选值未显示 |
| 精算代码 | 输入框 | 长度20 | 无 |
| 业务代码 | 输入框 | 长度20 | 无 |
| 久期类型 | 下拉框 | 长度20 | 可选值未显示 |
| 险种主类 | 下拉框 | 长度20 | 可选值未显示 |
| 设计类型 | 下拉框 | 长度20 | 可选值未显示 |
| 险种细类 | 下拉框 | 长度20 | 可选值未显示 |


##### 4.1.1.1.3 列表展示
| 字段名 | 字段类型 | 格式 | 备注 |
| ------ | -------- | ------ | ---- |
| 账期 | 文本 | 长度6 | 表格列,显示格式YYYYMM |
| 现金流类型 | 文本 | 长度20 | 表格列,显示现金流类别 |
| 精算代码 | 文本 | 长度20 | 表格列,显示代码,如AC001 |
| 业务代码 | 文本 | 长度20 | 表格列,显示代码,如BC001 |
| 日期 | 日期 | 长度10 | 表格列,格式为YYYY-MM-DD |
| 久期类型 | 文本 | 长度20 | 表格列,显示久期分类,如"中期" |
| 1 | 数值 | 长度20 | 表格列,显示金额,有千位分隔符 |
| 2          | 数值       | 长度20 | 表格列,显示金额,有千位分隔符 |
| ...        | ...        | ...    | ... |
| 1272       | 数值       | 长度20 | 表格列,显示金额,有千位分隔符 |
| 操作 | 操作按钮组 | - | 包含编辑、删除按钮 |
##### 4.1.1.1.4 其他描述

#### 4.1.1.2 折现因子列表页（PT0002）

##### 4.1.1.2.1 原型图

![image-20250410000106967](C:\Users\86135\AppData\Roaming\Typora\typora-user-images\image-20250410000106967.png)

##### 4.1.1.2.2 查询条件

| 字段名   | 字段类型 | 格式  | 备注       |
| -------- | -------- | ----- | ---------- |
| 账期     | 输入框   | 长度6 | 格式YYYYMM |
| 因子类型 | 下拉框   | 长度2 |            |
| 久期类型 | 下拉框   | 长度2 |            |

##### 4.1.1.2.3 列表展示

| 字段名   | 格式       | 备注               |
| -------- | ---------- | ------------------ |
| 账期名称 | 文本       | 长度6无            |
| 因子类型 | 文本       | 长度2              |
| 久期类型 | 文本       | 长度2              |
| 1        | 数值       | 长度20             |
| 2        | 数值       | 长度20             |
| ...      | ...        | ...                |
| 1272     | 数值       | 长度20             |
| 操作     | 操作按钮组 | 包含编辑、删除按钮 |

##### 4.1.1.2.4 其他描述

#### 4.1.1.3 折现曲线列表页（PT0003）

##### 4.1.1.3.1 原型图

![image-20250410002325588](C:\Users\86135\AppData\Roaming\Typora\typora-user-images\image-20250410002325588.png)

##### 4.1.1.3.2 查询条件

| 字段名   | 字段类型 | 格式  | 备注       |
| -------- | -------- | ----- | ---------- |
| 账期     | 输入框   | 长度6 | 格式YYYYMM |
| 曲线类型 | 下拉框   | 长度2 |            |
| 久期类型 | 下拉框   | 长度2 |            |

##### 4.1.1.3.3 列表展示

| 字段名   | 格式       | 备注               |
| -------- | ---------- | ------------------ |
| 账期名称 | 文本       | 长度6无            |
| 曲线类型 | 文本       | 长度2              |
| 久期类型 | 文本       | 长度2              |
| 1        | 数值       | 长度20             |
| 2        | 数值       | 长度20             |
| ...      | ...        | ...                |
| 1272     | 数值       | 长度20             |
| 操作     | 操作按钮组 | 包含编辑、删除按钮 |

##### 4.1.1.3.4 其他描述

#### 4.1.2 接口功能
##### 4.1.2.1 导入负债现金流数据(FN0001)
##### 4.1.2.1.1 接口功能概述

导入负债现金流数据

##### 4.1.2.1.2 接口基本信息
| 类型 | 描述 |
|--------|------|
| Method | POST |
| Content-Type | multipart/form-data |
| Url | https://alm.hongkang-life.com/api/dur/importLiabilibyCashFlow |

##### 4.1.2.1.3 接口入参
| 参数名 | 类型 | 是否必填 | 描述 |
|--------|------|----------|------|
| file | File | 是 |  |
##### 4.1.2.1.4 接口出参

| 参数名 | 类型 | 描述 |
|--------|------|------|
| code | Long | 代码为200为成功,反则异常 |
| msg | String | 异常描述 |
成功示例:
```json
{
	"code": 200,
	"msg": ""
}
```
失败示例:
```json
{
	"code": 500,
	"msg": "系统异常,请联系管理员!"
}
```
##### 4.1.2.1.5 接口功能详述

```mermaid
flowchart TD
    A[逐行读取上传的Excel文件] --> B{现金流类型为'流入'或'流出'}
    B --> |是| C[久期类型设置为修正久期]
    B --> |否| D[久期类型设置为有效久期]
    C --> E[写入TB0001表]
    D --> E
```
##### 4.1.2.2 导入折现曲线数据(IF0002)
##### 4.1.2.2.1 接口功能概述

导入折现曲线数据

##### 4.1.2.2.2 接口基本信息
| 类型 | 描述 |
|--------|------|
| Method | POST |
| Content-Type | multipart/form-data |
| Url | https://alm.hongkang-life.com/api/dur/importDiscountCurve |

##### 4.1.2.2.3 接口入参
| 参数名 | 类型 | 是否必填 | 描述 |
|--------|------|----------|------|
| file | File | 是 |  |
##### 4.1.2.2.4 接口出参
| 参数名 | 类型 | 描述 |
|--------|------|------|
| code | Long | 代码为200为成功,反则异常 |
| msg | String | 异常描述 |
成功示例:
```json
{
	"code": 200,
	"msg": ""
}
```
失败示例:
```json
{
	"code": 500,
	"msg": "系统异常,请联系管理员!"
}
```
##### 4.1.2.2.5 接口功能详述
```mermaid
flowchart TD
    A[逐行读取上传的Excel文件] --> B[写入TB0002表]
```
##### 4.1.2.3 导入折现因子数据(IF0003)
##### 4.1.2.3.1 接口功能概述

导入折现因子数据

##### 4.1.2.3.2 接口基本信息
| 类型 | 描述 |
|--------|------|
| Method | POST |
| Content-Type | multipart/form-data |
| Url | https://alm.hongkang-life.com/api/dur/importDiscountFactor |

##### 4.1.2.3.3 接口入参
| 参数名 | 类型 | 是否必填 | 描述 |
|--------|------|----------|------|
| file | File | 是 |  |
##### 4.1.2.3.4 接口出参
| 参数名 | 类型 | 描述 |
|--------|------|------|
| code | Long | 代码为200为成功,反则异常 |
| msg | String | 异常描述 |
成功示例:
```json
{
	"code": 200,
	"msg": ""
}
```
失败示例:
```json
{
	"code": 500,
	"msg": "系统异常,请联系管理员!"
}
```
##### 4.1.2.3.5 接口功能详述
```mermaid
flowchart TD
    A[逐行读取上传的Excel文件] --> B[写入TB0003表]
```
#### 4.1.3 功能描述

##### 4.1.3.1 分账户现金流及久期计算功能（FN0001）

##### 4.1.3.1.1 功能概要

通过多步完成分账户现金流及久期计算
##### 4.1.3.1.2 业务总流程
```mermaid
flowchart TD
    A[UC0001: 导入负债现金流数据]
    B[UC0002: 导入折现曲线数据]
    C[UC0003: 导入折现因子数据]
    E[UC0005: 负债现金流汇总及现值计算]
    F[UC0006: 负债久期计算]
    G[UC0007: 分账户负债现金流现值汇总]
    H[UC0008: 分账户负债久期汇总]
    A --> E
    B --> E
    C --> E
    E --> F
    F --> H
    E --> G
```

##### 4.1.3.1.3 用例描述
##### 4.1.3.1.3.4 负债现金流汇总及现值计算(UC0005)
|   用例标识   | 用例描述             |
| :----------: | ------------------------------------ |
| 类型 | 批处理 |
|   用例名称   | 负债现金流汇总及现值计算                |
|   功能描述   | 负债现金流汇总及现值计算 |
|    参与者    | 系统                     |
|    原型图    |                                |
|    关联表    | TB0001,TB0003,TB0005                        |
|    前置用例    | UC0001,UC0002,UC0003                       |

**步骤1.** 加载因子表

(1) 按账期读取TB0003表,用|拼接account_period,factor_type,bp_type,duration_type作为key,factor_val_set作为value构建因子HashMap,以便后面快速读取

**步骤2.** 现金流汇总

(1) 按账期遍历TB0001表读取现金流记录,用|拼接account_period,cash_flow_type,bp_type,duration_type,design_type,is_short_term作为key,用cash_val_set的value构建现金流valueMap(格式为{"0":123.00,"1":123.00,...,"1272":123.00}),用cash_val_set的date构建现金流dateMap(格式为{"0":"2025-01-01","1":"2025-01-02",...,"1272":"2025-12-01"})

(2) key相同的记录,遍历valueMap的value数据,按对应序号累加到valueMap对应序号的数据值上

(3) 所有记录遍历汇总完成后,valueMap和dateMap传递给后续的现金流现值计算环节，用于现值计算

**步骤3.** 计算现金流现值
(1) 遍历现金流valueMap记录,逐条记录执行以下操作
定义:
cash[i]代表现金流valueMap中value的第i个序号对应的value值
fac[j]是因子HashMap中value的第j个序号对应的值,提取现金流valueMap的key的account_period,cash_flow_type,bp_type,duration_type信息,其中cash_flow_type转为factor_type查询因子HashMap并找到对应的value信息,factor_type设置值的逻辑如下:
- design_type为"传统险","分红险","万能险"同时is_short_term为Y时factor_type为01
- design_type为"万能险"同时is_short_term为N时factor_type为01
- 其他情况factor_type为02
present_value[i]是通过cash和fac向量计算出来的现金流现值第i个序号对应的值:
**present_value[i]**（通用公式）
- 公式1(如果现金流类型为`流入`的情况): present_value[i]=∑[j=i+1,1272]cash[j]×fac[j−i-1],i从0开始至1272,最后present_value[1272]=0
- 公式2(如果现金流类型为`流出`的情况): present_value[i]=∑[j=i,1272]cash[j]×fac[j−i],i从0开始
- 精度：计算结果数值的小数位留8位，计算过程保留小数位16位，四舍五入
- 即使计算后的present_value[i]值小于等于0也要保存
- 计算过程不要用Double类型,要用BigDecimal类型保证计算精度
- present_value计算参考com.xl.alm.job.dur.BigDecimalMatrixExample类实现

**步骤4.** 数据入表
(1) 通过以上过程,把每个现金流和现金流现值记录保存到TB0005表里,其中present_cash_val_set中date数值按序号取dateMap的value值

##### 4.1.3.1.3.5 负债久期计算(UC0006)

|   用例标识   | 用例描述             |
| :----------: | ------------------------------------ |
| 类型 | 批处理 |
|   用例名称   | 负债久期计算         |
|   功能描述   | 负债久期计算 |
|    参与者    | 系统                 |
|    原型图    |                                |
|    关联表    | TB0002,TB0003,TB0005,TB0007                 |
|    前置用例    | UC0001,UC0002,UC0003                       |

**步骤1.** 加载因子表及曲线表

(1) 按账期读取TB0003表,用|拼接account_period,factor_type,bp_type,duration_type作为key,factor_val_set的value作为Map的value值构建因子HashMap,以便后面快速读取

(2) 按账期读取TB0002表,用|拼接account_period,curve_type,bp_type,duration_type作为key,curve_val_set的value作为Map的value值构建曲线HashMap,以便后面快速读取

**步骤2.** 负债久期计算
(1) 按账期读取TB0005表duration_type为修正久期的现金流汇总数据,通过以下公式逐条计算出久期值值集

a. 定义:
- 序号i从0至1272
- 现金流金额[i] = TB0005表cash_val_set[i].value
- 现金流现值[i] = TB0005表present_cash_val_set[i].valueue
- 折现因子[i] = 因子HashMap中value的第i个序号对应的值,提取现金流[i]的account_period,cash_flow_type,bp_type,duration_type信息,其中cash_flow_type转- 为factor_type查询因子HashMap并找到对应的value信息
- 折现率[i] = 曲线HashMap中value的第i个序号对应的值,提取现金流[i]的account_period,cash_flow_type,bp_type,duration_type信息,其中cash_flow_type转为factor_type查询因子HashMap并找到对应的value信息
- 公式1(cash_flow_type为01,即`流入`时)：duration_value[i]=(∑[j=i+1,1272]现金流金额[j]*折现因子[j-i-1]*(j-1/12) /(1+折现率[j-i-1]))/现金流现值[i],i从0开始至1272,最后duration_value[1272]=0,如果现金流现值[i]为0,则duration_value[i]为0
- 公式2(cash_flow_type为02,即`流出`时)：duration_value[i]=(∑[j=i,1272]现金流金额[j]*折现因子[j-i]*(j/12) /(1+折现率[j-i]))/现金流现值[i],i从0开始至1272,如果现金流现值[i]为0,则duration_value[i]为0
- 久期值值集,格式为{"0":{"date":现金流汇记录的cash_val_set[0].date,"value":duration_value[0]},"1":{"date":现金流汇记录的cash_val_set[1].date,"value":duration_value[1]},...,"1272":{"date":现金流汇记录的cash_val_set[1272].date,"value":duration_value[1272]}}
- 精度：计算结果数值的小数位留8位，计算过程保留小数位16位，四舍五入
- 即使计算后的duration_value[i]值小于等于0也要保存
- 计算过程不要用Double类型,要用BigDecimal类型保证计算精度
- duration_value计算参考com.xl.alm.job.dur.BigDecimalMatrixExample类实现

b. 修正久期数据入库：
- 负债久期其他字段来自现金流汇总信息，同时久期值值集写入此久期实体
- 所有记录遍历完成后,所有久期记录写入TB0007表

(2) 计算有效久期
- 按账期读取TB0005表duration_type为有效久期,bp_type为+50bp的记录,数据集标注为A
- 按账期读取TB0005表duration_type为有效久期,bp_type为-50bp的记录,数据集标注为B
- 按账期读取TB0005表duration_type为修正久期,bp_type为0bp的记录,数据集标注为C
- A数据集,B数据集与C数据集通过cash_flow_type,design_type,is_short_term字段进行关联,形成一张表数据
- 公式: duration_value[i]=(B.present_cash_val_set[i].value-A.present_cash_val_set[i].value)/0.01/C.present_cash_val_set[i].value,i从0开始至1272
- 久期值值集,格式为{"0":{"date":现金流汇记录的cash_val_set[0].date,"value":duration_value[0]},"1":{"date":现金流汇记录的cash_val_set[1].date,"value":duration_value[1]},...,"1272":{"date":现金流汇记录的cash_val_set[1272].date,"value":duration_value[1272]}}
- 精度：计算结果数值的小数位留8位，计算过程保留小数位16位，四舍五入

b. 有效久期数据入库
负债久期其他字段来自现金流汇总信息，同时久期值值集写入此久期实体
所有记录遍历完成后,所有久期记录写入TB0007表

##### 4.1.3.1.3.6 分账户负债现金流现值汇总(UC0007)
|   用例标识   | 用例描述             |
| :----------: | ------------------------------------ |
| 类型 | 批处理 |
|   用例名称   | 分账户负债现金流现值汇总   |
|   功能描述   | 分账户负债现金流现值汇总 |
|    参与者    | 系统                 |
|    原型图    |                                |
|    关联表    | TB0005,TB0008 |
|    前置用例    | UC0005                |

**步骤1.** 负债现金流现值汇总去掉中短期维度后进行汇总

(1) 按账期遍历TB0005表读取现金流汇总记录,用|拼接account_period,cash_flow_type,bp_type,duration_type,design_type作为key,用cash_val_set的value构建现金流汇总valueMap(格式为{"0":123.00,"1":123.00,...,"1272":123.00}),用cash_val_set的date构建现金流汇总dateMap(格式为{"0":"2025-01-01","1":"2025-01-02",...,"1272":"2025-12-01"})

(2) key相同的记录,遍历valueMap的value数据,按对应序号累加到valueMap对应序号的数据值上

(3) 所有记录遍历汇总完成后,数据写入TB0008表
- 负债现金流现值汇总值集,格式为{"0":{"date":dateMap[0].value,"value":valueMap[0].value},"0":{"date":dateMap[0].value,"value":valueMap[0].value},...,"1272":{"date":dateMap[1272].value,"value":valueMap[1272].value}}
- 负债现金流现值汇总的account_period,cash_flow_type,bp_type,duration_type,design_type信息从valueMap的key中提取
- 即使计算后的valueMap[i]值小于等于0也要保存
- 计算过程不要用Double类型,要用BigDecimal类型保证计算精度
- 最后写入TB0008表

##### 4.1.3.1.3.7 分账户负债久期汇总(UC0008)

| 用例标识 | 用例描述           |
| :------: | ------------------ |
|   类型   | 批处理             |
| 用例名称 | 分账户负债久期汇总 |
| 功能描述 | 分账户负债久期汇总 |
|  参与者  | 系统               |
|  原型图  |                    |
|  关联表  | TB0007,TB0009      |
| 前置用例 | UC0006             |

**步骤1.** 修正久期汇总

(1) 按account_period=传入参数,cash_flow_type,duration_type=01,design_type,is_short_term作为关联条件，关联TB0005和TB0007表查出TB0005表的present_cash_val_set和TB0007表的duration_val_set字段信息

(2) 查出来的数据记录按is_short_term类型分别存储到2个Map中,Y类型放到shortTermMap(key为cash_flow_type|duration_type,value为数据记录),N类型放到noneShortTermMap(key为cash_flow_type|duration_type,value为数据记录)

(3) 遍历shortTermMap，从shortTermMap和noneShortTermMap中按对应key分别取出数据记录，其中从shortTermMap提取的记录记为A,从noneShortTermMap中提取的记录记为B，并按以下公式计算出修正久期汇总值值集
- 公式：duration_summary_value[i] = ((A.present_cash_val_set[i].value * A.duration_val_set[i].value) + (B.present_cash_val_set[i].value * B.duration_val_set[i].value)) / (A.present_cash_val_set[i].value + B.present_cash_val_set[i].value)
- 久期汇总值值集,格式为{"0":{"date":A.present_cash_val_set[1].date,"value":duration_summary_value[0]},"1":{"date":A.present_cash_val_set[1].date,"value":duration_summary_value[1]},...,"1272":{"date":A.present_cash_val_set[1272].date,"value":duration_summary_value[1272]}}
- 精度：计算结果数值的小数位留8位，计算过程保留小数位16位，四舍五入
- 即使计算后的duration_summary_value[i]值小于等于0也要保存
- 计算过程不要用Double类型,要用BigDecimal类型保证计算精度

(4) 所有记录遍历汇总完成后,数据写入TB0009表

**步骤2.** 有效久期汇总

(1) 按账期读取TB0008表duration_type为有效久期的数据,通过以下步骤计算出久期汇总值值集

a. 找到基点类型为"+50bp","-50bp","0bp"的记录,分别标记为A,B,C,然后按以下公式进行计算久期汇总值
- 公式: duration_summary_value[i] = (A.present_cash_val_set[i].value - B.present_cash_val_set[i].value)/0.01/C.present_cash_val_set[i].value
- 久期汇总值值集,格式为{"0":{"date":A.present_cash_val_set[1].date,"value":duration_summary_value[0]},"1":{"date":A.present_cash_val_set[1].date,"value":duration_summary_value[1]},...,"1272":{"date":A.present_cash_val_set[1272].date,"value":duration_summary_value[1272]}}
- 精度：计算结果数值的小数位留8位，计算过程保留小数位16位，四舍五入
- 即使计算后的duration_summary_value[i]值小于等于0也要保存
- 计算过程不要用Double类型,要用BigDecimal类型保证计算精度

(2) 所有记录遍历汇总完成后,数据写入TB0009表