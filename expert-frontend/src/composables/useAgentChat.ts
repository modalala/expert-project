/**
 * Agent Chat 组合式函数
 *
 * 提供 Agent 初始化、消息发送、事件处理、记忆管理等核心功能
 * 可在其他聊天页面复用
 *
 * 注意：Agent 相关包需要从组件层面导入和初始化
 */

import { ref, shallowRef, type Ref, type ShallowRef } from 'vue'
import { SystemPromptBuilder, type AgentMemory, type DynamicContext } from '@/utils/agent'

// Agent 类型定义（避免直接导入缺失的包）
export interface AgentInstance {
  prompt: (content: string) => Promise<void>
  subscribe: (handler: (event: any) => void) => () => void
}

export interface AgentModelConfig {
  provider: string
  model: string
  apiKey: string
  thinkingLevel?: 'off' | 'low' | 'medium' | 'high'
}

export interface AgentInitOptions {
  systemPrompt: string
  modelConfig: AgentModelConfig
  tools?: any[]
}

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
  timestamp: number
  isStreaming?: boolean
}

interface UserInfo {
  name: string
  role: string
}

interface UseAgentChatOptions {
  userInfo?: UserInfo
  onMessageUpdate?: () => void
}

export function useAgentChat(options: UseAgentChatOptions = {}) {
  // 响应式状态
  const messages: Ref<ChatMessage[]> = ref([])
  const isLoading = ref(false)
  const streamingContent = ref('')
  const abortController: Ref<AbortController | null> = ref(null)

  // 内部状态
  const agent: ShallowRef<AgentInstance | null> = shallowRef(null)
  const promptBuilder: ShallowRef<SystemPromptBuilder | null> = shallowRef(null)
  let unsubscribe: (() => void) | null = null

  /**
   * 加载用户偏好记忆
   */
  const loadUserPreferences = () => {
    const savedPreferences = localStorage.getItem('agent-user-preferences')
    if (savedPreferences) {
      try {
        const prefs = JSON.parse(savedPreferences)
        for (const pref of prefs) {
          promptBuilder.value?.addMemory(pref as AgentMemory)
        }
      } catch (e) {
        console.warn('Failed to load user preferences:', e)
      }
    }
  }

  /**
   * 保存用户偏好到 localStorage
   */
  const saveUserPreferences = () => {
    if (!promptBuilder.value) return

    const prefs = JSON.parse(
      localStorage.getItem('agent-user-preferences') || '[]'
    )

    // 添加新偏好
    if (promptBuilder.value) {
      const memory = {
        type: 'feedback',
        name: '输出格式偏好',
        description: '用户偏好',
        content: '表格形式',
        timestamp: Date.now()
      }
      prefs.push(memory)
      localStorage.setItem('agent-user-preferences', JSON.stringify(prefs))
    }
  }

  /**
   * 检测并保存用户偏好
   */
  const detectAndSavePreference = (content: string) => {
    if (content.includes('表格') && content.includes('更喜欢')) {
      const memory: AgentMemory = {
        type: 'feedback',
        name: '输出格式偏好',
        description: '用户对输出格式的偏好',
        content: '用户偏好表格形式的数据展示',
        timestamp: Date.now()
      }
      promptBuilder.value?.addMemory(memory)
      saveUserPreferences()
    }
  }

  /**
   * 处理 Agent 事件
   */
  const handleEvent = (event: any) => {
    console.log('[Agent Event]', event.type, event) // 调试日志

    switch (event.type) {
      case 'agent_start':
        isLoading.value = true
        streamingContent.value = ''
        break

      case 'message_start':
        if (event.message?.role === 'assistant') {
          messages.value.push({
            role: 'assistant',
            content: '',
            timestamp: Date.now(),
            isStreaming: true
          })
          options.onMessageUpdate?.()
        }
        break

      case 'message_update':
        if (event.assistantMessageEvent?.type === 'text_delta') {
          const delta = event.assistantMessageEvent.delta || ''
          streamingContent.value += delta
          // 更新最后一条消息的内容
          const lastMsg = messages.value[messages.value.length - 1]
          if (lastMsg && lastMsg.role === 'assistant') {
            lastMsg.content = streamingContent.value
            options.onMessageUpdate?.()
          }
        }
        break

      case 'message_end':
        const completedMsg = messages.value[messages.value.length - 1]
        if (completedMsg && completedMsg.role === 'assistant') {
          completedMsg.content = streamingContent.value
          completedMsg.isStreaming = false
          detectAndSavePreference(completedMsg.content)
        }
        streamingContent.value = ''
        break

      case 'agent_end':
        isLoading.value = false
        options.onMessageUpdate?.()
        break

      case 'turn_end':
        // Turn 结束
        break

      default:
        console.log('[Unknown Event]', event.type)
    }
  }

  /**
   * 初始化 Prompt 构建器
   */
  const initPromptBuilder = (): SystemPromptBuilder => {
    promptBuilder.value = new SystemPromptBuilder()

    // 加载用户偏好
    loadUserPreferences()

    // 添加用户角色记忆
    if (options.userInfo) {
      promptBuilder.value.addMemory({
        type: 'user',
        name: '用户角色',
        description: '当前登录用户信息',
        content: `用户名: ${options.userInfo.name}\n角色: ${options.userInfo.role}`,
        timestamp: Date.now()
      })
    }

    return promptBuilder.value
  }

  /**
   * 设置 Agent 实例（由外部传入）
   */
  const setAgent = (agentInstance: AgentInstance): void => {
    agent.value = agentInstance
    unsubscribe = agentInstance.subscribe(handleEvent)
  }

  /**
   * 发送消息
   */
  const sendMessage = async (content: string): Promise<void> => {
    if (!content || isLoading.value || !agent.value) return

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content,
      timestamp: Date.now()
    })

    streamingContent.value = ''
    abortController.value = new AbortController()

    try {
      await agent.value.prompt(content)
    } catch (error: any) {
      if (error.name !== 'AbortError') {
        throw error
      }
    }
  }

  /**
   * 快捷发送
   */
  const sendQuickMessage = (text: string): Promise<void> => {
    return sendMessage(text)
  }

  /**
   * 停止生成
   */
  const abortMessage = (): void => {
    if (abortController.value) {
      abortController.value.abort()
      abortController.value = null
    }

    isLoading.value = false
    streamingContent.value = ''

    const lastMsg = messages.value[messages.value.length - 1]
    if (lastMsg && lastMsg.role === 'assistant' && lastMsg.isStreaming) {
      lastMsg.isStreaming = false
      if (!lastMsg.content) {
        lastMsg.content = '已停止生成'
      }
    }
  }

  /**
   * 添加记忆
   */
  const addMemory = (memory: AgentMemory): void => {
    promptBuilder.value?.addMemory(memory)
  }

  /**
   * 清除记忆
   */
  const clearMemory = (name?: string): void => {
    promptBuilder.value?.clearMemory(name)
  }

  /**
   * 销毁资源
   */
  const destroy = (): void => {
    if (unsubscribe) {
      unsubscribe()
      unsubscribe = null
    }
    if (abortController.value) {
      abortController.value.abort()
      abortController.value = null
    }
    agent.value = null
    promptBuilder.value = null
  }

  return {
    // 状态
    messages,
    isLoading,
    streamingContent,
    agent,
    promptBuilder,

    // 初始化
    initPromptBuilder,
    setAgent,

    // 操作
    sendMessage,
    sendQuickMessage,
    abortMessage,
    addMemory,
    clearMemory,
    destroy
  }
}