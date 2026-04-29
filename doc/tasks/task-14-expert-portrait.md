# Task 14: 专家画像功能

**Agent Task ID**: #15  
**依赖**: Task 8(#5), Task 11(#16)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Service层
- [ ] 创建ExpertPortraitService
- [ ] 实现getPortraitData（画像统计数据）
  - 参与评标次数
  - 确认/拒绝次数
  - 平均评分
  - 评分趋势（近12个月）
  - 评标类型分布
- [ ] 实现getBidHistory（评标历史列表）
- [ ] 实现getExtractionHistory（抽取历史列表）

#### 2. Controller层
- [ ] GET /api/expert/{id}/portrait - 画像数据
- [ ] GET /api/expert/{id}/bid-history - 评标历史
- [ ] GET /api/expert/{id}/extraction-history - 抽取历史

#### 3. 统计SQL
- [ ] 编写评标次数统计SQL
- [ ] 编写评分趋势统计SQL
- [ ] 编写评标类型分布SQL

---

### 前端开发

#### 1. 画像页面
- [ ] 创建views/expert/Portrait.vue
- [ ] 左侧：基本信息卡片
- [ ] 右侧上：饼图（评标类型分布）
- [ ] 右侧下：折线图（评分趋势）
- [ ] 下方：历史记录表格（评标、抽取）

#### 2. ECharts集成
- [ ] 使用vue-echarts组件
- [ ] 配置饼图
- [ ] 配置折线图

---

## 测试用例（真实环境）

### TC-14-01: 画像数据接口测试
**前置条件**: 张三已有评标记录（模拟数据）

**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl "http://localhost:8080/api/expert/1/portrait" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "bidCount": 10,
    "confirmCount": 8,
    "rejectCount": 2,
    "avgScore": 95.5,
    "scoreTrend": [
      {"month": "2024-01", "score": 95},
      {"month": "2024-02", "score": 96}
    ],
    "bidTypeDistribution": [
      {"type": "技术类", "count": 6},
      {"type": "经济类", "count": 4}
    ]
  }
}
```

---

### TC-14-02: 画像页面渲染
**测试步骤**:
1. 访问 `http://localhost:5173/expert/portrait/1`

**预期结果**: 画像页面正确渲染，图表显示

**截图保存**: `./temp/test-results/task-14-portrait.png`

---

### TC-14-03: 图表数据正确性
**测试步骤**:
1. 检查饼图数据与接口返回一致
2. 检查折线图数据正确

**预期结果**: 图表数据正确

---

## 验收标准

- [ ] 画像数据接口正确
- [ ] 画像页面正确渲染
- [ ] 饼图正确显示
- [ ] 折线图正确显示
- [ ] 历史记录表格正确
- [ ] 测试截图已保存