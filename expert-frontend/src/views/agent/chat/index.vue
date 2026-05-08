<template>
  <div class="agent-chat-page">
    <!-- 消息列表区域 -->
    <div class="chat-messages" ref="messagesRef">
      <div class="messages-container">
        <!-- 欢迎消息 -->
        <div v-if="messages.length === 0" class="welcome-message">
          <div class="welcome-icon">
            <el-icon :size="48"><Cpu /></el-icon>
          </div>
          <h2>智能助手</h2>
          <p>我是您的专家库管理助手，可以帮助您查询专家信息、分析评标数据等。</p>
          <div class="quick-actions">
            <el-button @click="sendQuickMessage('查询专家信息')">查询专家信息</el-button>
            <el-button @click="sendQuickMessage('分析评标数据')">分析评标数据</el-button>
            <el-button @click="sendQuickMessage('解释系统功能')">系统功能介绍</el-button>
          </div>
        </div>

        <!-- 消息列表 -->
        <div v-for="(msg, index) in messages" :key="index" :class="['message-item', msg.role]">
          <div class="message-avatar">
            <el-avatar v-if="msg.role === 'user'" :size="36" class="user-avatar">
              {{ userInitial }}
            </el-avatar>
            <el-avatar v-else :size="36" class="assistant-avatar">
              <el-icon><Cpu /></el-icon>
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="message-header">
              <span class="role-name">{{ msg.role === 'user' ? '我' : '智能助手' }}</span>
              <span class="message-time">{{ formatTime(msg.timestamp) }}</span>
            </div>
            <div class="message-body">
              <div v-if="msg.role === 'assistant'" class="assistant-text">
                <!-- 流式输出时显示打字效果 -->
                <span v-if="msg.isStreaming" class="streaming-text">
                  {{ msg.content }}
                  <span class="typing-indicator">
                    <span></span><span></span><span></span>
                  </span>
                </span>
                <span v-else>{{ msg.content }}</span>
              </div>
              <div v-else class="user-text">{{ msg.content }}</div>
            </div>
          </div>
        </div>

        <!-- 加载指示器 -->
        <div v-if="isLoading && !streamingContent" class="loading-indicator">
          <div class="thinking-animation">
            <span></span><span></span><span></span>
          </div>
          <span class="thinking-text">正在思考...</span>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-area">
      <div class="input-container">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="2"
          :placeholder="isLoading ? '请等待回复...' : '输入您的消息...'"
          :disabled="isLoading"
          @keydown.enter.ctrl="sendMessage"
          class="message-input"
        />
        <div class="input-actions">
          <el-button
            type="primary"
            :disabled="!inputMessage.trim() || isLoading"
            @click="sendMessage"
            class="send-btn"
          >
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
          <el-button
            v-if="isLoading"
            type="danger"
            @click="abortMessage"
            class="stop-btn"
          >
            <el-icon><VideoPause /></el-icon>
            停止
          </el-button>
        </div>
      </div>
      <div class="input-hint">
        按 Ctrl + Enter 快速发送
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Cpu, Promotion, VideoPause } from '@element-plus/icons-vue'
import { Agent } from '@earendil-works/pi-agent-core'
import { getModel } from '@earendil-works/pi-ai'
import { useAppStore } from '@/stores/app'

// DeepSeek API Key
const DEEPSEEK_API_KEY = 'sk-a8b10cc7ee804798b1e40c5060d9fe6e'

interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
  timestamp: number
  isStreaming?: boolean
}

const appStore = useAppStore()
const messagesRef = ref<HTMLDivElement>()
const inputMessage = ref('')
const messages = ref<ChatMessage[]>([])
const isLoading = ref(false)
const streamingContent = ref('')
const abortController = ref<AbortController | null>(null)

let agent: Agent | null = null
let unsubscribe: (() => void) | null = null

// 用户名首字母
const userInitial = computed(() => {
  return appStore.userInfo?.realName?.charAt(0) || 'U'
})

// 初始化 Agent
onMounted(async () => {
  try {
    agent = new Agent({
      initialState: {
        systemPrompt: '你是一个专业的专家库管理助手，可以帮助用户查询专家信息、分析评标数据等。请用简洁、专业的中文回答问题。',
        model: getModel('deepseek', 'deepseek-v4-flash'),
        thinkingLevel: 'off',
        messages: [],
        tools: []
      },
      getApiKey: async (provider: string) => {
        if (provider === 'deepseek') {
          return DEEPSEEK_API_KEY
        }
        return undefined
      }
    })

    // 订阅 Agent 事件
    unsubscribe = agent.subscribe((event) => {
      handleAgentEvent(event)
    })
  } catch (error: any) {
    ElMessage.error('初始化助手失败: ' + error.message)
  }
})

onUnmounted(() => {
  if (unsubscribe) {
    unsubscribe()
  }
  if (abortController.value) {
    abortController.value.abort()
  }
})

// 处理 Agent 事件
const handleAgentEvent = (event: any) => {
  switch (event.type) {
    case 'agent_start':
      isLoading.value = true
      streamingContent.value = ''
      break

    case 'message_start':
      if (event.message?.role === 'assistant') {
        // 开始助手消息
        messages.value.push({
          role: 'assistant',
          content: '',
          timestamp: Date.now(),
          isStreaming: true
        })
        scrollToBottom()
      }
      break

    case 'message_update':
      // 流式更新内容
      if (event.assistantMessageEvent?.type === 'text_delta') {
        streamingContent.value += event.assistantMessageEvent.delta || ''
        // 更新最后一条消息的内容
        const lastMsg = messages.value[messages.value.length - 1]
        if (lastMsg && lastMsg.role === 'assistant') {
          lastMsg.content = streamingContent.value
          scrollToBottom()
        }
      }
      break

    case 'message_end':
      // 消息完成
      const lastMsg = messages.value[messages.value.length - 1]
      if (lastMsg && lastMsg.role === 'assistant') {
        lastMsg.content = streamingContent.value
        lastMsg.isStreaming = false
      }
      streamingContent.value = ''
      break

    case 'agent_end':
      isLoading.value = false
      scrollToBottom()
      break

    case 'turn_end':
      // Turn 结束
      break
  }
}

// 发送消息
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || isLoading.value || !agent) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content,
    timestamp: Date.now()
  })

  inputMessage.value = ''
  streamingContent.value = ''

  // 创建 AbortController
  abortController.value = new AbortController()

  try {
    await agent.prompt(content)
  } catch (error: any) {
    if (error.name !== 'AbortError') {
      ElMessage.error('发送失败: ' + error.message)
      isLoading.value = false
    }
  }
}

// 快捷发送
const sendQuickMessage = (text: string) => {
  inputMessage.value = text
  sendMessage()
}

// 停止生成
const abortMessage = () => {
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
  }
  isLoading.value = false
  streamingContent.value = ''

  // 更新最后一条消息状态
  const lastMsg = messages.value[messages.value.length - 1]
  if (lastMsg && lastMsg.role === 'assistant' && lastMsg.isStreaming) {
    lastMsg.isStreaming = false
    if (!lastMsg.content) {
      lastMsg.content = '已停止生成'
    }
  }
}

// 格式化时间
const formatTime = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}
</script>

<style scoped lang="scss">
.agent-chat-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 90px);
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;

  .messages-container {
    max-width: 800px;
    margin: 0 auto;
  }
}

.welcome-message {
  text-align: center;
  padding: 60px 20px;

  .welcome-icon {
    margin-bottom: 20px;
    color: #409eff;
  }

  h2 {
    font-size: 28px;
    color: #303133;
    margin-bottom: 12px;
  }

  p {
    color: #606266;
    font-size: 16px;
    margin-bottom: 30px;
  }

  .quick-actions {
    display: flex;
    gap: 12px;
    justify-content: center;

    .el-button {
      border-radius: 20px;
      padding: 10px 20px;
    }
  }
}

.message-item {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease;

  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
  }

  .message-avatar {
    flex-shrink: 0;

    .user-avatar {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      font-weight: bold;
    }

    .assistant-avatar {
      background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
      color: #fff;
    }
  }

  .message-content {
    flex: 1;
    min-width: 0;

    .message-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 6px;

      .role-name {
        font-weight: 500;
        color: #303133;
      }

      .message-time {
        font-size: 12px;
        color: #909399;
      }
    }

    .message-body {
      .user-text {
        background: #409eff;
        color: #fff;
        padding: 12px 16px;
        border-radius: 12px;
        border-radius: 12px 12px 12px 4px;
        word-break: break-word;
        line-height: 1.6;
      }

      .assistant-text {
        background: #fff;
        color: #303133;
        padding: 12px 16px;
        border-radius: 12px;
        border-radius: 12px 12px 4px 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        word-break: break-word;
        line-height: 1.6;

        .streaming-text {
          display: inline;

          .typing-indicator {
            display: inline-flex;
            gap: 2px;
            margin-left: 4px;

            span {
              width: 4px;
              height: 4px;
              background: #409eff;
              border-radius: 50%;
              animation: typing 1s infinite;

              &:nth-child(2) { animation-delay: 0.2s; }
              &:nth-child(3) { animation-delay: 0.4s; }
            }
          }
        }
      }
    }
  }

  &.user {
    .message-content {
      align-self: flex-end;

      .message-header {
        justify-content: flex-end;
      }

      .message-body {
        text-align: right;
      }
    }
  }

  &.assistant {
    .message-avatar {
      order: 1;
    }

    .message-content {
      order: 0;
    }
  }
}

@keyframes typing {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 1; }
}

.loading-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;

  .thinking-animation {
    display: flex;
    gap: 4px;

    span {
      width: 8px;
      height: 8px;
      background: #409eff;
      border-radius: 50%;
      animation: thinking 1.5s infinite ease-in-out;

      &:nth-child(1) { animation-delay: 0s; }
      &:nth-child(2) { animation-delay: 0.3s; }
      &:nth-child(3) { animation-delay: 0.6s; }
    }
  }

  .thinking-text {
    color: #909399;
    font-size: 14px;
  }
}

@keyframes thinking {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.chat-input-area {
  background: #fff;
  border-top: 1px solid #e4e7ed;
  padding: 16px 20px;

  .input-container {
    max-width: 800px;
    margin: 0 auto;
    display: flex;
    gap: 12px;

    .message-input {
      flex: 1;

      :deep(.el-textarea__inner) {
        border-radius: 12px;
        resize: none;
        font-size: 14px;
        line-height: 1.6;
        padding: 10px 14px;

        &:focus {
          border-color: #409eff;
          box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
        }
      }
    }

    .input-actions {
      display: flex;
      flex-direction: column;
      gap: 8px;

      .send-btn, .stop-btn {
        border-radius: 12px;
        padding: 10px 20px;
        height: auto;

        .el-icon {
          margin-right: 4px;
        }
      }
    }
  }

  .input-hint {
    max-width: 800px;
    margin: 8px auto 0;
    font-size: 12px;
    color: #909399;
    text-align: center;
  }
}
</style>