# 专家库管理系统

气死我了，deepseek的KEY被人第二天全部花光！！！！

一个用于管理招投标过程中专家资源全流程的管理系统，包括专家注册、审核、抽取、评标、评分等功能。

## 项目结构

```
expert-project/
├── expert-backend/          # 后端项目 (Spring Boot)
├── expert-frontend/         # 前端项目 (Vue3)
├── doc/                     # 项目文档
│   ├── 项目理解与需求分析.md
│   ├── backend-design.md
│   ├── frontend-design.md
│   └── task-plan.md
└── CLAUDE.md                # Claude Code 配置
```

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.7
- **认证**: JWT (jjwt 0.12.5)
- **API文档**: SpringDoc OpenAPI 2.5.0
- **工具**: Hutool 5.8.26, EasyExcel 3.3.4

### 前端
- **框架**: Vue 3.5 + Composition API
- **构建**: Vite 8.0
- **UI**: Element Plus 2.13
- **图表**: ECharts 6.0
- **路由**: Vue Router 4.6
- **状态**: Pinia 3.0
- **HTTP**: Axios 1.15
- **类型**: TypeScript 6.0

## 功能模块

### 第一阶段：专家库核心
- 专家公开征集注册（基本信息、证书、教育经历、成果展示）
- 内部专家推荐
- 专家初审管理
- 专家复审（OA审批）
- 专家主数据管理
- 用户体系与权限管理

### 第二阶段：业务流程
- 采购方案单管理
- 专家抽取方案配置
- 专家确认流程（企微/邮件通知）
- 评标委员会管理
- 专家评分与违规记录
- 专家画像统计

### 第三阶段：完善优化
- 消息通知完善
- 数据导入导出
- 性能优化

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Node.js 18+
- Maven 3.9+

### 后端启动

```bash
cd expert-backend

# 配置数据库连接 (src/main/resources/application.yml)
# 创建数据库: CREATE DATABASE expert_db;

# 编译运行
mvn clean package -DskipTests
java -jar target/expert-backend-1.0.0.jar
```

后端服务: http://localhost:8080

### 前端启动

```bash
cd expert-frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build
```

前端服务: http://localhost:5173

### 默认账号
- 管理员: `admin` / `Admin@123`

## 数据库初始化

后端启动时自动执行初始化脚本:
- `db/init_schema.sql` - 创建表结构
- `db/init_data.sql` - 初始化数据

主要表:
- `sys_user` - 系统用户
- `sys_role` - 角色
- `sys_permission` - 权限
- `expert_info` - 专家信息
- `expert_certificate` - 专家证书
- `expert_education` - 教育经历
- `expert_achievement` - 成果记录
- `procurement_plan` - 采购方案
- `expert_extraction` - 抽取记录
- `bid_committee` - 评标委员会
- `expert_evaluation` - 评分记录

## API 文档

启动后端后访问 Swagger UI:
- http://localhost:8080/swagger-ui.html

主要接口:
- `/api/auth/*` - 认证相关
- `/api/user/*` - 用户管理
- `/api/expert/*` - 专家管理
- `/api/dashboard/*` - 首页统计
- `/api/plan/*` - 方案管理
- `/api/bid/*` - 评标管理

## 项目截图

### 首页仪表盘
展示专家总数、待审核、本月评标等统计数据，以及专家类型/级别分布图表。

### 专家管理
- 专家注册申请
- 专家审核列表
- 专家主数据管理
- 专家画像分析

### 评标流程
- 采购方案单
- 专家抽取
- 委员会组建
- 专家评分

## 开发说明

### 目录规范
- `doc/` - 设计文档
- `doc/tasks/` - 任务TODO文件
- `temp/` - 临时文件、测试数据

### Git 提交规范
- `feat:` 新功能
- `fix:` Bug修复
- `docs:` 文档更新
- `refactor:` 重构
- `test:` 测试

## License

MIT License
