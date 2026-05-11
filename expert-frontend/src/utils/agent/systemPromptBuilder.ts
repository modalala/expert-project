/**
 * System Prompt 分层构建器
 *
 * 核心设计：System Prompt 是管道，不是字符串
 *
 * 分层结构：
 * 1. Core - 核心指令（极少变化）
 * 2. Tools - 工具列表（添加工具时变化）
 * 3. Skills - 技能列表（添加技能时变化）
 * 4. Memory - 持久化记忆（用户偏好、项目状态）
 * 5. CLAUDE.md - 项目配置（编辑时变化）
 * 6. Dynamic - 动态上下文（每轮更新）
 *
 * 静态部分 (1-5) 可以缓存，动态部分 (6) 每轮重建
 */

import type { AgentTool, AgentSkill, AgentMemory, DynamicContext, BuiltPrompt } from './types'
import { AGENT_TOOLS } from './tools'

// 动态边界标记
const DYNAMIC_BOUNDARY = '=== DYNAMIC_BOUNDARY ==='

export class SystemPromptBuilder {
  private tools: AgentTool[]
  private skills: AgentSkill[]
  private memories: AgentMemory[]
  private customCore?: string

  constructor(
    tools: AgentTool[] = AGENT_TOOLS,
    skills: AgentSkill[] = [],
    memories: AgentMemory[] = []
  ) {
    this.tools = tools
    this.skills = skills
    this.memories = memories
  }

  /**
   * 设置自定义核心指令
   */
  setCorePrompt(prompt: string): void {
    this.customCore = prompt
  }

  /**
   * 添加记忆
   */
  addMemory(memory: AgentMemory): void {
    // 检查是否已存在同名记忆
    const existing = this.memories.findIndex(m => m.name === memory.name)
    if (existing >= 0) {
      this.memories[existing] = memory
    } else {
      this.memories.push(memory)
    }
  }

  /**
   * 清除记忆
   */
  clearMemory(name?: string): void {
    if (name) {
      this.memories = this.memories.filter(m => m.name !== name)
    } else {
      this.memories = []
    }
  }

  // ========== 静态部分构建 ==========

  /**
   * Layer 1: 核心指令
   */
  private buildCore(): string {
    if (this.customCore) {
      return this.customCore
    }

    return `你是专家库管理助手 — 熟悉招投标法规、专家评审流程的专业顾问。

## 你的能力

你可以访问专家库系统的核心数据：
- 专家信息（资质、专业领域、评审记录）
- 评标项目（抽取记录、评分详情、回避情况）
- 统计分析（专家活跃度、评分分布、合规报告）

## CRITICAL: 数据驱动回答

不要泛泛而谈。当用户提问时，主动查询数据支撑回答：

- **"张三专家的评审情况"** → query_expert → analyze_stats → 给出统计数据
- **"最近有哪些项目需要专家"** → query_project → 显示时间、专业要求、状态
- **"这个专家是否合规"** → check_compliance → 发现回避关系 → 提示风险

## 响应风格

- **数据优先。** "张三专家参与过 12 次评审，平均评分 4.8 分" 而非 "张三很有经验"
- **法规引用。** 涉及合规问题时引用具体条款
- **风险提示。** 发现回避关系、违规倾向时明确指出
- **简洁专业。** 用表格、列表呈现数据，避免大段文字

## What NOT To Do

- 不要说"让我查询一下" — 直接给出结果
- 不要编造数据 — 查不到就明说
- 不要问"需要我帮您做什么" — 每句话要有信息量
- 不要说"根据查询结果" — 直接陈述事实
- 不要重复相同信息填充字数

你的目标是帮助用户做出准确的专家管理决策。`
  }

  /**
   * Layer 2: 工具列表
   */
  private buildTools(): string {
    if (!this.tools.length) return ''

    const lines = ['# 可用工具']
    for (const tool of this.tools) {
      lines.push(`- **${tool.name}**(${tool.params}): ${tool.description}`)
    }
    return lines.join('\n')
  }

  /**
   * Layer 3: 技能列表
   */
  private buildSkills(): string {
    if (!this.skills.length) return ''

    const lines = ['# 可用技能']
    for (const skill of this.skills) {
      if (!skill.enabled) continue
      lines.push(`- **${skill.name}**: ${skill.description}`)
      if (skill.trigger) {
        lines.push(`  触发: ${skill.trigger}`)
      }
    }
    return lines.join('\n')
  }

  /**
   * Layer 4: 持久化记忆
   */
  private buildMemory(): string {
    if (!this.memories.length) return ''

    const lines = ['# 持久化记忆']
    const grouped: Record<string, AgentMemory[]> = {}

    for (const mem of this.memories) {
      if (!grouped[mem.type]) grouped[mem.type] = []
      grouped[mem.type].push(mem)
    }

    for (const [type, mems] of Object.entries(grouped)) {
      lines.push(`\n## ${type} 类型`)
      for (const mem of mems) {
        lines.push(`### ${mem.name}`)
        lines.push(mem.description)
        lines.push(mem.content)
      }
    }

    return lines.join('\n')
  }

  /**
   * Layer 5: 项目配置（可从外部加载）
   */
  private buildClaudeMd(): string {
    // 在前端环境，可以从 localStorage 或 API 加载
    const projectConfig = localStorage.getItem('agent-project-config')
    if (!projectConfig) return ''

    return `# 项目配置\n${projectConfig}`
  }

  // ========== 动态部分构建 ==========

  /**
   * Layer 6: 动态上下文
   */
  private buildDynamic(context?: DynamicContext): string {
    const date = new Date().toISOString().split('T')[0]

    const lines = [
      '# 当前上下文',
      `当前日期: ${date}`,
    ]

    if (context?.user) {
      lines.push(`当前用户: ${context.user.name} (${context.user.role})`)
    }

    if (context?.project) {
      lines.push(`当前项目: ${context.project}`)
    }

    if (context?.model) {
      lines.push(`当前模型: ${context.model}`)
    }

    return lines.join('\n')
  }

  // ========== 组装方法 ==========

  /**
   * 构建静态部分（可缓存）
   */
  buildStatic(): string {
    const sections = [
      this.buildCore(),
      this.buildTools(),
      this.buildSkills(),
      this.buildMemory(),
      this.buildClaudeMd(),
    ]

    return sections.filter(Boolean).join('\n\n')
  }

  /**
   * 构建完整 Prompt
   */
  build(context?: DynamicContext): BuiltPrompt {
    const staticPart = this.buildStatic()
    const dynamicPart = this.buildDynamic(context)

    return {
      static: staticPart,
      dynamic: dynamicPart,
      full: `${staticPart}\n\n${DYNAMIC_BOUNDARY}\n\n${dynamicPart}`
    }
  }

  /**
   * 构建每轮提醒（作为 user 消息注入）
   */
  buildReminder(extra: string): string {
    return `<system-reminder>\n${extra}\n</system-reminder>`
  }

  /**
   * 获取边界标记位置（用于调试）
   */
  getBoundaryPosition(prompt: string): number {
    return prompt.indexOf(DYNAMIC_BOUNDARY)
  }
}

// 默认实例
export const defaultPromptBuilder = new SystemPromptBuilder()