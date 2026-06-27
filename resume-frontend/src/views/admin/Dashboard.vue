<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h1>仪表盘</h1>
      <el-button type="primary" @click="logout">退出登录</el-button>
    </div>

    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon primary">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <h3>个人资料</h3>
              <p>{{ profile.name || '未设置' }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon success">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <h3>技能数量</h3>
              <p>{{ skills.length }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon warning">
              <el-icon><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-content">
              <h3>工作经历</h3>
              <p>{{ experiences.length }}</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon danger">
              <el-icon><FolderOpened /></el-icon>
            </div>
            <div class="stat-content">
              <h3>项目数量</h3>
              <p>{{ projects.length }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="quick-actions">
      <h2>快捷操作</h2>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="action-card" @click="$router.push('/admin/profile')">
            <el-icon><EditPen /></el-icon>
            <h3>编辑个人资料</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" @click="$router.push('/admin/skills')">
            <el-icon><TrendCharts /></el-icon>
            <h3>管理技能</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" @click="$router.push('/admin/experience')">
            <el-icon><OfficeBuilding /></el-icon>
            <h3>管理经历</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="action-card" @click="$router.push('/admin/projects')">
            <el-icon><FolderOpened /></el-icon>
            <h3>管理项目</h3>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="recent-updates">
      <h2>最近更新</h2>
      <el-timeline>
        <el-timeline-item
          v-for="update in recentUpdates"
          :key="update.id"
          :timestamp="update.time"
          :type="update.type"
        >
          {{ update.content }}
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminAPI, frontAPI } from '@/api'
import { User, TrendCharts, OfficeBuilding, FolderOpened, EditPen } from '@element-plus/icons-vue'

const router = useRouter()

const profile = ref({})
const skills = ref([])
const experiences = ref([])
const projects = ref([])
const recentUpdates = ref([
  { id: 1, content: '创建了新项目：个人博客系统', time: '2小时前', type: 'success' },
  { id: 2, content: '更新了技能列表', time: '1天前', type: 'primary' },
  { id: 3, content: '添加了新的工作经历', time: '3天前', type: 'warning' }
])

const logout = () => {
  localStorage.removeItem('adminToken')
  ElMessage.success('已退出登录')
  router.push('/admin')
}

onMounted(async () => {
  try {
    const [profileRes, skillsRes, experiencesRes, projectsRes] = await Promise.all([
      adminAPI.getProfile(),
      frontAPI.getSkills(),
      frontAPI.getExperiences(),
      frontAPI.getProjects()
    ])

    profile.value = profileRes.data
    skills.value = skillsRes.data
    experiences.value = experiencesRes.data
    projects.value = projectsRes.data
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('加载数据失败')
  }
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
  min-height: 100vh;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  color: #ffffff;
}

.dashboard-header h1 {
  font-size: 1.8rem;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stats-cards {
  margin-bottom: 40px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
}

.stat-icon.primary {
  background: linear-gradient(45deg, #00f5ff, #0080ff);
}

.stat-icon.success {
  background: linear-gradient(45deg, #00ff88, #00cc66);
}

.stat-icon.warning {
  background: linear-gradient(45deg, #ffaa00, #ff8800);
}

.stat-icon.danger {
  background: linear-gradient(45deg, #ff0066, #cc0044);
}

.stat-content h3 {
  color: #888888;
  font-size: 0.9rem;
  margin: 0;
  font-weight: normal;
}

.stat-content p {
  color: #ffffff;
  font-size: 1.8rem;
  font-weight: bold;
  margin: 5px 0 0 0;
}

.quick-actions, .recent-updates {
  margin-bottom: 40px;
}

.quick-actions h2, .recent-updates h2 {
  color: #00f5ff;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.action-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.action-card:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: #00f5ff;
  transform: translateY(-5px);
}

.action-card .el-icon {
  font-size: 2rem;
  color: #00f5ff;
  margin-bottom: 10px;
}

.action-card h3 {
  color: #ffffff;
  margin: 0;
  font-size: 1rem;
}

.recent-updates .el-timeline {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  padding: 20px;
}
</style>