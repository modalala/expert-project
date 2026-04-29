# Task 5: 用户管理CRUD

**Agent Task ID**: #7  
**依赖**: Task 1(#10), Task 2(#3)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Entity实体类
- [ ] 创建SysUser实体类（对应sys_user表）
- [ ] 创建SysRole实体类
- [ ] 创建SysPermission实体类
- [ ] 创建SysUserRole实体类
- [ ] 使用MyBatis Plus注解（@TableName, @TableId等）

#### 2. Mapper接口
- [ ] 创建UserMapper extends BaseMapper<SysUser>
- [ ] 创建自定义方法：
  - findUserWithRolesById
  - findUserListPage
- [ ] 创建UserMapper.xml

#### 3. Service层
- [ ] 创建UserService
- [ ] 实现getUserList分页查询
- [ ] 实现getUserById
- [ ] 实现createUser（密码BCrypt加密）
- [ ] 实现updateUser
- [ ] 实现deleteUser（逻辑删除）
- [ ] 实现resetPassword
- [ ] 实现toggleUserStatus

#### 4. Controller层
- [ ] 创建UserController
- [ ] GET /api/user/list - 用户列表（分页）
- [ ] GET /api/user/{id} - 用户详情
- [ ] POST /api/user - 创建用户
- [ ] PUT /api/user/{id} - 更新用户
- [ ] DELETE /api/user/{id} - 删除用户
- [ ] PUT /api/user/{id}/password - 重置密码
- [ ] PUT /api/user/{id}/status - 切换状态

#### 5. DTO类
- [ ] UserQueryRequest - 查询参数
- [ ] UserCreateRequest - 创建请求
- [ ] UserUpdateRequest - 更新请求
- [ ] UserResponse - 用户响应
- [ ] ResetPasswordRequest - 重置密码请求

---

## 测试用例（真实环境）

### TC-05-01: 创建测试用户
**前置条件**: 使用管理员Token（从task-04获取）

**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl -X POST http://localhost:8080/api/user \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123",
    "realName": "测试用户",
    "phone": "13800138001",
    "email": "testuser@test.com",
    "status": 1
  }'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "success",
  "data": {"id": 2, "username": "testuser"}
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-05-create-user.json`

---

### TC-05-02: 查询用户列表
**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl "http://localhost:8080/api/user/list?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {"id": 1, "username": "admin", "realName": "系统管理员"},
      {"id": 2, "username": "testuser", "realName": "测试用户"}
    ],
    "total": 2,
    "page": 1,
    "size": 10
  }
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-05-user-list.json`

---

### TC-05-03: 查询用户详情
**测试步骤**:
```bash
curl "http://localhost:8080/api/user/2" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回testuser完整信息

**测试数据保存**: JSON保存到 `./temp/test-data/task-05-user-detail.json`

---

### TC-05-04: 更新用户信息
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/user/2" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"realName": "测试用户-修改", "phone": "13900139001"}'
```

**预期结果**: 返回200成功

**验证**: 重新查询用户详情验证修改生效

---

### TC-05-05: 禁用用户
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/user/2/status" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": 0}'
```

**预期结果**: 用户状态变为0（禁用）

**验证**: 用testuser登录验证失败

**测试步骤验证禁用效果**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test@123"}'
```

**预期结果**: 返回"账号已被禁用"错误

**测试数据保存**: JSON保存到 `./temp/test-data/task-05-user-disabled.json`

---

### TC-05-06: 启用用户
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/user/2/status" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": 1}'
```

**预期结果**: 用户状态变为1，可正常登录

---

### TC-05-07: 重置密码
**测试步骤**:
```bash
curl -X PUT "http://localhost:8080/api/user/2/password" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"newPassword": "NewTest@123"}'
```

**预期结果**: 返回200成功

**验证**: 使用新密码登录验证成功

---

### TC-05-08: 删除用户
**测试步骤**:
```bash
curl -X DELETE "http://localhost:8080/api/user/2" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回200成功，用户is_deleted=1

---

## 测试数据持久化

保留testuser账号供后续测试使用：
- 创建testuser后不删除
- 保存testuser的Token到 `./temp/test-data/testuser-token.json`
- 后续权限测试使用此账号

---

## 验收标准

- [ ] 用户CRUD接口全部实现
- [ ] 分页查询正确返回
- [ ] 密码BCrypt加密存储
- [ ] 用户禁用后无法登录
- [ ] 重置密码功能正常
- [ ] 测试数据已保存