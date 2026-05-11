/**
 * Agent 工具定义
 *
 * 定义专家库助手可用的工具
 */

import type { AgentTool } from './types'

// 专家查询工具
export const queryExpertTool: AgentTool = {
  name: 'query_expert',
  description: '查询专家信息，包括资质、专业领域、评审记录',
  params: 'expertId, name, field',
  // 实际 handler 在运行时注入
}

// 项目查询工具
export const queryProjectTool: AgentTool = {
  name: 'query_project',
  description: '查询评标项目，包括抽取记录、评分详情、状态',
  params: 'projectId, status, dateRange',
}

// 统计分析工具
export const analyzeStatsTool: AgentTool = {
  name: 'analyze_stats',
  description: '统计分析专家活跃度、评分分布、合规报告',
  params: 'expertId, metric, timeRange',
}

// 合规检查工具
export const checkComplianceTool: AgentTool = {
  name: 'check_compliance',
  description: '检查专家回避关系、违规倾向、法规匹配',
  params: 'expertId, projectId',
}

// 注册所有工具
export const AGENT_TOOLS: AgentTool[] = [
  queryExpertTool,
  queryProjectTool,
  analyzeStatsTool,
  checkComplianceTool,
]

// 工具映射表（用于运行时查找）
export const TOOL_MAP = Object.fromEntries(
  AGENT_TOOLS.map(t => [t.name, t])
)