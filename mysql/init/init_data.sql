-- =============================================
-- 专家库管理系统 初始化数据脚本
-- 版本: 1.0.0
-- 日期: 2026-04-29
-- =============================================

USE expert_db;

-- =============================================
-- 1. 初始化管理员账号
-- 密码: Admin@123 (BCrypt加密)
-- =============================================
INSERT INTO sys_user (id, username, password, real_name, phone, email, status, create_time) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800000000', 'admin@example.com', 1, NOW());

-- =============================================
-- 2. 初始化角色
-- =============================================
INSERT INTO sys_role (id, role_code, role_name, description, status, create_time) VALUES
(1, 'ADMIN', '系统管理员', '拥有系统全部权限', 1, NOW()),
(2, 'REVIEWER', '审核员', '负责专家初审、复审', 1, NOW()),
(3, 'BID_MANAGER', '招标负责人', '负责抽取方案配置、评标管理', 1, NOW()),
(4, 'SUPERVISOR', '监督人员', '监督评标过程', 1, NOW()),
(5, 'EXPERT_USER', '专家用户', '专家个人使用', 1, NOW());

-- =============================================
-- 3. 初始化权限
-- =============================================
INSERT INTO sys_permission (id, perm_code, perm_name, perm_type, parent_id, path, icon, sort_order, status) VALUES
-- 系统管理菜单
(1, 'system', '系统管理', 1, 0, '/system', 'setting', 1, 1),
(2, 'system:user', '用户管理', 1, 1, '/system/user', 'user', 1, 1),
(3, 'system:user:create', '创建用户', 2, 2, NULL, NULL, 1, 1),
(4, 'system:user:update', '编辑用户', 2, 2, NULL, NULL, 2, 1),
(5, 'system:user:delete', '删除用户', 2, 2, NULL, NULL, 3, 1),
(6, 'system:role', '角色管理', 1, 1, '/system/role', 'team', 2, 1),
(7, 'system:role:create', '创建角色', 2, 6, NULL, NULL, 1, 1),
(8, 'system:role:update', '编辑角色', 2, 6, NULL, NULL, 2, 1),
(9, 'system:role:delete', '删除角色', 2, 6, NULL, NULL, 3, 1),
(10, 'system:dict', '数据字典', 1, 1, '/system/dict', 'book', 3, 1),

-- 专家管理菜单
(11, 'expert', '专家管理', 1, 0, '/expert', 'user-group', 2, 1),
(12, 'expert:register', '专家注册', 1, 11, '/expert/register', 'form', 1, 1),
(13, 'expert:review', '专家初审', 1, 11, '/expert/review', 'audit', 2, 1),
(14, 'expert:review:pass', '初审通过', 2, 13, NULL, NULL, 1, 1),
(15, 'expert:review:reject', '初审拒绝', 2, 13, NULL, NULL, 2, 1),
(16, 'expert:re-review', '专家复审', 1, 11, '/expert/re-review', 'check', 3, 1),
(17, 'expert:master', '专家主数据', 1, 11, '/expert/master', 'database', 4, 1),
(18, 'expert:master:view', '查看专家', 2, 17, NULL, NULL, 1, 1),
(19, 'expert:master:update', '编辑专家', 2, 17, NULL, NULL, 2, 1),
(20, 'expert:master:status', '状态变更', 2, 17, NULL, NULL, 3, 1),
(21, 'expert:portrait', '专家画像', 1, 11, '/expert/portrait', 'chart', 5, 1),

-- 抽取管理菜单
(22, 'extraction', '抽取管理', 1, 0, '/extraction', 'random', 3, 1),
(23, 'extraction:plan', '采购方案单', 1, 22, '/extraction/plan', 'file', 1, 1),
(24, 'extraction:plan:create', '创建方案单', 2, 23, NULL, NULL, 1, 1),
(25, 'extraction:scheme', '抽取方案配置', 1, 22, '/extraction/scheme', 'tool', 2, 1),
(26, 'extraction:execute', '执行抽取', 2, 25, NULL, NULL, 1, 1),
(27, 'extraction:confirm', '专家确认', 1, 22, '/extraction/confirm', 'confirm', 3, 1),

-- 评标管理菜单
(28, 'bid', '评标管理', 1, 0, '/bid', 'star', 4, 1),
(29, 'bid:committee', '评标委员会', 1, 28, '/bid/committee', 'group', 1, 1),
(30, 'bid:evaluation', '专家评分', 1, 28, '/bid/evaluation', 'score', 2, 1),
(31, 'bid:evaluation:submit', '提交评分', 2, 30, NULL, NULL, 1, 1),

-- 消息管理菜单
(32, 'message', '消息管理', 1, 0, '/message', 'message', 5, 1),
(33, 'message:template', '消息模板', 1, 32, '/message/template', 'template', 1, 1);

-- =============================================
-- 4. 分配系统管理员全部权限
-- =============================================
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- =============================================
-- 5. 初始化用户角色关联
-- =============================================
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- =============================================
-- 6. 初始化数据字典
-- =============================================
-- 专家类型
INSERT INTO sys_dict (dict_code, dict_name, description, status) VALUES
('EXPERT_TYPE', '专家类型', '专家专业类型分类', 1),
('EXPERT_LEVEL', '专家级别', '专家资历级别', 1),
('EXPERT_STATUS', '专家状态', '专家在库状态', 1),
('REVIEW_STATUS', '审核状态', '专家审核流程状态', 1),
('CONFIRM_STATUS', '确认状态', '专家抽取确认状态', 1),
('REJECT_REASON', '拒绝原因', '专家拒绝确认原因', 1),
('EXTRACTION_MODE', '抽取方式', '专家抽取方式', 1),
('PLAN_STATUS', '方案单状态', '采购方案单流程状态', 1),
('EDUCATION_TYPE', '学历类型', '教育学历分类', 1),
('ACHIEVEMENT_TYPE', '成果类型', '专家成果类型', 1);

-- 专家类型字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('EXPERT_TYPE', 'TECH', '技术类', 1, 1),
('EXPERT_TYPE', 'ECON', '经济类', 2, 1),
('EXPERT_TYPE', 'LAW', '法律类', 3, 1),
('EXPERT_TYPE', 'MGMT', '管理类', 4, 1);

-- 专家级别字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('EXPERT_LEVEL', 'JUNIOR', '初级', 1, 1),
('EXPERT_LEVEL', 'INTERMEDIATE', '中级', 2, 1),
('EXPERT_LEVEL', 'SENIOR', '高级', 3, 1),
('EXPERT_LEVEL', 'EXPERT', '资深专家', 4, 1);

-- 专家状态字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('EXPERT_STATUS', 'POTENTIAL', '潜在专家', 1, 1),
('EXPERT_STATUS', 'NORMAL', '正常', 2, 1),
('EXPERT_STATUS', 'SUSPENDED', '暂停', 3, 1),
('EXPERT_STATUS', 'ELIMINATED', '出库', 4, 1);

-- 审核状态字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('REVIEW_STATUS', 'PENDING', '待审核', 1, 1),
('REVIEW_STATUS', 'INIT_PASS', '初审通过', 2, 1),
('REVIEW_STATUS', 'INIT_REJECT', '初审拒绝', 3, 1),
('REVIEW_STATUS', 'RE_PASS', '复审通过', 4, 1),
('REVIEW_STATUS', 'RE_REJECT', '复审拒绝', 5, 1);

-- 确认状态字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('CONFIRM_STATUS', 'PENDING', '待确认', 1, 1),
('CONFIRM_STATUS', 'CONFIRMED', '已确认', 2, 1),
('CONFIRM_STATUS', 'REJECTED', '已拒绝', 3, 1),
('CONFIRM_STATUS', 'TIMEOUT', '已超时', 4, 1);

-- 拒绝原因字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('REJECT_REASON', 'BUSINESS', '工作冲突', 1, 1),
('REJECT_REASON', 'HEALTH', '健康原因', 2, 1),
('REJECT_REASON', 'TIME', '时间冲突', 3, 1),
('REJECT_REASON', 'OTHER', '其他原因', 4, 1);

-- 抽取方式字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('EXTRACTION_MODE', 'ONLINE', '在线抽取', 1, 1),
('EXTRACTION_MODE', 'OFFLINE', '线下抽取', 2, 1),
('EXTRACTION_MODE', 'MIXED', '混合抽取', 3, 1);

-- 方案单状态字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('PLAN_STATUS', 'DRAFT', '草稿', 1, 1),
('PLAN_STATUS', 'PENDING', '待抽取', 2, 1),
('PLAN_STATUS', 'EXTRACTED', '已抽取', 3, 1),
('PLAN_STATUS', 'CONFIRMED', '已确认', 4, 1),
('PLAN_STATUS', 'BID_START', '评标开始', 5, 1),
('PLAN_STATUS', 'BID_END', '评标结束', 6, 1);

-- 学历类型字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('EDUCATION_TYPE', 'HIGH', '高中', 1, 1),
('EDUCATION_TYPE', 'BACHELOR', '本科', 2, 1),
('EDUCATION_TYPE', 'MASTER', '硕士', 3, 1),
('EDUCATION_TYPE', 'DOCTOR', '博士', 4, 1);

-- 成果类型字典项
INSERT INTO sys_dict_item (dict_code, item_code, item_name, sort_order, status) VALUES
('ACHIEVEMENT_TYPE', 'PAPER', '论文', 1, 1),
('ACHIEVEMENT_TYPE', 'PATENT', '专利', 2, 1),
('ACHIEVEMENT_TYPE', 'PROJECT', '项目', 3, 1),
('ACHIEVEMENT_TYPE', 'AWARD', '奖项', 4, 1);

-- =============================================
-- 7. 初始化消息模板
-- =============================================
INSERT INTO message_template (template_code, template_name, template_type, template_content, variables, status) VALUES
('EXTRACT_NOTIFY_SMS', '抽取通知短信', 'SMS', '尊敬的{expertName}专家，您已被抽取参与{projectName}项目评标。开标时间：{bidTime}，地点：{bidLocation}。请点击链接确认：{confirmUrl}', 'expertName,projectName,bidTime,bidLocation,confirmUrl', 1),
('EXTRACT_NOTIFY_EMAIL', '抽取通知邮件', 'EMAIL', '尊敬的{expertName}专家：\n\n您已被抽取参与{projectName}项目评标活动。\n\n开标时间：{bidTime}\n开标地点：{bidLocation}\n\n请于确认截止时间前点击以下链接确认参与：\n{confirmUrl}\n\n如有疑问请联系招标负责人。', 'expertName,projectName,bidTime,bidLocation,confirmUrl', 1),
('REVIEW_PASS_NOTIFY', '审核通过通知', 'SMS', '尊敬的{expertName}，您已通过专家库审核，正式成为专家库成员。', 'expertName', 1),
('REVIEW_REJECT_NOTIFY', '审核拒绝通知', 'SMS', '尊敬的{expertName}，您的专家入库申请未通过审核。原因：{reason}', 'expertName,reason', 1),
('CONFIRM_SUCCESS_NOTIFY', '确认成功通知', 'SMS', '尊敬的{expertName}，您已确认参与{projectName}项目评标。请按时到达开标地点。', 'expertName,projectName', 1),
('BID_REMINDER', '评标提醒', 'SMS', '尊敬的{expertName}，提醒您明天{bidTime}参与{projectName}项目评标，地点：{bidLocation}。', 'expertName,projectName,bidTime,bidLocation', 1);

-- =============================================
-- 完成
-- =============================================
SELECT '初始化数据完成' AS message;