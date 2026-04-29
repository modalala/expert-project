<template>
  <div class="review-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专家初审</span>
          <el-tag type="warning">待审核: {{ total }}条</el-tag>
        </div>
      </template>

      <!-- Search Form -->
      <el-form :model="queryForm" inline class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="专家姓名" clearable />
        </el-form-item>
        <el-form-item label="专家类型">
          <el-select v-model="queryForm.expertType" placeholder="全部" clearable>
            <el-option value="TECH" label="技术类" />
            <el-option value="ECON" label="经济类" />
            <el-option value="LAW" label="法律类" />
            <el-option value="MGMT" label="管理类" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadReviewList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- Review List Table -->
      <el-table :data="reviewList" v-loading="loading" border stripe>
        <el-table-column prop="expertNo" label="专家编号" width="130" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="expertType" label="专家类型" width="80">
          <template #default="{ row }">
            <el-tag>{{ getExpertTypeLabel(row.expertType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expertLevel" label="专家级别" width="100">
          <template #default="{ row }">
            {{ getExpertLevelLabel(row.expertLevel) }}
          </template>
        </el-table-column>
        <el-table-column prop="workUnit" label="工作单位" show-overflow-tooltip />
        <el-table-column prop="source" label="来源" width="80">
          <template #default="{ row }">
            <el-tag type="info">{{ getSourceLabel(row.source) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showDetail(row)">查看</el-button>
            <el-button type="success" size="small" @click="handlePass(row)">通过</el-button>
            <el-button type="danger" size="small" @click="handleReject(row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadReviewList"
      />
    </el-card>

    <!-- Detail Dialog -->
    <el-dialog v-model="detailVisible" title="专家详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ currentExpert?.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentExpert?.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentExpert?.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentExpert?.email }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentExpert?.idCard }}</el-descriptions-item>
        <el-descriptions-item label="专家类型">{{ getExpertTypeLabel(currentExpert?.expertType) }}</el-descriptions-item>
        <el-descriptions-item label="专家级别">{{ getExpertLevelLabel(currentExpert?.expertLevel) }}</el-descriptions-item>
        <el-descriptions-item label="擅长领域">{{ currentExpert?.expertiseAreas }}</el-descriptions-item>
        <el-descriptions-item label="工作单位">{{ currentExpert?.workUnit }}</el-descriptions-item>
        <el-descriptions-item label="职务">{{ currentExpert?.position }}</el-descriptions-item>
        <el-descriptions-item label="简介" :span="2">{{ currentExpert?.introduction }}</el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="success" @click="handlePassFromDetail">通过</el-button>
        <el-button type="danger" @click="handleRejectFromDetail">拒绝</el-button>
      </template>
    </el-dialog>

    <!-- Reject Dialog -->
    <el-dialog v-model="rejectVisible" title="审核拒绝" width="500px">
      <el-form :model="rejectForm" label-width="100px">
        <el-form-item label="拒绝原因">
          <el-select v-model="rejectForm.rejectReason" placeholder="选择拒绝原因">
            <el-option value="QUALIFICATION_NOT_MEET" label="资质不符合要求" />
            <el-option value="CERTIFICATE_MISSING" label="证书材料缺失" />
            <el-option value="EXPERIENCE_NOT_ENOUGH" label="工作经验不足" />
            <el-option value="OTHER" label="其他原因" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="rejectForm.comment" type="textarea" :rows="3" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="submitReject">确认拒绝</el-button>
      </template>
    </el-dialog>

    <!-- Pass Dialog -->
    <el-dialog v-model="passVisible" title="审核通过" width="500px">
      <el-form :model="passForm" label-width="100px">
        <el-form-item label="审核意见">
          <el-input v-model="passForm.comment" type="textarea" :rows="3" placeholder="请输入审核意见（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passVisible = false">取消</el-button>
        <el-button type="success" @click="submitPass">确认通过</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface ReviewItem {
  expertId: number
  expertNo: string
  name: string
  phone: string
  expertType: string
  expertLevel: string
  workUnit: string
  reviewStatus: string
  source: string
  createTime: string
}

interface ExpertDetail {
  id: number
  name: string
  gender: number
  phone: string
  email: string
  idCard: string
  expertType: string
  expertLevel: string
  expertiseAreas: string
  workUnit: string
  position: string
  introduction: string
}

const loading = ref(false)
const reviewList = ref<ReviewItem[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const queryForm = ref({ name: '', expertType: '' })

const detailVisible = ref(false)
const currentExpert = ref<ExpertDetail | null>(null)
const currentReviewItem = ref<ReviewItem | null>(null)

const rejectVisible = ref(false)
const rejectForm = ref({ rejectReason: '', comment: '' })

const passVisible = ref(false)
const passForm = ref({ comment: '' })

const getExpertTypeLabel = (type: string | undefined) => {
  const map: Record<string, string> = {
    TECH: '技术类',
    ECON: '经济类',
    LAW: '法律类',
    MGMT: '管理类'
  }
  return map[type || ''] || type
}

const getExpertLevelLabel = (level: string | undefined) => {
  const map: Record<string, string> = {
    JUNIOR: '初级',
    INTERMEDIATE: '中级',
    SENIOR: '高级',
    EXPERT: '资深'
  }
  return map[level || ''] || level
}

const getSourceLabel = (source: string | undefined) => {
  const map: Record<string, string> = {
    PUBLIC: '公开申请',
    INTERNAL: '内部推荐'
  }
  return map[source || ''] || source
}

const loadReviewList = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('reviewType', 'INIT')
    params.append('page', page.value.toString())
    params.append('size', size.value.toString())
    if (queryForm.value.name) params.append('name', queryForm.value.name)
    if (queryForm.value.expertType) params.append('expertType', queryForm.value.expertType)

    const res: any = await api.get(`/review/list?${params}`)
    if (res.code === 200) {
      reviewList.value = res.data.records
      total.value = res.data.total
    }
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.value = { name: '', expertType: '' }
  loadReviewList()
}

const showDetail = async (row: ReviewItem) => {
  try {
    const res: any = await api.get(`/review/${row.expertId}`)
    if (res.code === 200) {
      currentExpert.value = res.data
      currentReviewItem.value = row
      detailVisible.value = true
    }
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}

const handlePass = (row: ReviewItem) => {
  currentReviewItem.value = row
  passForm.value = { comment: '' }
  passVisible.value = true
}

const handlePassFromDetail = () => {
  passForm.value = { comment: '' }
  passVisible.value = true
}

const submitPass = async () => {
  if (!currentReviewItem.value) return

  try {
    const res: any = await api.post(`/review/${currentReviewItem.value.expertId}/pass`, passForm.value)
    if (res.code === 200) {
      ElMessage.success('审核通过成功')
      passVisible.value = false
      detailVisible.value = false
      loadReviewList()
    } else {
      ElMessage.error(res.message || '审核失败')
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleReject = (row: ReviewItem) => {
  currentReviewItem.value = row
  rejectForm.value = { rejectReason: '', comment: '' }
  rejectVisible.value = true
}

const handleRejectFromDetail = () => {
  rejectForm.value = { rejectReason: '', comment: '' }
  rejectVisible.value = true
}

const submitReject = async () => {
  if (!currentReviewItem.value) return
  if (!rejectForm.value.rejectReason) {
    ElMessage.warning('请选择拒绝原因')
    return
  }

  try {
    const res: any = await api.post(`/review/${currentReviewItem.value.expertId}/reject`, rejectForm.value)
    if (res.code === 200) {
      ElMessage.success('审核拒绝成功')
      rejectVisible.value = false
      detailVisible.value = false
      loadReviewList()
    } else {
      ElMessage.error(res.message || '审核失败')
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadReviewList()
})
</script>

<style scoped>
.review-container {
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