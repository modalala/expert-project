<template>
  <div class="register-container">
    <el-card>
      <template #header>
        <h2>专家注册</h2>
      </template>

      <el-tabs v-model="activeTab">
        <!-- Basic Info Tab -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="basicForm" :rules="basicRules" ref="basicRef" label-width="120px">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="basicForm.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="basicForm.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="basicForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="basicForm.idCard" placeholder="请输入身份证号" />
            </el-form-item>
            <el-form-item label="专家类型" prop="expertType">
              <el-select v-model="basicForm.expertType" placeholder="请选择类型">
                <el-option label="技术类" value="TECH" />
                <el-option label="经济类" value="ECON" />
                <el-option label="法律类" value="LEGAL" />
              </el-select>
            </el-form-item>
            <el-form-item label="专家级别" prop="expertLevel">
              <el-select v-model="basicForm.expertLevel" placeholder="请选择级别">
                <el-option label="高级" value="SENIOR" />
                <el-option label="中级" value="INTERMEDIATE" />
                <el-option label="初级" value="JUNIOR" />
              </el-select>
            </el-form-item>
            <el-form-item label="专业领域" prop="expertiseAreas">
              <el-input v-model="basicForm.expertiseAreas" placeholder="请输入专业领域" />
            </el-form-item>
            <el-form-item label="工作单位" prop="workUnit">
              <el-input v-model="basicForm.workUnit" placeholder="请输入工作单位" />
            </el-form-item>
            <el-form-item label="职务" prop="position">
              <el-input v-model="basicForm.position" placeholder="请输入职务" />
            </el-form-item>
            <el-form-item label="个人简介">
              <el-input v-model="basicForm.introduction" type="textarea" :rows="4" placeholder="请输入个人简介" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- Certificates Tab -->
        <el-tab-pane label="资格证书" name="certificates">
          <div class="list-container">
            <el-button type="primary" @click="addCertificate">添加证书</el-button>
            <div v-for="(cert, index) in certificateList" :key="index" class="list-item">
              <el-card>
                <el-form :model="cert" label-width="100px">
                  <el-form-item label="证书名称">
                    <el-input v-model="cert.certName" placeholder="请输入证书名称" />
                  </el-form-item>
                  <el-form-item label="证书编号">
                    <el-input v-model="cert.certNo" placeholder="请输入证书编号" />
                  </el-form-item>
                  <el-form-item label="颁发机构">
                    <el-input v-model="cert.issueOrg" placeholder="请输入颁发机构" />
                  </el-form-item>
                  <el-form-item label="颁发日期">
                    <el-date-picker v-model="cert.issueDate" type="date" placeholder="选择日期" />
                  </el-form-item>
                </el-form>
                <el-button type="danger" @click="removeCertificate(index)">删除</el-button>
              </el-card>
            </div>
          </div>
        </el-tab-pane>

        <!-- Education Tab -->
        <el-tab-pane label="教育经历" name="education">
          <div class="list-container">
            <el-button type="primary" @click="addEducation">添加教育经历</el-button>
            <div v-for="(edu, index) in educationList" :key="index" class="list-item">
              <el-card>
                <el-form :model="edu" label-width="100px">
                  <el-form-item label="学校">
                    <el-input v-model="edu.school" placeholder="请输入学校名称" />
                  </el-form-item>
                  <el-form-item label="专业">
                    <el-input v-model="edu.major" placeholder="请输入专业" />
                  </el-form-item>
                  <el-form-item label="学历">
                    <el-select v-model="edu.education" placeholder="请选择学历">
                      <el-option label="本科" value="BACHELOR" />
                      <el-option label="硕士" value="MASTER" />
                      <el-option label="博士" value="DOCTOR" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="学位">
                    <el-input v-model="edu.degree" placeholder="请输入学位" />
                  </el-form-item>
                  <el-form-item label="毕业时间">
                    <el-date-picker v-model="edu.graduationDate" type="month" placeholder="选择时间" />
                  </el-form-item>
                </el-form>
                <el-button type="danger" @click="removeEducation(index)">删除</el-button>
              </el-card>
            </div>
          </div>
        </el-tab-pane>

        <!-- Achievements Tab -->
        <el-tab-pane label="业绩成果" name="achievements">
          <div class="list-container">
            <el-button type="primary" @click="addAchievement">添加业绩成果</el-button>
            <div v-for="(ach, index) in achievementList" :key="index" class="list-item">
              <el-card>
                <el-form :model="ach" label-width="100px">
                  <el-form-item label="成果名称">
                    <el-input v-model="ach.achievementName" placeholder="请输入成果名称" />
                  </el-form-item>
                  <el-form-item label="成果类型">
                    <el-select v-model="ach.achievementType" placeholder="请选择类型">
                      <el-option label="获奖" value="AWARD" />
                      <el-option label="论文" value="PAPER" />
                      <el-option label="专利" value="PATENT" />
                      <el-option label="项目" value="PROJECT" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="成果描述">
                    <el-input v-model="ach.achievementDesc" type="textarea" placeholder="请输入成果描述" />
                  </el-form-item>
                </el-form>
                <el-button type="danger" @click="removeAchievement(index)">删除</el-button>
              </el-card>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="submit-container">
        <el-button type="primary" size="large" @click="submitRegister" :loading="loading">
          提交注册申请
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { expertApi } from '@/api'

const activeTab = ref('basic')
const loading = ref(false)
const basicRef = ref<FormInstance>()

const basicForm = reactive({
  name: '',
  gender: 1,
  phone: '',
  email: '',
  idCard: '',
  expertType: '',
  expertLevel: '',
  expertiseAreas: '',
  workUnit: '',
  position: '',
  introduction: '',
  source: 'PUBLIC'
})

const certificateList = ref<any[]>([])
const educationList = ref<any[]>([])
const achievementList = ref<any[]>([])

const basicRules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dX]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  expertType: [{ required: true, message: '请选择专家类型', trigger: 'change' }],
  expertLevel: [{ required: true, message: '请选择专家级别', trigger: 'change' }],
  workUnit: [{ required: true, message: '请输入工作单位', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职务', trigger: 'blur' }]
}

const addCertificate = () => {
  certificateList.value.push({
    certName: '',
    certNo: '',
    issueOrg: '',
    issueDate: ''
  })
}

const removeCertificate = (index: number) => {
  certificateList.value.splice(index, 1)
}

const addEducation = () => {
  educationList.value.push({
    school: '',
    major: '',
    education: '',
    degree: '',
    graduationDate: ''
  })
}

const removeEducation = (index: number) => {
  educationList.value.splice(index, 1)
}

const addAchievement = () => {
  achievementList.value.push({
    achievementName: '',
    achievementType: '',
    achievementDesc: ''
  })
}

const removeAchievement = (index: number) => {
  achievementList.value.splice(index, 1)
}

const submitRegister = async () => {
  const valid = await basicRef.value?.validate()
  if (!valid) {
    ElMessage.warning('请完善基本信息')
    return
  }

  loading.value = true
  try {
    const data = {
      ...basicForm,
      certificates: certificateList.value,
      educations: educationList.value,
      achievements: achievementList.value
    }
    const res = await expertApi.register(data)
    if (res.code === 200) {
      ElMessage.success('注册申请提交成功，等待审核')
      // Reset form
      basicRef.value?.resetFields()
      certificateList.value = []
      educationList.value = []
      achievementList.value = []
      activeTab.value = 'basic'
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (e: any) {
    ElMessage.error(e.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container { max-width: 800px; margin: 20px auto; }
.list-container { padding: 10px; }
.list-item { margin-top: 16px; }
.submit-container { text-align: center; padding: 20px; }
</style>