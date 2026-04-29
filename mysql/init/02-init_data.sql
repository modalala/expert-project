-- =============================================
-- 专家库管理系统 测试数据脚本
-- 导出时间: 2026-04-30
-- =============================================

USE expert_db;

--
-- Host: localhost    Database: expert_db
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `bid_committee`
--

LOCK TABLES `bid_committee` WRITE;
/*!40000 ALTER TABLE `bid_committee` DISABLE KEYS */;
INSERT INTO `bid_committee` (`id`, `plan_id`, `committee_name`, `leader_id`, `supervisor_id`, `bid_start_time`, `bid_end_time`, `status`, `is_visible`, `create_time`, `update_time`) VALUES (1,1,'Test Committee 001',NULL,NULL,NULL,NULL,'FORMING',0,'2026-04-29 20:13:46','2026-04-29 20:13:45');
/*!40000 ALTER TABLE `bid_committee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bid_committee_member`
--

LOCK TABLES `bid_committee_member` WRITE;
/*!40000 ALTER TABLE `bid_committee_member` DISABLE KEYS */;
INSERT INTO `bid_committee_member` (`id`, `committee_id`, `expert_id`, `member_role`, `score`, `is_veto`, `create_time`) VALUES (1,1,1,'EXPERT',NULL,0,'2026-04-29 20:13:46');
/*!40000 ALTER TABLE `bid_committee_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `evaluation_item`
--

LOCK TABLES `evaluation_item` WRITE;
/*!40000 ALTER TABLE `evaluation_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_achievement`
--

LOCK TABLES `expert_achievement` WRITE;
/*!40000 ALTER TABLE `expert_achievement` DISABLE KEYS */;
/*!40000 ALTER TABLE `expert_achievement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_attachment`
--

LOCK TABLES `expert_attachment` WRITE;
/*!40000 ALTER TABLE `expert_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `expert_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_certificate`
--

LOCK TABLES `expert_certificate` WRITE;
/*!40000 ALTER TABLE `expert_certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `expert_certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_confirmation`
--

LOCK TABLES `expert_confirmation` WRITE;
/*!40000 ALTER TABLE `expert_confirmation` DISABLE KEYS */;
INSERT INTO `expert_confirmation` (`id`, `extraction_id`, `plan_id`, `expert_id`, `confirm_status`, `confirm_time`, `reject_reason`, `reject_comment`, `notify_time`, `sso_token`, `expire_time`) VALUES (1,1,1,1,'CONFIRMED','2026-04-29 22:41:33',NULL,NULL,'2026-04-29 22:39:53',NULL,'2026-04-30 22:39:53');
INSERT INTO `expert_confirmation` (`id`, `extraction_id`, `plan_id`, `expert_id`, `confirm_status`, `confirm_time`, `reject_reason`, `reject_comment`, `notify_time`, `sso_token`, `expire_time`) VALUES (2,1,1,2,'REJECTED','2026-04-29 22:43:51','BUSINESS','test','2026-04-29 22:39:53',NULL,'2026-04-30 22:39:53');
INSERT INTO `expert_confirmation` (`id`, `extraction_id`, `plan_id`, `expert_id`, `confirm_status`, `confirm_time`, `reject_reason`, `reject_comment`, `notify_time`, `sso_token`, `expire_time`) VALUES (3,1,1,3,'CONFIRMED',NULL,NULL,NULL,'2026-04-29 21:39:53',NULL,'2026-04-30 22:39:53');
INSERT INTO `expert_confirmation` (`id`, `extraction_id`, `plan_id`, `expert_id`, `confirm_status`, `confirm_time`, `reject_reason`, `reject_comment`, `notify_time`, `sso_token`, `expire_time`) VALUES (4,1,1,5,'TIMEOUT','2026-04-29 22:44:27',NULL,NULL,'2026-04-29 22:44:20',NULL,'2026-04-29 23:44:20');
/*!40000 ALTER TABLE `expert_confirmation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_education`
--

LOCK TABLES `expert_education` WRITE;
/*!40000 ALTER TABLE `expert_education` DISABLE KEYS */;
/*!40000 ALTER TABLE `expert_education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_evaluation`
--

LOCK TABLES `expert_evaluation` WRITE;
/*!40000 ALTER TABLE `expert_evaluation` DISABLE KEYS */;
INSERT INTO `expert_evaluation` (`id`, `committee_member_id`, `expert_id`, `evaluator_id`, `total_score`, `is_veto`, `veto_reason`, `comment`, `evaluate_time`) VALUES (1,1,1,1,95.50,0,NULL,'Excellent work','2026-04-29 20:16:15');
INSERT INTO `expert_evaluation` (`id`, `committee_member_id`, `expert_id`, `evaluator_id`, `total_score`, `is_veto`, `veto_reason`, `comment`, `evaluate_time`) VALUES (2,1,5,1,88.50,0,NULL,'表现良好','2026-04-29 23:37:16');
/*!40000 ALTER TABLE `expert_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_extraction`
--

LOCK TABLES `expert_extraction` WRITE;
/*!40000 ALTER TABLE `expert_extraction` DISABLE KEYS */;
INSERT INTO `expert_extraction` (`id`, `plan_id`, `expert_id`, `extraction_time`, `extraction_order`, `is_reserve`) VALUES (1,1,1,'2026-04-29 20:09:07',1,0);
INSERT INTO `expert_extraction` (`id`, `plan_id`, `expert_id`, `extraction_time`, `extraction_order`, `is_reserve`) VALUES (2,1,5,'2026-04-29 23:36:32',2,0);
INSERT INTO `expert_extraction` (`id`, `plan_id`, `expert_id`, `extraction_time`, `extraction_order`, `is_reserve`) VALUES (3,1,5,'2026-04-29 23:36:52',2,0);
INSERT INTO `expert_extraction` (`id`, `plan_id`, `expert_id`, `extraction_time`, `extraction_order`, `is_reserve`) VALUES (4,1,5,'2026-04-29 23:37:16',2,0);
/*!40000 ALTER TABLE `expert_extraction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_info`
--

LOCK TABLES `expert_info` WRITE;
/*!40000 ALTER TABLE `expert_info` DISABLE KEYS */;
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1,'EXP-2026-0001','Zhangsan',1,'13900139001','zhangsan@test.com','320102199001011234','TECH','SENIOR','Software Development,System Architecture','Test Tech Company','Senior Technical Director','Software development 15 years',NULL,'NORMAL','PUBLIC',5,'INIT_PASS',0,0.00,0,'2026-04-29 18:59:50','2026-04-29 20:39:33',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (2,'EXPB3695774','Lisi',1,'13900139002','lisi@test.com','320102199002021234','ECON','INTERMEDIATE','Economic Evaluation,Financial Analysis','Test Accounting Firm','Senior Auditor','Financial audit 10 years',NULL,'POTENTIAL','PUBLIC',NULL,'INIT_REJECT',0,0.00,0,'2026-04-29 18:59:50','2026-04-29 20:39:33',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (3,NULL,'TestExpert',1,'13900139003','test3@test.com',NULL,'TECH','SENIOR',NULL,'Test Company',NULL,NULL,NULL,'POTENTIAL','PUBLIC',NULL,'INIT_PASS',0,0.00,0,'2026-04-29 20:32:40','2026-04-29 20:34:33',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4,NULL,'TestReject',2,'13900139004','test4@test.com',NULL,'ECON','INTERMEDIATE',NULL,'Reject Test Co',NULL,NULL,NULL,'POTENTIAL','PUBLIC',NULL,'INIT_REJECT',0,0.00,0,'2026-04-29 20:34:48','2026-04-29 20:34:56',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (5,NULL,'Wangwu',1,'13900139005','wangwu@test.com',NULL,'TECH','SENIOR',NULL,'Tech Company A',NULL,NULL,NULL,'POTENTIAL','PUBLIC',NULL,'PENDING',0,0.00,0,'2026-04-29 20:44:51','2026-04-29 20:44:51',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (6,NULL,'Sunli',2,'13900139006','sunli@test.com',NULL,'ECON','INTERMEDIATE',NULL,'Finance Company B',NULL,NULL,NULL,'POTENTIAL','PUBLIC',NULL,'PENDING',0,0.00,0,'2026-04-29 20:44:51','2026-04-29 20:44:51',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (7,NULL,'Zhouqi',1,'13900139007','zhouqi@test.com',NULL,'LAW','SENIOR',NULL,'Law Firm C',NULL,NULL,NULL,'POTENTIAL','PUBLIC',NULL,'PENDING',0,0.00,0,'2026-04-29 20:44:51','2026-04-29 20:44:51',NULL,NULL);
INSERT INTO `expert_info` (`id`, `expert_no`, `name`, `gender`, `phone`, `email`, `id_card`, `expert_type`, `expert_level`, `expertise_areas`, `work_unit`, `position`, `introduction`, `photo_url`, `status`, `source`, `user_id`, `review_status`, `bid_count`, `score_avg`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (8,'EXP98AE5349','123',1,'13222142314','','441623411585761234','TECH','INTERMEDIATE','1231','去问驱蚊器我','去','恶趣味请问',NULL,'POTENTIAL','PUBLIC',NULL,'INIT_PASS',0,0.00,0,'2026-04-29 23:28:08','2026-04-29 23:28:22',NULL,NULL);
/*!40000 ALTER TABLE `expert_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_review`
--

LOCK TABLES `expert_review` WRITE;
/*!40000 ALTER TABLE `expert_review` DISABLE KEYS */;
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (1,1,'INIT','PASS',1,'2026-04-29 19:02:33','Qualification meets requirements, approved',NULL,NULL,NULL,'2026-04-29 19:02:33','2026-04-29 19:02:33');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (2,2,'INIT','REJECT',1,'2026-04-29 19:02:33','Missing required certificates','Qualification not meet requirements',NULL,NULL,'2026-04-29 19:02:33','2026-04-29 19:02:33');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (3,1,'RE_REVIEW','PASS',1,'2026-04-29 19:09:31','OA approval passed',NULL,'OA-20260429-0001','PASS','2026-04-29 19:09:17','2026-04-29 19:09:17');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (4,3,'INIT','PASS',1,'2026-04-29 20:34:33','Test initial review pass',NULL,NULL,NULL,'2026-04-29 20:34:33','2026-04-29 20:34:33');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (5,4,'INIT','REJECT',1,'2026-04-29 20:34:56','Incomplete application','Missing required documents',NULL,NULL,'2026-04-29 20:34:55','2026-04-29 20:34:55');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (6,1,'INIT','PASS',1,'2026-04-29 20:35:14','Test',NULL,NULL,NULL,'2026-04-29 20:35:13','2026-04-29 20:35:13');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (7,1,'INIT','PASS',1,'2026-04-29 20:39:33','Test',NULL,NULL,NULL,'2026-04-29 20:39:33','2026-04-29 20:39:33');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (8,2,'INIT','REJECT',1,'2026-04-29 20:39:33','Test','Test',NULL,NULL,'2026-04-29 20:39:33','2026-04-29 20:39:33');
INSERT INTO `expert_review` (`id`, `expert_id`, `review_type`, `review_status`, `reviewer_id`, `review_time`, `review_comment`, `reject_reason`, `oa_flow_id`, `oa_flow_status`, `create_time`, `update_time`) VALUES (9,8,'INIT','PASS',1,'2026-04-29 23:28:22','气味',NULL,NULL,NULL,'2026-04-29 23:28:21','2026-04-29 23:28:21');
/*!40000 ALTER TABLE `expert_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_review_log`
--

LOCK TABLES `expert_review_log` WRITE;
/*!40000 ALTER TABLE `expert_review_log` DISABLE KEYS */;
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (1,1,'PASS',1,'Reviewer','Qualification meets requirements, approved','2026-04-29 19:02:33');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (2,2,'REJECT',1,'Reviewer','Qualification not meet requirements: Missing required certificates','2026-04-29 19:02:33');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (3,3,'SUBMIT_OA',1,'Reviewer','Submitted to OA approval','2026-04-29 19:09:17');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (4,3,'OA_PASS',0,'OA System','OA approval passed. Expert account generated.','2026-04-29 19:09:31');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (5,4,'PASS',1,'Reviewer','Test initial review pass','2026-04-29 20:34:33');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (6,5,'REJECT',1,'Reviewer','Missing required documents: Incomplete application','2026-04-29 20:34:56');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (7,6,'PASS',1,'Reviewer','Test','2026-04-29 20:35:14');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (8,7,'PASS',1,'Reviewer','Test','2026-04-29 20:39:33');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (9,8,'REJECT',1,'Reviewer','Test: Test','2026-04-29 20:39:33');
INSERT INTO `expert_review_log` (`id`, `review_id`, `operate_type`, `operator_id`, `operator_name`, `comment`, `operate_time`) VALUES (10,9,'PASS',1,'Reviewer','气味','2026-04-29 23:28:22');
/*!40000 ALTER TABLE `expert_review_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `expert_status_log`
--

LOCK TABLES `expert_status_log` WRITE;
/*!40000 ALTER TABLE `expert_status_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `expert_status_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `extraction_scheme`
--

LOCK TABLES `extraction_scheme` WRITE;
/*!40000 ALTER TABLE `extraction_scheme` DISABLE KEYS */;
INSERT INTO `extraction_scheme` (`id`, `plan_id`, `scheme_name`, `extraction_count`, `expert_types`, `expert_levels`, `expertise_areas`, `exclude_month_count`, `exclude_max_count`, `exclude_experts`, `exclude_management`, `create_time`, `update_time`) VALUES (1,1,'Test Scheme 001',5,'TECH,ECON','SENIOR','Software Development',6,3,NULL,0,'2026-04-29 20:06:14','2026-04-29 20:06:14');
/*!40000 ALTER TABLE `extraction_scheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `message_log`
--

LOCK TABLES `message_log` WRITE;
/*!40000 ALTER TABLE `message_log` DISABLE KEYS */;
INSERT INTO `message_log` (`id`, `template_id`, `message_type`, `receiver`, `content`, `send_status`, `send_time`, `error_msg`, `create_time`) VALUES (1,1,'SMS','13900139001','尊敬的Zhangsan专家，您已被抽取参与Test Project项目评标。开标时间：2026-05-01 10:00，地点：Room A。请点击链接确认：http://localhost:5173','SUCCESS','2026-04-29 19:50:54',NULL,'2026-04-29 19:50:54');
INSERT INTO `message_log` (`id`, `template_id`, `message_type`, `receiver`, `content`, `send_status`, `send_time`, `error_msg`, `create_time`) VALUES (2,2,'EMAIL','test@example.com','尊敬的Zhangsan专家：\n\n您已被抽取参与Test Project项目评标活动。\n\n开标时间：{bidTime}\n开标地点：{bidLocation}\n\n请于确认截止时间前点击以下链接确认参与：\n{confirmUrl}\n\n如有疑问请联系招标负责人。','SUCCESS','2026-04-29 19:50:54',NULL,'2026-04-29 19:50:54');
/*!40000 ALTER TABLE `message_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `message_template`
--

LOCK TABLES `message_template` WRITE;
/*!40000 ALTER TABLE `message_template` DISABLE KEYS */;
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (1,'EXTRACT_NOTIFY_SMS','抽取通知短信','SMS','尊敬的{expertName}专家，您已被抽取参与{projectName}项目评标。开标时间：{bidTime}，地点：{bidLocation}。请点击链接确认：{confirmUrl}','expertName,projectName,bidTime,bidLocation,confirmUrl',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (2,'EXTRACT_NOTIFY_EMAIL','抽取通知邮件','EMAIL','尊敬的{expertName}专家：\n\n您已被抽取参与{projectName}项目评标活动。\n\n开标时间：{bidTime}\n开标地点：{bidLocation}\n\n请于确认截止时间前点击以下链接确认参与：\n{confirmUrl}\n\n如有疑问请联系招标负责人。','expertName,projectName,bidTime,bidLocation,confirmUrl',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (3,'REVIEW_PASS_NOTIFY','审核通过通知','SMS','尊敬的{expertName}，您已通过专家库审核，正式成为专家库成员。','expertName',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (4,'REVIEW_REJECT_NOTIFY','审核拒绝通知','SMS','尊敬的{expertName}，您的专家入库申请未通过审核。原因：{reason}','expertName,reason',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (5,'CONFIRM_SUCCESS_NOTIFY','确认成功通知','SMS','尊敬的{expertName}，您已确认参与{projectName}项目评标。请按时到达开标地点。','expertName,projectName',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (6,'BID_REMINDER','评标提醒','SMS','尊敬的{expertName}，提醒您明天{bidTime}参与{projectName}项目评标，地点：{bidLocation}。','expertName,projectName,bidTime,bidLocation',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `message_template` (`id`, `template_code`, `template_name`, `template_type`, `template_content`, `variables`, `status`, `create_time`, `update_time`) VALUES (7,'TEST_WECHAT','Test WeChat Template','WECHAT','Test: Hello ${name}, please check ${project}.','name,project',1,'2026-04-29 19:42:44','2026-04-29 19:42:44');
/*!40000 ALTER TABLE `message_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `procurement_plan`
--

LOCK TABLES `procurement_plan` WRITE;
/*!40000 ALTER TABLE `procurement_plan` DISABLE KEYS */;
INSERT INTO `procurement_plan` (`id`, `plan_no`, `plan_name`, `project_name`, `bid_time`, `bid_location`, `extraction_mode`, `committee_size`, `plan_status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1,'PLAN-2026-0001','Test Procurement 001','IT Project',NULL,'Room A','ONLINE',5,'PENDING',0,'2026-04-29 19:12:07','2026-04-29 19:12:08',1,1);
INSERT INTO `procurement_plan` (`id`, `plan_no`, `plan_name`, `project_name`, `bid_time`, `bid_location`, `extraction_mode`, `committee_size`, `plan_status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (2,'PLAN-2026-0002','萨达','阿萨大大',NULL,'','ONLINE',5,'DRAFT',1,'2026-04-29 23:42:25','2026-04-29 23:48:49',1,NULL);
/*!40000 ALTER TABLE `procurement_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_attachment`
--

LOCK TABLES `sys_attachment` WRITE;
/*!40000 ALTER TABLE `sys_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (1,'EXPERT_TYPE','专家类型','专家专业类型分类',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (2,'EXPERT_LEVEL','专家级别','专家资历级别',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (3,'EXPERT_STATUS','专家状态','专家在库状态',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (4,'REVIEW_STATUS','审核状态','专家审核流程状态',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (5,'CONFIRM_STATUS','确认状态','专家抽取确认状态',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (6,'REJECT_REASON','拒绝原因','专家拒绝确认原因',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (7,'EXTRACTION_MODE','抽取方式','专家抽取方式',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (8,'PLAN_STATUS','方案单状态','采购方案单流程状态',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (9,'EDUCATION_TYPE','学历类型','教育学历分类',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (10,'ACHIEVEMENT_TYPE','成果类型','专家成果类型',1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `description`, `status`, `create_time`, `update_time`) VALUES (11,'TEST_DICT','Test Dictionary','Test dictionary for testing',1,'2026-04-29 19:39:10','2026-04-29 19:39:10');
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_dict_item`
--

LOCK TABLES `sys_dict_item` WRITE;
/*!40000 ALTER TABLE `sys_dict_item` DISABLE KEYS */;
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (1,'EXPERT_TYPE','TECH','技术类',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (2,'EXPERT_TYPE','ECON','经济类',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (3,'EXPERT_TYPE','LAW','法律类',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (4,'EXPERT_TYPE','MGMT','管理类',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (5,'EXPERT_LEVEL','JUNIOR','初级',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (6,'EXPERT_LEVEL','INTERMEDIATE','中级',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (7,'EXPERT_LEVEL','SENIOR','高级',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (8,'EXPERT_LEVEL','EXPERT','资深专家',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (9,'EXPERT_STATUS','POTENTIAL','潜在专家',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (10,'EXPERT_STATUS','NORMAL','正常',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (11,'EXPERT_STATUS','SUSPENDED','暂停',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (12,'EXPERT_STATUS','ELIMINATED','出库',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (13,'REVIEW_STATUS','PENDING','待审核',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (14,'REVIEW_STATUS','INIT_PASS','初审通过',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (15,'REVIEW_STATUS','INIT_REJECT','初审拒绝',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (16,'REVIEW_STATUS','RE_PASS','复审通过',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (17,'REVIEW_STATUS','RE_REJECT','复审拒绝',5,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (18,'CONFIRM_STATUS','PENDING','待确认',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (19,'CONFIRM_STATUS','CONFIRMED','已确认',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (20,'CONFIRM_STATUS','REJECTED','已拒绝',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (21,'CONFIRM_STATUS','TIMEOUT','已超时',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (22,'REJECT_REASON','BUSINESS','工作冲突',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (23,'REJECT_REASON','HEALTH','健康原因',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (24,'REJECT_REASON','TIME','时间冲突',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (25,'REJECT_REASON','OTHER','其他原因',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (26,'EXTRACTION_MODE','ONLINE','在线抽取',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (27,'EXTRACTION_MODE','OFFLINE','线下抽取',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (28,'EXTRACTION_MODE','MIXED','混合抽取',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (29,'PLAN_STATUS','DRAFT','草稿',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (30,'PLAN_STATUS','PENDING','待抽取',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (31,'PLAN_STATUS','EXTRACTED','已抽取',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (32,'PLAN_STATUS','CONFIRMED','已确认',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (33,'PLAN_STATUS','BID_START','评标开始',5,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (34,'PLAN_STATUS','BID_END','评标结束',6,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (35,'EDUCATION_TYPE','HIGH','高中',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (36,'EDUCATION_TYPE','BACHELOR','本科',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (37,'EDUCATION_TYPE','MASTER','硕士',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (38,'EDUCATION_TYPE','DOCTOR','博士',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (39,'ACHIEVEMENT_TYPE','PAPER','论文',1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (40,'ACHIEVEMENT_TYPE','PATENT','专利',2,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (41,'ACHIEVEMENT_TYPE','PROJECT','项目',3,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (42,'ACHIEVEMENT_TYPE','AWARD','奖项',4,1,'2026-04-29 18:24:27');
INSERT INTO `sys_dict_item` (`id`, `dict_code`, `item_code`, `item_name`, `sort_order`, `status`, `create_time`) VALUES (43,'TEST_DICT','ITEM1','Item 1',1,1,'2026-04-29 19:39:10');
/*!40000 ALTER TABLE `sys_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (1,'system','系统管理',1,0,'/system','setting',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (2,'system:user','用户管理',1,1,'/system/user','user',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (3,'system:user:create','创建用户',2,2,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (4,'system:user:update','编辑用户',2,2,NULL,NULL,2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (5,'system:user:delete','删除用户',2,2,NULL,NULL,3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (6,'system:role','角色管理',1,1,'/system/role','team',2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (7,'system:role:create','创建角色',2,6,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (8,'system:role:update','编辑角色',2,6,NULL,NULL,2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (9,'system:role:delete','删除角色',2,6,NULL,NULL,3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (10,'system:dict','数据字典',1,1,'/system/dict','book',3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (11,'expert','专家管理',1,0,'/expert','user-group',2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (12,'expert:register','专家注册',1,11,'/expert/register','form',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (13,'expert:review','专家初审',1,11,'/expert/review','audit',2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (14,'expert:review:pass','初审通过',2,13,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (15,'expert:review:reject','初审拒绝',2,13,NULL,NULL,2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (16,'expert:re-review','专家复审',1,11,'/expert/re-review','check',3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (17,'expert:master','专家主数据',1,11,'/expert/master','database',4,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (18,'expert:master:view','查看专家',2,17,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (19,'expert:master:update','编辑专家',2,17,NULL,NULL,2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (20,'expert:master:status','状态变更',2,17,NULL,NULL,3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (21,'expert:portrait','专家画像',1,11,'/expert/portrait','chart',5,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (22,'extraction','抽取管理',1,0,'/extraction','random',3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (23,'extraction:plan','采购方案单',1,22,'/extraction/plan','file',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (24,'extraction:plan:create','创建方案单',2,23,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (25,'extraction:scheme','抽取方案配置',1,22,'/extraction/scheme','tool',2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (26,'extraction:execute','执行抽取',2,25,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (27,'extraction:confirm','专家确认',1,22,'/extraction/confirm','confirm',3,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (28,'bid','评标管理',1,0,'/bid','star',4,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (29,'bid:committee','评标委员会',1,28,'/bid/committee','group',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (30,'bid:evaluation','专家评分',1,28,'/bid/evaluation','score',2,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (31,'bid:evaluation:submit','提交评分',2,30,NULL,NULL,1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (32,'message','消息管理',1,0,'/message','message',5,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
INSERT INTO `sys_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `parent_id`, `path`, `icon`, `sort_order`, `status`, `create_time`, `update_time`) VALUES (33,'message:template','消息模板',1,32,'/message/template','template',1,1,'2026-04-29 18:24:27','2026-04-29 18:24:27');
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1,'ADMIN','系统管理员','拥有系统全部权限',1,0,'2026-04-29 18:24:27','2026-04-29 18:24:27',NULL,NULL);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (2,'REVIEWER','审核员','负责专家初审、复审',1,0,'2026-04-29 18:24:27','2026-04-29 18:24:27',NULL,NULL);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (3,'BID_MANAGER','招标负责人','负责抽取方案配置、评标管理',1,0,'2026-04-29 18:24:27','2026-04-29 18:24:27',NULL,NULL);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4,'SUPERVISOR','监督人员','监督评标过程',1,0,'2026-04-29 18:24:27','2026-04-29 18:24:27',NULL,NULL);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (5,'EXPERT_USER','专家用户','专家个人使用',1,0,'2026-04-29 18:24:27','2026-04-29 18:24:27',NULL,NULL);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (6,'TEST_ROLE','Test Role','Test role for testing',1,0,'2026-04-29 18:55:03',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (1,1,28,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (2,1,29,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (3,1,30,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (4,1,31,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (5,1,11,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (6,1,17,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (7,1,20,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (8,1,19,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (9,1,18,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (10,1,21,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (11,1,16,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (12,1,12,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (13,1,13,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (14,1,14,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (15,1,15,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (16,1,22,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (17,1,27,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (18,1,26,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (19,1,23,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (20,1,24,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (21,1,25,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (22,1,32,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (23,1,33,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (24,1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (25,1,10,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (26,1,6,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (27,1,7,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (28,1,9,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (29,1,8,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (30,1,2,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (31,1,3,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (32,1,5,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (33,1,4,'2026-04-29 18:24:27');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (64,6,1,'2026-04-29 18:56:45');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (65,6,2,'2026-04-29 18:56:45');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (66,6,3,'2026-04-29 18:56:45');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (67,6,11,'2026-04-29 18:56:45');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_time`) VALUES (68,6,12,'2026-04-29 18:56:45');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `avatar`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (1,'admin','$2a$10$Cg8naEi8caQK9CgRZOoMuepatHq5y7kd/ZA6TdGInTedTPpofYHNG','系统管理员','13800000000','admin@example.com',NULL,1,0,'2026-04-29 18:24:27','2026-04-29 18:45:38',NULL,NULL);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `avatar`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (3,'testuser2','$2a$10$wlepHbodJeFwjqTH3SpXBOqKv1Bp4feNqSH2F658ISKiDTNYK5eZG','Updated Name','13900139003','updated@example.com',NULL,0,1,'2026-04-29 18:52:06',NULL,NULL,NULL);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `avatar`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4,'testuser','$2a$10$GxmRAtg9uf5iC7CdepIr0.dKRVz7XCvlEMxiq7Xan7bFRSoohcYBG','Test User','13900001234','test@example.com',NULL,1,0,'2026-04-29 18:56:45',NULL,NULL,NULL);
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `avatar`, `status`, `is_deleted`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (5,'13900139001','$2a$10$fp4DiYtHff5IHacV.Y5g6.LVjIANqpHn8q.Hij6.7EHrfanUaE.9W','Zhangsan','13900139001','zhangsan@test.com',NULL,1,0,'2026-04-29 19:09:31',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`) VALUES (1,1,1,'2026-04-29 18:24:27');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`) VALUES (2,4,6,'2026-04-29 18:56:45');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-30  3:25:27
