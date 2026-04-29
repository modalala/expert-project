<template>
  <div class="committee-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>评标委员会管理</span>
          <el-select v-model="selectedPlanId" placeholder="选择采购方案" @change="loadCommittee" style="width: 200px; margin-right: 10px;">
            <el-option v-for="plan in planList" :key="plan.id" :label="plan.planName" :value="plan.id" />
          </el-select>
          <el-button type="primary" @click="showCreateDialog" v-if="!committee">组建委员会</el-button>
        </div>
      </template>

      <!-- Committee Info -->
      <div v-if="committee" class="committee-info">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="方案单号">{{ committee.planNo }}</el-descriptions-item>
          <el-descriptions-item label="委员会名称">{{ committee.committeeName || '待设置' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(committee.committeeStatus)">{{ getStatusLabel(committee.committeeStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ committee.createTime }}</el-descriptions-item>
          <el-descriptions-item label="成员数">{{ committee.members?.length || 0 }}人</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- Member List -->
      <div v-if="committee && committee.members" class="member-section">
        <el-divider content-position="left">委员会成员</el-divider>
        <el-table :data="committee.members" border stripe>
          <el-table-column prop="expertNo" label="专家编号" width="130" />
          <el-table-column prop="expertName" label="专家姓名" width="120" />
          <el-table-column prop="memberRole" label="角色" width="100">
            <template #default="{ row }">
              <el-tag :type="row.memberRole === 'LEADER' ? 'warning' : row.memberRole === 'SUPERVISOR' ? 'info' : ''">
                {{ getRoleLabel(row.memberRole) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评分" width="100">
            <template #default="{ row }">
              {{ row.score || '未评分' }}
            </template>
          </el-table-column>
          <el-table-column label="一票否决" width="100">
            <template #default="{ row }">
              <el-tag type="danger" v-if="row.isVeto">是</el-tag>
              <span v-else>否</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="showEvaluationDialog(row)" v-if="committee.committeeStatus === 'EVALUATING'">评分</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Empty State -->
      <el-empty v-if="!committee && !loading" description="请选择采购方案查看或组建委员会" />
    </el-card>

    <!-- Create Committee Dialog -->
    <el-dialog v-model="createVisible" title="组建评标委员会" width="600px">
      <el-form :model="createForm" label-width="120px">
        <el-form-item label="委员会名称">
          <el-input v-model="createForm.committeeName" placeholder="请输入委员会名称" />
        </el-form-item>
        <el-form-item label="组长">
          <el-select v-model="createForm.leaderId" placeholder="选择组长">
            <el-option v-for="m in availableExperts" :key="m.expertId" :label="m.name" :value="m.expertId" />
          </el-select>
        </el-form-item>
        <el-form-item label="监督员">
          <el-select v-model="createForm.supervisorId" placeholder="选择监督员">
            <el-option v-for="m in availableExperts" :key="m.expertId" :label="m.name" :value="m.expertId" />
          </el-select>
        </el-form-item>
        <el-form-item label="开标时间">
          <el-date-picker v-model="createForm.bidStartTime" type="datetime" placeholder="选择开标时间" />
        </el-form-item>
        <el-form-item label="预计结束">
          <el-date-picker v-model="createForm.bidEndTime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCreate">组建</el-button>
      </template>
    </el-dialog>

    <!-- Evaluation Dialog -->
    <el-dialog v-model="evaluationVisible" title="专家评分" width="500px">
      <el-form :model="evaluationForm" label-width="120px">
        <el-form-item label="评分">
          <el-slider v-model="evaluationForm.score" :min="0" :max="100" show-input />
        </el-form-item>
        <el-form-item label="一票否决">
          <el-switch v-model="evaluationForm.isVeto" />
        </el-form-item>
        <el-form-item label="否决原因" v-if="evaluationForm.isVeto">
          <el-input v-model="evaluationForm.vetoReason" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="评分说明">
          <el-input v-model="evaluationForm.comment" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evaluationVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluation">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

interface Committee {
  id: number
  committeeName: string
  planId: number
  planNo: string
  committeeStatus: string
  createTime: string
  members?: any[]
}

interface Member {
  id: number
  expertId: number
  expertNo: string
  expertName: string
  expertType?: string
  memberRole: string
  score?: number
  isVeto: boolean
}

interface Plan {
  id: number
  planNo: string
  planName: string
}

const loading = ref(false)
const committee = ref<Committee | null>(null)
const planList = ref<Plan[]>([])
const selectedPlanId = ref<number | null>(null)
const availableExperts = ref<any[]>([])

const createVisible = ref(false)
const createForm = reactive({
  committeeName: '',
  leaderId: null,
  supervisorId: null,
  bidStartTime: '',
  bidEndTime: ''
})

const evaluationVisible = ref(false)
const evaluationForm = reactive({
  score: 100,
  isVeto: false,
  vetoReason: '',
  comment: ''
})
const currentMemberId = ref<number | null>(null)

const getExpertTypeLabel = (type: string) => {
  const map: Record<string, string> = { TECH: '技术', ECON: '经济', LAW: '法律', MGMT: '管理' }
  return map[type] || type
}

const getRoleLabel = (role: string) => {
  const map: Record<string, string> = { LEADER: '组长', MEMBER: '成员', SUPERVISOR: '监督', EXPERT: '专家' }
  return map[role] || role
}

const getStatusLabel = (status: string) => {
  const map: Record<string, string> = { FORMING: '组建中', CONFIRMED: '已确认', EVALUATING: '评标中', COMPLETED: '已完成' }
  return map[status] || status
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = { FORMING: 'warning', CONFIRMED: 'success', EVALUATING: 'primary', COMPLETED: 'info' }
  return map[status] || 'info'
}

const loadPlanList = async () => {
  try {
    const res: any = await api.get('/plan/list')
    if (res.code === 200 && res.data.records.length > 0) {
      planList.value = res.data.records
      selectedPlanId.value = planList.value[0].id
      loadCommittee()
    }
  } catch (e) {
    ElMessage.error('加载方案单失败')
  }
}

const loadCommittee = async () => {
  if (!selectedPlanId.value) return
  loading.value = true
  try {
    const res: any = await api.get(`/bid/committee/plan/${selectedPlanId.value}`)
    if (res.code === 200 && res.data) {
      committee.value = res.data
    } else {
      committee.value = null
      loadAvailableExperts()
    }
  } catch (e) {
    committee.value = null
    loadAvailableExperts()
  } finally {
    loading.value = false
  }
}

const loadAvailableExperts = async () => {
  try {
    const res: any = await api.get(`/extraction/scheme/result/plan/${selectedPlanId.value}`)
    if (res.code === 200) {
      availableExperts.value = res.data
    }
  } catch (e) {
    availableExperts.value = []
  }
}

const showCreateDialog = () => {
  createForm.committeeName = `评标委员会-${selectedPlanId.value}`
  createForm.leaderId = null
  createForm.supervisorId = null
  createForm.bidStartTime = ''
  createForm.bidEndTime = ''
  createVisible.value = true
}

const submitCreate = async () => {
  try {
    const res: any = await api.post('/bid/committee', {
      ...createForm,
      planId: selectedPlanId.value
    })
    if (res.code === 200) {
      ElMessage.success('组建成功')
      createVisible.value = false
      loadCommittee()
    }
  } catch (e) {
    ElMessage.error('组建失败')
  }
}

const showEvaluationDialog = (row: any) => {
  currentMemberId.value = row.id
  evaluationForm.score = row.score || 100
  evaluationForm.isVeto = row.isVeto || false
  evaluationForm.vetoReason = ''
  evaluationForm.comment = ''
  evaluationVisible.value = true
}

const submitEvaluation = async () => {
  try {
    const res: any = await api.post('/bid/committee/evaluation', {
      committeeMemberId: currentMemberId.value,
      ...evaluationForm
    })
    if (res.code === 200) {
      ElMessage.success('评分提交成功')
      evaluationVisible.value = false
      loadCommittee()
    }
  } catch (e) {
    ElMessage.error('评分提交失败')
  }
}

onMounted(() => {
  loadPlanList()
})
</script>

<style scoped>
.committee-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.committee-info { margin-bottom: 20px; }
.member-section { margin-top: 20px; }
</style>