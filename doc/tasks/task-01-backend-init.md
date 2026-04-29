# Task 1: 后端项目初始化

**Agent Task ID**: #10  
**依赖**: 无  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 1. 项目骨架创建
- [ ] 创建Spring Boot 3.2.x项目（Maven）
- [ ] 配置pom.xml依赖：
  - spring-boot-starter-web
  - spring-boot-starter-security
  - spring-boot-starter-validation
  - mybatis-plus-boot-starter 3.5.x
  - mysql-connector-j
  - jjwt-api, jjwt-impl, jjwt-jackson
  - hutool-all 5.x
  - easyexcel 3.x
  - springdoc-openapi-starter-webmvc-ui 2.x
- [ ] 创建项目目录结构：
  ```
  src/main/java/com/expert/
  ├── common/config/
  ├── common/exception/
  ├── common/result/
  ├── common/utils/
  ├── common/constant/
  ├── common/annotation/
  ├── common/enums/
  └── ExpertApplication.java
  ```

### 2. 公共模块开发
- [ ] 创建ApiResponse统一响应类
- [ ] 创建GlobalExceptionHandler全局异常处理
- [ ] 创建BusinessException业务异常类
- [ ] 创建ResultCode响应码枚举
- [ ] 创建application.yml配置文件

### 3. 数据库连接配置
- [ ] 配置MySQL数据源（HikariCP）
- [ ] 配置MyBatis Plus
- [ ] 创建数据库连接测试类

### 4. 安全框架基础配置
- [ ] 创建SecurityConfig基础配置（暂时放开所有接口）
- [ ] 创建CorsConfig跨域配置

### 5. API文档配置
- [ ] 创建SwaggerConfig（OpenAPI 3.0）
- [ ] 配置API文档访问路径

---

## 测试用例（真实环境）

### TC-01-01: 应用启动测试
**测试步骤**:
1. 执行 `mvn clean package -DskipTests`
2. 执行 `java -jar target/expert-backend-1.0.0.jar`
3. 访问 `http://localhost:8080/actuator/health`

**预期结果**: 
```json
{
  "status": "UP"
}
```

**测试数据保存**: 截图保存到 `./temp/test-results/task-01-app-start.png`

---

### TC-01-02: 数据库连接测试
**测试步骤**:
1. 创建测试接口 `/api/test/db-conn`
2. 接口执行SQL: `SELECT 1 as test`
3. 使用curl访问: `curl http://localhost:8080/api/test/db-conn`

**预期结果**:
```json
{
  "code": 200,
  "message": "success",
  "data": {"test": 1}
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-01-db-conn.json`

---

### TC-01-03: API文档访问测试
**测试步骤**:
1. 启动应用
2. 访问 `http://localhost:8080/swagger-ui/index.html`

**预期结果**: Swagger UI页面正常显示，显示test接口

**测试数据保存**: 截图保存到 `./temp/test-results/task-01-swagger.png`

---

### TC-01-04: 跨域配置测试
**测试步骤**:
1. 从前端开发服务器发起请求
2. 检查响应头包含 `Access-Control-Allow-Origin`

**预期结果**: 跨域请求成功，无CORS错误

**测试数据保存**: Console日志截图保存到 `./temp/test-results/task-01-cors.png`

---

## 验收标准

- [ ] 应用可正常启动
- [ ] 健康检查接口返回UP状态
- [ ] 数据库连接测试接口返回正确结果
- [ ] Swagger UI可正常访问
- [ ] 跨域请求无错误
- [ ] 测试截图和数据已保存