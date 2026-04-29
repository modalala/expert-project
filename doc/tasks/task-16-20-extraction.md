# Task 16-20: 抽取管理功能模块

**包含任务**:
- Task 16: 抽取方案配置 (#18)
- Task 17: 专家抽取执行 (#22)
- Task 18: 专家确认流程 (#19)
- Task 19: 抽取方案配置页面 (#14)
- Task 20: 专家确认页面 (#17)

---

## Task 16: 抽取方案配置

### 开发TODO清单

#### 后端开发
- [ ] 创建ExtractionScheme实体类
- [ ] 创建ExtractionRule实体类
- [ ] 创建ExtractionSchemeMapper
- [ ] 创建ExtractionSchemeService
- [ ] 实现saveScheme（保存抽取方案）
- [ ] 实现getSchemeByPlanId
- [ ] 实现saveExclusionRules

#### Controller层
- [ ] POST /api/extraction/scheme - 保存方案
- [ ] GET /api/extraction/scheme/{planId} - 获取方案

---

## Task 17: 专家抽取执行

### 开发TODO清单

#### 抽取算法实现
- [ ] 创建ExtractionService
- [ ] 实现executeExtraction方法：
  1. 查询符合条件的专家
  2. 应用排除规则
  3. 加权随机选择
  4. 保存抽取记录
- [ ] 实现排除条件处理：
  - 近N月评标次数>=N次排除
  - 指定专家ID排除
  - 管理层排除
- [ ] 实现加权随机算法（评标少的权重高）

#### Controller层
- [ ] POST /api/extraction/execute - 执行抽取
- [ ] GET /api/extraction/result/{planId} - 抽取结果

---

### 测试用例（真实环境）

#### TC-17-01: 准备测试专家数据
**前置条件**: 需要至少10个NORMAL状态专家

**测试步骤**:
```bash
# 导入10个专家数据
curl -X POST http://localhost:8080/api/expert/master/import \
  -F "file=@./temp/test-data/10-experts-import.xlsx"
```

---

#### TC-17-02: 配置抽取方案
**测试步骤**:
```bash
PLAN_ID=$(cat ./temp/test-data/procurement-plan-data.json | jq -r '.planId')
curl -X POST "http://localhost:8080/api/extraction/scheme" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "planId": $PLAN_ID,
    "extractionCount": 5,
    "expertTypes": ["TECH","ECON"],
    "expertLevels": ["SENIOR","INTERMEDIATE"],
    "excludeMonthCount": 3,
    "excludeMaxCount": 2
  }'
```

---

#### TC-17-03: 执行抽取
**测试步骤**:
```bash
curl -X POST "http://localhost:8080/api/extraction/execute" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"planId": $PLAN_ID}'
```

**预期结果**: 抽取5名专家

**验证数据库**:
```sql
SELECT * FROM expert_extraction WHERE plan_id = 1;
SELECT COUNT(*) FROM expert_extraction WHERE plan_id = 1;
-- 预期: 5条记录
```

---

## Task 18: 专家确认流程

### 开发TODO清单

#### SSO Token生成
- [ ] 创建generateSsoToken方法
- [ ] Token包含expertId、planId
- [ ] Token有效期4小时

#### 确认Service
- [ ] 实现expertConfirm（专家确认）
- [ ] 实现expertReject（专家拒绝）
- [ ] 实现checkTimeout（超时检查定时任务）

#### Controller层
- [ ] GET /api/extraction/confirm/{token} - 获取确认信息（SSO）
- [ ] POST /api/extraction/confirm - 提交确认
- [ ] POST /api/extraction/reject - 提交拒绝

---

### 测试用例

#### TC-18-01: 专家确认测试
**测试步骤**:
```bash
# 获取SSO Token（从抽取结果中）
SSO_TOKEN=$(cat ./temp/test-data/extraction-result.json | jq -r '.data.experts[0].ssoToken')

# 获取确认信息
curl "http://localhost:8080/api/extraction/confirm/$SSO_TOKEN"

# 确认参加
curl -X POST "http://localhost:8080/api/extraction/confirm" \
  -d '{"token": "$SSO_TOKEN", "confirmed": true}'
```

---

## Task 19-20: 抽取页面

### 前端开发

#### 抽取方案配置页面
- [ ] 创建views/extraction/SchemeConfig.vue
- [ ] 方案单基本信息展示
- [ ] 抽取规则配置表单
- [ ] 排除条件配置
- [ ] 执行抽取按钮

#### 专家确认页面（SSO）
- [ ] 创建views/extraction/Confirm.vue
- [ ] 无需登录访问
- [ ] 专家信息展示
- [ ] 项目信息展示
- [ ] 确认/拒绝按钮
- [ ] 拒绝原因选择

---

## 测试数据持久化

保存抽取结果供后续评标测试：
```json
{
  "planId": 1,
  "extractedExperts": [
    {"expertId": 1, "name": "张三", "ssoToken": "...", "status": "CONFIRMED"},
    ...
  ]
}
```

保存到 `./temp/test-data/extraction-complete-data.json`

---

## 验收标准

- [ ] 抽取方案配置成功
- [ ] 抽取执行成功
- [ ] 抽取数量正确
- [ ] 排除条件生效
- [ ] SSO Token生成成功
- [ ] 专家确认功能正常
- [ ] 专家拒绝功能正常
- [ ] 页面正确渲染
- [ ] 测试数据已保存