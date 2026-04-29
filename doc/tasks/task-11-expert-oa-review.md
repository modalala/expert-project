# Task 11: 专家复审/OA审批

**Agent Task ID**: #16  
**依赖**: Task 10(#8)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. OA审批集成
- [ ] 创建OAService（模拟OA审批）
- [ ] 实现submitToOA方法（提交审批申请）
- [ ] 实现模拟OA审批通过回调
- [ ] 实现模拟OA审批拒绝回调

#### 2. 复审Service扩展
- [ ] 实现getReReviewList（待复审列表）
- [ ] 实现submitOAApproval（提交OA审批）
- [ ] 实现oaApprovalCallback（OA审批回调处理）：
  - 通过：生成专家账号、专家编号，status=NORMAL，发送通知
  - 拒绝：review_status=RE_REJECT，发送通知

#### 3. 专家账号自动生成
- [ ] 创建generateExpertAccount方法：
  - 账号=手机号
  - 密码随机生成
  - 创建sys_user记录
  - 绑定expert_info.user_id
- [ ] 创建generateExpertNo方法（专家编号生成）

#### 4. Controller层
- [ ] GET /api/review/re-list - 复审列表
- [ ] POST /api/review/{expertId}/oa - 提交OA审批
- [ ] POST /api/review/oa/callback - OA审批回调（公开接口）

#### 5. 通知发送
- [ ] 创建通知（通过）：专家账号密码通知
- [ ] 创建通知（拒绝）：审核拒绝通知

---

## 测试用例（真实环境）

### TC-11-01: 查询复审列表
**前置条件**: Task 10已初审通过张三

**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl "http://localhost:8080/api/review/re-list?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {"expertId": 1, "name": "张三", "reviewStatus": "INIT_PASS"}
    ]
  }
}
```

---

### TC-11-02: 提交OA审批-张三
**测试步骤**:
```bash
curl -X POST "http://localhost:8080/api/review/1/oa" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {"oaFlowId": "OA-2024-001"}
}
```

**验证数据库**:
```sql
SELECT oa_flow_id, oa_flow_status FROM expert_review WHERE expert_id = 1;
```

---

### TC-11-03: OA审批通过回调
**测试步骤**:
```bash
# 模拟OA审批通过回调
curl -X POST "http://localhost:8080/api/review/oa/callback" \
  -H "Content-Type: application/json" \
  -d '{
    "oaFlowId": "OA-2024-001",
    "status": "PASS",
    "comment": "审批通过"
  }'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "OA审批处理完成"
}
```

**验证数据库**:
```sql
-- 专家状态变为NORMAL
SELECT status, review_status, user_id, expert_no FROM expert_info WHERE id = 1;
-- 预期: status=NORMAL, review_status=RE_PASS, user_id已绑定, expert_no已生成

-- 用户账号已创建
SELECT * FROM sys_user WHERE phone = '13900139001';
-- 预期: username=13900139001（手机号），real_name=张三

-- 审核记录
SELECT * FROM expert_review WHERE expert_id = 1;
SELECT * FROM expert_review_log WHERE review_id = last_id;
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-11-oa-pass.json`

---

### TC-11-04: 专家账号登录验证
**测试步骤**:
```bash
# 查询生成的账号密码
cat ./temp/test-data/task-11-oa-pass.json | jq '.data.generatedAccount'

# 使用生成的账号登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"13900139001","password":"生成的密码"}'
```

**预期结果**: 登录成功，返回Token

---

### TC-11-05: 注册新专家测试OA拒绝
**前置条件**: 注册新专家王五并初审通过

**测试步骤**:
```bash
# 注册王五
curl -X POST http://localhost:8080/api/expert/register \
  -d '{"name":"王五","phone":"13900139003","idCard":"320102199003031234",...}'

# 初审通过
curl -X POST "http://localhost:8080/api/review/王五ID/pass"

# 提交OA
curl -X POST "http://localhost:8080/api/review/王五ID/oa"

# OA拒绝回调
curl -X POST "http://localhost:8080/api/review/oa/callback" \
  -d '{"oaFlowId":"OA-2024-002","status":"REJECT","comment":"资质不符合"}'
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "expertId": 王五ID,
    "reviewStatus": "RE_REJECT"
  }
}
```

**验证数据库**:
```sql
SELECT status, review_status FROM expert_info WHERE name = '王五';
-- 预期: status=POTENTIAL, review_status=RE_REJECT
```

---

### TC-11-06: 专家编号生成验证
**测试步骤**:
```sql
SELECT expert_no FROM expert_info WHERE id = 1;
```

**预期结果**: 专家编号格式正确（如：EXP-2024-0001）

---

## 测试数据持久化

保存张三的完整信息供后续抽取测试使用：
```json
{
  "zhangsan": {
    "expertId": 1,
    "expertNo": "EXP-2024-0001",
    "name": "张三",
    "phone": "13900139001",
    "status": "NORMAL",
    "userId": 生成的用户ID,
    "accountUsername": "13900139001",
    "accountPassword": "生成的密码"
  }
}
```

保存到 `./temp/test-data/experts-normal.json`

---

## 验收标准

- [ ] 复审列表查询正确
- [ ] OA审批提交成功
- [ ] OA通过回调处理正确
- [ ] 专家账号自动生成
- [ ] 专家编号自动生成
- [ ] 专家状态正确更新（NORMAL）
- [ ] OA拒绝回调处理正确
- [ ] 通知发送正确（模拟）
- [ ] 张三账号可登录
- [ ] 测试数据已保存