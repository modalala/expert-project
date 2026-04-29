# Task 10: 专家初审功能

**Agent Task ID**: #8  
**依赖**: Task 8(#5), Task 3(#11)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Mapper接口
- [ ] 创建ExpertReviewMapper
- [ ] 创建ExpertReviewLogMapper
- [ ] 创建自定义方法：
  - findReviewListPage
  - findReviewDetailById

#### 2. Service层
- [ ] 创建ExpertReviewService
- [ ] 实现getReviewList（分页查询待初审专家）
- [ ] 实现getReviewDetail（查询专家详情+审核记录）
- [ ] 实现reviewPass（初审通过）：
  - 更新expert_info.review_status=INIT_PASS
  - 创建expert_review记录
  - 创建expert_review_log记录
- [ ] 实现reviewReject（初审拒绝）：
  - 更新expert_info.review_status=INIT_REJECT
  - 创建expert_review记录
  - 记录拒绝原因
  - 发送拒绝通知

#### 3. Controller层
- [ ] 创建ExpertReviewController
- [ ] GET /api/review/list - 初审列表（需权限review:init:view）
- [ ] GET /api/review/{expertId} - 初审详情
- [ ] POST /api/review/{expertId}/pass - 初审通过（需权限review:init:pass）
- [ ] POST /api/review/{expertId}/reject - 初审拒绝（需权限review:init:reject）

#### 4. DTO类
- [ ] ReviewQueryRequest
- [ ] ReviewListResponse
- [ ] ReviewDetailResponse
- [ ] ReviewPassRequest
- [ ] ReviewRejectRequest（含拒绝原因）

---

## 测试用例（真实环境）

### TC-10-01: 查询初审列表
**前置条件**: Task 8已注册张三、李四

**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl "http://localhost:8080/api/review/list?page=1&size=10&reviewType=INIT" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {"expertId": 1, "name": "张三", "reviewStatus": "PENDING", "source": "PUBLIC"},
      {"expertId": 2, "name": "李四", "reviewStatus": "PENDING", "source": "PUBLIC"}
    ],
    "total": 2
  }
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-10-review-list.json`

---

### TC-10-02: 查询初审详情
**测试步骤**:
```bash
curl "http://localhost:8080/api/review/1" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回张三完整信息+审核历史

---

### TC-10-03: 初审通过-张三
**测试步骤**:
```bash
curl -X POST "http://localhost:8080/api/review/1/pass" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"comment": "资质符合要求，初审通过"}'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "初审通过"
}
```

**验证数据库**:
```sql
SELECT review_status FROM expert_info WHERE id = 1;
-- 预期: INIT_PASS

SELECT * FROM expert_review WHERE expert_id = 1 AND review_type = 'INIT';
SELECT * FROM expert_review_log WHERE review_id = last_review_id;
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-10-review-pass.json`

---

### TC-10-04: 初审拒绝-李四
**测试步骤**:
```bash
curl -X POST "http://localhost:8080/api/review/2/reject" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rejectReason": "资质不符合要求", "comment": "缺少必要证书"}'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "初审拒绝"
}
```

**验证数据库**:
```sql
SELECT review_status FROM expert_info WHERE id = 2;
-- 预期: INIT_REJECT
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-10-review-reject.json`

---

### TC-10-05: 再次查询初审列表
**测试步骤**:
```bash
curl "http://localhost:8080/api/review/list?page=1&size=10&reviewType=INIT" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 列表为空（张三已通过，李四已拒绝）

---

### TC-10-06: 权限控制测试
**前置条件**: 使用testuser（只有查看权限，无审核权限）

**测试步骤**:
```bash
TEST_TOKEN=$(cat ./temp/test-data/testuser-token.json | jq -r '.data.token')
curl -X POST "http://localhost:8080/api/review/1/pass" \
  -H "Authorization: Bearer $TEST_TOKEN"
```

**预期结果**: 返回403 Forbidden

---

## 测试数据持久化

保存审核结果供后续复审使用：
```json
{
  "zhangsan": {
    "expertId": 1,
    "reviewStatus": "INIT_PASS",
    "initReviewResult": "PASS"
  },
  "lisi": {
    "expertId": 2,
    "reviewStatus": "INIT_REJECT",
    "initReviewResult": "REJECT"
  }
}
```

保存到 `./temp/test-data/experts-review-result.json`

---

## 验收标准

- [ ] 初审列表查询正确
- [ ] 初审详情查询正确
- [ ] 初审通过功能正常
- [ ] 初审拒绝功能正常
- [ ] 审核日志正确记录
- [ ] 状态正确更新
- [ ] 权限控制生效
- [ ] 测试数据已保存（供复审使用）