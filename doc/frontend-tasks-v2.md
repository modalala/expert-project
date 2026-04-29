# 前端页面开发任务 V2

## 任务状态总览

| 序号 | 页面 | 路径 | 后端API | 状态 |
|------|------|------|---------|------|
| 1 | 专家初审 | expert/review/index.vue | /api/review | ✅ 已完成 |
| 2 | 评标委员会 | bid/committee/index.vue | /api/bid/committee | ✅ 已完成 |
| 3 | 专家确认 | extraction/confirm/index.vue | /api/extraction | ✅ 已完成 |
| 4 | 采购方案单 | extraction/plan/index.vue | /api/plan | ✅ 已完成 |
| 5 | 角色管理 | system/role/index.vue | /api/role | ✅ 已完成 |
| 6 | 首页统计 | dashboard/index.vue | /api/dashboard | 已实现 |

---

## Task-1: 评标委员会页面

### 后端API对接
- GET /api/bid/committee?planId={id} - 获取委员会详情
- GET /api/bid/committee/members?committeeId={id} - 获取成员列表
- POST /api/bid/committee/evaluation - 提交评分
- PUT /api/bid/committee/{id}/status - 更新状态

### 功能需求
1. 委员会列表展示（关联采购方案）
2. 成员列表展示（组长、成员、监督员）
3. 专家评分功能（总分、一票否决）
4. 委员会状态管理（组建中→确认→评标中→完成）

### TODO清单
- [ ] 创建页面基础结构
- [ ] 实现委员会列表表格
- [ ] 实现成员管理弹窗
- [ ] 实现评分表单
- [ ] 对接后端API
- [ ] 测试验证

---

## Task-2: 专家确认页面

### 后端API对接
- GET /api/extraction/confirm/list?planId={id} - 获取确认列表
- POST /api/extraction/confirm/{id}/accept - 确认参加
- POST /api/extraction/confirm/{id}/reject - 拒绝参加
- POST /api/extraction/confirm/{id}/timeout - 超时处理

### 功能需求
1. 确认列表展示（专家信息、状态、截止时间）
2. 确认操作（通过/拒绝）
3. 状态筛选（待确认/已确认/已拒绝/超时）
4. 通知发送记录

### TODO清单
- [ ] 创建页面基础结构
- [ ] 实现确认列表表格
- [ ] 实现确认/拒绝操作
- [ ] 实现状态筛选
- [ ] 对接后端API
- [ ] 测试验证

---

## Task-3: 采购方案单页面

### 后端API对接
- GET /api/plan/list - 方案单列表
- GET /api/plan/{id} - 方案单详情
- POST /api/plan - 创建方案单
- PUT /api/plan/{id} - 更新方案单
- DELETE /api/plan/{id} - 删除方案单
- PUT /api/plan/{id}/status - 更新状态

### 功能需求
1. 方案单列表展示
2. 创建/编辑方案单弹窗
3. 方案单状态流转
4. 关联抽取方案查看

### TODO清单
- [ ] 创建页面基础结构
- [ ] 实现方案单列表表格
- [ ] 实现创建/编辑弹窗
- [ ] 实现状态操作
- [ ] 对接后端API
- [ ] 测试验证

---

## Task-4: 角色管理页面

### 后端API对接
- GET /api/role/list - 角色列表
- GET /api/role/{id} - 角色详情
- POST /api/role - 创建角色
- PUT /api/role/{id} - 更新角色
- DELETE /api/role/{id} - 删除角色
- GET /api/role/{id}/permissions - 获取权限
- PUT /api/role/{id}/permissions - 设置权限

### 功能需求
1. 角色列表展示
2. 创建/编辑角色弹窗
3. 权限配置树形结构
4. 角色状态管理

### TODO清单
- [ ] 创建页面基础结构
- [ ] 实现角色列表表格
- [ ] 实现创建/编辑弹窗
- [ ] 实现权限配置
- [ ] 对接后端API
- [ ] 测试验证

---

## Task-5: 首页统计优化

### 后端API对接（需新增）
- GET /api/dashboard/stats - 获取统计数据
  - expertCount: 专家总数
  - monthBidCount: 本月评标数
  - pendingReviewCount: 待审核专家数
  - activeExtractionCount: 进行中抽取数

### 功能需求
1. 统计数据实时获取（非静态）
2. 最近活动列表
3. 快捷操作入口

### TODO清单
- [ ] 后端新增dashboard统计API
- [ ] 前端对接实时数据
- [ ] 添加最近活动列表
- [ ] 测试验证

---

## 执行顺序

1. Task-3: 采购方案单（是抽取和评标的前置）
2. Task-2: 专家确认（抽取流程关键环节）
3. Task-1: 评标委员会（业务核心）
4. Task-4: 角色管理（系统管理）
5. Task-5: 首页优化（展示优化）

---

## 后端API状态

已有API（需验证对接）：
- ✅ /api/plan/list - 采购方案单列表
- ✅ /api/bid/committee - 评标委员会
- ✅ /api/role/list - 角色列表
- ⚠️ /api/extraction/confirm - 专家确认（需检查）

需新增API：
- /api/dashboard/stats - 统计数据