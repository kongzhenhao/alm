# ALM-Admin-UI 前端代码规范

## 1. 项目整体结构

### 1.1 目录结构
```
alm-admin-ui/                             # 项目根目录
├── build/                                # 构建相关配置
├── public/                               # 静态资源
│   ├── favicon.ico                       # 网站图标
│   ├── index.html                        # HTML模板
│   └── libs/                             # 外部库文件
├── src/                                  # 源代码
│   ├── api/                              # API请求
│   ├── assets/                           # 主题、字体、图片等静态资源
│   ├── components/                       # 全局公用组件
│   ├── directive/                        # 全局指令
│   ├── i18n/                             # 国际化
│   ├── icons/                            # 图标
│   ├── layout/                           # 全局布局
│   ├── router/                           # 路由
│   ├── store/                            # 全局状态管理
│   ├── styles/                           # 全局样式
│   ├── utils/                            # 全局工具方法
│   ├── views/                            # 所有页面
│   ├── App.vue                           # 入口页面
│   ├── main.js                           # 入口文件
│   ├── permission.js                     # 权限管理
│   └── settings.js                       # 设置文件
├── .env.development                      # 开发环境变量
├── .env.production                       # 生产环境变量
├── .eslintrc.js                          # ESLint配置
├── .prettierrc                           # Prettier配置
├── babel.config.js                       # Babel配置
├── package.json                          # 依赖管理
└── vue.config.js                         # Vue配置
```

### 1.2 模块结构规范
每个功能模块都应遵循以下标准结构：

```
views/                                    # 视图目录
├── {module_name}/                        # 模块目录
│   ├── index.vue                         # 模块主页面
│   ├── components/                       # 模块私有组件
│   │   └── {ComponentName}.vue           # 具体组件
│   └── {subpage}.vue                     # 子页面
```

### 1.3 API结构规范
```
api/                                      # API目录
├── {module_name}/                        # 模块API
│   └── index.js                          # API方法定义
```

## 2. 编码规范

### 2.0 通用约束
- 使用ES6+语法
- 使用Vue.js官方推荐的风格指南
- 使用Vuex进行状态管理
- 使用Vue Router进行路由管理
- 使用Axios进行HTTP请求
- 使用ESLint + Prettier进行代码格式化

### 2.1 命名规范

#### 2.1.1 文件命名
- 组件文件：使用PascalCase命名，如`UserProfile.vue`
- 页面文件：使用kebab-case命名，如`user-profile.vue`
- JS/TS文件：使用kebab-case命名，如`request-utils.js`
- 样式文件：使用kebab-case命名，如`main-theme.scss`

#### 2.1.2 组件命名
- 组件名应该始终是多个单词的，根组件App除外
- 组件名应该以高级别的单词开头，以描述性的修饰词结尾
- 基础组件应该全部以特定的前缀开头，如`Base`、`App`或`V`
- 单例组件应该以`The`前缀命名，以表示全局唯一性

```javascript
// 好的组件命名
export default {
  name: 'UserProfileCard'
}
```

#### 2.1.3 变量命名
- 变量：使用camelCase命名，如`userData`
- 常量：使用大写下划线命名，如`MAX_COUNT`
- 组件引用：使用PascalCase命名，如`UserProfile`
- 私有属性/方法：使用下划线前缀，如`_privateMethod`

### 2.2 组件规范

#### 2.2.1 组件结构
组件应当按照以下顺序组织：
```vue
<template>
  <!-- 模板内容 -->
</template>

<script>
// 导入语句
import ComponentA from '@/components/ComponentA'

export default {
  name: 'ComponentName',
  components: { ComponentA },
  mixins: [],
  props: {},
  data() {},
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {}
}
</script>

<style scoped>
/* 样式内容 */
</style>
```

#### 2.2.2 Props定义
- Props应该尽可能详细地定义类型和默认值
- 使用camelCase在JavaScript中定义props，使用kebab-case在模板中使用

```javascript
props: {
  status: {
    type: String,
    required: true,
    validator: function(value) {
      return ['success', 'warning', 'danger'].indexOf(value) !== -1
    }
  },
  defaultValue: {
    type: Number,
    default: 0
  }
}
```

### 2.3 样式规范

#### 2.3.1 CSS预处理器
- 使用SCSS作为CSS预处理器
- 使用BEM命名规范
- 组件样式使用scoped属性或CSS Modules隔离

#### 2.3.2 样式结构
```scss
// 变量定义
$primary-color: #409EFF;

// 混合器定义
@mixin flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

// 组件样式
.component {
  &__header {
    @include flex-center;
    color: $primary-color;
  }
  
  &__content {
    padding: 20px;
    
    &--active {
      background-color: #f0f0f0;
    }
  }
}
```

### 2.4 API请求规范

#### 2.4.1 API定义
```javascript
// api/user/index.js
import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/api/users',
    method: 'get',
    params
  })
}

export function getUserById(id) {
  return request({
    url: `/api/users/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/api/users',
    method: 'post',
    data
  })
}
```

#### 2.4.2 API调用
```javascript
// 在组件中调用API
import { getUserList } from '@/api/user'

export default {
  data() {
    return {
      userList: [],
      loading: false
    }
  },
  methods: {
    async fetchUserList() {
      this.loading = true
      try {
        const { data } = await getUserList()
        this.userList = data
      } catch (error) {
        this.$message.error('获取用户列表失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    }
  },
  created() {
    this.fetchUserList()
  }
}
```

### 2.5 路由规范

#### 2.5.1 路由定义
```javascript
// router/modules/user.js
export default {
  path: '/user',
  component: Layout,
  redirect: '/user/list',
  name: 'User',
  meta: {
    title: '用户管理',
    icon: 'user'
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/user/index'),
      name: 'UserList',
      meta: { title: '用户列表', icon: 'list' }
    },
    {
      path: 'create',
      component: () => import('@/views/user/create'),
      name: 'CreateUser',
      meta: { title: '创建用户', icon: 'edit' }
    },
    {
      path: 'edit/:id(\\d+)',
      component: () => import('@/views/user/edit'),
      name: 'EditUser',
      meta: { title: '编辑用户', noCache: true, activeMenu: '/user/list' },
      hidden: true
    }
  ]
}
```

### 2.6 状态管理规范

#### 2.6.1 Store模块定义
```javascript
// store/modules/user.js
import { login, logout, getInfo } from '@/api/user'

const state = {
  token: '',
  name: '',
  avatar: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  }
}

const actions = {
  // 登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          const { data } = response
          commit('SET_TOKEN', data.token)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
```

## 3. 开发新功能时请注意
1. 优先复用项目已有的组件和工具
2. 只开发确实需要新增的代码
3. 遵循项目既有的开发规范
4. 命名必须遵循规范，保持项目风格一致性
5. 新增功能需要考虑响应式设计和浏览器兼容性
6. 代码提交前进行代码格式化和静态检查

## 4. 性能优化建议
1. 使用路由懒加载减少首屏加载时间
2. 合理使用keep-alive缓存组件
3. 大型列表使用虚拟滚动
4. 合理拆分组件，避免过大的单文件组件
5. 使用v-show替代v-if进行频繁切换的元素
6. 使用computed属性代替复杂的模板表达式
7. 避免在模板中使用复杂的计算或方法调用