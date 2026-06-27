<template>
  <div class="home">
    <div class="hero-section">
      <div class="hero-content">
        <div class="hero-avatar">
          <el-avatar :size="120" src="https://via.placeholder.com/120x120" />
        </div>
        <h1 class="hero-name">{{ profile.name }}</h1>
        <p class="hero-title">{{ profile.title }}</p>
        <p class="hero-intro">{{ profile.intro }}</p>
        <div class="hero-social">
          <el-button v-if="profile.github" type="primary" circle>
            <el-icon><Link /></el-icon>
            <a :href="profile.github" target="_blank" style="color: white; text-decoration: none;">
              GitHub
            </a>
          </el-button>
          <el-button v-if="profile.linkedin" type="primary" circle>
            <el-icon><Link /></el-icon>
            <a :href="profile.linkedin" target="_blank" style="color: white; text-decoration: none;">
              LinkedIn
            </a>
          </el-button>
        </div>
      </div>
      <div class="particles-container">
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
      </div>
    </div>

    <div class="quick-links">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="quick-link-card" @click="$router.push('/about')">
            <el-icon><User /></el-icon>
            <h3>关于我</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-link-card" @click="$router.push('/skills')">
            <el-icon><TrendCharts /></el-icon>
            <h3>技能</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-link-card" @click="$router.push('/experience')">
            <el-icon><OfficeBuilding /></el-icon>
            <h3>经历</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-link-card" @click="$router.push('/projects')">
            <el-icon><FolderOpened /></el-icon>
            <h3>项目</h3>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { frontAPI } from '@/api'
import { User, TrendCharts, OfficeBuilding, FolderOpened, Link } from '@element-plus/icons-vue'

const profile = ref({
  name: '加载中...',
  title: '',
  intro: '',
  github: '',
  linkedin: ''
})

onMounted(async () => {
  try {
    const response = await frontAPI.getProfile()
    profile.value = response.data
  } catch (error) {
    console.error('获取个人信息失败:', error)
  }
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

.hero-section {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.hero-content {
  text-align: center;
  z-index: 2;
  position: relative;
}

.hero-avatar {
  margin-bottom: 20px;
}

.hero-name {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 10px;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-title {
  font-size: 1.5rem;
  color: #00f5ff;
  margin-bottom: 20px;
}

.hero-intro {
  font-size: 1.1rem;
  color: #ffffff;
  max-width: 600px;
  margin: 0 auto 30px;
  opacity: 0.9;
}

.hero-social {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.particles-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: #00f5ff;
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.particle:nth-child(1) { left: 10%; top: 20%; animation-delay: 0s; }
.particle:nth-child(2) { left: 20%; top: 60%; animation-delay: 1s; }
.particle:nth-child(3) { left: 70%; top: 30%; animation-delay: 2s; }
.particle:nth-child(4) { left: 80%; top: 70%; animation-delay: 3s; }
.particle:nth-child(5) { left: 50%; top: 10%; animation-delay: 4s; }

@keyframes float {
  0%, 100% { transform: translateY(0) translateX(0); opacity: 0.5; }
  33% { transform: translateY(-100px) translateX(50px); opacity: 1; }
  66% { transform: translateY(-50px) translateX(-50px); opacity: 0.8; }
}

.quick-links {
  position: relative;
  z-index: 2;
  padding: 60px 20px;
  background: rgba(0, 0, 0, 0.5);
}

.quick-link-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.quick-link-card:hover {
  transform: translateY(-10px);
  border-color: #00f5ff;
  box-shadow: 0 10px 30px rgba(0, 245, 255, 0.3);
}

.quick-link-card .el-icon {
  font-size: 2rem;
  margin-bottom: 10px;
  color: #00f5ff;
}

.quick-link-card h3 {
  color: #ffffff;
  margin: 0;
}
</style>