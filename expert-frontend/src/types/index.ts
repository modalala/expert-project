// API响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: string
}

// 分页响应类型
export interface PageResult<T = any> {
  records: T[]
  total: number
  page: number
  size: number
}

// 分页请求参数
export interface PageParams {
  page: number
  size: number
}

// 用户信息
export interface UserInfo {
  id: number
  username: string
  realName: string
  phone?: string
  email?: string
  avatar?: string
  status: number
}

// 菜单项
export interface MenuItem {
  id: number
  permCode: string
  permName: string
  permType: number
  parentId: number
  path?: string
  icon?: string
  sortOrder: number
  children?: MenuItem[]
}

// 首页概览统计
export interface DashboardOverview {
  totalExperts: number
  currentMonthBids: number
  pendingReviewExperts: number
  ongoingExtractions: number
  normalExperts: number
  seniorExperts: number
}

// 图表分布项
export interface ChartDistributionItem {
  code: string
  name: string
  count: number
  percentage: number
}

// 月度趋势项
export interface MonthlyTrendItem {
  month: string
  monthLabel: string
  extractionCount: number
  bidCount: number
}