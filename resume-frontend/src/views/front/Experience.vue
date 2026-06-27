<template>
  <div class="experience">
    <div class="experience-header">
      <el-button
        type="primary"
        @click="$router.push('/')"
        class="return-btn"
      >
        <el-icon><ArrowLeft /></el-icon>
      </el-button>
      <h1>工作经历</h1>
      <div class="header-line"></div>
    </div>

    <div class="experience-content">
      <div class="timeline">
        <div v-for="(exp, index) in experiences" :key="exp.id" class="timeline-item">
          <div class="timeline-marker" :class="{ 'first': index === 0 }">
            <div class="marker-dot"></div>
          </div>
          <div class="timeline-content">
            <div class="experience-card">
              <div class="exp-header">
                <h3>{{ exp.company }}</h3>
                <span class="exp-period">{{ formatDate(exp.startDate) }} - {{ exp.endDate ? formatDate(exp.endDate) : '至今' }}</span>
              </div>
              <h4>{{ exp.position }}</h4>
              <p class="exp-description">{{ exp.description }}</p>
              <div class="exp-tech">
                <el-tag v-for="tech in getTechArray(exp.technologies)" :key="tech" type="info" size="small">
                  {{ tech }}
                </el-tag>
              </div>
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
import { formatDate } from '@/utils'

const experiences = ref([])

onMounted(async () => {
  try {
    const response = await frontAPI.getExperiences()
    experiences.value = response.data.sort((a, b) => {
      // 按开始日期降序排列
      return new Date(b.startDate) - new Date(a.startDate)
    })
  } catch (error) {
    console.error('获取工作经历失败:', error)
  }
})

const getTechArray = (techString) => {
  if (!techString) return []
  return techString.split(',').map(tech => tech.trim())
}
</script>

<style scoped>
.experience {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.experience-header {
  text-align: center;
  margin-bottom: 60px;
}

.experience-header {
  text-align: center;
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  position: relative;
}

.experience-header h1 {
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

.experience-content {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
}

.timeline {
  position: relative;
  padding: 20px 0;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 30px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, #00f5ff, #ff00ff);
}

.timeline-item {
  position: relative;
  padding-left: 80px;
  margin-bottom: 40px;
}

.timeline-marker {
  position: absolute;
  left: 0;
  top: 10px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.marker-dot {
  width: 16px;
  height: 16px;
  background: #00f5ff;
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(0, 245, 255, 0.5);
}

.timeline-marker.first .marker-dot {
  background: #ff00ff;
  box-shadow: 0 0 10px rgba(255, 0, 255, 0.5);
}

.timeline-content {
  position: relative;
}

.experience-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
  position: relative;
  transition: transform 0.3s ease;
}

.experience-card:hover {
  transform: translateX(10px);
}

.exp-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.exp-header h3 {
  color: #00f5ff;
  font-size: 1.3rem;
  margin: 0;
}

.exp-period {
  color: #ff00ff;
  font-size: 0.9rem;
  background: rgba(255, 0, 255, 0.1);
  padding: 5px 10px;
  border-radius: 15px;
}

.exp-content h4 {
  color: #ffffff;
  font-size: 1.1rem;
  margin-bottom: 15px;
}

.exp-description {
  color: #cccccc;
  line-height: 1.8;
  margin-bottom: 20px;
}

.exp-tech {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.exp-tech .el-tag {
  background: rgba(0, 245, 255, 0.1);
  border-color: rgba(0, 245, 255, 0.3);
  color: #00f5ff;
}
</style>