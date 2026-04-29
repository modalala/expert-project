# Task 2: 数据库表结构创建

**Agent Task ID**: #3  
**依赖**: Task 1 (#10)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 1. 创建数据库
- [ ] 创建数据库 `expert_db`
- [ ] 设置字符集 `utf8mb4`
- [ ] 创建应用数据库用户

### 2. 用户权限表DDL
- [ ] 创建 sys_user 表
- [ ] 创建 sys_role 表
- [ ] 创建 sys_permission 表
- [ ] 创建 sys_role_permission 表
- [ ] 创建 sys_user_role 表

### 3. 专家相关表DDL
- [ ] 创建 expert_info 表
- [ ] 创建 expert_certificate 表
- [ ] 创建 expert_education 表
- [ ] 创建 expert_achievement 表
- [ ] 创建 expert_attachment 表
- [ ] 创建 expert_status_log 表

### 4. 审核相关表DDL
- [ ] 创建 expert_review 表
- [ ] 创建 expert_review_log 表

### 5. 抽取相关表DDL
- [ ] 创建 procurement_plan 表
- [ ] 创建 extraction_scheme 表
- [ ] 创建 extraction_rule 表
- [ ] 创建 expert_extraction 表
- [ ] 创建 expert_confirmation 表

### 6. 评标相关表DDL
- [ ] 创建 bid_committee 表
- [ ] 创建 bid_committee_member 表
- [ ] 创建 expert_evaluation 表
- [ ] 创建 evaluation_item 表

### 7. 消息相关表DDL
- [ ] 创建 message_template 表
- [ ] 创建 message_log 表

### 8. 数据字典表DDL
- [ ] 创建 sys_dict 表
- [ ] 创建 sys_dict_item 表

### 9. 初始化数据
- [ ] 初始化管理员账号（admin/Admin@123）
- [ ] 初始化系统管理员角色
- [ ] 初始化管理员用户角色关联
- [ ] 初始化默认权限数据
- [ ] 初始化数据字典数据

---

## 测试用例（真实环境）

### TC-02-01: 表创建验证
**测试步骤**:
1. 连接MySQL数据库
2. 执行 `SHOW TABLES`
3. 统计表数量

**预期结果**: 返回30+张表

**SQL命令**:
```sql
SHOW TABLES;
SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'expert_db';
```

**测试数据保存**: SQL执行结果保存到 `./temp/test-data/task-02-tables.json`

---

### TC-02-02: 管理员账号验证
**测试步骤**:
1. 查询sys_user表
2. 验证admin账号存在且密码已加密

**预期结果**:
```json
{
  "id": 1,
  "username": "admin",
  "real_name": "系统管理员",
  "status": 1
}
```

**SQL命令**:
```sql
SELECT id, username, real_name, password, status, create_time FROM sys_user WHERE username = 'admin';
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-02-admin-user.json`

---

### TC-02-03: 角色权限数据验证
**测试步骤**:
1. 查询sys_role表验证系统管理员角色
2. 查询sys_permission表验证权限数量
3. 查询sys_role_permission验证角色权限关联

**预期结果**: 存在系统管理员角色，关联全部权限

**SQL命令**:
```sql
SELECT * FROM sys_role;
SELECT COUNT(*) FROM sys_permission;
SELECT COUNT(*) FROM sys_role_permission WHERE role_id = 1;
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-02-role-perm.json`

---

### TC-02-04: 数据字典验证
**测试步骤**:
1. 查询sys_dict表验证字典类型
2. 查询sys_dict_item验证字典项

**预期结果**: 存在专家类型、专家级别、专家状态等字典

**SQL命令**:
```sql
SELECT * FROM sys_dict;
SELECT dict_code, item_code, item_name FROM sys_dict_item WHERE dict_code = 'EXPERT_TYPE';
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-02-dict-data.json`

---

### TC-02-05: 表索引验证
**测试步骤**:
1. 查询expert_info表的索引
2. 验证关键字段有索引

**预期结果**: name, phone, status, expert_type+expert_level有索引

**SQL命令**:
```sql
SHOW INDEX FROM expert_info;
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-02-indexes.json`

---

## 验收标准

- [ ] 数据库创建成功
- [ ] 30+张表全部创建
- [ ] 管理员账号已初始化（admin/Admin@123）
- [ ] 系统管理员角色已初始化
- [ ] 权限数据已初始化
- [ ] 数据字典已初始化
- [ ] 关键索引已创建
- [ ] 测试数据已保存