import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router'
import pinia from './stores'
import App from './App.vue'
import './assets/styles/index.scss'

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus)
app.use(router)
app.use(pinia)

// 配置 Vue 识别 pi-web-ui 的 custom elements
app.config.compilerOptions.isCustomElement = (tag) =>
  tag.startsWith('pi-') || tag === 'agent-interface' || tag === 'chat-panel'

app.mount('#app')
