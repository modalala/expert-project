# Agent 模块文档

本模块包含 Agent 相关的工具函数和组合式函数。

## 目录结构

```
src/
├── utils/agent/                    # Agent 工具模块
│   ├── types.ts                   # 类型定义
│   ├── tools.ts                   # 工具定义
│   ├── systemPromptBuilder.ts     # System Prompt 分层构建器
│   ├── index.ts                   # 导出入口
│   └── README.md                  # 本文档
│
├── composables/                    # 组合式函数
│   ├── useAgentChat.ts            # Agent 聊天核心逻辑
│   ├── usePromptDebug.ts          # Prompt 调试功能
│   ├── index.ts                   # 导出入口
│   └── README.md                  # Composables 文档
│
└── views/agent/chat/               # 聊天页面
    └── index.vue                  # 聊天组件 (~120行 script)
```

---

## System Prompt 分层构建系统

### 架构设计

System Prompt 采用分层构建，而非单一字符串：

```
┌─────────────────────────────────────────────┐
│ Layer 1: Core - 核心指令                     │
│ Layer 2: Tools - 工具列表                    │
│ Layer 3: Skills - 技能列表                   │
│ Layer 4: Memory - 持久化记忆                 │
│ Layer 5: CLAUDE.md - 项目配置                │
├─────────────────────────────────────────────┤ ← DYNAMIC_BOUNDARY
│ Layer 6: Dynamic - 动态上下文 (每轮更新)      │
└─────────────────────────────────────────────┘
```

### 静态 vs 动态分离

| 部分 | 缓存策略 | 更新时机 |
|------|----------|----------|
| 1-5层 | 可跨轮次缓存 | 配置变化时 |
| 6层 | 不缓存 | **每轮重建** |

---

## Composables 使用指南

### useAgentChat - Agent 聊天核心

提供 Agent 初始化、消息发送、事件处理、记忆管理等核心功能。

```typescript
import { useAgentChat } from '@/composables'

const {
  messages,       // 消息列表 Ref
  isLoading,      // 加载状态 Ref
  streamingContent, // 流式内容 Ref
  promptBuilder,  // Prompt 构建器 Ref
  
  initAgent,      // 初始化 Agent
  sendMessage,    // 发送消息
  abortMessage,   // 停止生成
  addMemory,      // 添加记忆
  clearMemory,    // 清除记忆
  destroy         // 销毁 Agent
} = useAgentChat({
  apiKey: 'your-api-key',
  model: 'deepseek-v4-flash',
  provider: 'deepseek',
  userInfo: { name: '张三', role: '管理员' },
  onMessageUpdate: () => scrollToBottom()  // 消息更新回调
})

// 初始化
onMounted(async () => {
  await initAgent()
})

// 发送消息
await sendMessage('查询专家信息')

// 停止生成
abortMessage()

// 清理
onUnmounted(() => {
  destroy()
})
```

### usePromptDebug - 调试功能

提供调试面板状态管理，独立于核心 Agent 逻辑。

```typescript
import { usePromptDebug } from '@/composables'

const debug = usePromptDebug()

// 显示调试面板
debug.showDebug(promptBuilder.value, {
  date: '2024-05-10',
  model: 'deepseek-v4-flash',
  user: { name: '张三', role: '管理员' }
})

// 状态
debug.visible        // 对话框可见性
debug.currentPrompt  // 完整 Prompt
debug.staticPrompt   // 静态部分
debug.dynamicPrompt  // 动态部分
```

---

## SystemPromptBuilder 直接使用

### 基础使用

```typescript
import { SystemPromptBuilder } from '@/utils/agent'

// 创建构建器
const builder = new SystemPromptBuilder()

// 构建 prompt
const { static, dynamic, full } = builder.build({
  date: '2024-05-10',
  user: { name: '张三', role: '管理员' },
  model: 'deepseek-v4-flash'
})

// static - 可缓存的部分
// dynamic - 每轮更新的部分
// full - 完整 prompt
```

### 添加记忆

```typescript
// 用户偏好记忆
builder.addMemory({
  type: 'feedback',
  name: '输出格式偏好',
  description: '用户对输出格式的偏好',
  content: '用户偏好表格形式的数据展示',
  timestamp: Date.now()
})

// 项目状态记忆
builder.addMemory({
  type: 'project',
  name: '当前项目',
  description: '用户正在处理的项目',
  content: '项目名称: 专家库管理系统\n状态: 开发中',
  timestamp: Date.now()
})
```

### 自定义核心指令

```typescript
builder.setCorePrompt(`你是一个专业的专家库管理助手...`)
```

---

## 记忆类型

| 类型 | 用途 | 示例 |
|------|------|------|
| `user` | 用户信息 | 用户名、角色、权限 |
| `project` | 项目状态 | 当前任务、进度 |
| `feedback` | 用户偏好 | 输出格式、交互风格 |

---

## 工具定义

当前定义的工具：

| 工具 | 描述 |
|------|------|
| `query_expert` | 查询专家信息 |
| `query_project` | 查询评标项目 |
| `analyze_stats` | 统计分析 |
| `check_compliance` | 合规检查 |

后续可扩展更多工具并实现 handler 函数。

---

## 组件集成示例

```vue
<script setup lang="ts">
import { useAgentChat } from '@/composables/useAgentChat'
import { usePromptDebug } from '@/composables/usePromptDebug'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

// Agent 核心
const {
  messages,
  isLoading,
  sendMessage,
  abortMessage
} = useAgentChat({
  apiKey: import.meta.env.VITE_DEEPSEEK_API_KEY,
  model: 'deepseek-v4-flash',
  provider: 'deepseek',
  userInfo: appStore.userInfo
})

// 调试
const debug = usePromptDebug()

// UI 函数保持原位
const scrollToBottom = () => { ... }
const formatTime = (ts: number) => { ... }
</script>
```

---

## 后续优化方向

1. **Prompt Caching**: 利用静态/动态分离实现 API 层缓存
2. **CLAUDE.md 加载**: 从项目根目录加载配置文件
3. **智能记忆提取**: 自动从对话中提取用户偏好
4. **工具 Handler 实现**: 接入真实的后端 API