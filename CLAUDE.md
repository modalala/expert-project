# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目信息

专家库管理系统 - 用于管理招投标过程中的专家资源全流程管理

**技术栈**: Vue3 + Spring Boot + MySQL

---

## 新对话快速入口

### 第一步：阅读核心文档

按以下顺序阅读文档：

1. **需求理解**: `./doc/项目理解与需求分析.md` - 了解项目需求和功能模块
2. **任务计划**: `./doc/task-plan.md` - **核心入口**，包含任务状态和TODO引用
3. **详细设计**: 根据需要查看
   - `./doc/backend-design.md` - 后端架构设计
   - `./doc/frontend-design.md` - 前端架构设计

### 第二步：查看任务状态

在 `./doc/task-plan.md` 中查看：
- 当前可执行的任务（无依赖或依赖已完成）
- 任务状态（pending/in_progress/completed）
- 每个任务的TODO文件引用

### 第三步：执行任务

1. 打开任务对应的TODO文件（如 `./doc/tasks/task-01-backend-init.md`）
2. 按TODO清单逐项完成开发
3. 执行真实环境测试（curl命令+数据库验证）
4. 保存测试数据到 `./temp/test-data/`
5. 更新任务状态到 `./doc/task-status.json`

---

## 目录规范

| 目录 | 用途 |
|------|------|
| `./doc` | 文档类信息（需求文档、设计文档、API文档等） |
| `./doc/tasks` | 任务TODO文件（每个任务的开发步骤+测试用例） |
| `./temp` | 临时性代码、测试文件、中间产物 |
| `./temp/test-data` | 测试数据JSON文件（跨对话共享） |
| `./temp/test-results` | 测试截图、日志、报告 |

---

## 文档索引

| 文档 | 路径 | 说明 |
|------|------|------|
| 需求分析 | `./doc/项目理解与需求分析.md` | 功能模块、工期估算 |
| 任务计划 | `./doc/task-plan.md` | **核心入口**，任务状态表+TODO引用 |
| 后端设计 | `./doc/backend-design.md` | Spring Boot架构、数据库DDL、API设计 |
| 前端设计 | `./doc/frontend-design.md` | Vue3架构、页面设计、组件设计 |
| 任务详情 | `./doc/tasks/*.md` | 每个任务的TODO+测试用例 |
| 任务状态 | `./doc/task-status.json` | 任务执行状态持久化 |

---

## 开发优先级

先开发专家库核心功能（注册、审核、主数据管理），后开发业务流程（抽取、确认、评分）

---

## 测试数据共享

以下测试数据可跨对话使用：

| 文件 | 生成任务 | 用途 |
|------|----------|------|
| `auth-token.json` | Task 4 | 管理员Token，后续API测试使用 |
| `testuser-token.json` | Task 6 | 测试用户Token，权限测试使用 |
| `experts-for-review.json` | Task 8 | 张三、李四专家数据，审核测试使用 |
| `experts-normal.json` | Task 11 | 正常状态专家，抽取测试使用 |
| `procurement-plan-data.json` | Task 15 | 方案单数据，抽取测试使用 |

---

## Agent工作流程

1. **开始新对话**: 
   - 读取 `./doc/task-plan.md` 获取当前状态
   - 读取 `./doc/task-status.json` 获取已完成任务
   
2. **选择任务**:
   - 找到状态为pending且依赖已完成的任务
   - 打开对应的TODO文件
   
3. **执行开发**:
   - 按TODO清单完成开发
   - 参考backend-design.md或frontend-design.md
   
4. **执行测试**:
   - 必须接触真实环境（数据库、API）
   - 使用curl命令测试后端接口
   - 保存测试数据JSON
   
5. **更新状态**:
   - 更新 `./doc/task-status.json`
   - 更新 `./doc/task-plan.md` 中的状态表