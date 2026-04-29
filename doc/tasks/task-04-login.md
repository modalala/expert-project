# Task 4: 用户登录功能

**Agent Task ID**: #12  
**依赖**: Task 1(#10), Task 3(#11)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. JWT工具类开发
- [ ] 创建JwtUtils类（基于jjwt）
- [ ] 实现generateToken方法（用户ID、角色、有效期）
- [ ] 实现validateToken方法
- [ ] 实现getUserIdFromToken方法
- [ ] 配置JWT密钥和有效期（application.yml）

#### 2. 登录接口开发
- [ ] 创建AuthController
- [ ] 创建LoginRequest DTO
- [ ] 创建LoginResponse DTO
- [ ] 实现login接口：
  - 验证用户名密码（BCrypt校验）
  - 生成JWT token
  - 返回用户信息+token
- [ ] 实现logout接口

#### 3. Spring Security配置
- [ ] 创建JwtAuthenticationFilter
- [ ] 创建JwtAuthenticationEntryPoint
- [ ] 配置SecurityFilterChain：
  - 放行登录接口
  - 其他接口需要JWT认证
- [ ] 配置PasswordEncoder（BCrypt）

#### 4. 用户查询接口
- [ ] 创建UserService.getUserByUsername
- [ ] 创建UserDetailsServiceImpl（Spring Security集成）
- [ ] 创建UserMapper.findByUsername

---

### 前端开发

#### 1. 登录页面开发
- [ ] 创建Login.vue页面
- [ ] 实现登录表单（用户名、密码）
- [ ] 实现表单验证
- [ ] 实现登录按钮Loading状态

#### 2. 登录逻辑实现
- [ ] 创建auth API模块（api/auth.ts）
- [ ] 实现login方法调用后端接口
- [ ] 创建useUserStore（Pinia）
- [ ] 实现Token存储（localStorage）
- [ ] 实现用户信息存储

#### 3. 路由守卫配置
- [ ] 创建authGuard：
  - 检查Token是否存在
  - Token失效跳转登录页
  - 保存目标路由redirect
- [ ] 配置全局路由守卫

#### 4. Axios拦截器完善
- [ ] 请求拦截器：自动添加Authorization header
- [ ] 响应拦截器：
  - 401自动跳转登录页
  - 清除Token
  - 提示登录过期

---

## 测试用例（真实环境）

### TC-04-01: 管理员登录测试
**测试步骤**:
1. 启动后端服务
2. 启动前端服务
3. 使用curl测试登录接口：
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin@123"}'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "roles": ["系统管理员"]
    }
  }
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-04-admin-login.json`

---

### TC-04-02: 错误密码测试
**测试步骤**:
1. 使用错误密码登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrongpassword"}'
```

**预期结果**:
```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-04-login-fail.json`

---

### TC-04-03: Token验证测试
**测试步骤**:
1. 使用登录获取的Token访问需要认证的接口：
```bash
TOKEN=$(cat ./temp/test-data/task-04-admin-login.json | jq -r '.data.token')
curl http://localhost:8080/api/user/list \
  -H "Authorization: Bearer $TOKEN"
```

**预期结果**: 返回用户列表数据

**测试数据保存**: JSON保存到 `./temp/test-data/task-04-token-valid.json`

---

### TC-04-04: Token失效测试
**测试步骤**:
1. 使用无效Token访问接口
```bash
curl http://localhost:8080/api/user/list \
  -H "Authorization: Bearer invalid_token_string"
```

**预期结果**: 返回401错误

**测试数据保存**: JSON保存到 `./temp/test-data/task-04-token-invalid.json`

---

### TC-04-05: 前端登录流程测试
**测试步骤**:
1. 浏览器访问登录页面
2. 输入admin/Admin@123
3. 点击登录按钮
4. 检查跳转到首页
5. 检查localStorage存储Token
6. F12打开开发者工具检查请求

**预期结果**:
- 登录成功跳转首页
- localStorage存在token键
- 请求头包含Authorization

**测试数据保存**: 截图保存到 `./temp/test-results/task-04-frontend-login.png`

---

### TC-04-06: 前端Token过期测试
**测试步骤**:
1. 登录成功后，手动清除localStorage的token
2. 刷新页面或访问需要认证的页面
3. 检查是否跳转登录页

**预期结果**: 自动跳转登录页，显示提示信息

**测试数据保存**: 截图保存到 `./temp/test-results/task-04-token-expire.png`

---

## 测试数据持久化

保存以下数据供后续任务使用：
```json
{
  "adminToken": "登录获取的管理员Token",
  "adminUserInfo": "管理员用户信息",
  "loginTime": "登录时间"
}
```

保存到 `./temp/test-data/auth-token.json`，后续API测试都使用此Token。

---

## 验收标准

- [ ] 后端登录接口返回Token
- [ ] 错误密码返回正确错误信息
- [ ] JWT Token验证通过
- [ ] 无效Token返回401
- [ ] 前端登录成功跳转首页
- [ ] Token存储到localStorage
- [ ] Token失效自动跳转登录页
- [ ] 测试数据已保存（供后续使用）