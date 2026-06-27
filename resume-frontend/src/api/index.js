import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 前台API
export const frontAPI = {
  // 获取个人信息
  getProfile: () => api.get('/profile'),
  // 获取技能列表
  getSkills: () => api.get('/skills'),
  // 获取工作经历
  getExperiences: () => api.get('/experiences'),
  // 获取项目列表
  getProjects: () => api.get('/projects'),
  // 获取教育背景
  getEducation: () => api.get('/education'),
  // 提交联系表单
  submitContact: (data) => api.post('/contact', data)
}

// 后台API
export const adminAPI = {
  // 登录
  login: (data) => api.post('/admin/login', data),
  // 获取个人信息
  getProfile: () => api.get('/admin/profile'),
  // 更新个人信息
  updateProfile: (data) => api.put('/admin/profile', data),
  // 技能管理
  createSkill: (data) => api.post('/admin/skills', data),
  updateSkill: (id, data) => api.put(`/admin/skills/${id}`, data),
  deleteSkill: (id) => api.delete(`/admin/skills/${id}`),
  // 工作经历管理
  createExperience: (data) => api.post('/admin/experiences', data),
  updateExperience: (id, data) => api.put(`/admin/experiences/${id}`, data),
  deleteExperience: (id) => api.delete(`/admin/experiences/${id}`),
  // 项目管理
  createProject: (data) => api.post('/admin/projects', data),
  updateProject: (id, data) => api.put(`/admin/projects/${id}`, data),
  deleteProject: (id) => api.delete(`/admin/projects/${id}`),
  // 教育背景管理
  createEducation: (data) => api.post('/admin/education', data),
  updateEducation: (id, data) => api.put(`/admin/education/${id}`, data),
  deleteEducation: (id) => api.delete(`/admin/education/${id}`)
}

export default api