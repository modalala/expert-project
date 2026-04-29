<template>
  <el-container class="default-layout">
    <el-aside :width="appStore.collapsed ? '64px' : '210px'">
      <div class="sidebar-logo">
        <span v-if="!appStore.collapsed">专家库管理系统</span>
        <span v-else>专家</span>
      </div>
      <el-menu
        :default-active="$route.path"
        :collapse="appStore.collapsed"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/system/role">
            <el-icon><UserFilled /></el-icon>
            <span>角色管理</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="expert">
          <template #title>
            <el-icon><Avatar /></el-icon>
            <span>专家管理</span>
          </template>
          <el-menu-item index="/expert/register">
            <el-icon><EditPen /></el-icon>
            <span>专家注册</span>
          </el-menu-item>
          <el-menu-item index="/expert/review">
            <el-icon><Document /></el-icon>
            <span>专家初审</span>
          </el-menu-item>
          <el-menu-item index="/expert/master">
            <el-icon><List /></el-icon>
            <span>专家主数据</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="extraction">
          <template #title>
            <el-icon><Sort /></el-icon>
            <span>抽取管理</span>
          </template>
          <el-menu-item index="/extraction/plan">
            <el-icon><Tickets /></el-icon>
            <span>采购方案单</span>
          </el-menu-item>
          <el-menu-item index="/extraction/confirm">
            <el-icon><Finished /></el-icon>
            <span>专家确认</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="bid">
          <template #title>
            <el-icon><Star /></el-icon>
            <span>评标管理</span>
          </template>
          <el-menu-item index="/bid/committee">
            <el-icon><Grid /></el-icon>
            <span>评标委员会</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="appStore.toggleCollapsed">
            <Expand v-if="appStore.collapsed" />
            <Fold v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="username">{{ appStore.userInfo?.realName || '管理员' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useAppStore } from '@/stores/app'
import { useRouter } from 'vue-router'
import {
  HomeFilled, Setting, User, UserFilled, Avatar, EditPen,
  Document, List, Sort, Tickets, Finished, Star, Grid,
  Expand, Fold
} from '@element-plus/icons-vue'

const appStore = useAppStore()
const router = useRouter()

const handleLogout = () => {
  appStore.clearAuth()
  router.push('/login')
}
</script>

<style scoped lang="scss">
.default-layout {
  height: 100vh;
}

.el-aside {
  background-color: #001529;
  transition: width 0.3s;
}

.sidebar-logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #002140;
}

.el-menu {
  border-right: none;
}

.layout-header {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  color: #606266;
}

.layout-main {
  background-color: #f5f5f5;
  padding: 20px;
}
</style>