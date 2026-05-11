/**
 * Agent 类型定义
 */

// 工具定义
export interface AgentTool {
  name: string
  description: string
  params: string // 参数列表，如 "query, limit"
  handler?: (params: Record<string, any>) => Promise<string>
}

// 技能定义
export interface AgentSkill {
  name: string
  description: string
  trigger?: string // 触发条件
  enabled: boolean
}

// 用户记忆
export interface AgentMemory {
  type: 'user' | 'project' | 'feedback'
  name: string
  description: string
  content: string
  timestamp: number
}

// 动态上下文
export interface DynamicContext {
  date: string
  user?: {
    name: string
    role: string
  }
  project?: string
  model: string
}

// 构建结果
export interface BuiltPrompt {
  static: string // 可缓存的静态部分
  dynamic: string // 每轮更新的动态部分
  full: string // 完整 prompt
}