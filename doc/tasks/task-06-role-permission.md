# Task 6: 角色权限管理

**Agent Task ID**: #4  
**依赖**: Task 5(#7), Task 4(#12)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Mapper接口
- [ ] 创建RoleMapper
- [ ] 创建PermissionMapper
- [ ] 创建UserRoleMapper
- [ ] 创建RolePermissionMapper
- [ ] 创建自定义查询方法：
  - findPermissionsByRoleId
  - findRolesByUserId
  - findPermissionTree

#### 2. Service层
- [ ] 创建RoleService
  - getRoleList
  - getRoleById
  - createRole
  - updateRole
  - deleteRole
  - getRolePermissions
  - assignPermissionsToRole
- [ ] 创建PermissionService
  - getPermissionTree
  - getPermissionList
  - createPermission
  - updatePermission
  - deletePermission
- [ ] 创建UserRoleService
  - getUserRoles
  - assignRolesToUser
  - removeRoleFromUser

#### 3. Controller层
- [ ] RoleController
  - GET /api/role/list
  - GET /api/role/{id}
  - POST /api/role
  - PUT /api/role/{id}
  - DELETE /api/role/{id}
  - GET /api/role/{id}/permissions
  - POST /api/role/{id}/permissions
- [ ] PermissionController
  - GET /api/permission/tree
  - GET /api/permission/list
- [ ] UserController新增
  - GET /api/user/{id}/roles
  - POST /api/user/{id}/roles

#### 4. 权限校验拦截器
- [ ] 创建@RequirePermission注解
- [ ] 创建PermissionInterceptor
- [ ] 实现权限校验逻辑
- [ ] 校验失败返回403

#### 5. 权限数据初始化
- [ ] 初始化菜单权限（专家管理、用户管理等）
- [ ] 初始化按钮权限（新增、编辑、删除等）
- [ ] 初始化接口权限（API接口）

---

## 测试用例（真实环境）

### TC-06-01: 创建测试角色
**前置条件**: 使用管理员Token

**测试步骤**:
```bash
TOKEN=$(cat ./temp/test-data/auth-token.json | jq -r '.adminToken')
curl -X POST http://localhost:8080/api/role \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roleCode": "TEST_ROLE",
    "roleName": "测试角色",
    "description": "用于测试的角色"
  }'
```

**预期结果**: 返回角色ID

**测试数据保存**: JSON保存到 `./temp/test-data/task-06-create-role.json`

---

### TC-06-02: 查询权限树
**测试步骤**:
```bash
curl "http://localhost:8080/api/permission/tree" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回树形权限结构
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "permName": "专家管理",
      "permType": 1,
      "children": [
        {"id": 11, "permName": "专家注册", "permType": 2},
        {"id": 12, "permName": "专家审核", "permType": 2}
      ]
    }
  ]
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-06-permission-tree.json`

---

### TC-06-03: 给角色分配权限
**测试步骤**:
```bash
# 给测试角色分配部分权限（专家查看权限，不给删除权限）
ROLE_ID=$(cat ./temp/test-data/task-06-create-role.json | jq -r '.data.id')
curl -X POST "http://localhost:8080/api/role/$ROLE_ID/permissions" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"permissionIds": [11, 12, 13]}'  # 只给查看、新增、编辑权限
```

**预期结果**: 返回200成功

---

### TC-06-04: 给用户分配角色
**前置条件**: Task 5已创建testuser用户

**测试步骤**:
```bash
curl -X POST "http://localhost:8080/api/user/2/roles" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roleIds": [$ROLE_ID]}'
```

**预期结果**: 返回200成功

---

### TC-06-05: testuser登录验证
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test@123"}'
```

**预期结果**: 返回Token，roles包含"测试角色"

**保存testuser Token**: 保存到 `./temp/test-data/testuser-token.json`

---

### TC-06-06: 权限控制测试（有权限接口）
**测试步骤**:
```bash
TEST_TOKEN=$(cat ./temp/test-data/testuser-token.json | jq -r '.data.token')
# 访问专家列表（有权限）
curl "http://localhost:8080/api/expert/list?page=1&size=10" \
  -H "Authorization: Bearer $TEST_TOKEN"
```

**预期结果**: 返回200成功，数据正常

---

### TC-06-07: 权限控制测试（无权限接口）
**测试步骤**:
```bash
# 访问删除接口（无权限）
curl -X DELETE "http://localhost:8080/api/expert/1" \
  -H "Authorization: Bearer $TEST_TOKEN"
```

**预期结果**: 返回403 Forbidden
```json
{
  "code": 403,
  "message": "无权限访问"
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-06-permission-403.json`

---

### TC-06-08: 管理员权限测试
**测试步骤**:
```bash
# 管理员访问删除接口（有权限）
curl -X DELETE "http://localhost:8080/api/expert/1" \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回200成功（或专家不存在返回业务错误）

---

## 测试数据持久化

保存以下数据供后续任务使用：
```json
{
  "testRoleId": "测试角色ID",
  "testRoleName": "测试角色",
  "testuserToken": "testuser的Token",
  "testuserPermissions": ["expert:view", "expert:create", "expert:edit"]
}
```

保存到 `./temp/test-data/role-permission-config.json`

---

## 验收标准

- [ ] 角色CRUD接口实现
- [ ] 权限树查询正确
- [ ] 角色权限分配成功
- [ ] 用户角色分配成功
- [ ] 权限校验拦截器生效
- [ ] 无权限返回403
- [ ] testuser账号可用（供后续测试）