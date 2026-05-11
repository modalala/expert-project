import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', layout: 'blank' }
  },
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'system/user',
        name: 'UserManage',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'system/role',
        name: 'RoleManage',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'system/dict',
        name: 'DictManage',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '数据字典' }
      },
      {
        path: 'system/message',
        name: 'MessageTemplate',
        component: () => import('@/views/system/message/index.vue'),
        meta: { title: '消息模板' }
      },
      {
        path: 'expert/register',
        name: 'ExpertRegister',
        component: () => import('@/views/expert/register/index.vue'),
        meta: { title: '专家注册' }
      },
      {
        path: 'expert/review',
        name: 'ExpertReview',
        component: () => import('@/views/expert/review/index.vue'),
        meta: { title: '专家初审' }
      },
      {
        path: 'expert/master',
        name: 'ExpertMaster',
        component: () => import('@/views/expert/master/index.vue'),
        meta: { title: '专家主数据' }
      },
      {
        path: 'extraction/plan',
        name: 'ProcurementPlan',
        component: () => import('@/views/extraction/plan/index.vue'),
        meta: { title: '采购方案单' }
      },
      {
        path: 'extraction/confirm',
        name: 'ExpertConfirm',
        component: () => import('@/views/extraction/confirm/index.vue'),
        meta: { title: '专家确认' }
      },
      {
        path: 'bid/committee',
        name: 'BidCommittee',
        component: () => import('@/views/bid/committee/index.vue'),
        meta: { title: '评标委员会' }
      },
      {
        path: 'agent/chat',
        name: 'AgentChat',
        component: () => import('@/views/agent/chat/index.vue'),
        meta: { title: 'Agent对话' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  document.title = `${to.meta.title || '首页'} - 专家库管理系统`
  const token = localStorage.getItem('token')

  // 允许直接访问的页面
  const publicPages = ['/login', '/agent/chat']

  if (publicPages.includes(to.path)) {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router