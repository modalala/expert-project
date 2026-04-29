# Task 12: 专家主数据管理

**Agent Task ID**: #20  
**依赖**: Task 10(#8), Task 9(#9)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Service层扩展
- [ ] 创建ExpertMasterService
- [ ] 实现getExpertList（多条件筛选分页查询）
- [ ] 实现getExpertDetail（完整详情含所有子表）
- [ ] 实现updateExpertStatus（状态变更）
- [ ] 实现importExperts（Excel导入）
- [ ] 实现exportExperts（Excel导出）

#### 2. Controller层
- [ ] 创建ExpertMasterController
- [ ] GET /api/expert/master/list - 主数据列表
- [ ] GET /api/expert/master/{id} - 主数据详情
- [ ] PUT /api/expert/master/{id}/status - 状态变更
- [ ] POST /api/expert/master/import - Excel导入
- [ ] GET /api/expert/master/export - Excel导出

#### 3. Excel处理
- [ ] 创建ExpertImportListener（EasyExcel）
- [ ] 创建ExpertExportService
- [ ] 定义导入Excel模板格式
- [ ] 定义导出Excel格式

#### 4. 状态变更
- [ ] 实现状态变更逻辑
- [ ] 记录状态变更日志（expert_status_log）
- [ ] 状态变更原因必填

---

## 测试用例（真实环境）

### TC-12-01: 主数据列表查询
**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl "http://localhost:8080/api/expert/master/list?page=1&size=10&status=NORMAL" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回张三专家信息

---

### TC-12-02: 多条件筛选查询
**测试步骤**:
```bash
curl "http://localhost:8080/api/expert/master/list?expertType=TECH&expertLevel=SENIOR&page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回符合条件的专家

---

### TC-12-03: 主数据详情查询
**测试步骤**:
```bash
curl "http://localhost:8080/api/expert/master/1" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回张三完整信息（含证书、教育、成果）

---

### TC-12-04: 状态变更-暂停
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/expert/master/1/status" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"status":"SUSPENDED","reason":"近期违规行为"}'
```

**预期结果**: 状态变为SUSPENDED

---

### TC-12-05: 状态变更-恢复
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/expert/master/1/status" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"status":"NORMAL","reason":"暂停期满恢复"}'
```

---

### TC-12-06: Excel导出
**测试步骤**:
```bash
curl "http://localhost:8080/api/expert/master/export" \
  -H "Authorization: Bearer $TOKEN" \
  -o ./temp/test-data/expert-export.xlsx
```

**预期结果**: Excel文件下载成功

---

### TC-12-07: Excel导入
**测试步骤**:
1. 准备导入Excel文件（至少5条专家数据）
2. 上传导入

```bash
curl -X POST "http://localhost:8080/api/expert/master/import" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@./temp/test-data/expert-import.xlsx"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {"successCount": 5, "failCount": 0}
}
```

**测试数据保存**: 导入文件保存到 `./temp/test-data/expert-import-export/`

---

## 验收标准

- [ ] 主数据列表查询正确
- [ ] 多条件筛选生效
- [ ] 主数据详情完整
- [ ] 状态变更功能正常
- [ ] Excel导出成功
- [ ] Excel导入成功
- [ ] 测试数据已保存