<template>
  <div class="confirm-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专家确认管理</span>
          <el-select v-model="selectedPlanId" placeholder="选择采购方案" @change="loadConfirmList" style="width: 200px; margin-right: 10px;">
            <el-option v-for="plan in planList" :key="plan.id" :label="plan.planName" :value="plan.id" />
          </el-select>
        </div>
      </template>

      <!-- Confirm List Table -->
      <el-table :data="confirmList" v-loading="loading" border stripe>
        <el-table-column prop="expertName" label="专家姓名" width="120" />
        <el-table-column prop="expertPhone" label="联系电话" width="130" />
        <el-table-column prop="expertType" label="专家类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ getExpertTypeLabel(row.expertType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="extractionTime" label="抽取时间" width="180" />
        <el-table-column prop="notifyTime" label="通知时间" width="180" />
        <el-table-column prop="expireTime" label="截止时间" width="180">
          <template #default="{ row }">
            <span :style="{ color: isExpired(row.expireTime) ? 'red' : '' }">{{ row.expireTime || '未设置' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="confirmStatus" label="确认状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.confirmStatus)">{{ getStatusLabel(row.confirmStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleConfirm(row)" :disabled="row.confirmStatus !== 'PENDING'">确认</el-button>
            <el-button type="danger" size="small" @click="handleReject(row)" :disabled="row.confirmStatus !== 'PENDING'">拒绝</el-button>
            <el-button type="info" size="small" @click="handleTimeout(row)" :disabled="row.confirmStatus !== 'PENDING'">超时</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Reject Dialog -->
    <el-dialog v-model="rejectVisible" title="拒绝原因" width="400px">
      <el-form :model="rejectForm" label-width="100px">
        <el-form-item label="拒绝原因">
          <el-select v-model="rejectForm.rejectReason" placeholder="选择拒绝原因">
            <el-option value="BUSINESS" label="工作安排冲突" />
            <el-option value="HEALTH" label="健康原因" />
            <el-option value="OTHER" label="其他原因" />
          </el-select>
        </el-form-item>
        <el-form-item label="补充说明">
          <el-input v-model="rejectForm.rejectComment" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="submitReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

interface ConfirmItem {
  id: number
  expertId: number
  expertName: string
  expertPhone: string
  expertType: string
  extractionTime: string
  notifyTime: string
  expireTime: string
  confirmStatus: string
}

interface Plan {
  id: number
  planNo: string
  planName: string
}

const loading = ref(false)
const confirmList = ref<ConfirmItem[]>([])
const planList = ref<Plan[]>([])
const selectedPlanId = ref<number | null>(null)

const rejectVisible = ref(false)
const rejectForm = reactive({ rejectReason: '', rejectComment: '' })
const currentConfirmId = ref<number | null>(null)

const getExpertTypeLabel = (type: string) => {
  const map: Record<string, string> = { TECH: '技术类', ECON: '经济类', LAW: '法律类', MGMT: '管理类' }
  return map[type] || type
}

const getStatusLabel = (status: string) => {
  const map: Record<string, string> = { PENDING: '待确认', CONFIRMED: '已确认', REJECTED: '已拒绝', TIMEOUT: '已超时' }
  return map[status] || status
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = { PENDING: 'warning', CONFIRMED: 'success', REJECTED: 'danger', TIMEOUT: 'info' }
  return map[status] || 'info'
}

const isExpired = (expireTime: string) => {
  if (!expireTime) return false
  return new Date(expireTime) < new Date()
}

const loadPlanList = async () => {
  try {
    const res: any = await api.get('/plan/list')
    if (res.code === 200 && res.data.records.length > 0) {
      planList.value = res.data.records
      selectedPlanId.value = planList.value[0].id
      loadConfirmList()
    }
  } catch (e) {
    ElMessage.error('加载方案单失败')
  }
}

const loadConfirmList = async () => {
  if (!selectedPlanId.value) return
  loading.value = true
  try {
    const res: any = await api.get(`/extraction/confirm/list/${selectedPlanId.value}`)
    if (res.code === 200) {
      confirmList.value = res.data.map((item: any) => ({
        id: item.id,
        expertId: item.expertId,
        expertName: item.expertName || '未知',
        expertPhone: item.expertPhone || '',
        expertType: item.expertType,
        extractionTime: item.extractionTime,
        notifyTime: item.notifyTime || new Date().toISOString(),
        expireTime: item.expireTime || '',
        confirmStatus: item.confirmStatus || 'PENDING'
      }))
    }
  } catch (e) {
    ElMessage.error('加载确认列表失败')
  } finally {
    loading.value = false
  }
}

const handleConfirm = async (row: ConfirmItem) => {
  try {
    const res: any = await api.post(`/extraction/confirm/${row.id}/accept`)
    if (res.code === 200) {
      ElMessage.success('确认成功')
      loadConfirmList()
    }
  } catch (e) {
    ElMessage.error('确认失败')
  }
}

const handleReject = (row: ConfirmItem) => {
  currentConfirmId.value = row.id
  rejectForm.rejectReason = ''
  rejectForm.rejectComment = ''
  rejectVisible.value = true
}

const submitReject = async () => {
  if (!currentConfirmId.value) return
  try {
    const res: any = await api.post(`/extraction/confirm/${currentConfirmId.value}/reject`, rejectForm)
    if (res.code === 200) {
      ElMessage.success('拒绝成功')
      rejectVisible.value = false
      loadConfirmList()
    }
  } catch (e) {
    ElMessage.error('拒绝失败')
  }
}

const handleTimeout = async (row: ConfirmItem) => {
  try {
    const res: any = await api.post(`/extraction/confirm/${row.id}/timeout`)
    if (res.code === 200) {
      ElMessage.success('已标记超时')
      loadConfirmList()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadPlanList()
})
</script>

<style scoped>
.confirm-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>