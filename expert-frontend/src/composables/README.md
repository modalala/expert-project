# Composables 组合式函数

本目录包含可复用的 Vue 组合式函数。

## 文件列表

| 文件 | 用途 | 行数 |
|------|------|------|
| `useAgentChat.ts` | Agent 聊天核心逻辑 | ~315 |
| `usePromptDebug.ts` | Prompt 调试功能 | ~99 |
| `index.ts` | 导出入口 | - |

---

## useAgentChat

Agent 聊天核心功能，可在多个聊天页面复用。

### 功能列表

| 功能 | 说明 |
|------|------|
| `initAgent` | 初始化 Agent 和 Prompt 构建器 |
| `sendMessage` | 发送消息 |
| `abortMessage` | 停止生成 |
| `addMemory` | 添加持久化记忆 |
| `clearMemory` | 清除记忆 |
| `destroy` | 销毁 Agent，清理资源 |

### 状态

| 状态 | 类型 | 说明 |
|------|------|------|
| `messages` | `Ref<ChatMessage[]>` | 消息列表 |
| `isLoading` | `Ref<boolean>` | 加载状态 |
| `streamingContent` | `Ref<string>` | 当前流式内容 |
| `promptBuilder` | `Ref<SystemPromptBuilder>` | Prompt 构建器 |

### 使用示例

```typescript
import { useAgentChat } from '@/composables'

const chat = useAgentChat({
  apiKey: import.meta.env.VITE_DEEPSEEK_API_KEY,
  model: 'deepseek-v4-flash',
  provider: 'deepseek',
  userInfo: { name: '张三', role: '管理员' },
  onMessageUpdate: () => scrollToBottom()
})

// 初始化
await chat.initAgent()

// 发送消息
await chat.sendMessage('查询专家信息')

// 停止生成
chat.abortMessage()

// 清理
chat.destroy()
```

### 配置选项

```typescript
interface UseAgentChatOptions {
  apiKey: string           // API Key
  model: string            // 模型名称
  provider: string         // 提供商 (deepseek, openai, etc.)
  userInfo?: UserInfo      // 用户信息（可选）
  onMessageUpdate?: () => void  // 消息更新回调（可选）
}
```

---

## usePromptDebug

Prompt 调试功能，独立于核心逻辑。

### 功能列表

| 功能 | 说明 |
|------|------|
| `showDebug` | 显示调试面板 |
| `hideDebug` | 隐藏调试面板 |
| `toggleDebug` | 切换显示状态 |
| `refresh` | 刷新 Prompt 内容 |

### 状态

| 状态 | 类型 | 说明 |
|------|------|------|
| `visible` | `Ref<boolean>` | 对话框可见性 |
| `currentPrompt` | `Ref<string>` | 完整 Prompt |
| `staticPrompt` | `Ref<string>` | 静态部分（可缓存） |
| `dynamicPrompt` | `Ref<string>` | 动态部分 |
| `memoriesList` | `Ref<AgentMemory[]>` | 记忆列表 |

### 使用示例

```typescript
import { usePromptDebug } from '@/composables'

const debug = usePromptDebug()

// 显示调试面板
debug.showDebug(promptBuilder, {
  date: '2024-05-10',
  model: 'deepseek-v4-flash',
  user: { name: '张三', role: '管理员' }
})

// 在模板中使用
<el-dialog v-model="debug.visible">
  <pre>{{ debug.currentPrompt }}</pre>
</el-dialog>
```

---

## 设计原则

### 适度分离

- **提取**: 可复用或逻辑独立的模块
- **保持**: UI 函数、组件特有逻辑

### 不过度设计

```typescript
// ❌ 过度分离 - 每个函数都提取
const useScrollToBottom = () => { ... }
const useFormatTime = () => { ... }

// ✅ 适度分离 - 只提取核心逻辑
const useAgentChat = () => { ... }
const usePromptDebug = () => { ... }

// ✅ UI 函数保持在组件中
const scrollToBottom = () => { ... }
const formatTime = (ts: number) => { ... }
```

---

## 扩展建议

后续可添加更多可复用的 composables：

| Composable | 用途 | 适用组件 |
|------------|------|----------|
| `usePagination` | 分页逻辑 | 所有列表页面 |
| `useTable` | 表格数据加载 | 所有列表页面 |
| `useFormDialog` | 表单对话框 | 管理页面 |
| `useLoading` | 加载状态管理 | 多处使用 |