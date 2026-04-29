<template>
  <div class="plan-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>采购方案单管理</span>
          <el-button type="primary" @click="showCreateDialog">新建方案单</el-button>
        </div>
      </template>

      <!-- Plan List Table -->
      <el-table :data="planList" v-loading="loading" border stripe>
        <el-table-column prop="planNo" label="方案单号" width="150" />
        <el-table-column prop="planName" label="方案名称" width="200" />
        <el-table-column prop="projectName" label="项目名称" show-overflow-tooltip />
        <el-table-column prop="bidTime" label="开标时间" width="180">
          <template #default="{ row }">
            {{ row.bidTime || '待定' }}
          </template>
        </el-table-column>
        <el-table-column prop="bidLocation" label="开标地点" width="120" />
        <el-table-column prop="extractionMode" label="抽取方式" width="100">
          <template #default="{ row }">
            <el-tag>{{ getModeLabel(row.extractionMode) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="committeeSize" label="委员会人数" width="100" />
        <el-table-column prop="planStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.planStatus)">{{ getStatusLabel(row.planStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
            <el-button type="success" size="small" @click="goToExtraction(row)">配置抽取</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadPlanList"
      />
    </el-card>

    <!-- Create/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑方案单' : '新建方案单'" width="600px">
      <el-form :model="planForm" :rules="planRules" ref="planRef" label-width="120px">
        <el-form-item label="方案名称" prop="planName">
          <el-input v-model="planForm.planName" placeholder="请输入方案名称" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="planForm.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="开标时间">
          <el-date-picker v-model="planForm.bidTime" type="datetime" placeholder="选择开标时间" />
        </el-form-item>
        <el-form-item label="开标地点">
          <el-input v-model="planForm.bidLocation" placeholder="请输入开标地点" />
        </el-form-item>
        <el-form-item label="抽取方式" prop="extractionMode">
          <el-select v-model="planForm.extractionMode" placeholder="选择抽取方式">
            <el-option value="ONLINE" label="线上抽取" />
            <el-option value="OFFLINE" label="线下抽取" />
            <el-option value="MIXED" label="混合抽取" />
          </el-select>
        </el-form-item>
        <el-form-item label="委员会人数" prop="committeeSize">
          <el-input-number v-model="planForm.committeeSize" :min="3" :max="20" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPlan">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import api from '@/api'

interface Plan {
  id: number
  planNo: string
  planName: string
  projectName: string
  bidTime: string
  bidLocation: string
  extractionMode: string
  committeeSize: number
  planStatus: string
}

const loading = ref(false)
const planList = ref<Plan[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)
const planRef = ref<FormInstance>()
const currentPlanId = ref<number | null>(null)

const planForm = reactive({
  planName: '',
  projectName: '',
  bidTime: '',
  bidLocation: '',
  extractionMode: 'ONLINE',
  committeeSize: 5
})

const planRules: FormRules = {
  planName: [{ required: true, message: '请输入方案名称', trigger: 'blur' }],
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  extractionMode: [{ required: true, message: '请选择抽取方式', trigger: 'change' }],
  committeeSize: [{ required: true, message: '请设置委员会人数', trigger: 'change' }]
}

const getModeLabel = (mode: string) => {
  const map: Record<string, string> = { ONLINE: '线上', OFFLINE: '线下', MIXED: '混合' }
  return map[mode] || mode
}

const getStatusLabel = (status: string) => {
  const map: Record<string, string> = {
    DRAFT: '草稿', PENDING: '待抽取', EXTRACTED: '已抽取',
    CONFIRMED: '已确认', BID_START: '评标中', BID_END: '已结束'
  }
  return map[status] || status
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    DRAFT: 'info', PENDING: 'warning', EXTRACTED: 'primary',
    CONFIRMED: 'success', BID_START: 'danger', BID_END: 'success'
  }
  return map[status] || 'info'
}

const loadPlanList = async () => {
  loading.value = true
  try {
    const res: any = await api.get(`/plan/list?page=${page.value}&size=${size.value}`)
    if (res.code === 200) {
      planList.value = res.data.records
      total.value = res.data.total
    }
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  currentPlanId.value = null
  Object.assign(planForm, {
    planName: '', projectName: '', bidTime: '', bidLocation: '',
    extractionMode: 'ONLINE', committeeSize: 5
  })
  dialogVisible.value = true
}

const showEditDialog = (row: Plan) => {
  isEdit.value = true
  currentPlanId.value = row.id
  Object.assign(planForm, row)
  dialogVisible.value = true
}

const submitPlan = async () => {
  const valid = await planRef.value?.validate()
  if (!valid) return

  try {
    if (isEdit.value && currentPlanId.value) {
      const res: any = await api.put(`/plan/${currentPlanId.value}`, planForm)
      if (res.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadPlanList()
      }
    } else {
      const res: any = await api.post('/plan', planForm)
      if (res.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadPlanList()
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row: Plan) => {
  await ElMessageBox.confirm('确定删除该方案单？', '提示', { type: 'warning' })
  try {
    await api.delete(`/plan/${row.id}`)
    ElMessage.success('删除成功')
    loadPlanList()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

const goToExtraction = (row: Plan) => {
  ElMessage.info(`跳转到抽取方案配置，方案单ID: ${row.id}`)
  // 可以跳转到抽取方案配置页面
}

onMounted(() => {
  loadPlanList()
})
</script>

<style scoped>
.plan-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.el-pagination { margin-top: 20px; justify-content: flex-end; }
</style>