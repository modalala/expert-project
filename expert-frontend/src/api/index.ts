import request from '@/utils/request'
import type { ApiResponse, PageResult, PageParams, DashboardOverview, ChartDistributionItem, MonthlyTrendItem } from '@/types'

// 默认导出axios实例
export default request

// 测试接口
export const testApi = {
  getDbConn: () => request.get<any, ApiResponse>('/test/db-conn'),
  getInfo: () => request.get<any, ApiResponse>('/test/info')
}

// 认证接口
export const authApi = {
  login: (data: { username: string; password: string }) =>
    request.post<any, ApiResponse>('/auth/login', data),
  logout: () => request.post<any, ApiResponse>('/auth/logout'),
  refresh: () => request.post<any, ApiResponse>('/auth/refresh')
}

// 用户接口
export const userApi = {
  getList: (params: PageParams & { username?: string; realName?: string; status?: number }) =>
    request.get<any, ApiResponse<PageResult>>('/user/list', { params }),
  getDetail: (id: number) => request.get<any, ApiResponse>(`/user/${id}`),
  create: (data: any) => request.post<any, ApiResponse>('/user', data),
  update: (id: number, data: any) => request.put<any, ApiResponse>(`/user/${id}`, data),
  delete: (id: number) => request.delete<any, ApiResponse>(`/user/${id}`),
  resetPassword: (id: number, data: { password: string }) =>
    request.put<any, ApiResponse>(`/user/${id}/password`, data),
  toggleStatus: (id: number) => request.put<any, ApiResponse>(`/user/${id}/status`),
  getRoles: (id: number) => request.get<any, ApiResponse>(`/user/${id}/roles`),
  assignRoles: (id: number, data: { roleIds: number[] }) =>
    request.post<any, ApiResponse>(`/user/${id}/roles`, data)
}

// 角色接口
export const roleApi = {
  getList: () => request.get<any, ApiResponse>('/role/list'),
  getDetail: (id: number) => request.get<any, ApiResponse>(`/role/${id}`),
  create: (data: any) => request.post<any, ApiResponse>('/role', data),
  update: (id: number, data: any) => request.put<any, ApiResponse>(`/role/${id}`, data),
  delete: (id: number) => request.delete<any, ApiResponse>(`/role/${id}`),
  getPermissions: (id: number) => request.get<any, ApiResponse>(`/role/${id}/permissions`),
  assignPermissions: (id: number, data: { permissionIds: number[] }) =>
    request.post<any, ApiResponse>(`/role/${id}/permissions`, data)
}

// 专家接口
export const expertApi = {
  getList: (params: PageParams & { name?: string; type?: string; status?: string }) =>
    request.get<any, ApiResponse<PageResult>>('/expert/list', { params }),
  getDetail: (id: number) => request.get<any, ApiResponse>(`/expert/${id}`),
  register: (data: any) => request.post<any, ApiResponse>('/expert/register', data),
  updateStatus: (id: number, data: { status: string; reason?: string }) =>
    request.put<any, ApiResponse>(`/expert/${id}/status`, data)
}

// 首页统计接口
export const dashboardApi = {
  getOverview: () =>
    request.get<any, ApiResponse<DashboardOverview>>('/dashboard/overview'),
  getExpertTypeDistribution: () =>
    request.get<any, ApiResponse<ChartDistributionItem[]>>('/dashboard/expert-type-distribution'),
  getExpertLevelDistribution: () =>
    request.get<any, ApiResponse<ChartDistributionItem[]>>('/dashboard/expert-level-distribution'),
  getExpertStatusDistribution: () =>
    request.get<any, ApiResponse<ChartDistributionItem[]>>('/dashboard/expert-status-distribution'),
  getExpertSourceDistribution: () =>
    request.get<any, ApiResponse<ChartDistributionItem[]>>('/dashboard/expert-source-distribution'),
  getMonthlyBidTrend: (months: number = 6) =>
    request.get<any, ApiResponse<MonthlyTrendItem[]>>('/dashboard/monthly-bid-trend', { params: { months } })
}