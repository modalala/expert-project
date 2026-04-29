<template>
  <div class="expert-master-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专家主数据管理</span>
          <el-button type="primary" @click="showPortraitDialog">画像分析</el-button>
        </div>
      </template>

      <!-- Search Form -->
      <el-form :model="queryForm" inline class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="专家姓名" clearable />
        </el-form-item>
        <el-form-item label="专家类型">
          <el-select v-model="queryForm.expertType" placeholder="专家类型" clearable>
            <el-option value="TECH" label="技术类" />
            <el-option value="ECON" label="经济类" />
            <el-option value="LAW" label="法律类" />
            <el-option value="MGMT" label="管理类" />
          </el-select>
        </el-form-item>
        <el-form-item label="专家级别">
          <el-select v-model="queryForm.expertLevel" placeholder="专家级别" clearable>
            <el-option value="SENIOR" label="高级" />
            <el-option value="INTERMEDIATE" label="中级" />
            <el-option value="JUNIOR" label="初级" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="状态" clearable>
            <el-option value="NORMAL" label="正常" />
            <el-option value="POTENTIAL" label="潜在" />
            <el-option value="BLACKLIST" label="黑名单" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadExperts">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- Expert Table -->
      <el-table :data="expertList" v-loading="loading" border stripe>
        <el-table-column prop="expertNo" label="专家编号" width="130" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="expertType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag>{{ getExpertTypeLabel(row.expertType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expertLevel" label="级别" width="100">
          <template #default="{ row }">
            {{ getExpertLevelLabel(row.expertLevel) }}
          </template>
        </el-table-column>
        <el-table-column prop="workUnit" label="工作单位" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'NORMAL' ? 'success' : row.status === 'BLACKLIST' ? 'danger' : 'warning'">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewStatus" label="审核状态" width="100">
          <template #default="{ row }">
            {{ getReviewStatusLabel(row.reviewStatus) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showDetail(row)">详情</el-button>
            <el-button type="warning" size="small" @click="showEdit(row)">编辑</el-button>
            <el-button type="info" size="small" @click="showPortrait(row)">画像</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadExperts"
      />
    </el-card>

    <!-- Detail Dialog -->
    <el-dialog v-model="detailVisible" title="专家详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="专家编号">{{ currentExpert?.expertNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentExpert?.name }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentExpert?.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentExpert?.email }}</el-descriptions-item>
        <el-descriptions-item label="专家类型">{{ getExpertTypeLabel(currentExpert?.expertType) }}</el-descriptions-item>
        <el-descriptions-item label="专家级别">{{ getExpertLevelLabel(currentExpert?.expertLevel) }}</el-descriptions-item>
        <el-descriptions-item label="工作单位">{{ currentExpert?.workUnit }}</el-descriptions-item>
        <el-descriptions-item label="职务">{{ currentExpert?.position }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusLabel(currentExpert?.status) }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ getReviewStatusLabel(currentExpert?.reviewStatus) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- Edit Dialog -->
    <el-dialog v-model="editVisible" title="编辑专家" width="600px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="专家类型">
          <el-select v-model="editForm.expertType">
            <el-option value="TECH" label="技术类" />
            <el-option value="ECON" label="经济类" />
            <el-option value="LAW" label="法律类" />
            <el-option value="MGMT" label="管理类" />
          </el-select>
        </el-form-item>
        <el-form-item label="专家级别">
          <el-select v-model="editForm.expertLevel">
            <el-option value="SENIOR" label="高级" />
            <el-option value="INTERMEDIATE" label="中级" />
            <el-option value="JUNIOR" label="初级" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作单位">
          <el-input v-model="editForm.workUnit" />
        </el-form-item>
        <el-form-item label="职务">
          <el-input v-model="editForm.position" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- Portrait Dialog -->
    <el-dialog v-model="portraitVisible" title="专家画像" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="专家编号">{{ portrait?.expertNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ portrait?.name }}</el-descriptions-item>
        <el-descriptions-item label="专家类型">{{ getExpertTypeLabel(portrait?.expertType) }}</el-descriptions-item>
        <el-descriptions-item label="专家级别">{{ getExpertLevelLabel(portrait?.expertLevel) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">统计数据</el-divider>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="抽取次数" :value="portrait?.totalExtractions || 0" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="确认次数" :value="portrait?.confirmedCount || 0" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="平均得分" :value="portrait?.avgScore || 0" :precision="2" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="否决次数" :value="portrait?.vetoCount || 0" />
        </el-col>
      </el-row>

      <el-divider content-position="left">抽取记录</el-divider>
      <el-table :data="portrait?.extractionHistory || []" border size="small">
        <el-table-column prop="planNo" label="方案编号" width="120" />
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="extractionTime" label="抽取时间" width="180" />
      </el-table>

      <el-divider content-position="left">评分记录</el-divider>
      <el-table :data="portrait?.evaluationHistory || []" border size="small">
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="score" label="得分" width="80" />
        <el-table-column prop="evaluateTime" label="评分时间" width="180" />
        <el-table-column prop="comment" label="评语" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

interface Expert {
  id: number
  expertNo: string
  name: string
  phone: string
  email: string
  expertType: string
  expertLevel: string
  workUnit: string
  position: string
  status: string
  reviewStatus: string
}

interface Portrait {
  expertId: number
  expertNo: string
  name: string
  expertType: string
  expertLevel: string
  totalExtractions: number
  confirmedCount: number
  avgScore: number
  vetoCount: number
  extractionHistory: any[]
  evaluationHistory: any[]
}

const loading = ref(false)
const expertList = ref<Expert[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const queryForm = ref({ name: '', expertType: '', expertLevel: '', status: '' })

const detailVisible = ref(false)
const currentExpert = ref<Expert | null>(null)

const editVisible = ref(false)
const editForm = ref<any>({})

const portraitVisible = ref(false)
const portrait = ref<Portrait | null>(null)

const getExpertTypeLabel = (type: string | undefined) => {
  const map: Record<string, string> = { TECH: '技术类', ECON: '经济类', LAW: '法律类', MGMT: '管理类' }
  return map[type || ''] || type || ''
}

const getExpertLevelLabel = (level: string | undefined) => {
  const map: Record<string, string> = { SENIOR: '高级', INTERMEDIATE: '中级', JUNIOR: '初级', EXPERT: '资深' }
  return map[level || ''] || level || ''
}

const getStatusLabel = (status: string | undefined) => {
  const map: Record<string, string> = { NORMAL: '正常', POTENTIAL: '潜在', BLACKLIST: '黑名单' }
  return map[status || ''] || status || ''
}

const getReviewStatusLabel = (status: string | undefined) => {
  const map: Record<string, string> = { PENDING: '待审核', INIT_PASS: '初审通过', INIT_REJECT: '初审拒绝', RE_PASS: '复审通过', RE_REJECT: '复审拒绝' }
  return map[status || ''] || status || ''
}

const loadExperts = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('page', page.value.toString())
    params.append('size', size.value.toString())
    if (queryForm.value.name) params.append('name', queryForm.value.name)
    if (queryForm.value.expertType) params.append('expertType', queryForm.value.expertType)
    if (queryForm.value.expertLevel) params.append('expertLevel', queryForm.value.expertLevel)
    if (queryForm.value.status) params.append('status', queryForm.value.status)

    const res = await api.get(`/expert/list?${params}`)
    if (res.code === 200) {
      expertList.value = res.data.records
      total.value = res.data.total
    }
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.value = { name: '', expertType: '', expertLevel: '', status: '' }
  loadExperts()
}

const showDetail = (row: Expert) => {
  currentExpert.value = row
  detailVisible.value = true
}

const showEdit = (row: Expert) => {
  editForm.value = { ...row }
  editVisible.value = true
}

const saveEdit = async () => {
  try {
    const res = await api.put(`/expert/${editForm.value.id}`, editForm.value)
    if (res.code === 200) {
      ElMessage.success('专家信息已更新')
      editVisible.value = false
      loadExperts()
    }
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const showPortrait = async (row: Expert) => {
  try {
    const res = await api.get(`/expert/${row.id}/portrait`)
    if (res.code === 200) {
      portrait.value = res.data
      portraitVisible.value = true
    }
  } catch (e) {
    ElMessage.error('加载画像失败')
  }
}

const showPortraitDialog = () => {
  if (expertList.value.length > 0) {
    showPortrait(expertList.value[0])
  }
}

onMounted(() => {
  loadExperts()
})
</script>

<style scoped>
.expert-master-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>