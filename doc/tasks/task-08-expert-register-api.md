# Task 8: 专家注册接口

**Agent Task ID**: #5  
**依赖**: Task 2(#3), Task 4(#12)  
**预估**: 0.5天  
**状态**: pending

---

## 开发TODO清单

### 后端开发

#### 1. Entity实体类
- [ ] 创建ExpertInfo实体类（对应expert_info表）
- [ ] 创建ExpertCertificate实体类
- [ ] 创建ExpertEducation实体类
- [ ] 创建ExpertAchievement实体类
- [ ] 创建ExpertAttachment实体类

#### 2. Mapper接口
- [ ] 创建ExpertInfoMapper
- [ ] 创建ExpertCertificateMapper
- [ ] 创建ExpertEducationMapper
- [ ] 创建ExpertAchievementMapper
- [ ] 创建ExpertAttachmentMapper
- [ ] 创建自定义方法：
  - findByPhone
  - findByIdCard
  - findExpertDetailById（关联查询）

#### 3. Service层
- [ ] 创建ExpertRegisterService
- [ ] 实现registerExpert方法：
  - 校验手机号唯一性
  - 校验身份证唯一性
  - 保存expert_info（status=POTENTIAL）
  - 保存expert_certificate列表
  - 保存expert_education列表
  - 保存expert_achievement列表
  - 处理附件上传
  - 记录状态日志
- [ ] 实现checkPhoneUnique方法
- [ ] 实现checkIdCardUnique方法

#### 4. Controller层
- [ ] 创建ExpertRegisterController
- [ ] POST /api/expert/register - 专家注册（公开接口）
- [ ] GET /api/expert/check-phone - 手机号唯一性校验
- [ ] GET /api/expert/check-idcard - 身份证唯一性校验
- [ ] POST /api/expert/upload - 附件上传

#### 5. DTO类
- [ ] ExpertRegisterRequest（包含所有子表数据）
- [ ] CertificateDTO
- [ ] EducationDTO
- [ ] AchievementDTO
- [ ] AttachmentDTO

#### 6. 文件上传处理
- [ ] 创建FileUploadService
- [ ] 实现文件存储到指定目录
- [ ] 生成唯一文件名
- [ ] 返回文件访问URL
- [ ] 文件类型校验（jpg, png, pdf）

---

## 测试用例（真实环境）

### TC-08-01: 专家注册-张三
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/expert/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "gender": 1,
    "phone": "13900139001",
    "email": "zhangsan@test.com",
    "idCard": "320102199001011234",
    "expertType": "TECH",
    "expertLevel": "SENIOR",
    "expertiseAreas": "软件开发,系统架构",
    "workUnit": "测试科技公司",
    "position": "技术总监",
    "introduction": "从事软件开发15年",
    "source": "PUBLIC",
    "certificates": [
      {
        "certName": "高级工程师证书",
        "certNo": "ENG202001001",
        "issueOrg": "省人社厅",
        "issueDate": "2020-01-15"
      }
    ],
    "educations": [
      {
        "school": "南京大学",
        "major": "计算机科学",
        "education": "MASTER",
        "degree": "硕士",
        "graduationDate": "2005-06"
      }
    ],
    "achievements": [
      {
        "achievementName": "省级科技进步奖",
        "achievementType": "AWARD",
        "achievementDesc": "2020年省级科技进步二等奖"
      }
    ]
  }'
```

**预期结果**:
```json
{
  "code": 200,
  "message": "success",
  "data": {"id": 1, "name": "张三", "status": "POTENTIAL", "reviewStatus": "PENDING"}
}
```

**验证数据库**:
```sql
SELECT * FROM expert_info WHERE name = '张三';
SELECT * FROM expert_certificate WHERE expert_id = 1;
SELECT * FROM expert_education WHERE expert_id = 1;
SELECT * FROM expert_achievement WHERE expert_id = 1;
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-08-expert-zhangsan.json`

---

### TC-08-02: 专家注册-李四
**测试步骤**:
```bash
curl -X POST http://localhost:8080/api/expert/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "李四",
    "gender": 1,
    "phone": "13900139002",
    "email": "lisi@test.com",
    "idCard": "320102199002021234",
    "expertType": "ECON",
    "expertLevel": "INTERMEDIATE",
    "expertiseAreas": "经济评估,财务分析",
    "workUnit": "测试会计师事务所",
    "position": "高级审计师",
    "introduction": "从事财务审计10年",
    "source": "PUBLIC"
  }'
```

**预期结果**: 注册成功，id=2

**测试数据保存**: JSON保存到 `./temp/test-data/task-08-expert-lisi.json`

---

### TC-08-03: 手机号重复注册测试
**测试步骤**:
```bash
# 使用张三的手机号再次注册
curl -X POST http://localhost:8080/api/expert/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三克隆",
    "gender": 1,
    "phone": "13900139001",
    ...
  }'
```

**预期结果**:
```json
{
  "code": 400,
  "message": "手机号已被注册"
}
```

**测试数据保存**: JSON保存到 `./temp/test-data/task-08-phone-duplicate.json`

---

### TC-08-04: 身份证重复注册测试
**测试步骤**:
```bash
# 使用张三的身份证再次注册（换手机号）
curl -X POST http://localhost:8080/api/expert/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三克隆2",
    "phone": "13900139003",
    "idCard": "320102199001011234",
    ...
  }'
```

**预期结果**: 返回"身份证号已被注册"错误

---

### TC-08-05: 手机号唯一性校验
**测试步骤**:
```bash
# 校验已存在的手机号
curl "http://localhost:8080/api/expert/check-phone?phone=13900139001"

# 校验新手机号
curl "http://localhost:8080/api/expert/check-phone?phone=13900139099"
```

**预期结果**:
- 已存在返回 false
- 不存在返回 true

---

### TC-08-06: 身份证唯一性校验
**测试步骤**:
```bash
curl "http://localhost:8080/api/expert/check-idcard?idCard=320102199001011234"
```

**预期结果**: 返回校验结果

---

### TC-08-07: 附件上传测试
**测试步骤**:
```bash
# 创建测试图片文件
echo "test image content" > ./temp/test-data/test-image.jpg

# 上传附件
curl -X POST http://localhost:8080/api/expert/upload \
  -F "file=@./temp/test-data/test-image.jpg" \
  -F "fileType=ID_CARD"
```

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "fileId": 1,
    "fileName": "test-image.jpg",
    "fileUrl": "/uploads/expert/xxx.jpg"
  }
}
```

---

## 测试数据持久化

保存张三、李四的专家信息供后续测试：
```json
{
  "zhangsan": {
    "id": 1,
    "name": "张三",
    "phone": "13900139001",
    "status": "POTENTIAL",
    "reviewStatus": "PENDING"
  },
  "lisi": {
    "id": 2,
    "name": "李四",
    "phone": "13900139002",
    "status": "POTENTIAL",
    "reviewStatus": "PENDING"
  }
}
```

保存到 `./temp/test-data/experts-for-review.json`

---

## 验收标准

- [ ] 专家注册接口实现
- [ ] 多表数据正确保存
- [ ] 手机号唯一性校验生效
- [ ] 身份证唯一性校验生效
- [ ] 附件上传成功
- [ ] 状态正确设置（POTENTIAL/PENDING）
- [ ] 张三、李四数据已保存（供后续审核测试）