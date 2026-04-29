# Task 21-24: 评标管理功能模块

**包含任务**:
- Task 21: 评标委员会功能 (#23)
- Task 22: 专家评分功能 (#27)
- Task 23: 评标委员会页面 (#30)
- Task 24: 专家评分页面 (#31)

---

## Task 21: 评标委员会功能

### 开发TODO清单

#### Entity实体类
- [ ] 创建BidCommittee实体类
- [ ] 创建BidCommitteeMember实体类
- [ ] 创建ExpertEvaluation实体类
- [ ] 创建EvaluationItem实体类

#### Service层
- [ ] 创建CommitteeService
- [ ] 实现createCommittee（抽取确认后自动生成）
- [ ] 实现getCommitteeList
- [ ] 实现getCommitteeDetail
- [ ] 实现updateCommitteeStatus
- [ ] 实现setVisible（开标前后信息控制）

#### Controller层
- [ ] POST /api/bid/committee - 创建委员会
- [ ] GET /api/bid/committee/list - 委员会列表
- [ ] GET /api/bid/committee/{id} - 委员会详情
- [ ] PUT /api/bid/committee/{id}/visible - 设置可见

---

### 测试用例（真实环境）

#### TC-21-01: 委员会自动生成
**前置条件**: 抽取确认完成

**测试步骤**:
```bash
curl "http://localhost:8080/api/bid/committee/list?planId=1" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 委员会已自动生成

---

#### TC-21-02: 委员会详情查询
**测试步骤**:
```bash
curl "http://localhost:8080/api/bid/committee/1" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回委员会完整信息（含成员列表）

---

## Task 22: 专家评分功能

### 开发TODO清单

#### Service层
- [ ] 创建EvaluationService
- [ ] 实现submitEvaluation（提交评分）
- [ ] 实现getEvaluationHistory（评分历史）
- [ ] 实现计算总分
- [ ] 实现一票否决处理

#### 评分项处理
- [ ] 扣分项记录
- [ ] 总分计算（100分 - 扣分）
- [ ] 一票否决标记
- [ ] 同步更新专家主数据score_avg

#### Controller层
- [ ] POST /api/bid/evaluation - 提交评分
- [ ] GET /api/bid/evaluation/history/{expertId} - 评分历史

---

### 测试用例

#### TC-22-01: 提交评分
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/bid/evaluation \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "committeeMemberId": 1,
    "expertId": 1,
    "evaluationItems": [
      {"itemCode": "LATE", "deductScore": 5, "reason": "迟到15分钟"},
      {"itemCode": "OTHER", "deductScore": 0, "reason": ""}
    ],
    "isVeto": false,
    "comment": "表现良好"
  }'
```

**预期结果**: 评分提交成功，总分=95

---

#### TC-22-02: 一票否决
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/bid/evaluation \
  -d '{
    "isVeto": true,
    "vetoReason": "严重违规行为",
    ...
  }'
```

**预期结果**: 专家状态变为SUSPENDED

---

## Task 23-24: 评标页面

### 前端开发

#### 委员会页面
- [ ] 创建views/bid/CommitteeList.vue
- [ ] 创建views/bid/CommitteeDetail.vue
- [ ] 成员信息展示
- [ ] 开标前后信息控制

#### 评分页面
- [ ] 创建views/bid/Evaluation.vue
- [ ] 评分表单
- [ ] 扣分项选择
- [ ] 一票否决开关
- [ ] 评分历史查看

---

## 测试数据持久化

保存评分数据：
```json
{
  "committeeId": 1,
  "members": [
    {"expertId": 1, "name": "张三", "score": 95}
  ]
}
```

保存到 `./temp/test-data/evaluation-complete-data.json`

---

## 验收标准

- [ ] 委员会自动生成
- [ ] 委员会详情正确
- [ ] 评分提交成功
- [ ] 扣分计算正确
- [ ] 一票否决处理正确
- [ ] 页面正确渲染
- [ ] 测试数据已保存