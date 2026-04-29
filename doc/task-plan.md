# 专家库管理系统 - 开发任务计划（Agent版本）

> 此文档供Agent使用，包含任务状态、进度跟踪、执行记录、TODO文件引用

---

## 当前状态概览

| 统计项 | 数量 |
|--------|------|
| 总任务数 | 30 |
| 待开始 | 29 |
| 进行中 | 0 |
| 已完成 | 1 |
| 已阻塞 | 0 |

**当前可执行任务**: Task 3(#11), Task 15(#21)

---

## 任务状态定义

| 状态 | 代码 | 说明 |
|------|------|------|
| pending | 0 | 待开始，等待依赖完成或可立即执行 |
| in_progress | 1 | 进行中，正在开发 |
| completed | 2 | 已完成，测试通过 |
| blocked | 3 | 已阻塞，依赖任务未完成 |
| failed | 4 | 测试失败，需要修复 |

---

## 任务详细状态表

### 第一阶段：基础框架搭建（4个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 1 | #10 | 后端项目初始化 | completed | 无 | [task-01-backend-init.md](./tasks/task-01-backend-init.md) | 2026-04-29 | 2026-04-29 | 代码结构完成 |
| Task 2 | #3 | 数据库表结构创建 | pending | #10 | [task-02-db-schema.md](./tasks/task-02-db-schema.md) | - | - | - |
| Task 3 | #11 | 前端项目初始化 | pending | 无 | [task-03-frontend-init.md](./tasks/task-03-frontend-init.md) | - | - | - |
| Task 4 | #12 | 用户登录功能 | pending | #10,#11 | [task-04-login.md](./tasks/task-04-login.md) | - | - | - |

---

### 第二阶段：用户权限管理（4个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 5 | #7 | 用户管理CRUD | pending | #10,#3 | [task-05-user-crud.md](./tasks/task-05-user-crud.md) | - | - | - |
| Task 6 | #4 | 角色权限管理 | pending | #7,#12 | [task-06-role-permission.md](./tasks/task-06-role-permission.md) | - | - | - |
| Task 7 | #6 | 用户管理页面 | pending | #7,#12 | [task-07-user-page.md](./tasks/task-07-user-page.md) | - | - | - |
| Task 29 | #25 | 数据字典管理 | pending | #7 | 见 task-25-29-message-dict.md | - | - | - |

---

### 第三阶段：专家注册管理（10个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 8 | #5 | 专家注册接口 | pending | #3,#12 | [task-08-expert-register-api.md](./tasks/task-08-expert-register-api.md) | - | - | - |
| Task 9 | #9 | 专家注册页面 | pending | #5 | [task-09-expert-register-page.md](./tasks/task-09-expert-register-page.md) | - | - | - |
| Task 10 | #8 | 专家初审功能 | pending | #5,#11 | [task-10-expert-review.md](./tasks/task-10-expert-review.md) | - | - | - |
| Task 11 | #16 | 专家复审/OA审批 | pending | #8 | [task-11-expert-oa-review.md](./tasks/task-11-expert-oa-review.md) | - | - | - |
| Task 12 | #20 | 专家主数据管理 | pending | #8,#9 | [task-12-expert-master-data.md](./tasks/task-12-expert-master-data.md) | - | - | - |
| Task 13 | #13 | 专家主数据页面 | pending | #20,#11 | [task-13-expert-master-page.md](./tasks/task-13-expert-master-page.md) | - | - | - |
| Task 14 | #15 | 专家画像功能 | pending | #5,#16 | [task-14-expert-portrait.md](./tasks/task-14-expert-portrait.md) | - | - | - |
| Task 25 | #26 | 消息模板管理 | pending | #5,#11 | 见 task-25-29-message-dict.md | - | - | - |
| Task 27 | #29 | 短信邮件通知 | pending | #25 | 见 task-25-29-message-dict.md | - | - | - |
| Task 26 | #24 | 企业微信通知 | pending | #5,#11 | 见 task-25-29-message-dict.md | - | - | - |

---

### 第四阶段：专家抽取管理（6个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 15 | #21 | 采购方案单功能 | pending | 无 | [task-15-procurement-plan.md](./tasks/task-15-procurement-plan.md) | - | - | - |
| Task 16 | #18 | 抽取方案配置 | pending | #21 | 见 task-16-20-extraction.md | - | - | - |
| Task 17 | #22 | 专家抽取执行 | pending | #5,#18,#11 | 见 task-16-20-extraction.md | - | - | - |
| Task 18 | #19 | 专家确认流程 | pending | #22,#17 | 见 task-16-20-extraction.md | - | - | - |
| Task 19 | #14 | 抽取方案配置页面 | pending | #19,#22 | 见 task-16-20-extraction.md | - | - | - |
| Task 20 | #17 | 专家确认页面 | pending | #22,#17,#11 | 见 task-16-20-extraction.md | - | - | - |

---

### 第五阶段：评标管理（4个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 21 | #23 | 评标委员会功能 | pending | #22,#18 | 见 task-21-24-bid-evaluation.md | - | - | - |
| Task 22 | #27 | 专家评分功能 | pending | #5,#23 | 见 task-21-24-bid-evaluation.md | - | - | - |
| Task 23 | #30 | 评标委员会页面 | pending | #23,#11 | 见 task-21-24-bid-evaluation.md | - | - | - |
| Task 24 | #31 | 专家评分页面 | pending | #27,#11 | 见 task-21-24-bid-evaluation.md | - | - | - |

---

### 第六阶段：完善优化（2个任务）

| Task# | Agent# | 任务名称 | 状态 | 依赖 | TODO文件 | 开始时间 | 完成时间 | 测试结果 |
|-------|--------|----------|------|------|----------|----------|----------|----------|
| Task 28 | #28 | 专家补抽功能 | pending | #22,#17 | 见 task-25-29-message-dict.md | - | - | - |
| Task 30 | #32 | 系统集成测试 | pending | #27,#28,#29,#30,#31 | [task-30-integration-test.md](./tasks/task-30-integration-test.md) | - | - | - |

---

## TODO文件清单

| 文件 | 包含任务 | 说明 |
|------|----------|------|
| task-01-backend-init.md | Task 1 | 后端初始化TODO+测试用例 |
| task-02-db-schema.md | Task 2 | 数据库创建TODO+测试用例 |
| task-03-frontend-init.md | Task 3 | 前端初始化TODO+测试用例 |
| task-04-login.md | Task 4 | 登录功能TODO+测试用例 |
| task-05-user-crud.md | Task 5 | 用户CRUD TODO+测试用例 |
| task-06-role-permission.md | Task 6 | 角色权限TODO+测试用例 |
| task-07-user-page.md | Task 7 | 用户页面TODO+测试用例 |
| task-08-expert-register-api.md | Task 8 | 专家注册接口TODO+测试用例 |
| task-09-expert-register-page.md | Task 9 | 专家注册页面TODO+测试用例 |
| task-10-expert-review.md | Task 10 | 专家初审TODO+测试用例 |
| task-11-expert-oa-review.md | Task 11 | 专家复审OA TODO+测试用例 |
| task-12-expert-master-data.md | Task 12 | 专家主数据TODO+测试用例 |
| task-13-expert-master-page.md | Task 13 | 专家主数据页面TODO+测试用例 |
| task-14-expert-portrait.md | Task 14 | 专家画像TODO+测试用例 |
| task-15-procurement-plan.md | Task 15 | 采购方案单TODO+测试用例 |
| task-16-20-extraction.md | Task 16-20 | 抽取模块TODO+测试用例 |
| task-21-24-bid-evaluation.md | Task 21-24 | 评标模块TODO+测试用例 |
| task-25-29-message-dict.md | Task 25-29 | 消息字典模块TODO+测试用例 |
| task-30-integration-test.md | Task 30 | 集成测试TODO+测试用例 |

---

## Agent执行指令

### 开始任务流程

1. **读取TODO**: 打开对应的TODO文件（如 task-01-backend-init.md）
2. **检查依赖**: 确认所有依赖任务状态为completed
3. **更新状态**: 将任务状态改为in_progress，记录开始时间
4. **执行开发**: 按照TODO清单逐项完成开发
5. **执行测试**: 每个测试用例必须接触真实环境
6. **保存数据**: 测试数据保存到指定路径（./temp/test-data/）
7. **更新状态**: 测试通过改为completed，记录完成时间

### 测试数据保存规范

- JSON数据: `./temp/test-data/task-{编号}-{名称}.json`
- 截图: `./temp/test-results/task-{编号}-{名称}.png`
- 日志: `./temp/test-results/task-{编号}-{名称}.log`

---

## 快速查询

### 可立即执行的任务
- Task 1 (#10) - 后端项目初始化 - 无依赖
- Task 3 (#11) - 前端项目初始化 - 无依赖
- Task 15 (#21) - 采购方案单功能 - 无依赖

### 关键依赖链
```
Task 1 → Task 2 → Task 4 → Task 5 → Task 8 → Task 10 → Task 11 → Task 17 → Task 22 → Task 30
Task 3 → Task 4 → Task 5 → ...
```

---

## 测试数据目录结构

```
./temp/test-data/
├── auth-token.json          # 管理员Token（Task 4生成）
├── testuser-token.json      # 测试用户Token（Task 6生成）
├── experts-for-review.json  # 待审核专家（Task 8生成）
├── experts-review-result.json # 审核结果（Task 10生成）
├── experts-normal.json      # 正常状态专家（Task 11生成）
├── procurement-plan-data.json # 方案单数据（Task 15生成）
├── extraction-complete-data.json # 抽取数据（Task 17生成）
├── evaluation-complete-data.json # 评分数据（Task 22生成）
└ expert-import-export/
└ ...（其他测试数据）

./temp/test-results/
├── task-01-*.png            # Task 1测试截图
├── task-02-*.sql            # Task 2 SQL日志
├── ...（其他截图）
├── integration-test-report.md # Task 30测试报告
```