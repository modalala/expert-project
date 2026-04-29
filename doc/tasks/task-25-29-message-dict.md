# Task 25-29: 消息通知与完善功能模块

**包含任务**:
- Task 25: 消息模板管理 (#26)
- Task 26: 企业微信通知集成 (#24)
- Task 27: 短信邮件通知 (#29)
- Task 28: 专家补抽功能 (#28)
- Task 29: 数据字典管理 (#25)

---

## Task 25: 消息模板管理

### 开发TODO清单

#### Entity实体类
- [ ] 创建MessageTemplate实体类
- [ ] 创建MessageLog实体类

#### Service层
- [ ] 创建MessageTemplateService
- [ ] 实现模板CRUD
- [ ] 实现模板初始化数据

#### Controller层
- [ ] GET /api/message/template/list
- [ ] POST /api/message/template
- [ ] PUT /api/message/template/{id}
- [ ] DELETE /api/message/template/{id}

#### 初始化模板数据
```sql
INSERT INTO message_template (template_code, template_name, template_type, template_content, variables) VALUES
('EXTRACT_NOTIFY', '抽取通知', 'WECHAT', '尊敬的{expertName}专家，您被抽选参与{projectName}评标...', 'expertName,projectName,bidTime,confirmUrl'),
('REVIEW_PASS', '审核通过通知', 'EMAIL', '尊敬的{expertName}，您的专家注册申请已通过审核...', 'expertName,account,password'),
('CONFIRM_SUCCESS', '确认成功通知', 'SMS', '您已确认参与{projectName}评标...', 'projectName,bidTime');
```

---

### 测试用例

#### TC-25-01: 模板列表查询
**测试步骤**:
```bash
curl "http://localhost:8080/api/message/template/list" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回初始化的模板列表

---

## Task 26: 企业微信通知集成

### 开发TODO清单

#### WeChatService
- [ ] 创建WeChatService
- [ ] 实现getAccessToken
- [ ] 实现sendTextCardMessage
- [ ] 实现generateSsoUrl

#### 配置
- [ ] application.yml配置企微参数
- [ ] 配置corpId、agentId、secret

#### Controller层
- [ ] POST /api/message/wechat/send - 发送企微消息
- [ ] GET /api/message/wechat/test - 测试发送

---

### 测试用例

#### TC-26-01: 企微消息发送测试
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/message/wechat/test \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"userId": "测试企微用户ID", "content": "测试消息"}'
```

**预期结果**: 返回企微接口响应

---

## Task 27: 短信邮件通知

### 开发TODO清单

#### SmsService（预留）
- [ ] 创建SmsService接口
- [ ] 创建MockSmsServiceImpl（模拟实现）

#### EmailService
- [ ] 创建EmailService
- [ ] 配置邮件服务器参数
- [ ] 实现sendEmail方法

#### MessageLog
- [ ] 记录发送日志
- [ ] 记录发送状态

---

### 测试用例

#### TC-27-01: 邮件发送测试
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/message/email/send \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"to": "test@test.com", "subject": "测试邮件", "content": "这是测试邮件"}'
```

**预期结果**: 返回发送结果

---

## Task 28: 专家补抽功能

### 开发TODO清单

#### Service层扩展
- [ ] 实现reExtract（重新抽取）
- [ ] 实现手动SelectExpert（手动选择专家）
- [ ] 更新委员会成员

#### Controller层
- [ ] POST /api/extraction/re-extract - 重新抽取
- [ ] POST /api/extraction/manual-select - 手动选择

---

### 测试用例

#### TC-28-01: 手动补抽测试
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/extraction/re-extract \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"planId": 1, "replaceExpertId": 拒绝的专家ID}'
```

**预期结果**: 新专家抽取成功

---

## Task 29: 数据字典管理

### 开发TODO清单

#### Service层
- [ ] 创建DictService
- [ ] 实现字典CRUD
- [ ] 实现字典项CRUD

#### Controller层
- [ ] GET /api/dict/list
- [ ] GET /api/dict/{dictCode}/items
- [ ] POST /api/dict/item
- [ ] PUT /api/dict/item/{id}
- [ ] DELETE /api/dict/item/{id}

---

### 测试用例

#### TC-29-01: 字典查询测试
**测试步骤**:
```bash
curl "http://localhost:8080/api/dict/EXPERT_TYPE/items" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回专家类型字典项

---

## 验收标准

- [ ] 消息模板初始化完成
- [ ] 模板CRUD功能正常
- [ ] 企微消息发送（模拟）成功
- [ ] 邮件发送成功
- [ ] 补抽功能正常
- [ ] 手动选择功能正常
- [ ] 字典管理功能正常
- [ ] 测试数据已保存