# Task 15: 采购方案单功能

**Agent Task ID**: #21  
**依赖**: 无  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Entity实体类
- [ ] 创建ProcurementPlan实体类

#### 2. Mapper接口
- [ ] 创建ProcurementPlanMapper

#### 3. Service层
- [ ] 创建ProcurementPlanService
- [ ] 实现createPlan
- [ ] 实现updatePlan
- [ ] 实现deletePlan（逻辑删除）
- [ ] 实现getPlanList
- [ ] 实现getPlanDetail
- [ ] 实现updatePlanStatus

#### 4. Controller层
- [ ] 创建ProcurementPlanController
- [ ] POST /api/plan - 创建方案单
- [ ] PUT /api/plan/{id} - 更新方案单
- [ ] DELETE /api/plan/{id} - 删除方案单
- [ ] GET /api/plan/list - 方案单列表
- [ ] GET /api/plan/{id} - 方案单详情
- [ ] PUT /api/plan/{id}/status - 状态流转

#### 5. DTO类
- [ ] PlanCreateRequest
- [ ] PlanUpdateRequest
- [ ] PlanResponse

---

## 测试用例（真实环境）

### TC-15-01: 创建采购方案单
**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl -X POST http://localhost:8080/api/plan \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "planName": "测试采购项目001",
    "projectName": "信息化建设项目",
    "bidTime": "2024-05-15 09:00:00",
    "bidLocation": "会议室A",
    "extractionMode": "ONLINE",
    "committeeSize": 5
  }'
```

**预期结果**:
```json
{
  "code": 200,
  "data": {"id": 1, "planNo": "PLAN-2024-001"}
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-15-plan-create.json`

---

### TC-15-02: 方案单列表查询
**测试步骤**:
```bash
curl "http://localhost:8080/api/plan/list?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回方案单列表

---

### TC-15-03: 方案单状态流转
**测试步骤**:
```bash
# 状态从DRAFT流转到PENDING
curl -X PUT "http://localhost:8080/api/plan/1/status" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"status":"PENDING"}'
```

**预期结果**: 状态更新成功

---

## 测试数据持久化

保存方案单供后续抽取测试：
```json
{
  "planId": 1,
  "planNo": "PLAN-2024-001",
  "planName": "测试采购项目001",
  "extractionMode": "ONLINE",
  "committeeSize": 5
}
```

保存到 `./temp/test-data/procurement-plan-data.json`

---

## 验收标准

- [ ] 方案单CRUD接口实现
- [ ] 方案单编号自动生成
- [ ] 状态流转功能正常
- [ ] 测试数据已保存