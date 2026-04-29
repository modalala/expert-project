import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo, MenuItem } from '@/types'

export const useAppStore = defineStore('app', () => {
  // 侧边栏折叠状态
  const collapsed = ref(false)

  // 用户信息
  const userInfo = ref<UserInfo | null>(null)

  // Token
  const token = ref<string | null>(localStorage.getItem('token'))

  // 菜单列表
  const menuList = ref<MenuItem[]>([])

  // 切换侧边栏
  const toggleCollapsed = () => {
    collapsed.value = !collapsed.value
  }

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
  }

  // 设置Token
  const setToken = (t: string) => {
    token.value = t
    localStorage.setItem('token', t)
  }

  // 清除登录状态
  const clearAuth = () => {
    token.value = null
    userInfo.value = null
    menuList.value = []
    localStorage.removeItem('token')
  }

  return {
    collapsed,
    userInfo,
    token,
    menuList,
    toggleCollapsed,
    setUserInfo,
    setToken,
    clearAuth
  }
})