# Task 3: 前端项目初始化

**Agent Task ID**: #11  
**依赖**: 无  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 1. 项目骨架创建
- [ ] 使用Vite创建Vue3项目
- [ ] 配置package.json依赖：
  - vue 3.4+
  - vue-router 4.x
  - pinia 2.x
  - element-plus 2.x
  - axios 1.x
  - @vueuse/core 10.x
  - echarts 5.x
  - sass
- [ ] 创建项目目录结构：
  ```
  src/
  ├── api/
  ├── assets/styles/
  ├── components/common/
  ├── components/business/
  ├── components/layout/
  ├── composables/
  ├── directives/
  ├── hooks/
  ├── layouts/
  ├── router/
  ├── stores/
  ├── types/
  ├── utils/
  ├── views/
  └── App.vue
  ```

### 2. Vite配置
- [ ] 配置vite.config.ts
- [ ] 配置路径别名 @/src
- [ ] 配置API代理到后端
- [ ] 配置环境变量文件

### 3. Axios封装
- [ ] 创建request.ts基础配置
- [ ] 配置请求拦截器（添加token）
- [ ] 配置响应拦截器（统一错误处理）
- [ ] 创建ApiResponse类型定义

### 4. 基础样式配置
- [ ] 配置CSS变量（主题色、字体等）
- [ ] 配置Element Plus样式覆盖
- [ ] 配置公共样式

### 5. 布局组件
- [ ] 创建DefaultLayout（带侧边栏菜单）
- [ ] 创建BlankLayout（空白布局）
- [ ] 创建AppHeader头部组件
- [ ] 创建AppSidebar侧边栏组件

### 6. 路由基础配置
- [ ] 创建router/index.ts
- [ ] 创建router/routes.ts基础路由表
- [ ] 创建router/guards.ts路由守卫（暂时简化）

### 7. 状态管理基础
- [ ] 创建stores/index.ts
- [ ] 创建stores/app.ts应用状态

---

## 测试用例（真实环境）

### TC-03-01: 项目启动测试
**测试步骤**:
1. 执行 `npm install`
2. 执行 `npm run dev`
3. 浏览器访问 `http://localhost:5173`

**预期结果**: 页面正常显示，无报错

**测试数据保存**: 截图保存到 `./temp/test-results/task-03-app-start.png`

---

### TC-03-02: 布局组件渲染测试
**测试步骤**:
1. 访问首页
2. 检查头部组件渲染
3. 检查侧边栏渲染
4. 检查主内容区渲染

**预期结果**: 布局完整，侧边栏可展开折叠

**测试数据保存**: 截图保存到 `./temp/test-results/task-03-layout.png`

---

### TC-03-03: API代理测试
**测试步骤**:
1. 启动后端服务（Task 1）
2. 前端调用 `/api/test/db-conn`
3. 检查代理是否正常转发

**预期结果**: API请求成功返回数据

**测试命令**:
```bash
# 在前端项目执行
curl http://localhost:5173/api/test/db-conn
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-03-api-proxy.json`

---

### TC-03-04: Element Plus组件测试
**测试步骤**:
1. 创建测试页面使用el-button、el-table
2. 检查组件渲染正常
3. 检查样式无冲突

**预期结果**: Element Plus组件正常显示

**测试数据保存**: 截图保存到 `./temp/test-results/task-03-element-plus.png`

---

### TC-03-05: TypeScript编译测试
**测试步骤**:
1. 执行 `npm run build`
2. 检查编译是否成功

**预期结果**: 无TypeScript错误，构建成功

**测试命令**:
```bash
npm run build
ls -la dist/
```

**测试数据保存**: 构建日志保存到 `./temp/test-data/task-03-build-log.txt`

---

## 验收标准

- [ ] 项目可正常启动
- [ ] 布局组件完整渲染
- [ ] API代理配置正确
- [ ] Element Plus组件正常
- [ ] TypeScript编译无错误
- [ ] 测试截图和数据已保存