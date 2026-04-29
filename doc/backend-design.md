# 专家库管理系统 - 后端详细设计文档

## 1. 项目架构设计

### 1.1 目录结构

```
expert-backend/
├── src/main/java/com/expert/
│   ├── common/                 # 公共模块
│   │   ├── config/             # 配置类
│   │   │   ├── SecurityConfig.java      # 安全配置
│   │   │   ├── WebConfig.java           # Web配置
│   │   │   ├── CorsConfig.java          # CORS配置
│   │   │   ├── MybatisConfig.java       # MyBatis配置
│   │   │   └── SwaggerConfig.java       # API文档配置
│   │   ├── exception/          # 异常处理
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── BusinessException.java
│   │   │   └── AuthException.java
│   │   ├── result/             # 响应封装
│   │   │   ├── ApiResponse.java         # 统一响应
│   │   │   ├── PageResult.java          # 分页响应
│   │   │   └── ResultCode.java          # 响应码
│   │   ├── utils/              # 工具类
│   │   │   ├── JwtUtils.java            # JWT工具
│   │   │   ├── StringUtils.java
│   │   │   ├── DateUtils.java
│   │   │   ├── ExcelUtils.java          # Excel导入导出
│   │   │   ├── RandomUtils.java         # 随机抽取算法
│   │   │   └── MessageUtils.java        # 消息发送工具
│   │   ├── constant/           # 常量定义
│   │   │   ├── ExpertStatus.java        # 专家状态常量
│   │   │   ├── ExpertType.java          # 专家类型常量
│   │   │   ├── ExpertLevel.java         # 专家级别常量
│   │   │   ├── ConfirmStatus.java       # 确认状态常量
│   │   │   └── RejectReason.java        # 拒绝原因常量
│   │   └── annotation/         # 自定义注解
│   │   │   ├── RequirePermission.java   # 权限注解
│   │   │   └── LogOperation.java        # 操作日志注解
│   │   └── interceptor/        # 拦截器
│   │   │   ├── AuthInterceptor.java     # 认证拦截器
│   │   │   └── PermissionInterceptor.java
│   │   └── enums/              # 枚举定义
│   │       ├── ExpertSourceEnum.java    # 专家来源枚举
│   │       ├── ExtractionModeEnum.java  # 抽取方式枚举
│   │       └── ReviewStatusEnum.java    # 审核状态枚举
│   │
│   ├── modules/                # 业务模块
│   │   ├── auth/               # 认证模块
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   └── impl/AuthServiceImpl.java
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── SsoRequest.java
│   │   │   │   └── SsoResponse.java
│   │   │   └── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   └── AuthenticationEntryPointImpl.java
│   │   │
│   │   ├── user/               # 用户管理模块
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java
│   │   │   │   ├── RoleController.java
│   │   │   │   └── PermissionController.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   ├── PermissionService.java
│   │   │   │   └── impl/
│   │   │   ├── entity/
│   │   │   │   ├── SysUser.java
│   │   │   │   ├── SysRole.java
│   │   │   │   ├── SysPermission.java
│   │   │   │   └── SysUserRole.java
│   │   │   ├── mapper/
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── RoleMapper.java
│   │   │   │   ├── PermissionMapper.java
│   │   │   │   └── UserRoleMapper.java
│   │   │   └── dto/
│   │   │   │   ├── UserCreateRequest.java
│   │   │   │   ├── UserUpdateRequest.java
│   │   │   │   ├── UserQueryRequest.java
│   │   │   │   ├── UserResponse.java
│   │   │   │   ├── RoleCreateRequest.java
│   │   │   │   ├── AssignRoleRequest.java
│   │   │   │   └── PermissionResponse.java
│   │   │
│   │   ├── expert/             # 专家管理模块
│   │   │   ├── controller/
│   │   │   │   ├── ExpertController.java         # 专家CRUD
│   │   │   │   ├── ExpertRegisterController.java # 专家注册
│   │   │   │   ├── ExpertReviewController.java   # 专家初审
│   │   │   │   ├── ExpertReReviewController.java # 专家复审
│   │   │   │   └── ExpertMasterController.java   # 专家主数据
│   │   │   ├── service/
│   │   │   │   ├── ExpertService.java
│   │   │   │   ├── ExpertRegisterService.java
│   │   │   │   ├── ExpertReviewService.java
│   │   │   │   ├── ExpertMasterService.java
│   │   │   │   └── impl/
│   │   │   ├── entity/
│   │   │   │   ├── ExpertInfo.java
│   │   │   │   ├── ExpertCertificate.java
│   │   │   │   ├── ExpertEducation.java
│   │   │   │   ├── ExpertAchievement.java
│   │   │   │   ├── ExpertAttachment.java
│   │   │   │   ├── ExpertReview.java
│   │   │   │   ├── ExpertReviewLog.java
│   │   │   │   └── ExpertStatusLog.java
│   │   │   ├── mapper/
│   │   │   │   ├── ExpertInfoMapper.java
│   │   │   │   ├── ExpertCertificateMapper.java
│   │   │   │   ├── ExpertEducationMapper.java
│   │   │   │   ├── ExpertAchievementMapper.java
│   │   │   │   ├── ExpertAttachmentMapper.java
│   │   │   │   ├── ExpertReviewMapper.java
│   │   │   │   └── ExpertStatusLogMapper.java
│   │   │   └── dto/
│   │   │   │   ├── ExpertRegisterRequest.java
│   │   │   │   ├── ExpertQueryRequest.java
│   │   │   │   ├── ExpertResponse.java
│   │   │   │   ├── ExpertDetailResponse.java
│   │   │   │   ├── ExpertPortraitResponse.java
│   │   │   │   ├── ReviewPassRequest.java
│   │   │   │   ├── ReviewRejectRequest.java
│   │   │   │   ├── StatusChangeRequest.java
│   │   │   │   └ ImportRequest.java
│   │   │   │   └ ExportRequest.java
│   │   │
│   │   ├── extraction/         # 抽取管理模块
│   │   │   ├── controller/
│   │   │   │   ├── PlanController.java           # 采购方案单
│   │   │   │   ├── SchemeController.java         # 抽取方案
│   │   │   │   ├── ExtractionController.java     # 抽取执行
│   │   │   │   └── ConfirmController.java        # 专家确认
│   │   │   ├── service/
│   │   │   │   ├── PlanService.java
│   │   │   │   ├── SchemeService.java
│   │   │   │   ├── ExtractionService.java        # 抽取算法核心
│   │   │   │   ├── ConfirmService.java
│   │   │   │   └ impl/
│   │   │   ├── entity/
│   │   │   │   ├── ProcurementPlan.java
│   │   │   │   ├── ExtractionScheme.java
│   │   │   │   ├── ExtractionRule.java
│   │   │   │   ├── ExpertExtraction.java
│   │   │   │   └── ExpertConfirmation.java
│   │   │   ├── mapper/
│   │   │   │   ├── PlanMapper.java
│   │   │   │   ├── SchemeMapper.java
│   │   │   │   ├── ExtractionRuleMapper.java
│   │   │   │   ├── ExtractionMapper.java
│   │   │   │   └ ConfirmationMapper.java
│   │   │   └── dto/
│   │   │   │   ├── PlanCreateRequest.java
│   │   │   │   ├── PlanResponse.java
│   │   │   │   ├── SchemeCreateRequest.java
│   │   │   │   ├── SchemeResponse.java
│   │   │   │   ├── ExtractionRequest.java
│   │   │   │   ├── ExtractionResultResponse.java
│   │   │   │   ├── ConfirmRequest.java
│   │   │   │   └ RejectRequest.java
│   │   │   │   └ ReExtractRequest.java
│   │   │
│   │   ├── bid/                # 评标管理模块
│   │   │   ├── controller/
│   │   │   │   ├── CommitteeController.java
│   │   │   │   └ EvaluationController.java
│   │   │   ├── service/
│   │   │   │   ├── CommitteeService.java
│   │   │   │   ├── EvaluationService.java
│   │   │   │   └ impl/
│   │   │   ├── entity/
│   │   │   │   ├── BidCommittee.java
│   │   │   │   ├── BidCommitteeMember.java
│   │   │   │   ├── ExpertEvaluation.java
│   │   │   │   └ EvaluationItem.java
│   │   │   ├── mapper/
│   │   │   │   ├── CommitteeMapper.java
│   │   │   │   ├── CommitteeMemberMapper.java
│   │   │   │   └ EvaluationMapper.java
│   │   │   └ and dto/
│   │   │   │   ├── CommitteeCreateRequest.java
│   │   │   │   ├── CommitteeResponse.java
│   │   │   │   ├── EvaluationRequest.java
│   │   │   │   ├── EvaluationResponse.java
│   │   │   │   └ EvaluationHistoryResponse.java
│   │   │
│   │   └ message/              # 消息通知模块
│   │   │   ├── controller/
│   │   │   │   └ MessageController.java
│   │   │   ├── service/
│   │   │   │   ├── SmsService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   ├── WeChatService.java        # 企业微信服务
│   │   │   │   ├── MessageTemplateService.java
│   │   │   │   └ impl/
│   │   │   ├── entity/
│   │   │   │   ├── MessageTemplate.java
│   │   │   │   ├── MessageLog.java
│   │   │   ├── mapper/
│   │   │   │   ├── TemplateMapper.java
│   │   │   │   └ MessageLogMapper.java
│   │   │   └ and dto/
│   │   │   │   ├── MessageSendRequest.java
│   │   │   │   ├── TemplateCreateRequest.java
│   │   │
│   │   └ attachment/          # 文件管理模块
│   │   │   ├── controller/
│   │   │   │   └ AttachmentController.java
│   │   │   ├── service/
│   │   │   │   ├── FileStorageService.java
│   │   │   │   └ impl/
│   │   │   ├── entity/
│   │   │   │   ├── Attachment.java
│   │   │   ├── mapper/
│   │   │   │   ├── AttachmentMapper.java
│   │   │   └ and dto/
│   │   │   │   ├── UploadResponse.java
│   │   │
│   │   └ dictparam/            # 数据字典模块
│   │   │   ├── controller/
│   │   │   │   ├── DictController.java
│   │   │   │   ├── DictItemController.java
│   │   │   ├── service/
│   │   │   │   ├── DictService.java
│   │   │   │   └ impl/
│   │   │   ├── entity/
│   │   │   │   ├── SysDict.java
│   │   │   │   ├── SysDictItem.java
│   │   │   ├── mapper/
│   │   │   │   ├── DictMapper.java
│   │   │   │   ├── DictItemMapper.java
│   │   │   └ and dto/
│   │   │   │   ├── DictResponse.java
│   │   │   │   ├── DictItemResponse.java
│   │   │
│   ├── ExpertApplication.java  # 启动类
│   │
│   └── resources/
│   ├── application.yml          # 主配置
│   ├── application-dev.yml      # 开发环境配置
│   ├── application-prod.yml     # 生产环境配置
│   ├── mapper/                  # MyBatis XML
│   │   ├── ExpertInfoMapper.xml
│   │   ├── UserMapper.xml
│   │   ├── ...
│   └── db/
│   │   ├── migration/           # 数据库迁移脚本
│   │   │   ├── V1__init_schema.sql
│   │   │   ├── V2__add_expert_tables.sql
│   │   │   ├── ...
│   │   └── data/                # 初始化数据
│   │   │   ├── init_dict.sql
│   │   │   ├── init_permission.sql
│   │   │   └── ...
│   └── logback-spring.xml       # 日志配置
│
├── pom.xml                      # Maven依赖
└── README.md                    # 项目说明
```

### 1.2 技术栈详情

| 类别 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 核心 | Spring Boot | 3.2.x | 主框架 |
| 安全 | Spring Security + JWT | - | 认证授权 |
| ORM | MyBatis Plus | 3.5.x | 数据访问 |
| 数据库 | MySQL | 8.x | 数据存储 |
| 连接池 | HikariCP | - | 连接池 |
| 文档 | SpringDoc OpenAPI | 2.x | API文档 |
| 工具 | Hutool | 5.x | Java工具库 |
| Excel | EasyExcel | 3.x | Excel处理 |
| 验证 | Hibernate Validator | - | 参数校验 |
| 日志 | Logback | - | 日志框架 |
| 构建 | Maven | 3.x | 构建工具 |

### 1.3 分层架构

```
Controller层 -> Service层 -> Mapper层 -> Entity层

Controller：接收请求、参数校验、调用Service、返回响应
Service：业务逻辑处理、事务管理
Mapper：数据访问、SQL执行
Entity：数据实体映射

DTO/VO分离：
Request DTO：接收前端请求参数
Response DTO：返回前端响应数据
Entity：数据库实体映射
```

---

## 2. 数据库设计

### 2.1 数据库表结构

#### 2.1.1 用户相关表

**sys_user（系统用户表）**
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(200) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人ID',
    update_by BIGINT COMMENT '更新人ID',
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
```

**sys_role（角色表）**
```sql
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

**sys_permission（权限表）**
```sql
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    perm_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    perm_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    perm_type TINYINT NOT NULL COMMENT '权限类型：1菜单 2按钮 3接口',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    path VARCHAR(200) COMMENT '路由路径/接口路径',
    icon VARCHAR(50) COMMENT '菜单图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
```

**sys_role_permission（角色权限关联表）**
```sql
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_perm (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
```

**sys_user_role（用户角色关联表）**
```sql
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

#### 2.1.2 专家相关表

**expert_info（专家基本信息表）**
```sql
CREATE TABLE expert_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    expert_no VARCHAR(50) UNIQUE COMMENT '专家编号（审核通过后生成）',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT NOT NULL COMMENT '性别：1男 2女',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    id_card VARCHAR(20) COMMENT '身份证号',
    expert_type VARCHAR(20) NOT NULL COMMENT '专家类型：TECH/ECON/LAW/MGMT',
    expert_level VARCHAR(20) NOT NULL COMMENT '专家级别：JUNIOR/INTERMEDIATE/SENIOR/EXPERT',
    expertise_areas VARCHAR(500) COMMENT '擅长领域（逗号分隔）',
    work_unit VARCHAR(100) COMMENT '工作单位',
    position VARCHAR(50) COMMENT '职务',
    introduction TEXT COMMENT '简介',
    photo_url VARCHAR(200) COMMENT '照片URL',
    status VARCHAR(20) DEFAULT 'POTENTIAL' COMMENT '状态：POTENTIAL/NORMAL/SUSPENDED/ELIMINATED',
    source VARCHAR(20) NOT NULL COMMENT '来源：PUBLIC/INTERNAL',
    user_id BIGINT COMMENT '关联用户ID（审核通过后绑定）',
    review_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '审核状态：PENDING/INIT_PASS/INIT_REJECT/RE_PASS/RE_REJECT',
    bid_count INT DEFAULT 0 COMMENT '参与评标次数',
    score_avg DECIMAL(5,2) DEFAULT 0 COMMENT '平均评分',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    INDEX idx_name (name),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_type_level (expert_type, expert_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家基本信息表';
```

**expert_certificate（专家证书表）**
```sql
CREATE TABLE expert_certificate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    cert_name VARCHAR(100) NOT NULL COMMENT '证书名称',
    cert_no VARCHAR(50) COMMENT '证书编号',
    issue_org VARCHAR(100) COMMENT '发证机构',
    issue_date DATE COMMENT '发证日期',
    valid_date DATE COMMENT '有效期',
    cert_url VARCHAR(200) COMMENT '证书附件URL',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家证书表';
```

**expert_education（专家教育经历表）**
```sql
CREATE TABLE expert_education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    school VARCHAR(100) NOT NULL COMMENT '学校',
    major VARCHAR(100) COMMENT '专业',
    education VARCHAR(20) COMMENT '学历：HIGH/BACHELOR/MASTER/DOCTOR',
    degree VARCHAR(20) COMMENT '学位',
    graduation_date DATE COMMENT '毕业时间',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家教育经历表';
```

**expert_achievement（专家成果展示表）**
```sql
CREATE TABLE expert_achievement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    achievement_name VARCHAR(200) NOT NULL COMMENT '成果名称',
    achievement_type VARCHAR(50) COMMENT '成果类型：PAPER/PATENT/PROJECT/AWARD',
    achievement_desc TEXT COMMENT '成果描述',
    achievement_url VARCHAR(200) COMMENT '成果附件URL',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家成果展示表';
```

**expert_attachment（专家附件表）**
```sql
CREATE TABLE expert_attachment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    file_name VARCHAR(200) NOT NULL COMMENT '文件名',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型：ID_CARD/CERT/EDU/ACHIEVE/OTHER',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小（字节）',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家附件表';
```

**expert_status_log（专家状态变更记录表）**
```sql
CREATE TABLE expert_status_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    old_status VARCHAR(20) COMMENT '原状态',
    new_status VARCHAR(20) NOT NULL COMMENT '新状态',
    reason VARCHAR(500) COMMENT '变更原因',
    operate_by BIGINT NOT NULL COMMENT '操作人ID',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家状态变更记录表';
```

#### 2.1.3 审核相关表

**expert_review（专家审核记录表）**
```sql
CREATE TABLE expert_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    review_type VARCHAR(20) NOT NULL COMMENT '审核类型：INIT/RE',
    review_status VARCHAR(20) NOT NULL COMMENT '审核状态：PENDING/PASS/REJECT',
    reviewer_id BIGINT COMMENT '审核人ID',
    review_time DATETIME COMMENT '审核时间',
    review_comment VARCHAR(500) COMMENT '审核意见',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    oa_flow_id VARCHAR(100) COMMENT 'OA审批流程ID',
    oa_flow_status VARCHAR(20) COMMENT 'OA审批状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_expert_id (expert_id),
    INDEX idx_review_status (review_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家审核记录表';
```

**expert_review_log（审核操作日志表）**
```sql
CREATE TABLE expert_review_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_id BIGINT NOT NULL COMMENT '审核记录ID',
    operate_type VARCHAR(50) NOT NULL COMMENT '操作类型：SUBMIT/PASS/REJECT/OA_SUBMIT/OA_PASS/OA_REJECT',
    operator_id BIGINT NOT NULL COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    comment VARCHAR(500) COMMENT '操作备注',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_review_id (review_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核操作日志表';
```

#### 2.1.4 抽取相关表

**procurement_plan（采购方案单表）**
```sql
CREATE TABLE procurement_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_no VARCHAR(50) UNIQUE COMMENT '方案单号',
    plan_name VARCHAR(200) NOT NULL COMMENT '方案名称',
    project_name VARCHAR(200) COMMENT '项目名称',
    bid_time DATETIME COMMENT '开标时间',
    bid_location VARCHAR(200) COMMENT '开标地点',
    extraction_mode VARCHAR(20) NOT NULL COMMENT '抽取方式：ONLINE/OFFLINE/MIXED',
    committee_size INT DEFAULT 5 COMMENT '委员会人数',
    plan_status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PENDING/EXTRACTED/CONFIRMED/BID_START/BID_END',
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT,
    INDEX idx_plan_no (plan_no),
    INDEX idx_status (plan_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购方案单表';
```

**extraction_scheme（抽取方案表）**
```sql
CREATE TABLE extraction_scheme (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL COMMENT '采购方案单ID',
    scheme_name VARCHAR(100) COMMENT '方案名称',
    extraction_count INT NOT NULL COMMENT '抽取数量',
    expert_types VARCHAR(200) COMMENT '专家类型条件（逗号分隔）',
    expert_levels VARCHAR(200) COMMENT '专家级别条件（逗号分隔）',
    expertise_areas VARCHAR(500) COMMENT '擅长领域条件（逗号分隔）',
    exclude_month_count INT DEFAULT 0 COMMENT '排除：近N月评标次数>=',
    exclude_max_count INT DEFAULT 0 COMMENT '排除：评标次数上限',
    exclude_experts VARCHAR(500) COMMENT '排除专家ID列表（逗号分隔）',
    exclude_management TINYINT DEFAULT 0 COMMENT '是否排除管理层',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_plan_id (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽取方案表';
```

**expert_extraction（专家抽取记录表）**
```sql
CREATE TABLE expert_extraction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL COMMENT '采购方案单ID',
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    extraction_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '抽取时间',
    extraction_order INT COMMENT '抽取顺序',
    is_reserve TINYINT DEFAULT 0 COMMENT '是否备选：0否 1是',
    INDEX idx_plan_id (plan_id),
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家抽取记录表';
```

**expert_confirmation（专家确认记录表）**
```sql
CREATE TABLE expert_confirmation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    extraction_id BIGINT NOT NULL COMMENT '抽取记录ID',
    plan_id BIGINT NOT NULL COMMENT '采购方案单ID',
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    confirm_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '确认状态：PENDING/CONFIRMED/REJECTED/TIMEOUT',
    confirm_time DATETIME COMMENT '确认时间',
    reject_reason VARCHAR(50) COMMENT '拒绝原因编码',
    reject_comment VARCHAR(500) COMMENT '拒绝理由',
    notify_time DATETIME COMMENT '通知时间',
    sso_token VARCHAR(100) COMMENT '单点登录token',
    expire_time DATETIME COMMENT '确认截止时间',
    INDEX idx_plan_id (plan_id),
    INDEX idx_expert_id (expert_id),
    INDEX idx_status (confirm_status),
    INDEX idx_sso_token (sso_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家确认记录表';
```

#### 2.1.5 评标相关表

**bid_committee（评标委员会表）**
```sql
CREATE TABLE bid_committee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL COMMENT '采购方案单ID',
    committee_name VARCHAR(100) COMMENT '委员会名称',
    leader_id BIGINT COMMENT '组长专家ID',
    supervisor_id BIGINT COMMENT '监督人员ID',
    bid_start_time DATETIME COMMENT '开标时间',
    bid_end_time DATETIME COMMENT '评标结束时间',
    status VARCHAR(20) DEFAULT 'FORMING' COMMENT '状态：FORMING/CONFIRMED/EVALUATING/COMPLETED',
    is_visible TINYINT DEFAULT 0 COMMENT '是否公开：0隐藏 1公开',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_plan_id (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评标委员会表';
```

**bid_committee_member（委员会成员表）**
```sql
CREATE TABLE bid_committee_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    committee_id BIGINT NOT NULL COMMENT '委员会ID',
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    member_role VARCHAR(20) COMMENT '角色：LEADER/MEMBER/SUPERVISOR',
    score DECIMAL(5,2) COMMENT '评分得分',
    is_veto TINYINT DEFAULT 0 COMMENT '是否一票否决：0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_committee_id (committee_id),
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='委员会成员表';
```

**expert_evaluation（专家评分表）**
```sql
CREATE TABLE expert_evaluation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    committee_member_id BIGINT NOT NULL COMMENT '委员会成员ID',
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    evaluator_id BIGINT NOT NULL COMMENT '评分人ID',
    total_score DECIMAL(5,2) DEFAULT 100 COMMENT '总得分',
    is_veto TINYINT DEFAULT 0 COMMENT '是否一票否决',
    veto_reason VARCHAR(500) COMMENT '否决原因',
    comment VARCHAR(500) COMMENT '评分说明',
    evaluate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评分时间',
    INDEX idx_member_id (committee_member_id),
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家评分表';
```

**evaluation_item（评分项表）**
```sql
CREATE TABLE evaluation_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    evaluation_id BIGINT NOT NULL COMMENT '评分记录ID',
    item_code VARCHAR(50) NOT NULL COMMENT '评分项编码',
    item_name VARCHAR(100) NOT NULL COMMENT '评分项名称',
    deduct_score DECIMAL(5,2) DEFAULT 0 COMMENT '扣分',
    reason VARCHAR(500) COMMENT '扣分原因',
    INDEX idx_evaluation_id (evaluation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分项表';
```

#### 2.1.6 消息相关表

**message_template（消息模板表）**
```sql
CREATE TABLE message_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_code VARCHAR(50) UNIQUE COMMENT '模板编码',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_type VARCHAR(20) NOT NULL COMMENT '模板类型：SMS/EMAIL/WECHAT',
    template_content TEXT NOT NULL COMMENT '模板内容',
    variables VARCHAR(500) COMMENT '变量列表（逗号分隔）',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';
```

**message_log（消息发送日志表）**
```sql
CREATE TABLE message_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT COMMENT '模板ID',
    message_type VARCHAR(20) NOT NULL COMMENT '消息类型',
    receiver VARCHAR(100) NOT NULL COMMENT '接收人（手机/邮箱/企微ID）',
    content TEXT COMMENT '消息内容',
    send_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '发送状态：PENDING/SUCCESS/FAILED',
    send_time DATETIME COMMENT '发送时间',
    error_msg VARCHAR(500) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_receiver (receiver),
    INDEX idx_status (send_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息发送日志表';
```

#### 2.1.7 数据字典表

**sys_dict（数据字典表）**
```sql
CREATE TABLE sys_dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_code VARCHAR(50) UNIQUE COMMENT '字典编码',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';
```

**sys_dict_item（字典项表）**
```sql
CREATE TABLE sys_dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_code VARCHAR(50) NOT NULL COMMENT '字典编码',
    item_code VARCHAR(50) NOT NULL COMMENT '项编码',
    item_name VARCHAR(100) NOT NULL COMMENT '项名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_dict_item (dict_code, item_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项表';
```

### 2.2 表关系图

```
用户体系：
sys_user -> sys_user_role -> sys_role -> sys_role_permission -> sys_permission

专家体系：
expert_info -> expert_certificate
           -> expert_education
           -> expert_achievement
           -> expert_attachment
           -> expert_status_log
           -> expert_review -> expert_review_log

抽取体系：
procurement_plan -> extraction_scheme
                -> expert_extraction -> expert_confirmation
                -> bid_committee -> bid_committee_member -> expert_evaluation -> evaluation_item

消息体系：
message_template -> message_log
```

---

## 3. API接口设计

### 3.1 统一响应格式

```java
public class ApiResponse<T> {
    private Integer code;       // 响应码：200成功，其他失败
    private String message;     // 响应消息
    private T data;             // 响应数据
    private Long timestamp;     // 时间戳
}

public class PageResult<T> {
    private List<T> records;    // 数据列表
    private Long total;         // 总记录数
    private Integer page;       // 当前页
    private Integer size;       // 每页大小
}
```

### 3.2 响应码定义

| 响应码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权/token失效 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户名已存在 |
| 1002 | 密码错误 |
| 2001 | 专家不存在 |
| 2002 | 专家状态异常 |
| 3001 | 抽取条件不满足 |
| 3002 | 确认已过期 |

### 3.3 API接口列表

#### 认证接口 (/api/auth)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 登录 | POST | /login | username, password | token, userInfo |
| 登出 | POST | /logout | - | - |
| 刷新Token | POST | /refresh | refreshToken | token |
| SSO登录 | GET | /sso | token(ssoToken) | expertId, planId, confirmInfo |

#### 用户管理接口 (/api/user)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 用户列表 | GET | /list | page, size, username, status | PageResult<UserResponse> |
| 用户详情 | GET | /{id} | id | UserResponse |
| 创建用户 | POST | / | UserCreateRequest | id |
| 更新用户 | PUT | /{id} | UserUpdateRequest | - |
| 删除用户 | DELETE | /{id} | id | - |
| 分配角色 | POST | /{id}/role | AssignRoleRequest | - |
| 角色列表 | GET | /role/list | - | List<RoleResponse> |
| 创建角色 | POST | /role | RoleCreateRequest | id |
| 权限列表 | GET | /permission/list | - | List<PermissionResponse> |

#### 专家接口 (/api/expert)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 专家注册 | POST | /register | ExpertRegisterRequest | id |
| 专家列表 | GET | /list | page, size, name, type, level, status | PageResult<ExpertResponse> |
| 专家详情 | GET | /{id} | id | ExpertDetailResponse |
| 更新专家 | PUT | /{id} | ExpertUpdateRequest | - |
| 状态变更 | PUT | /{id}/status | StatusChangeRequest | - |
| 专家画像 | GET | /{id}/portrait | id | ExpertPortraitResponse |
| 导出专家 | GET | /export | query params | file |
| 导入专家 | POST | /import | file | importResult |

#### 审核接口 (/api/review)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 初审列表 | GET | /list | page, size, status | PageResult<ReviewResponse> |
| 初审详情 | GET | /{id} | id | ReviewDetailResponse |
| 初审通过 | POST | /{id}/pass | ReviewPassRequest | - |
| 初审拒绝 | POST | /{id}/reject | ReviewRejectRequest | - |
| 复审列表 | GET | /re-list | page, size, status | PageResult<ReviewResponse> |
| 提交OA审批 | POST | /{id}/oa | - | oaFlowId |
| OA审批回调 | POST | /oa/callback | oaFlowId, status, comment | - |

#### 抽取接口 (/api/extraction)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 方案单列表 | GET | /plan/list | page, size, status | PageResult<PlanResponse> |
| 创建方案单 | POST | /plan | PlanCreateRequest | id |
| 方案单详情 | GET | /plan/{id} | id | PlanDetailResponse |
| 获取抽取方案 | GET | /scheme/{planId} | planId | SchemeResponse |
| 保存抽取方案 | POST | /scheme | SchemeCreateRequest | id |
| 执行抽取 | POST | /execute | planId | ExtractionResultResponse |
| 抽取结果 | GET | /result/{planId} | planId | ExtractionResultResponse |
| 专家确认 | POST | /confirm | ConfirmRequest | - |
| 专家拒绝 | POST | /reject | RejectRequest | - |
| 重新抽取 | POST | /re-extract | ReExtractRequest | - |

#### 评标接口 (/api/bid)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 委员会列表 | GET | /committee/list | page, size | PageResult<CommitteeResponse> |
| 委员会详情 | GET | /committee/{id} | id | CommitteeDetailResponse |
| 提交评分 | POST | /evaluation | EvaluationRequest | - |
| 评分历史 | GET | /evaluation/history/{expertId} | expertId | EvaluationHistoryResponse |

#### 消息接口 (/api/message)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 发送消息 | POST | /send | MessageSendRequest | messageId |
| 模板列表 | GET | /template/list | type | List<TemplateResponse> |

#### 文件接口 (/api/file)

| 接口 | 方法 | 路径 | 请求参数 | 响应数据 |
|------|------|------|----------|----------|
| 上传文件 | POST | /upload | file, type | UploadResponse |
| 下载文件 | GET | /download/{id} | id | file |

---

## 4. 业务模块设计

### 4.1 专家注册流程

```
1. 前端提交注册信息
2. 后端接收并验证
3. 创建expert_info记录（status=POTENTIAL, review_status=PENDING）
4. 创建expert_certificate、expert_education、expert_achievement记录
5. 上传附件，创建expert_attachment记录
6. 返回注册成功
7. 后续：进入初审流程
```

### 4.2 专家审核流程

```
初审流程：
1. 审核员查看待初审列表（review_status=PENDING）
2. 审核员审核（通过/拒绝）
3. 通过：review_status=INIT_PASS，进入复审
4. 拒绝：review_status=INIT_REJECT，发送拒绝通知

复审流程：
1. 审核员查看待复审列表（review_status=INIT_PASS）
2. 提交OA审批系统
3. OA审批回调：
   - 通过：自动生成专家账号，绑定user_id，status=NORMAL，发送欢迎通知
   - 拒绝：review_status=RE_REJECT，发送拒绝通知
```

### 4.3 专家抽取算法

```java
public List<ExpertInfo> executeExtraction(ExtractionScheme scheme) {
    // 1. 构建查询条件
    LambdaQueryWrapper<ExpertInfo> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(ExpertInfo::getStatus, "NORMAL");
    
    // 2. 添加筛选条件
    if (scheme.getExpertTypes() != null) {
        wrapper.in(ExpertInfo::getExpertType, scheme.getExpertTypes().split(","));
    }
    if (scheme.getExpertLevels() != null) {
        wrapper.in(ExpertInfo::getExpertLevel, scheme.getExpertLevels().split(","));
    }
    if (scheme.getExpertiseAreas() != null) {
        // 模糊匹配擅长领域
        wrapper.apply("expertise_areas LIKE CONCAT('%', {0}, '%')", scheme.getExpertiseAreas());
    }
    
    // 3. 添加排除条件
    List<Long> excludeIds = new ArrayList<>();
    
    // 排除近期评标次数过多的专家
    if (scheme.getExcludeMonthCount() > 0 && scheme.getExcludeMaxCount() > 0) {
        List<Long> busyExperts = findExpertsWithRecentBids(
            scheme.getExcludeMonthCount(), scheme.getExcludeMaxCount());
        excludeIds.addAll(busyExperts);
    }
    
    // 排除指定专家
    if (scheme.getExcludeExperts() != null) {
        excludeIds.addAll(Arrays.stream(scheme.getExcludeExperts().split(","))
            .map(Long::parseLong).collect(Collectors.toList()));
    }
    
    if (!excludeIds.isEmpty()) {
        wrapper.notIn(ExpertInfo::getId, excludeIds);
    }
    
    // 4. 查询候选专家
    List<ExpertInfo> candidates = expertMapper.selectList(wrapper);
    
    // 5. 随机抽取
    int extractCount = scheme.getExtractionCount();
    if (candidates.size() < extractCount) {
        throw new BusinessException("可抽取专家数量不足");
    }
    
    // 使用加权随机（考虑评标次数平衡）
    List<ExpertInfo> selected = weightedRandomSelect(candidates, extractCount);
    
    return selected;
}

// 加权随机选择（评标次数少的权重高）
private List<ExpertInfo> weightedRandomSelect(List<ExpertInfo> candidates, int count) {
    // 计算权重：权重 = 100 - min(评标次数, 50)
    List<Double> weights = candidates.stream()
        .map(e -> 100.0 - Math.min(e.getBidCount(), 50))
        .collect(Collectors.toList());
    
    // 加权随机抽样
    return RandomUtils.weightedSample(candidates, weights, count);
}
```

### 4.4 专家确认流程

```
1. 抽取后创建expert_confirmation记录（status=PENDING）
2. 生成SSO token，设置过期时间（开标前4小时）
3. 发送消息通知专家（短信/邮件/企微），链接包含SSO token
4. 专家点击链接，SSO验证：
   - 验证token有效性
   - 返回确认页面信息
5. 专家确认/拒绝：
   - 确认：status=CONFIRMED
   - 拒绝：status=REJECTED，记录原因，触发重新抽取
6. 超时未确认：定时任务检查，status=TIMEOUT，触发重新抽取
```

---

## 5. 权限认证设计

### 5.1 JWT认证方案

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/extraction/confirm/**").permitAll() // SSO确认
                .requestMatchers("/api/**").authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### 5.2 权限注解

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String value();  // 权限编码
}

// 使用示例
@RequirePermission("expert:review:pass")
@PostMapping("/{id}/pass")
public ApiResponse<Void> reviewPass(@PathVariable Long id) { ... }
```

### 5.3 角色权限定义

| 角色 | 权限 |
|------|------|
| 系统管理员 | 全部权限 |
| 审核员 | 专家初审、复审、主数据查看 |
| 招标负责人 | 抽取方案配置、评标委员会查看、专家评分 |
| 监督人员 | 评标委员会查看、专家评分 |
| 专家用户 | 专家信息查看、评标确认 |

---

## 6. 消息通知设计

### 6.1 消息模板定义

| 模板编码 | 模板名称 | 类型 | 变量 |
|----------|----------|------|------|
| EXTRACT_NOTIFY | 抽取通知 | SMS/EMAIL/WECHAT | expertName, projectName, bidTime, bidLocation, confirmUrl |
| REVIEW_PASS | 审核通过通知 | SMS/EMAIL | expertName |
| REVIEW_REJECT | 审核拒绝通知 | SMS/EMAIL | expertName, reason |
| CONFIRM_SUCCESS | 确认成功通知 | SMS/EMAIL | expertName, projectName |
| BID_REMINDER | 评标提醒 | SMS/WECHAT | expertName, projectName, bidTime |

### 6.2 企业微信集成

```java
public class WeChatService {
    
    // 发送企微消息
    public void sendMessage(String userId, String content, String url) {
        // 1. 获取企微access_token
        String accessToken = getAccessToken();
        
        // 2. 构建消息体
        JSONObject message = new JSONObject();
        message.put("touser", userId);
        message.put("msgtype", "textcard");
        message.put("textcard", new JSONObject()
            .put("title", "专家评标确认")
            .put("description", content)
            .put("url", url)
            .put("btntxt", "点击确认"));
        
        // 3. 发送请求
        String result = HttpUtil.post(WECHAT_API_URL + accessToken, message.toString());
    }
    
    // 生成SSO链接（带token）
    public String generateSsoUrl(Long expertId, Long planId) {
        String token = JwtUtils.generateSsoToken(expertId, planId, 4 * 60); // 4小时有效
        return CONFIRM_URL + "?token=" + token;
    }
}
```

---

## 7. 配置文件

### 7.1 application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expert_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 100MB

# MyBatis配置
mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl

# JWT配置
jwt:
  secret: ${JWT_SECRET}
  expiration: 7200  # 2小时
  sso-expiration: 14400  # 4小时

# 企业微信配置
wechat:
  corp-id: ${WECHAT_CORP_ID}
  agent-id: ${WECHAT_AGENT_ID}
  secret: ${WECHAT_SECRET}

# 短信配置（预留）
sms:
  provider: aliyun
  access-key: ${SMS_ACCESS_KEY}
  secret-key: ${SMS_SECRET_KEY}

# 邮件配置
mail:
  host: smtp.exmail.qq.com
  username: ${MAIL_USERNAME}
  password: ${MAIL_PASSWORD}

# 日志配置
logging:
  level:
    com.expert: DEBUG
    org.springframework: INFO
```

---

## 8. 部署设计

### 8.1 部署架构

```
内网服务器部署：
├── MySQL数据库（端口3306）
├── Spring Boot应用（端口8080）
├── Nginx反向代理（端口80/443）
│   ├── 前端静态文件
│   └── API代理 -> Spring Boot
└── 文件存储目录（/data/files）
```

### 8.2 启动脚本

```bash
#!/bin/bash
# start.sh

APP_NAME=expert-backend
APP_PORT=8080

nohup java -jar \
  -Xms512m -Xmx1024m \
  -Dspring.profiles.active=prod \
  -Dserver.port=$APP_PORT \
  $APP_NAME.jar > logs/app.log 2>&1 &

echo "Application started on port $APP_PORT"
```

---

## 9. 安全设计

- 密码加密：BCrypt加密存储
- JWT Token：RS256签名，2小时过期
- SSO Token：独立签名，4小时过期，仅用于确认页面
- SQL注入防护：MyBatis参数绑定
- XSS防护：输入内容过滤
- CSRF防护：无状态JWT，无需CSRF
- 操作日志：关键操作记录日志