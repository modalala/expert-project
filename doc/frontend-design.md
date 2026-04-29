# 专家库管理系统 - 前端详细设计文档

## 1. 项目架构设计

### 1.1 目录结构

```
expert-frontend/
├── public/
│   └── favicon.ico
├── src/
│   ├── api/                    # API接口模块
│   │   ├── index.ts            # axios封装
│   │   ├── auth.ts             # 认证相关接口
│   │   ├── expert.ts           # 专家管理接口
│   │   ├── review.ts           # 审核相关接口
│   │   ├── extraction.ts       # 抽取相关接口
│   │   ├── bid.ts              # 评标相关接口
│   │   └── user.ts             # 用户管理接口
│   ├── assets/                 # 静态资源
│   │   ├── images/
│   │   └── styles/
│   │       ├── variables.scss  # CSS变量
│   │       ├── common.scss     # 公共样式
│   │       └── element.scss    # Element Plus样式覆盖
│   ├── components/             # 公共组件
│   │   ├── common/
│   │   │   ├── TablePlus.vue   # 增强表格组件
│   │   │   ├── FormPlus.vue    # 增强表单组件
│   │   │   ├── UploadPlus.vue  # 增强上传组件
│   │   │   ├── DialogPlus.vue  # 增强弹窗组件
│   │   │   ├── SearchBar.vue   # 搜索栏组件
│   │   │   └── PageHeader.vue  # 页面头部组件
│   │   ├── business/
│   │   │   ├── ExpertCard.vue      # 专家信息卡片
│   │   │   ├── ExpertForm.vue      # 专家表单组件
│   │   │   ├── ReviewFlow.vue      # 审核流程组件
│   │   │   ├── ExtractionRule.vue  # 抽取规则配置
│   │   │   ├── ScoreForm.vue       # 评分表单
│   │   │   └── StatusTag.vue       # 状态标签
│   │   └── layout/
│   │   │   ├── AppHeader.vue   # 应用头部
│   │   │   ├── AppSidebar.vue  # 侧边栏菜单
│   │   │   ├── AppFooter.vue   # 应用底部
│   │   │   └── AppBreadcrumb.vue # 面包屑
│   ├── composables/            # 组合式函数
│   │   ├── useTable.ts         # 表格逻辑复用
│   │   ├── useForm.ts          # 表单逻辑复用
│   │   ├── useUpload.ts        # 上传逻辑复用
│   │   ├── usePermission.ts    # 权限判断
│   │   └── useExport.ts        # 导出功能
│   ├── directives/             # 自定义指令
│   │   ├── permission.ts       # 权限指令 v-permission
│   │   └── loading.ts          # 加载指令
│   ├── hooks/                  # 通用hooks
│   │   ├── useRequest.ts       # 请求hook
│   │   ├── useMessage.ts       # 消息提示hook
│   │   └── useConfirm.ts       # 确认弹窗hook
│   ├── layouts/                # 布局组件
│   │   ├── DefaultLayout.vue   # 默认布局（带菜单）
│   │   ├── SimpleLayout.vue    # 简单布局（无菜单）
│   │   └── BlankLayout.vue     # 空白布局（注册/登录）
│   ├── router/                 # 路由配置
│   │   ├── index.ts            # 路由入口
│   │   ├── routes.ts           # 路由表
│   │   └── guards.ts           # 路由守卫
│   ├── stores/                 # Pinia状态管理
│   │   ├── index.ts            # Store入口
│   │   ├── user.ts             # 用户状态
│   │   ├── permission.ts       # 权限状态
│   │   ├── expert.ts           # 专家状态
│   │   └── app.ts              # 应用状态（菜单、主题等）
│   ├── types/                  # TypeScript类型定义
│   │   ├── api.d.ts            # API响应类型
│   │   ├── expert.d.ts         # 专家相关类型
│   │   ├── user.d.ts           # 用户相关类型
│   │   └── common.d.ts         # 通用类型
│   ├── utils/                  # 工具函数
│   │   ├── request.ts          # 请求工具
│   │   ├── auth.ts             # 认证工具（token处理）
│   │   ├── storage.ts          # 本地存储
│   │   ├── download.ts         # 文件下载
│   │   ├── validate.ts         # 表单验证
│   │   ├── date.ts             # 日期处理
│   │   └── tree.ts             # 树形数据处理
│   ├── views/                  # 页面视图
│   │   ├── auth/               # 认证模块
│   │   │   ├── Login.vue       # 登录页
│   │   │   ├── Register.vue    # 注册页（专家公开注册入口）
│   │   │   └── ForgotPassword.vue # 忘记密码
│   │   ├── expert/             # 专家管理
│   │   │   ├── Register.vue    # 专家公开注册页面
│   │   │   ├── Internal.vue    # 内部专家推荐
│   │   │   ├── Review.vue      # 专家初审列表
│   │   │   ├── ReviewDetail.vue # 专家初审详情
│   │   │   ├── ReReview.vue    # 专家复审列表
│   │   │   ├── ReReviewDetail.vue # 专家复审详情
│   │   │   ├── MasterData.vue  # 专家主数据管理
│   │   │   ├── MasterDetail.vue # 专家详情
│   │   │   ├── Portrait.vue    # 专家画像
│   │   │   └── StatusChange.vue # 状态变更
│   │   ├── extraction/         # 抽取管理
│   │   │   ├── PlanList.vue    # 采购方案单列表
│   │   │   ├── PlanDetail.vue  # 采购方案单详情
│   │   │   ├── Scheme.vue      # 抽取方案配置
│   │   │   ├── RuleConfig.vue  # 抽取规则配置
│   │   │   ├── Result.vue      # 抽取结果
│   │   │   └── Confirm.vue     # 专家确认页面（单点登录）
│   │   ├── bid/                # 评标管理
│   │   │   ├── CommitteeList.vue # 评标委员会列表
│   │   │   ├── CommitteeDetail.vue # 评标委员会详情
│   │   │   ├── Evaluation.vue  # 专家评分
│   │   │   └── EvaluationDetail.vue # 评分详情
│   │   ├── user/               # 用户管理
│   │   │   ├── List.vue        # 用户列表
│   │   │   ├── Role.vue        # 角色管理
│   │   │   └── Permission.vue  # 权限管理
│   │   ├── dashboard/          # 首页/仪表盘
│   │   │   └── Index.vue       # 首页
│   │   └── error/              # 错误页面
│   │   │   ├── 404.vue         # 404页面
│   │   │   └──  403.vue        # 403页面
│   ├── App.vue                 # 应用根组件
│   ├── main.ts                 # 应用入口
│   └── env.d.ts                # 环境变量类型
├── .env                        # 环境变量
├── .env.development            # 开发环境变量
├── .env.production             # 生产环境变量
├── vite.config.ts              # Vite配置
├── tsconfig.json               # TypeScript配置
├── package.json                # 项目依赖
└── README.md                   # 项目说明
```

### 1.2 技术栈详情

| 类别 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 核心 | Vue | 3.4+ | Composition API |
| 构建 | Vite | 5.x | 开发构建工具 |
| 状态 | Pinia | 2.x | 状态管理 |
| 路由 | Vue Router | 4.x | 路由管理 |
| UI | Element Plus | 2.x | UI组件库 |
| HTTP | Axios | 1.x | HTTP请求 |
| 工具 | VueUse | 10.x | 组合式API工具集 |
| 表单 | VeeValidate | 4.x | 表单验证 |
| 图表 | ECharts | 5.x | 图表展示（专家画像） |
| CSS | SCSS | - | CSS预处理器 |
| 语言 | TypeScript | 5.x | 类型支持 |

### 1.3 代码规范

- 使用 ESLint + Prettier 进行代码格式化
- 文件命名：组件使用 PascalCase，其他使用 camelCase
- 组件定义：使用 `<script setup lang="ts">`
- API接口：统一返回类型 `ApiResponse<T>`
- 样式：使用 scoped 样式，避免全局污染

---

## 2. 页面设计

### 2.1 认证模块

#### 2.1.1 登录页 (Login.vue)

**布局**：
```
+------------------------------------------+
|              [Logo] 专家库管理系统          |
+------------------------------------------+
|                                          |
|    +------------------------------+      |
|    |      用户名/手机号            |      |
|    +------------------------------+      |
|    |      密码                     |      |
|    +------------------------------+      |
|    |  [记住我]          [登录按钮] |      |
|    +------------------------------+      |
|                                          |
|    专家注册入口 | 忘记密码                  |
+------------------------------------------+
```

**功能**：
- 用户名/密码登录
- 记住登录状态
- 登录失败提示
- 跳转专家注册入口

#### 2.1.2 专家公开注册页面 (expert/Register.vue)

**布局**：多页签表单
```
+------------------------------------------+
|  [基本信息] [职业证书] [教育经历] [成果展示] |
+------------------------------------------+
|                                          |
|  +----------------------------------+    |
|  | 姓名: [____]    性别: [男/女]     |    |
|  | 手机: [____]    邮箱: [____]     |    |
|  | 身份证: [____]                   |    |
|  | 专家类型: [下拉选择]              |    |
|  | 专家级别: [下拉选择]              |    |
|  | 擅长领域: [多选]                  |    |
|  | 工作单位: [____]                  |    |
|  | 职务: [____]                     |    |
|  +----------------------------------+    |
|                                          |
|  [附件上传区域]                           |
|                                          |
|  [上一步]              [下一步/提交]       |
+------------------------------------------+
```

**页签内容**：
| 页签 | 字段 |
|------|------|
| 基本信息 | 姓名、性别、手机、邮箱、身份证、专家类型、专家级别、擅长领域、工作单位、职务、简介 |
| 职业证书 | 证书名称、证书编号、发证机构、发证日期、证书附件 |
| 教育经历 | 学校、专业、学历、学位、毕业时间 |
| 成果展示 | 成果名称、成果类型、成果描述、成果附件 |

### 2.2 专家管理模块

#### 2.2.1 专家初审列表 (expert/Review.vue)

**布局**：
```
+------------------------------------------+
|  专家初审                                 |
+------------------------------------------+
|  [搜索栏: 姓名/状态/来源/日期范围]         |
|  [审核通过] [审核拒绝] [批量操作]          |
+------------------------------------------+
|  +------------------------------------+  |
|  | 专家姓名 | 状态 | 来源 | 注册时间   |  |
|  | 张三     | 待审核| 公开 | 2024-01-01|  |
|  | 李四     | 待审核| 内部 | 2024-01-02|  |
|  +------------------------------------+  |
|  [查看详情] [审核]                         |
+------------------------------------------+
|  [分页控件]                               |
+------------------------------------------+
```

#### 2.2.2 专家主数据管理 (expert/MasterData.vue)

**布局**：
```
+------------------------------------------+
|  专家主数据管理                            |
+------------------------------------------+
|  [搜索栏: 姓名/类型/级别/状态/领域]         |
|  [导入] [导出] [新增]                      |
+------------------------------------------+
|  +------------------------------------+  |
|  | 姓名 | 类型 | 级别 | 状态 | 评标次数 |  |
|  | 张三 | 技术 | 高级 | 正常 | 15      |  |
|  | 李四 | 经济 | 中级 | 正常 | 8       |  |
|  +------------------------------------+  |
|  [详情] [编辑] [状态变更] [评分记录]        |
+------------------------------------------+
|  [分页控件]                               |
+------------------------------------------+
```

#### 2.2.3 专家画像 (expert/Portrait.vue)

**布局**：
```
+------------------------------------------+
|  专家画像 - 张三                           |
+------------------------------------------+
|  +----------------+  +------------------+ |
|  | 基本信息       |  | [饼图]评标类型分布| |
|  | 姓名: 张三     |  +------------------+ |
|  | 类型: 技术类   |  +------------------+ |
|  | 级别: 高级     |  | [折线图]评分趋势  | |
|  | 状态: 正常     |  +------------------+ |
|  +----------------+                      |
+------------------------------------------+
|  +------------------------------------+  |
|  | 评标记录列表                        |  |
|  | 项目名称 | 评标日期 | 角色 | 得分   |  |
|  | 项目A   | 2024-01  | 成员 | 95    |  |
|  +------------------------------------+  |
+------------------------------------------+
|  +------------------------------------+  |
|  | 抽取记录列表                        |  |
|  | 项目名称 | 抽取日期 | 状态 | 确认时间|  |
|  | 项目B   | 2024-02  | 已确认| 10:00 |  |
|  +------------------------------------+  |
+------------------------------------------+
```

### 2.3 抽取管理模块

#### 2.3.1 抽取方案配置 (extraction/Scheme.vue)

**布局**：
```
+------------------------------------------+
|  抽取方案配置                              |
+------------------------------------------+
|  采购方案单: [选择/新增]                    |
| 抽取方式: [线上] [线下] [线上线下相结合]    |
+------------------------------------------+
|  +------------------------------------+  |
|  | 抽取规则配置                        |  |
|  | 专家类型: [多选]                    |  |
|  | 专家级别: [多选]                    |  |
|  | 擅长领域: [多选]                    |  |
|  | 抽取数量: [____]                    |  |
|  +------------------------------------+  |
|  +------------------------------------+  |
|  | 排除条件                            |  |
|  | 近N月评标次数>=N: [勾选]            |  |
|  | 排除特定专家: [选择专家]             |  |
|  | 排除管理层: [勾选]                   |  |
|  +------------------------------------+  |
|  [保存] [立即抽取]                         |
+------------------------------------------+
```

#### 2.3.2 专家确认页面 (extraction/Confirm.vue)

**说明**：此页面通过企微链接单点登录访问，无需登录状态

**布局**：
```
+------------------------------------------+
|  专家评标确认                              |
+------------------------------------------+
|  尊敬的张三专家：                          |
|  您被抽选参与以下项目的评标工作：           |
+------------------------------------------+
|  +------------------------------------+  |
|  | 项目名称: XXX采购项目               |  |
|  | 评标时间: 2024-03-15 09:00          |  |
|  | 评标地点: XX会议室                  |  |
|  | 项目简介: [查看详情]                 |  |
|  +------------------------------------+  |
+------------------------------------------+
|  请在开标前4小时确认是否参加               |
+------------------------------------------+
|  [同意参加] [拒绝参加]                     |
|                                          |
|  拒绝原因: [下拉选择]                      |
|  拒绝理由: [文本输入]                      |
+------------------------------------------+
```

### 2.4 评标管理模块

#### 2.4.1 专家评分页面 (bid/Evaluation.vue)

**布局**：
```
+------------------------------------------+
|  专家评分                                  |
+------------------------------------------+
|  项目: XXX采购项目                         |
|  评标委员会: [查看成员列表]                 |
+------------------------------------------+
|  +------------------------------------+  |
|  | 评分专家: 张三                       |  |
|  +------------------------------------+  |
|  +------------------------------------+  |
|  | 评分指标                            |  |
|  | [-] 迟到扣分: 0分                    |  |
|  | [-] 缺席扣分: 0分                    |  |
|  | [-] 其他违规: 0分                    |  |
|  +------------------------------------+  |
|  是否重大违规(一票否决): [是/否]           |
|  扣分说明: [文本输入]                       |
+------------------------------------------+
|  [提交评分]                                |
+------------------------------------------+
```

### 2.5 用户管理模块

#### 2.5.1 用户列表 (user/List.vue)

**布局**：
```
+------------------------------------------+
|  用户管理                                  |
+------------------------------------------+
|  [搜索栏: 用户名/姓名/状态]                 |
|  [新增用户]                                |
+------------------------------------------+
|  +------------------------------------+  |
|  | 用户名 | 姓名 | 角色 | 状态 | 创建时间|  |
|  | admin | 管理员| 系统管理员| 正常|... |  |
|  | user1 | 张三 | 审核员   | 正常|... |  |
|  +------------------------------------+  |
|  [编辑] [分配角色] [启用/禁用]              |
+------------------------------------------+
|  [分页控件]                               |
+------------------------------------------+
```

---

## 3. 组件设计

### 3.1 公共组件

#### 3.1.1 TablePlus.vue - 增强表格组件

**Props**：
| 参数 | 类型 | 说明 |
|------|------|------|
| columns | Column[] | 列配置 |
| data | any[] | 表格数据 |
| loading | boolean | 加载状态 |
| pagination | Pagination | 分页配置 |
| selectable | boolean | 是否可选择 |
| toolbar | ToolbarItem[] | 工具栏按钮 |

**Events**：
| 事件 | 参数 | 说明 |
|------|------|------|
| selection-change | selection[] | 选择变化 |
| page-change | page, size | 分页变化 |
| toolbar-click | item | 工具栏点击 |

**Features**：
- 自动分页
- 工具栏按钮权限控制
- 列排序/筛选
- 导出Excel

#### 3.1.2 UploadPlus.vue - 增强上传组件

**Props**：
| 参数 | 类型 | 说明 |
|------|------|------|
| accept | string | 接收文件类型 |
| limit | number | 文件数量限制 |
| maxSize | number | 文件大小限制(MB) |
| fileList | File[] | 已上传文件列表 |
| disabled | boolean | 是否禁用 |

**Events**：
| 事件 | 参数 | 说明 |
|------|------|------|
| success | file, response | 上传成功 |
| error | file, error | 上传失败 |
| remove | file | 删除文件 |

**Features**：
- 图片预览
- 文件类型校验
- 大小限制校验
- 上传进度显示

### 3.2 业务组件

#### 3.2.1 ExpertCard.vue - 专家信息卡片

**Props**：
| 参数 | 类型 | 说明 |
|------|------|------|
| expert | Expert | 专家信息对象 |
| showActions | boolean | 是否显示操作按钮 |

**展示内容**：
- 专家头像/照片
- 姓名、类型、级别
- 擅长领域标签
- 状态标签
- 评标次数统计

#### 3.2.2 ReviewFlow.vue - 审核流程组件

**Props**：
| 参数 | 类型 | 说明 |
|------|------|------|
| currentStep | number | 当前步骤 |
| history | ReviewLog[] | 审核历史 |

**流程步骤**：
1. 提交注册
2. 初审
3. 复审(OA审批)
4. 入库

---

## 4. 路由设计

### 4.1 路由表

```typescript
const routes = [
  // 空白布局（无需登录）
  {
    path: '/auth',
    component: BlankLayout,
    children: [
      { path: 'login', name: 'Login', component: () => import('@/views/auth/Login.vue') },
      { path: 'register', name: 'ExpertRegister', component: () => import('@/views/expert/Register.vue') },
      { path: 'confirm', name: 'ExpertConfirm', component: () => import('@/views/extraction/Confirm.vue'), meta: { sso: true } },
    ]
  },

  // 默认布局（需登录）
  {
    path: '/',
    component: DefaultLayout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/Index.vue') },

      // 专家管理
      {
        path: 'expert',
        children: [
          { path: 'internal', name: 'ExpertInternal', component: () => import('@/views/expert/Internal.vue') },
          { path: 'review', name: 'ExpertReview', component: () => import('@/views/expert/Review.vue') },
          { path: 'review/:id', name: 'ExpertReviewDetail', component: () => import('@/views/expert/ReviewDetail.vue') },
          { path: 're-review', name: 'ExpertReReview', component: () => import('@/views/expert/ReReview.vue') },
          { path: 're-review/:id', name: 'ExpertReReviewDetail', component: () => import('@/views/expert/ReReviewDetail.vue') },
          { path: 'master', name: 'ExpertMaster', component: () => import('@/views/expert/MasterData.vue') },
          { path: 'master/:id', name: 'ExpertDetail', component: () => import('@/views/expert/MasterDetail.vue') },
          { path: 'portrait/:id', name: 'ExpertPortrait', component: () => import('@/views/expert/Portrait.vue') },
        ]
      },

      // 抽取管理
      {
        path: 'extraction',
        children: [
          { path: 'plan', name: 'PlanList', component: () => import('@/views/extraction/PlanList.vue') },
          { path: 'plan/:id', name: 'PlanDetail', component: () => import('@/views/extraction/PlanDetail.vue') },
          { path: 'scheme', name: 'Scheme', component: () => import('@/views/extraction/Scheme.vue') },
          { path: 'result/:id', name: 'ExtractionResult', component: () => import('@/views/extraction/Result.vue') },
        ]
      },

      // 评标管理
      {
        path: 'bid',
        children: [
          { path: 'committee', name: 'CommitteeList', component: () => import('@/views/bid/CommitteeList.vue') },
          { path: 'committee/:id', name: 'CommitteeDetail', component: () => import('@/views/bid/CommitteeDetail.vue') },
          { path: 'evaluation', name: 'Evaluation', component: () => import('@/views/bid/Evaluation.vue') },
          { path: 'evaluation/:id', name: 'EvaluationDetail', component: () => import('@/views/bid/EvaluationDetail.vue') },
        ]
      },

      // 用户管理
      {
        path: 'user',
        children: [
          { path: 'list', name: 'UserList', component: () => import('@/views/user/List.vue') },
          { path: 'role', name: 'RoleManage', component: () => import('@/views/user/Role.vue') },
          { path: 'permission', name: 'PermissionManage', component: () => import('@/views/user/Permission.vue') },
        ]
      },
    ]
  },

  // 错误页面
  { path: '/404', name: 'NotFound', component: () => import('@/views/error/404.vue') },
  { path: '/403', name: 'Forbidden', component: () => import('@/views/error/403.vue') },
  { path: '/:pathMatch(.*)*', redirect: '/404' }
]
```

### 4.2 路由守卫

```typescript
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  // SSO页面（企微单点登录）直接放行
  if (to.meta.sso) {
    // 验证token参数
    const token = to.query.token as string
    if (token) {
      userStore.setSsoToken(token)
    }
    return next()
  }

  // 需要登录的页面
  if (to.path.startsWith('/auth')) {
    return next()
  }

  if (!userStore.token) {
    return next({ path: '/auth/login', query: { redirect: to.fullPath } })
  }

  // 权限检查
  const permissionStore = usePermissionStore()
  if (to.meta.permission && !permissionStore.hasPermission(to.meta.permission)) {
    return next('/403')
  }

  next()
})
```

---

## 5. 状态管理设计

### 5.1 用户状态 (stores/user.ts)

```typescript
export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null as UserInfo | null,
    roles: [] as string[],
    permissions: [] as string[],
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    hasRole: (state) => (role: string) => state.roles.includes(role),
    hasPermission: (state) => (perm: string) => state.permissions.includes(perm),
  },

  actions: {
    async login(params: LoginParams) { ... },
    async logout() { ... },
    async getUserInfo() { ... },
    setSsoToken(token: string) { ... },
  }
})
```

### 5.2 应用状态 (stores/app.ts)

```typescript
export const useAppStore = defineStore('app', {
  state: () => ({
    sidebarCollapsed: false,
    menuList: [] as MenuItem[],
    theme: 'light',
    size: 'default',
  }),

  actions: {
    toggleSidebar() { ... },
    async loadMenu() { ... },
    setTheme(theme: string) { ... },
  }
})
```

---

## 6. API接口设计

### 6.1 Axios封装 (api/index.ts)

```typescript
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
})

// 请求拦截：添加token
request.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截：统一处理错误
request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      // token过期，跳转登录
      router.push('/auth/login')
    }
    return Promise.reject(error)
  }
)

// 统一响应类型
interface ApiResponse<T> {
  code: number
  message: string
  data: T
}
```

### 6.2 API模块划分

#### 认证接口 (api/auth.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| login | POST | /api/auth/login | 用户登录 |
| logout | POST | /api/auth/logout | 用户登出 |
| refreshToken | POST | /api/auth/refresh | 刷新token |
| ssoLogin | GET | /api/auth/sso | 单点登录验证 |

#### 专家接口 (api/expert.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| register | POST | /api/expert/register | 专家注册 |
| getList | GET | /api/expert/list | 专家列表 |
| getDetail | GET | /api/expert/:id | 专家详情 |
| update | PUT | /api/expert/:id | 更新专家 |
| changeStatus | PUT | /api/expert/:id/status | 状态变更 |
| export | GET | /api/expert/export | 导出专家 |
| import | POST | /api/expert/import | 导入专家 |
| getPortrait | GET | /api/expert/:id/portrait | 专家画像 |

#### 审核接口 (api/review.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| getReviewList | GET | /api/review/list | 初审列表 |
| reviewPass | POST | /api/review/:id/pass | 初审通过 |
| reviewReject | POST | /api/review/:id/reject | 初审拒绝 |
| getReReviewList | GET | /api/review/re-list | 复审列表 |
| submitOA | POST | /api/review/:id/oa | 提交OA审批 |

#### 抽取接口 (api/extraction.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| getPlanList | GET | /api/extraction/plan/list | 方案单列表 |
| createPlan | POST | /api/extraction/plan | 创建方案单 |
| getScheme | GET | /api/extraction/scheme/:planId | 获取抽取方案 |
| saveScheme | POST | /api/extraction/scheme | 保存抽取方案 |
| execute | POST | /api/extraction/execute | 执行抽取 |
| getResult | GET | /api/extraction/result/:id | 抽取结果 |
| confirm | POST | /api/extraction/confirm | 专家确认 |
| reject | POST | /api/extraction/reject | 专家拒绝 |
| reExtract | POST | /api/extraction/re-extract | 重新抽取 |

#### 评标接口 (api/bid.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| getCommitteeList | GET | /api/bid/committee/list | 委员会列表 |
| getCommitteeDetail | GET | /api/bid/committee/:id | 委员会详情 |
| submitEvaluation | POST | /api/bid/evaluation | 提交评分 |
| getEvaluationHistory | GET | /api/bid/evaluation/history/:expertId | 评分历史 |

#### 用户接口 (api/user.ts)
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| getUserList | GET | /api/user/list | 用户列表 |
| createUser | POST | /api/user | 创建用户 |
| updateUser | PUT | /api/user/:id | 更新用户 |
| getRoleList | GET | /api/user/role/list | 角色列表 |
| createRole | POST | /api/user/role | 创建角色 |
| assignRole | POST | /api/user/:id/role | 分配角色 |
| getPermissionList | GET | /api/user/permission/list | 权限列表 |

---

## 7. 数据字典设计

### 7.1 专家类型
| 代码 | 名称 |
|------|------|
| TECH | 技术类 |
| ECON | 经济类 |
| LAW | 法律类 |
| MGMT | 管理类 |

### 7.2 专家级别
| 代码 | 名称 |
|------|------|
| JUNIOR | 初级 |
| INTERMEDIATE | 中级 |
| SENIOR | 高级 |
| EXPERT | 资深 |

### 7.3 专家状态
| 代码 | 名称 | 说明 |
|------|------|------|
| POTENTIAL | 潜在专家 | 注册后待审核 |
| NORMAL | 正常 | 审核通过，可参与评标 |
| SUSPENDED | 暂停 | 暂时不可参与评标 |
| ELIMINATED | 淘汰 | 不再使用 |

### 7.4 专家来源
| 代码 | 名称 |
|------|------|
| PUBLIC | 公开注册 |
| INTERNAL | 内部推荐 |

### 7.5 抽取方式
| 代码 | 名称 |
|------|------|
| ONLINE | 线上抽取 |
| OFFLINE | 线下抽取 |
| MIXED | 线上线下结合 |

### 7.6 确认状态
| 代码 | 名称 |
|------|------|
| PENDING | 待确认 |
| CONFIRMED | 已确认 |
| REJECTED | 已拒绝 |
| TIMEOUT | 超时未确认 |

### 7.7 拒绝原因
| 代码 | 名称 |
|------|------|
| TIME_CONFLICT | 时间冲突 |
| PERSONAL_REASON | 个人原因 |
| WORK_BUSY | 工作繁忙 |
| HEALTH_REASON | 健康原因 |
| OTHER | 其他原因 |

---

## 8. 安全设计

- Token存储：localStorage，过期时间2小时
- 路由守卫：未登录跳转登录页
- 权限指令：v-permission 控制按钮显示
- XSS防护：用户输入内容过滤
- CSRF防护：token放在header中

---

## 9. 性能优化

- 路由懒加载：所有页面使用动态import
- 图片懒加载：使用VueUse的useIntersectionObserver
- 组件缓存：keep-alive缓存常用页面
- 请求缓存：相同请求短时间内复用结果
- 虚拟滚动：大列表使用虚拟滚动组件