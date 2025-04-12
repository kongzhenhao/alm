# ALM-Job 批处理程序代码规范

## 1. 项目整体结构

### 1.1 目录结构
```
salm-job/                                 # 项目根目录
├── src/                                  # 源代码目录
│   ├── main/                             # 主要源代码
│   │   ├── java/                         # Java源代码
│   │   │   └── com/xl/salm/              # 基础包路径
│   │   │       ├── JobApplication.java   # 应用启动类
│   │   │       ├── config/               # 框架配置模块
│   │   │       ├── utils/                # 公共工具类模块
│   │   │       ├── xxx/                  # 公共xxx模块
│   │   │       └── job/                  # 批处理包
│   │   │           ├── sample/           # 示例模块（参考实现）
│   │   │           └── {business}/       # 业务模块（实际业务代码）
│   │   └── resources/                    # 资源文件目录
│   │       ├── mapper/                   # MyBatis映射文件
│   │       ├── application.yml           # 主配置文件
│   │       ├── application-dev.yml       # 开发环境配置
│   │       └── logback.xml               # 日志配置
│   └── test/                             # 测试代码目录
├── pom.xml                               # Maven配置文件
├── README.md                             # 项目说明文档
└── code-standards.md                     # 代码规范文档
```

### 1.2 模块结构规范
每个功能模块都应遵循以下标准结构（所有Java包路径均以src/main/java为根目录）：
```
com.xl.alm.job.{module_name}              # 功能模块根包
├── constant                               # 常量定义
│   └── {Module}Constant.java              # 模块相关常量
├── dto                                    # 数据传输对象
│   └── {Module}Dto.java                   # Dto示例对象
├── entity                                 # 数据库实体
│   └── {EntityName}.java                  # 具体实体类
├── mapper                                 # MyBatis Mapper接口
│   └── {Entity}Mapper.java                # 对应实体的Mapper接口
├── service                                # 服务层
│   ├── impl                               # 服务实现
│   │   └── {Service}Impl.java             # 具体服务实现
│   └── {Service}.java                     # 服务接口
├── task                                   # 批处理实现
│   └── {Module}Processor.java             # 具体处理器实现
└── utils                                  # 模块工具类
    └── {Module}Utils.java                 # Module相关工具类
```

### 1.3 资源文件规范
```
src/main/resources
├── mapper                                # MyBatis映射文件
│   └── {module_name}                     # 模块相关SQL
│       └── {Entity}Mapper.xml            # 具体Mapper XML文件
├── application.yml                       # 主配置文件
├── application-dev.yml                   # 开发环境配置文件
└── logback.xml                           # 日志配置文件
```

## 2. 编码规范

### 2.0 通用约束
- 实体类属性与数据库字段对应，但不使用MyBatis-Plus相关注解，例如@TableName、@TableId、@TableField等相关注解，这个限制了数据库字段的后期修改。
- 代码中避免硬编码，常量应提取到常量类中。
- 所有实现类都应当有对应的接口定义。

### 2.1 命名规范

#### 2.1.1 类命名
- 实体类：`{EntityName}`
- Service接口：`{ModuleName}Service`
- Service实现：`{ModuleName}ServiceImpl`
- Mapper接口：`{EntityName}Mapper`
- 工具类：`{ModuleName}Util`
- 常量类：`{ModuleName}Constant`

#### 2.1.2 方法命名
- 查询：`query{Entity}`、`get{Entity}`、`find{Entity}`
- 保存：`save{Entity}`、`insert{Entity}`
- 更新：`update{Entity}`
- 删除：`delete{Entity}`、`remove{Entity}`
- 统计：`count{Entity}`、`sum{Entity}`
- 批处理：`batch{Operation}`

#### 2.1.3 变量命名
- 常量：全大写下划线分隔，如 `MAX_RETRY_TIMES`、`DISCOUNT_CURVE`
- 变量：小驼峰命名，如 `entityName`、`accountPeriod`
- 集合：复数形式，如 `entities`、`discountFactors`
- 布尔类型：以is/has/can等开头，如 `isValid`、`hasExpired`

### 2.2 SQL规范
```xml
<!-- {操作说明} -->
<select id="{method_name}" resultType="{EntityType}">
    SELECT 
        column_name_1 as columnName1,
        column_name_2 as columnName2
    FROM table_name
    WHERE condition = #{parameter}
    AND is_del = 0
</select>
```

#### 2.2.1 XML命名规范
- Mapper XML文件名必须与对应的Mapper接口名一致
- SQL语句的id必须与Mapper接口中的方法名一致
- XML文件必须放在resources/mapper/{module_name}/目录下

### 2.4 异常处理与日志规范

#### 2.4.1 异常处理原则
1. 批处理程序采用统一的异常处理机制
2. 服务层专注业务逻辑实现，遇到异常直接向上抛出
3. 处理器层统一捕获并处理异常，根据异常类型返回不同的错误消息
4. 业务校验失败时抛出IllegalArgumentException
5. 业务处理失败时抛出RuntimeException
6. 对于影响流程的严重异常，需要发送告警通知

#### 2.4.2 日志记录规范
1. 处理开始日志：
```java
log.info("{module} 处理开始 jobId = {}, taskId = {}, params = {}", 
    moduleName, context.getJobId(), context.getTaskId(), JSONObject.toJSONString(params));
```

2. 关键步骤日志：
```java
log.info("{module} {step} 处理中 jobId = {}, taskId = {}, 数据：{}", 
    moduleName, stepName, context.getJobId(), context.getTaskId(), JSONObject.toJSONString(data));
```

3. 异常记录日志：
```java
log.error("{module} {step} 异常 jobId = {}, taskId = {}, params = {}, 错误信息: {}", 
    moduleName, stepName, context.getJobId(), context.getTaskId(), JSONObject.toJSONString(params), e.getMessage(), e);
```

4. 处理完成日志：
```java
log.info("{module} 处理完成 jobId = {}, taskId = {}, result = {}", 
    moduleName, context.getJobId(), context.getTaskId(), JSONObject.toJSONString(result));
```

#### 2.4.3 日志内容要求
1. 必须包含模块名称和处理步骤
2. 异常日志必须包含完整的异常堆栈
3. 参数和结果使用JSON格式记录
4. 涉及敏感数据时需要脱敏处理
5. 大数据量时注意日志输出的性能影响

## 3. 批处理开发模板

### 3.1 处理器模板示例
```java
@Slf4j
@RequiredArgsConstructor
@Component
public class {Module}Processor implements BasicProcessor {
    
    private final {Module}Service {module}Service;
    
    @Override
    public ProcessResult process(TaskContext context) {
        log.info("{module} 处理开始 jobId = {}, taskId = {}, params = {}", 
                moduleName, context.getJobId(), context.getTaskId(), context.getJobParams());
        try {
            // 1. 参数解析和校验
            {Module}Dto params = Optional.ofNullable(context.getJobParams())
                    .map(str -> JSONObject.parseObject(str, {Module}Dto.class))
                    .orElse(new {Module}Dto());
            
            if (params.getRequiredField() == null || params.getRequiredField().isEmpty()) {
                log.error("{module} 参数错误：必填字段不能为空 jobId = {}, taskId = {}", 
                    moduleName, context.getJobId(), context.getTaskId());
                return ProcessResultUtil.fail("参数错误：必填字段不能为空");
            }
            
            // 2. 执行处理
            {module}Service.process(params);
            
            // 3. 返回结果
            log.info("{module} 处理完成 jobId = {}, taskId = {}", 
                    moduleName, context.getJobId(), context.getTaskId());
            
            return ProcessResultUtil.success("{module}处理执行成功");
            
        } catch (IllegalArgumentException e) {
            // 参数异常，无效输入
            log.error("参数异常 jobId = {}, taskId = {}, params = {}, 错误信息: {}", context.getJobId(), context.getTaskId(), context.getJobParams(), e.getMessage(), e);
            return ProcessResultUtil.fail("{module}参数异常：" + e.getMessage());
        } catch (RuntimeException e) {
            // 运行时异常，包括业务规则异常
            log.error("业务异常 jobId = {}, taskId = {}, params = {}, 错误信息: {}",, context.getJobId(), context.getTaskId(), context.getJobParams(), e.getMessage(), e);
            return ProcessResultUtil.fail("{module}业务异常：" + e.getMessage());
        } catch (Exception e) {
            // 其他未知异常
            log.error("系统异常 jobId = {}, taskId = {}, params = {}",context.getJobId(), context.getTaskId(), context.getJobParams(), e);
            return ProcessResultUtil.fail("{module}系统异常：" + e.getMessage());
        }
    }
}
```

### 4 开发新功能时请注意:
1. 优先复用项目已有的基础设施代码
2. 只开发确实需要新增的代码
3. 遵循项目既有的开发规范
4. 命名必须遵循规范，保持项目风格一致性

### 4.1 参考实现
请参考已有的sample目录作为标准实现示例，新开发的模块应当保持相同的结构和风格。
