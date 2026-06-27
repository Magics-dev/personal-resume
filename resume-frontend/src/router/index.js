import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/front/Home.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/front/About.vue')
  },
  {
    path: '/skills',
    name: 'Skills',
    component: () => import('../views/front/Skills.vue')
  },
  {
    path: '/experience',
    name: 'Experience',
    component: () => import('../views/front/Experience.vue')
  },
  {
    path: '/projects',
    name: 'Projects',
    component: () => import('../views/front/Projects.vue')
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/front/Contact.vue')
  },
  {
    path: '/admin',
    name: 'AdminLogin',
    component: () => import('../views/admin/Login.vue')
  },
  {
    path: '/admin/dashboard',
    name: 'Dashboard',
    component: () => import('../views/admin/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/profile',
    name: 'AdminProfile',
    component: () => import('../views/admin/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/skills',
    name: 'AdminSkills',
    component: () => import('../views/admin/Skills.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/experience',
    name: 'AdminExperience',
    component: () => import('../views/admin/Experience.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/projects',
    name: 'AdminProjects',
    component: () => import('../views/admin/Projects.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/education',
    name: 'AdminEducation',
    component: () => import('../views/admin/Education.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 简单的路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('adminToken') === 'admin-token'

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/admin')
  } else {
    next()
  }
})

export default router