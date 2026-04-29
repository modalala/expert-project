<template>
  <div class="dashboard-page">
    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card-wrapper" v-loading="loadingOverview">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409EFF;">
              <el-icon><Avatar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview?.totalExperts || 0 }}</div>
              <div class="stat-label">专家总数</div>
              <div class="stat-sub">正常: {{ overview?.normalExperts || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card-wrapper" v-loading="loadingOverview">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67C23A;">
              <el-icon><Finished /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview?.currentMonthBids || 0 }}</div>
              <div class="stat-label">本月评标</div>
              <div class="stat-sub">高级专家: {{ overview?.seniorExperts || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card-wrapper" v-loading="loadingOverview">
          <div class="stat-card">
            <div class="stat-icon" style="background: #E6A23C;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview?.pendingReviewExperts || 0 }}</div>
              <div class="stat-label">待审核专家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card-wrapper" v-loading="loadingOverview">
          <div class="stat-card">
            <div class="stat-icon" style="background: #F56C6C;">
              <el-icon><Tickets /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview?.ongoingExtractions || 0 }}</div>
              <div class="stat-label">进行中抽取</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 - 第一行 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="6">
        <el-card shadow="hover" v-loading="loadingType">
          <PieChart
            :data="expertTypeDistribution"
            title="专家类型分布"
            height="280px"
            :colors="['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']"
            @click="handleTypeClick"
          />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" v-loading="loadingLevel">
          <PieChart
            :data="expertLevelDistribution"
            title="专家级别分布"
            height="280px"
            :colors="['#5470c6', '#91cc75', '#fac858', '#ee6666']"
            @click="handleLevelClick"
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" v-loading="loadingTrend">
          <LineChart
            :data="monthlyTrend"
            title="月度评标趋势"
            height="280px"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 - 第二行 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="6">
        <el-card shadow="hover" v-loading="loadingStatus">
          <BarChart
            :data="expertStatusDistribution"
            title="专家状态分布"
            height="280px"
            :colors="['#67C23A', '#E6A23C', '#F56C6C', '#909399']"
            @click="handleStatusClick"
          />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" v-loading="loadingSource">
          <PieChart
            :data="expertSourceDistribution"
            title="专家来源分布"
            height="280px"
            :colors="['#409EFF', '#67C23A']"
            @click="handleSourceClick"
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>快速操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/expert/register')">
              <el-icon><Plus /></el-icon> 专家注册
            </el-button>
            <el-button type="success" @click="$router.push('/extraction/plan')">
              <el-icon><DocumentCopy /></el-icon> 创建方案
            </el-button>
            <el-button type="warning" @click="$router.push('/expert/review')">
              <el-icon><Edit /></el-icon> 审核专家
            </el-button>
            <el-button type="info" @click="$router.push('/bid/committee')">
              <el-icon><Tickets /></el-icon> 评标管理
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Avatar, Finished, Document, Tickets, Plus, DocumentCopy, Edit } from '@element-plus/icons-vue'
import { dashboardApi } from '@/api'
import PieChart from '@/components/charts/PieChart.vue'
import BarChart from '@/components/charts/BarChart.vue'
import LineChart from '@/components/charts/LineChart.vue'
import type { DashboardOverview, ChartDistributionItem, MonthlyTrendItem } from '@/types'

const router = useRouter()

const overview = ref<DashboardOverview | null>(null)
const expertTypeDistribution = ref<ChartDistributionItem[]>([])
const expertLevelDistribution = ref<ChartDistributionItem[]>([])
const expertStatusDistribution = ref<ChartDistributionItem[]>([])
const expertSourceDistribution = ref<ChartDistributionItem[]>([])
const monthlyTrend = ref<MonthlyTrendItem[]>([])

const loadingOverview = ref(false)
const loadingType = ref(false)
const loadingLevel = ref(false)
const loadingStatus = ref(false)
const loadingSource = ref(false)
const loadingTrend = ref(false)

const loadOverview = async () => {
  loadingOverview.value = true
  try {
    const res = await dashboardApi.getOverview()
    overview.value = res.data
  } catch (e) {
    ElMessage.error('加载概览数据失败')
  } finally {
    loadingOverview.value = false
  }
}

const loadExpertTypeDistribution = async () => {
  loadingType.value = true
  try {
    const res = await dashboardApi.getExpertTypeDistribution()
    expertTypeDistribution.value = res.data
  } catch (e) {
    ElMessage.error('加载类型分布失败')
  } finally {
    loadingType.value = false
  }
}

const loadExpertLevelDistribution = async () => {
  loadingLevel.value = true
  try {
    const res = await dashboardApi.getExpertLevelDistribution()
    expertLevelDistribution.value = res.data
  } catch (e) {
    ElMessage.error('加载级别分布失败')
  } finally {
    loadingLevel.value = false
  }
}

const loadExpertStatusDistribution = async () => {
  loadingStatus.value = true
  try {
    const res = await dashboardApi.getExpertStatusDistribution()
    expertStatusDistribution.value = res.data
  } catch (e) {
    ElMessage.error('加载状态分布失败')
  } finally {
    loadingStatus.value = false
  }
}

const loadExpertSourceDistribution = async () => {
  loadingSource.value = true
  try {
    const res = await dashboardApi.getExpertSourceDistribution()
    expertSourceDistribution.value = res.data
  } finally {
    loadingSource.value = false
  }
}

const loadMonthlyTrend = async () => {
  loadingTrend.value = true
  try {
    const res = await dashboardApi.getMonthlyBidTrend(6)
    monthlyTrend.value = res.data
  } catch (e) {
    ElMessage.error('加载月度趋势失败')
  } finally {
    loadingTrend.value = false
  }
}

const handleTypeClick = (code: string) => {
  router.push({ path: '/expert/master', query: { expertType: code } })
}

const handleLevelClick = (code: string) => {
  router.push({ path: '/expert/master', query: { expertLevel: code } })
}

const handleStatusClick = (code: string) => {
  router.push({ path: '/expert/master', query: { status: code } })
}

const handleSourceClick = (code: string) => {
  router.push({ path: '/expert/master', query: { source: code } })
}

const loadAllData = async () => {
  await Promise.all([
    loadOverview(),
    loadExpertTypeDistribution(),
    loadExpertLevelDistribution(),
    loadExpertStatusDistribution(),
    loadExpertSourceDistribution(),
    loadMonthlyTrend()
  ])
}

onMounted(() => {
  loadAllData()
})
</script>

<style scoped lang="scss">
.dashboard-page {
  padding: 20px;

  .stat-row {
    margin-bottom: 20px;
  }

  .stat-card-wrapper {
    .stat-card {
      display: flex;
      align-items: center;
      gap: 16px;
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 28px;
    }

    .stat-info {
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
      }
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
      .stat-sub {
        font-size: 12px;
        color: #C0C4CC;
        margin-top: 4px;
      }
    }
  }

  .chart-row {
    margin-bottom: 20px;
  }

  .quick-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    padding: 20px;

    .el-button {
      flex: 1;
      min-width: 140px;
    }
  }
}
</style>