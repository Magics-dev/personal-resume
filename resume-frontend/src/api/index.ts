import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('adminToken')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('adminToken')
      window.location.href = '/admin/login'
    }
    return Promise.reject(error)
  }
)

export const frontAPI = {
  // 获取用户信息
  getProfile: () => api.get('/profile'),
  // 获取技能列表
  getSkills: () => api.get('/skills'),
  // 获取工作经历
  getExperiences: () => api.get('/experiences'),
  // 获取项目
  getProjects: () => api.get('/projects'),
  // 获取教育背景
  getEducation: () => api.get('/education')
}

export const adminAPI = {
  // 登录
  login: (data: { username: string; password: string }) => api.post('/admin/login', data),
  // 获取用户信息
  getProfile: () => api.get('/admin/profile'),
  // 更新用户信息
  updateProfile: (data: any) => api.put('/admin/profile', data),
  // 技能管理
  createSkill: (data: any) => api.post('/admin/skills', data),
  updateSkill: (id: number, data: any) => api.put(`/admin/skills/${id}`, data),
  deleteSkill: (id: number) => api.delete(`/admin/skills/${id}`),
  // 工作经历管理
  createExperience: (data: any) => api.post('/admin/experiences', data),
  updateExperience: (id: number, data: any) => api.put(`/admin/experiences/${id}`, data),
  deleteExperience: (id: number) => api.delete(`/admin/experiences/${id}`),
  // 项目管理
  createProject: (data: any) => api.post('/admin/projects', data),
  updateProject: (id: number, data: any) => api.put(`/admin/projects/${id}`, data),
  deleteProject: (id: number) => api.delete(`/admin/projects/${id}`),
  // 教育背景管理
  createEducation: (data: any) => api.post('/admin/education', data),
  updateEducation: (id: number, data: any) => api.put(`/admin/education/${id}`, data),
  deleteEducation: (id: number) => api.delete(`/admin/education/${id}`)
}