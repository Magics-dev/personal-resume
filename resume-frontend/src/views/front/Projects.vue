<template>
  <div class="projects">
    <div class="projects-header">
      <el-button
        type="primary"
        @click="$router.push('/')"
        class="return-btn"
      >
        <el-icon><ArrowLeft /></el-icon>
      </el-button>
      <h1>项目经验</h1>
      <div class="header-line"></div>
    </div>

    <div class="projects-content">
      <div class="projects-grid">
        <div v-for="project in projects" :key="project.id" class="project-card">
          <div class="project-image">
            <el-image
              :src="project.imageUrl || 'https://via.placeholder.com/400x200'"
              fit="cover"
              class="image"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          <div class="project-info">
            <h3>{{ project.name }}</h3>
            <p class="project-description">{{ project.description }}</p>
            <div class="project-tech">
              <el-tag v-for="tech in getTechArray(project.techStack)" :key="tech" type="info" size="small">
                {{ tech }}
              </el-tag>
            </div>
            <div class="project-links">
              <el-button v-if="project.demoUrl" type="primary" size="small" @click="openUrl(project.demoUrl)">
                <el-icon><View /></el-icon>
                在线预览
              </el-button>
              <el-button v-if="project.githubUrl" type="success" size="small" @click="openUrl(project.githubUrl)">
                <el-icon><Link /></el-icon>
                查看代码
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { frontAPI } from '@/api'
import { Picture, View, Link } from '@element-plus/icons-vue'

const projects = ref([])

onMounted(async () => {
  try {
    const response = await frontAPI.getProjects()
    projects.value = response.data.sort((a, b) => a.sortOrder - b.sortOrder)
  } catch (error) {
    console.error('获取项目数据失败:', error)
  }
})

const getTechArray = (techString) => {
  if (!techString) return []
  return techString.split(',').map(tech => tech.trim())
}

const openUrl = (url) => {
  window.open(url, '_blank')
}
</script>

<style scoped>
.projects {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.projects-header {
  text-align: center;
  margin-bottom: 60px;
}

.projects-header {
  text-align: center;
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  position: relative;
}

.projects-header h1 {
  font-size: 2.5rem;
  color: #ffffff;
  margin-bottom: 20px;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-line {
  width: 100px;
  height: 3px;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  margin: 0 auto;
}

.return-btn {
  position: absolute;
  left: 20px;
  top: 20px;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  font-weight: bold;
  box-shadow: 0 4px 15px rgba(0, 245, 255, 0.3);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.return-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 245, 255, 0.5);
  background: linear-gradient(45deg, #00d9ff, #e500ff);
}

.return-btn .el-icon {
  margin-right: 0;
}

.projects-content {
  max-width: 1200px;
  margin: 0 auto;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 30px;
}

.project-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.project-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 245, 255, 0.2);
  border-color: rgba(0, 245, 255, 0.3);
}

.project-image {
  height: 200px;
  overflow: hidden;
}

.project-image .image {
  width: 100%;
  height: 100%;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.2);
  color: #666;
}

.project-info {
  padding: 25px;
}

.project-info h3 {
  color: #00f5ff;
  font-size: 1.3rem;
  margin-bottom: 15px;
}

.project-description {
  color: #cccccc;
  line-height: 1.6;
  margin-bottom: 20px;
  height: 60px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.project-tech {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.project-tech .el-tag {
  background: rgba(0, 245, 255, 0.1);
  border-color: rgba(0, 245, 255, 0.3);
  color: #00f5ff;
}

.project-links {
  display: flex;
  gap: 10px;
}
</style>