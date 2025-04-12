# alm-admin 后台管理系统代码规范

## 1. 项目整体结构

### 1.1 目录结构
```
alm-admin/                                # 项目根目录
├── src/                                  # 源代码目录
│   ├── main/                             # 主要源代码
│   │   ├── java/                         # Java源代码
│   │   │   └── com/xl/alm/               # 基础包路径
│   │   │       ├── AdminApplication.java # 应用启动类
│   │   │       └── web/                  # Web应用包
│   │   │           ├── controller/       # 控制器层
│   │   │           │   ├── common/       # 通用控制器
│   │   │           │   ├── monitor/      # 监控相关控制器
│   │   │           │   ├── system/       # 系统管理控制器
│   │   │           │   └── tool/         # 工具类控制器
│   │   │           └── core/             # 核心配置
│   │   │               └── config/       # 配置类
│   │   └── resources/                    # 资源文件目录
│   │       ├── application.yml           # 主配置文件
│   │       ├── application-dev.yml       # 开发环境配置
│   │       ├── i18n/                     # 国际化资源
│   │       ├── logback.xml               # 日志配置
│   │       └── mybatis/                  # MyBatis配置
│   └── test/                             # 测试代码目录
└── pom.xml                               # Maven配置文件
```

### 1.2 模块结构规范
每个功能模块都应遵循以下标准结构（所有Java包路径均以src/main/java为根目录）：
```
com.xl.alm.{module_name}                  # 功能模块根包
├── controller                            # 控制器层
│   └── {Name}Controller.java           # 控制器类
├── service                               # 服务层
│   ├── impl                              # 服务实现
│   │   └── {service_name}Impl.java            # 具体服务实现
│   └── {service_name}.java                    # 服务接口
├── domain                                # 领域模型
│   ├── entity                            # 数据库实体
│   │   └── {entity_name}Entity.java                 # 实体类
│   ├── vo                                # 视图对象
│   │   └── {vo_name}Vo.java               # 视图对象类
│   └── dto                               # 数据传输对象
│       └── {dto_name}Dto.java              # DTO类
├── mapper                                # MyBatis Mapper接口
│   └── {entity_name}Mapper.java               # 对应实体的Mapper接口
└── utils                                 # 模块工具类
    └── {util_name}Utils.java                # 模块相关工具类
```

### 1.3 资源文件规范
```
src/main/resources
├── mapper                                # MyBatis映射文件
│   └── {module_name}                     # 模块相关SQL
│       └── {entity_name}EntityMapper.xml            # 具体Mapper XML文件
├── i18n                                  # 国际化资源文件
│   └── messages.properties               # 默认语言资源
├── application.yml                       # 主配置文件
├── application-dev.yml                   # 开发环境配置文件
└── logback.xml                           # 日志配置文件
```

## 2. 编码规范

### 2.0 通用约束
- 遵循RESTful API设计规范
- 控制器方法返回统一的响应格式
- 代码中避免硬编码，常量应提取到常量类中
- 所有实现类都应当有对应的接口定义
- 使用统一的异常处理机制

### 2.1 命名规范

#### 2.1.1 类命名
- 控制器类：`{module_name}Controller`
- 服务接口：`{service_name}Service`
- 服务实现：`{service_name}ServiceImpl`
- 实体类：`{entity_name}Entity`
- Mapper接口：`{entity_name}EntityMapper`
- 工具类：`{util_name}Utils`
- 常量类：`{constants_name}Constants`

#### 2.1.2 方法命名
- 查询单个：`get{Entity}`
- 查询列表：`list{Entity}`
- 分页查询：`page{Entity}`
- 新增：`add{Entity}`
- 修改：`update{Entity}`
- 删除：`delete{Entity}`
- 导出：`export{Entity}`
- 导入：`import{Entity}`

#### 2.1.3 变量命名
- 常量：全大写下划线分隔，如 `MAX_COUNT`、`DEFAULT_EXPIRE_TIME`
- 变量：小驼峰命名，如 `userName`、`orderAmount`
- 集合：复数形式，如 `users`、`orderItems`
- 布尔类型：以is/has/can等开头，如 `isValid`、`hasPermission`

### 2.2 Controller层规范

#### 2.2.1 接口URL命名规范
- 使用RESTful风格的API设计
- URL使用小写，多个单词用连字符（-）分隔
- 资源采用复数形式

示例：
```
GET    /api/users          # 获取用户列表
GET    /api/users/{id}     # 获取指定用户
POST   /api/users          # 新增用户
PUT    /api/users/{id}     # 更新用户
DELETE /api/users/{id}     # 删除用户
```

#### 2.2.2 响应结果规范
所有接口返回统一的响应格式：
```java
public class AjaxResult<T> {
    private Integer code;      // 状态码
    private String msg;       // 消息
    private T data;           // 数据
    // 省略getter/setter
}
```

### 2.3 Service层规范
- 业务逻辑应在Service层实现，不在Controller层处理复杂业务
- 事务控制在Service层
- 参数校验应在Service层进行
- 避免在Service中直接操作HttpServletRequest等Web相关对象

### 2.4 异常处理与日志规范

#### 2.4.1 异常处理原则
1. 使用全局异常处理器统一处理异常
2. 业务异常应继承自自定义的基础异常类
3. 不要捕获异常后不处理或只打印日志
4. 避免在finally块中抛出异常

#### 2.4.2 日志记录规范
1. 使用SLF4J + Logback作为日志框架
2. 日志级别合理设置：ERROR、WARN、INFO、DEBUG
3. 敏感信息不应记录到日志中
4. 异常日志应包含完整的堆栈信息

日志示例：
```java
// 操作开始日志
log.info("开始处理[{}]业务，参数：{}", operationName, JSON.toJSONString(params));

// 关键步骤日志
log.info("[{}]处理步骤：{}，数据：{}", operationName, stepName, JSON.toJSONString(data));

// 异常日志
log.error("[{}]处理异常，参数：{}，异常信息：{}", operationName, JSON.toJSONString(params), e.getMessage(), e);

// 操作完成日志
log.info("[{}]处理完成，结果：{}", operationName, JSON.toJSONString(result));
```

### 2.5 安全规范
1. 使用参数绑定接收前端数据，避免直接使用HttpServletRequest获取参数
2. 对外部输入的参数进行校验
3. 敏感数据加密存储
4. 使用HTTPS协议传输数据
5. 防止SQL注入、XSS攻击、CSRF攻击
6. 敏感操作需要进行日志记录

## 3. 开发新功能时请注意
1. 优先复用项目已有的基础设施代码
2. 只开发确实需要新增的代码
3. 遵循项目既有的开发规范
4. 命名必须遵循规范，保持项目风格一致性
5. 新增功能需要编写单元测试
6. 代码提交前进行代码审查