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
                <!-- Markdown 渲染 -->
                <div v-else class="markdown-content" v-html="renderMarkdown(msg.content)"></div>
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
          @keydown.enter.ctrl="handleSend"
          class="message-input"
        />
        <div class="input-actions">
          <el-button
            type="primary"
            :disabled="!inputMessage.trim() || isLoading"
            @click="handleSend"
            class="send-btn"
          >
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
          <el-button
            v-if="isLoading"
            type="danger"
            @click="handleAbort"
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
import { ref, computed, nextTick, onMounted, onUnmounted } from 'vue'
import { Cpu, Promotion, VideoPause } from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app'
import { useAgentChat } from '@/composables/useAgentChat'
import { Agent } from '@earendil-works/pi-agent-core'
import { getModel } from '@earendil-works/pi-ai'
import { marked } from 'marked'
import type { DynamicContext } from '@/utils/agent'

// 配置 marked 选项
marked.setOptions({
  breaks: true, // 支持 GitHub 风格的换行
  gfm: true // 支持 GitHub 风格的 markdown
})

// Markdown 渲染函数
const renderMarkdown = (content: string) => {
  return marked.parse(content) as string
}

const appStore = useAppStore()
const messagesRef = ref<HTMLDivElement>()
const inputMessage = ref('')

// ========== UI 辅助函数 ==========

// 滚动到底部（需要先定义，以便传递给 useAgentChat）
const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

// Agent 核心逻辑（不直接依赖 Agent 包）
const {
  messages,
  isLoading,
  streamingContent,
  promptBuilder,
  initPromptBuilder,
  setAgent,
  sendMessage,
  abortMessage,
  destroy
} = useAgentChat({
  userInfo: appStore.userInfo ? {
    name: appStore.userInfo.realName || appStore.userInfo.username,
    role: appStore.userInfo.role || '普通用户'
  } : undefined,
  onMessageUpdate: scrollToBottom
})

// 用户名首字母
const userInitial = computed(() => {
  return appStore.userInfo?.realName?.charAt(0) || 'U'
})

// 格式化时间
const formatTime = (timestamp: number) => {
  return new Date(timestamp).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// ========== Agent 初始化（在组件层面处理） ==========

const API_KEY = import.meta.env.VITE_DEEPSEEK_API_KEY || 'sk-503bccf39b6d4e4d9e00a19bc7eea56b'
const MODEL = 'deepseek-v4-flash'  // 正确的模型名称
const PROVIDER = 'deepseek'

let unsubscribe: (() => void) | null = null

onMounted(async () => {
  try {
    // 1. 初始化 Prompt 构建器
    const builder = initPromptBuilder()
    console.log('[Init] PromptBuilder initialized')

    // 2. 构建动态上下文
    const context: DynamicContext = {
      date: new Date().toISOString().split('T')[0],
      model: MODEL,
      user: appStore.userInfo ? {
        name: appStore.userInfo.realName || appStore.userInfo.username,
        role: appStore.userInfo.role || '普通用户'
      } : undefined
    }

    // 3. 构建 System Prompt
    const { full } = builder.build(context)
    console.log('[Init] System Prompt length:', full.length)

    // 4. 获取模型配置
    const model = getModel(PROVIDER, MODEL)
    if (!model) {
      throw new Error(`Model "${MODEL}" not found for provider "${PROVIDER}"`)
    }
    console.log('[Init] Model config:', model)

    // 5. 创建 Agent
    const agent = new Agent({
      initialState: {
        systemPrompt: full,
        model: model,
        thinkingLevel: 'high',  // deepseek-v4-flash 支持的 level
        messages: [],
        tools: []
      },
      getApiKey: async (provider: string) => {
        console.log('[Init] getApiKey called for:', provider)
        if (provider === PROVIDER) {
          return API_KEY
        }
        return undefined
      }
    })
    console.log('[Init] Agent created')

    // 6. 设置 Agent 到 composable
    setAgent(agent)
    console.log('[Init] Agent set to composable - Ready!')

  } catch (error: any) {
    console.error('[Init Error]', error)
  }
})

onUnmounted(() => {
  destroy()
})

// ========== 用户交互 ==========

// 发送消息
const handleSend = async () => {
  const content = inputMessage.value.trim()
  if (!content) return

  console.log('[Send] Message:', content)
  console.log('[Send] Agent exists:', !!promptBuilder.value)

  inputMessage.value = ''
  scrollToBottom()

  try {
    await sendMessage(content)
    console.log('[Send] Message sent successfully')
  } catch (error: any) {
    console.error('[Send Error]', error)
  }
}

// 快捷发送
const sendQuickMessage = (text: string) => {
  inputMessage.value = text
  handleSend()
}

// 停止生成
const handleAbort = () => {
  abortMessage()
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

        // Markdown 内容样式
        .markdown-content {
          // 标题样式
          h1, h2, h3, h4, h5, h6 {
            margin: 16px 0 8px 0;
            font-weight: 600;
            color: #303133;
          }

          h1 { font-size: 20px; }
          h2 { font-size: 18px; border-bottom: 1px solid #e4e7ed; padding-bottom: 4px; }
          h3 { font-size: 16px; }
          h4 { font-size: 14px; }

          // 段落
          p {
            margin: 8px 0;
            line-height: 1.8;
          }

          // 表格样式
          table {
            width: 100%;
            border-collapse: collapse;
            margin: 12px 0;

            th, td {
              border: 1px solid #dcdfe6;
              padding: 8px 12px;
              text-align: left;
            }

            th {
              background: #f5f7fa;
              font-weight: 600;
              color: #303133;
            }

            tr:nth-child(even) td {
              background: #fafafa;
            }
          }

          // 代码块样式
          pre {
            background: #282c34;
            color: #abb2bf;
            padding: 12px 16px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 12px 0;
            font-family: 'Consolas', 'Monaco', monospace;
            font-size: 13px;
            line-height: 1.5;

            code {
              background: transparent;
              padding: 0;
              color: inherit;
            }
          }

          // 行内代码
          code {
            background: #f5f7fa;
            color: #e96900;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Consolas', 'Monaco', monospace;
            font-size: 13px;
          }

          // 列表样式
          ul, ol {
            margin: 8px 0;
            padding-left: 20px;

            li {
              margin: 4px 0;
              line-height: 1.8;
            }
          }

          // 引用样式
          blockquote {
            border-left: 4px solid #409eff;
            margin: 12px 0;
            padding: 8px 16px;
            background: #f0f7ff;
            color: #606266;

            p {
              margin: 0;
            }
          }

          // 分割线
          hr {
            border: none;
            border-top: 1px solid #e4e7ed;
            margin: 16px 0;
          }

          // 强调
          strong {
            font-weight: 600;
            color: #303133;
          }

          em {
            color: #606266;
          }

          // 链接
          a {
            color: #409eff;
            text-decoration: none;

            &:hover {
              text-decoration: underline;
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