<template>
  <div class="message-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>消息模板管理</span>
          <el-button type="primary" @click="showAddTemplate">新增模板</el-button>
        </div>
      </template>

      <el-table :data="templateList" v-loading="loading" border>
        <el-table-column prop="templateCode" label="模板编码" width="150" />
        <el-table-column prop="templateName" label="模板名称" width="150" />
        <el-table-column prop="templateType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ getTypeLabel(row.templateType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="templateContent" label="内容" show-overflow-tooltip />
        <el-table-column prop="variables" label="变量" width="150" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="testSend(row)">测试</el-button>
            <el-button type="danger" size="small" @click="deleteTemplate(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add Template Dialog -->
    <el-dialog v-model="addTemplateVisible" title="新增消息模板" width="600px">
      <el-form :model="templateForm" label-width="120px">
        <el-form-item label="模板编码">
          <el-input v-model="templateForm.templateCode" placeholder="如：EXTRACT_NOTIFY" />
        </el-form-item>
        <el-form-item label="模板名称">
          <el-input v-model="templateForm.templateName" placeholder="如：专家抽取通知" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="templateForm.templateType">
            <el-option value="WECHAT" label="微信" />
            <el-option value="SMS" label="短信" />
            <el-option value="EMAIL" label="邮件" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板内容">
          <el-input v-model="templateForm.templateContent" type="textarea" :rows="4"
            placeholder="尊敬的${expertName}，您已被抽取参加..." />
        </el-form-item>
        <el-form-item label="变量列表">
          <el-input v-model="templateForm.variables" placeholder="如：expertName,projectName,time" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addTemplateVisible = false">取消</el-button>
        <el-button type="primary" @click="createTemplate">创建</el-button>
      </template>
    </el-dialog>

    <!-- Test Send Dialog -->
    <el-dialog v-model="testSendVisible" title="测试发送消息" width="500px">
      <el-form :model="testForm" label-width="100px">
        <el-form-item label="接收人">
          <el-input v-model="testForm.receiver" placeholder="专家手机号或邮箱" />
        </el-form-item>
        <el-alert v-if="currentTemplate" :title="`模板: ${currentTemplate.templateCode}`" type="info" :closable="false" />
        <el-form-item label="参数值">
          <div v-for="v in templateVariables" :key="v" class="param-item">
            <el-input v-model="testParams[v]" :placeholder="v" style="width: 200px" />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="testSendVisible = false">取消</el-button>
        <el-button type="primary" @click="sendTestMessage">发送测试</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface Template {
  id: number
  templateCode: string
  templateName: string
  templateType: string
  templateContent: string
  variables: string
}

const loading = ref(false)
const templateList = ref<Template[]>([])
const addTemplateVisible = ref(false)
const templateForm = ref({ templateCode: '', templateName: '', templateType: 'WECHAT', templateContent: '', variables: '' })

const testSendVisible = ref(false)
const currentTemplate = ref<Template | null>(null)
const testForm = ref({ receiver: '' })
const testParams = ref<Record<string, string>>({})

const templateVariables = computed(() => {
  if (!currentTemplate.value?.variables) return []
  return currentTemplate.value.variables.split(',').map(v => v.trim())
})

const getTypeLabel = (type: string) => {
  const map: Record<string, string> = { WECHAT: '微信', SMS: '短信', EMAIL: '邮件' }
  return map[type] || type
}

const loadTemplates = async () => {
  loading.value = true
  try {
    const res: any = await api.get('/message/template/list')
    if (res.code === 200) {
      templateList.value = res.data.records
    }
  } finally {
    loading.value = false
  }
}

const showAddTemplate = () => {
  templateForm.value = { templateCode: '', templateName: '', templateType: 'WECHAT', templateContent: '', variables: '' }
  addTemplateVisible.value = true
}

const createTemplate = async () => {
  try {
    const res: any = await api.post('/message/template', templateForm.value)
    if (res.code === 200) {
      ElMessage.success('模板创建成功')
      addTemplateVisible.value = false
      loadTemplates()
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    ElMessage.error('创建模板失败')
  }
}

const deleteTemplate = async (row: Template) => {
  await ElMessageBox.confirm('确定删除该模板？', '提示')
  try {
    await api.delete(`/message/template/${row.id}`)
    ElMessage.success('模板已删除')
    loadTemplates()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

const testSend = (row: Template) => {
  currentTemplate.value = row
  testParams.value = {}
  templateVariables.value.forEach(v => testParams.value[v] = '')
  testForm.value = { receiver: '' }
  testSendVisible.value = true
}

const sendTestMessage = async () => {
  try {
    const res: any = await api.post('/message/wechat/send', {
      templateCode: currentTemplate.value?.templateCode,
      receiver: testForm.value.receiver,
      params: testParams.value
    })
    if (res.code === 200) {
      ElMessage.success('发送成功')
      testSendVisible.value = false
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    ElMessage.error('发送失败')
  }
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.message-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.param-item {
  margin-bottom: 10px;
}
</style>