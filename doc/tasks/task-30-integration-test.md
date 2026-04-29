# Task 30: 系统集成测试

**Agent Task ID**: #32  
**依赖**: Task 22(#27), Task 28(#28), Task 29(#29), Task 30(#30), Task 31(#31)  
**预估**: 1.5天  
**状态**: pending

---

## 开发TODO清单

### 测试环境准备
- [ ] 准备完整测试数据（10+专家、5+方案单）
- [ ] 验证所有功能模块已实现
- [ ] 验证前后端部署成功

---

## 测试用例（真实环境）

### TC-30-01: 完整流程测试 - 专家注册到入库

**测试流程**:
1. 专家公开注册 → 创建expert_info(status=POTENTIAL)
2. 初审通过 → review_status=INIT_PASS
3. 提交OA审批 → oa_flow_status=PENDING
4. OA审批通过 → 自动生成账号、status=NORMAL
5. 专家账号登录验证

**验证点**:
- [ ] 每个步骤状态正确流转
- [ ] 数据正确入库
- [ ] 账号可登录

---

### TC-30-02: 完整流程测试 - 抽取到评标

**测试流程**:
1. 创建采购方案单 → plan_status=DRAFT
2. 配置抽取方案
3. 执行抽取 → 生成expert_extraction记录
4. 发送企微通知（模拟）
5. 专家确认 → confirm_status=CONFIRMED
6. 委员会自动生成 → committee成员确定
7. 开标 → committee信息可见
8. 评分 → 记录扣分项
9. 评标结束 → committee_status=COMPLETED

**验证点**:
- [ ] 每个步骤数据正确
- [ ] 抽取算法正确
- [ ] 委员会生成正确
- [ ] 评分计算正确

---

### TC-30-03: 并发抽取测试

**测试步骤**:
```bash
# 同时发起10次抽取请求
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/extraction/execute \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"planId": $PLAN_ID}' &
done
wait
```

**预期结果**: 
- 无数据冲突
- 无重复抽取
- 无数据库死锁

---

### TC-30-04: 权限控制完整测试

**测试矩阵**:

| 操作 | 系统管理员 | 审核员 | 招标负责人 | 监督人员 | 专家用户 |
|------|-----------|--------|-----------|---------|---------|
| 用户管理 | ✓ | ✗ | ✗ | ✗ | ✗ |
| 专家初审 | ✓ | ✓ | ✗ | ✗ | ✗ |
| 抽取配置 | ✓ | ✗ | ✓ | ✗ | ✗ |
| 评分 | ✓ | ✗ | ✓ | ✓ | ✗ |
| 专家确认 | ✗ | ✗ | ✗ | ✗ | ✓ |

**验证点**:
- [ ] 各角色权限正确
- [ ] 无权限返回403

---

### TC-30-05: 数据一致性测试

**测试步骤**:
1. 执行完整流程
2. 检查专家主数据统计：
   - bid_count正确累加
   - score_avg正确计算
3. 检查专家画像数据：
   - 评标次数正确
   - 评分趋势正确

**SQL验证**:
```sql
SELECT e.id, e.name, e.bid_count, e.score_avg, 
       COUNT(ec.id) as extraction_count,
       AVG(ev.total_score) as avg_score
FROM expert_info e
LEFT JOIN expert_extraction ec ON e.id = ec.expert_id
LEFT JOIN expert_evaluation ev ON e.id = ev.expert_id
GROUP BY e.id;
```

---

### TC-30-06: 边界测试

**测试场景**:
- [ ] 抽取专家数量不足时的处理
- [ ] 全部专家拒绝后的重新抽取
- [ ] 评分一票否决后的状态变更
- [ ] Token过期后的处理
- [ ] 重复提交的处理

---

### TC-30-07: 性能测试

**测试指标**:
- [ ] 专家列表查询 < 500ms
- [ ] 抽取执行 < 2s
- [ ] 评分提交 < 300ms

**测试步骤**:
```bash
# 使用ab工具进行压力测试
ab -n 100 -c 10 http://localhost:8080/api/expert/master/list
```

---

## 测试报告

生成完整测试报告，包含：

```markdown
## 系统集成测试报告

### 测试环境
- 后端版本: v1.0.0
- 前端版本: v1.0.0
- 数据库: MySQL 8.x
- 测试时间: YYYY-MM-DD

### 测试结果汇总
| 测试类型 | 测试数 | 通过 | 失败 |
|----------|--------|------|------|
| 功能测试 | 50 | xx | xx |
| 权限测试 | 20 | xx | xx |
| 并发测试 | 5 | xx | xx |
| 性能测试 | 5 | xx | xx |

### 发现问题列表
1. 问题描述 - 严重程度 - 处理状态

### 建议
1. 优化建议

### 结论
系统整体功能正常，可以上线使用。
```

保存到 `./temp/test-results/integration-test-report.md`

---

## 验收标准

- [ ] 完整流程测试通过
- [ ] 并发测试无冲突
- [ ] 权限控制完整正确
- [ ] 数据一致性正确
- [ ] 边界测试通过
- [ ] 性能达标
- [ ] 测试报告已生成