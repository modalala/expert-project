-- =============================================
-- 专家库管理系统 数据库初始化脚本
-- 版本: 1.0.0
-- 日期: 2026-04-29
-- =============================================

-- 使用expert_db数据库
USE expert_db;

-- =============================================
-- 1. 用户权限相关表
-- =============================================

-- 系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
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

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
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

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
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

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_perm (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- =============================================
-- 2. 专家相关表
-- =============================================

-- 专家基本信息表
CREATE TABLE IF NOT EXISTS expert_info (
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
    INDEX idx_type_level (expert_type, expert_level),
    INDEX idx_review_status (review_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家基本信息表';

-- 专家证书表
CREATE TABLE IF NOT EXISTS expert_certificate (
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

-- 专家教育经历表
CREATE TABLE IF NOT EXISTS expert_education (
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

-- 专家成果展示表
CREATE TABLE IF NOT EXISTS expert_achievement (
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

-- 专家附件表
CREATE TABLE IF NOT EXISTS expert_attachment (
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

-- 专家状态变更记录表
CREATE TABLE IF NOT EXISTS expert_status_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    old_status VARCHAR(20) COMMENT '原状态',
    new_status VARCHAR(20) NOT NULL COMMENT '新状态',
    reason VARCHAR(500) COMMENT '变更原因',
    operate_by BIGINT NOT NULL COMMENT '操作人ID',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家状态变更记录表';

-- =============================================
-- 3. 审核相关表
-- =============================================

-- 专家审核记录表
CREATE TABLE IF NOT EXISTS expert_review (
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

-- 审核操作日志表
CREATE TABLE IF NOT EXISTS expert_review_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_id BIGINT NOT NULL COMMENT '审核记录ID',
    operate_type VARCHAR(50) NOT NULL COMMENT '操作类型：SUBMIT/PASS/REJECT/OA_SUBMIT/OA_PASS/OA_REJECT',
    operator_id BIGINT NOT NULL COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    comment VARCHAR(500) COMMENT '操作备注',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_review_id (review_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核操作日志表';

-- =============================================
-- 4. 抽取相关表
-- =============================================

-- 采购方案单表
CREATE TABLE IF NOT EXISTS procurement_plan (
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

-- 抽取方案表
CREATE TABLE IF NOT EXISTS extraction_scheme (
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

-- 专家抽取记录表
CREATE TABLE IF NOT EXISTS expert_extraction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL COMMENT '采购方案单ID',
    expert_id BIGINT NOT NULL COMMENT '专家ID',
    extraction_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '抽取时间',
    extraction_order INT COMMENT '抽取顺序',
    is_reserve TINYINT DEFAULT 0 COMMENT '是否备选：0否 1是',
    INDEX idx_plan_id (plan_id),
    INDEX idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专家抽取记录表';

-- 专家确认记录表
CREATE TABLE IF NOT EXISTS expert_confirmation (
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

-- =============================================
-- 5. 评标相关表
-- =============================================

-- 评标委员会表
CREATE TABLE IF NOT EXISTS bid_committee (
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

-- 委员会成员表
CREATE TABLE IF NOT EXISTS bid_committee_member (
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

-- 专家评分表
CREATE TABLE IF NOT EXISTS expert_evaluation (
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

-- 评分项表
CREATE TABLE IF NOT EXISTS evaluation_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    evaluation_id BIGINT NOT NULL COMMENT '评分记录ID',
    item_code VARCHAR(50) NOT NULL COMMENT '评分项编码',
    item_name VARCHAR(100) NOT NULL COMMENT '评分项名称',
    deduct_score DECIMAL(5,2) DEFAULT 0 COMMENT '扣分',
    reason VARCHAR(500) COMMENT '扣分原因',
    INDEX idx_evaluation_id (evaluation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分项表';

-- =============================================
-- 6. 消息相关表
-- =============================================

-- 消息模板表
CREATE TABLE IF NOT EXISTS message_template (
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

-- 消息发送日志表
CREATE TABLE IF NOT EXISTS message_log (
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

-- =============================================
-- 7. 数据字典表
-- =============================================

-- 数据字典表
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_code VARCHAR(50) UNIQUE COMMENT '字典编码',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- 字典项表
CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_code VARCHAR(50) NOT NULL COMMENT '字典编码',
    item_code VARCHAR(50) NOT NULL COMMENT '项编码',
    item_name VARCHAR(100) NOT NULL COMMENT '项名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_dict_item (dict_code, item_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项表';

-- =============================================
-- 8. 附件表
-- =============================================

CREATE TABLE IF NOT EXISTS sys_attachment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(200) NOT NULL COMMENT '文件名',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小',
    upload_by BIGINT COMMENT '上传人',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT DEFAULT 0,
    INDEX idx_upload_by (upload_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统附件表';