<template>
  <div class="about">
    <div class="about-header">
      <el-button
  type="primary"
  @click="$router.push('/')"
  class="return-btn"
>
  <el-icon><ArrowLeft /></el-icon>
</el-button>
      <h1>关于我</h1>
      <div class="header-line"></div>
    </div>

    <div class="about-content">
      <el-row :gutter="40">
        <el-col :span="12">
          <div class="info-card">
            <h2>个人信息</h2>
            <div class="info-item">
              <strong>姓名：</strong>{{ profile.name }}
            </div>
            <div class="info-item">
              <strong>职位：</strong>{{ profile.title }}
            </div>
            <div class="info-item">
              <strong>邮箱：</strong>{{ profile.email }}
            </div>
            <div class="info-item">
              <strong>电话：</strong>{{ profile.phone }}
            </div>
            <div class="info-item">
              <strong>GitHub：</strong>
              <a v-if="profile.github" :href="profile.github" target="_blank" class="link">{{ profile.github }}</a>
            </div>
            <div class="info-item">
              <strong>LinkedIn：</strong>
              <a v-if="profile.linkedin" :href="profile.linkedin" target="_blank" class="link">{{ profile.linkedin }}</a>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="intro-card">
            <h2>个人简介</h2>
            <p>{{ profile.intro }}</p>
          </div>
        </el-col>
      </el-row>

      <div class="skills-summary">
        <h2>核心技能</h2>
        <div class="skills-cloud">
          <el-tag
            v-for="skill in skills"
            :key="skill.name"
            :effect="skill.level > 80 ? 'dark' : 'light'"
            :type="skill.level > 80 ? 'primary' : 'info'"
            class="skill-tag"
          >
            {{ skill.name }}
          </el-tag>
        </div>
      </div>

      <div class="education-section">
        <h2>教育背景</h2>
        <div class="education-list">
          <div v-for="edu in education" :key="edu.id" class="education-item">
            <h3>{{ edu.school }}</h3>
            <p>{{ edu.major }} - {{ edu.degree }}</p>
            <p>{{ formatDate(edu.startDate) }} - {{ formatDate(edu.endDate) }}</p>
            <p>{{ edu.description }}</p>
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

const profile = ref({
  name: '',
  title: '',
  intro: '',
  email: '',
  phone: '',
  github: '',
  linkedin: ''
})

const skills = ref([])
const education = ref([])

onMounted(async () => {
  try {
    const [profileRes, skillsRes, eduRes] = await Promise.all([
      frontAPI.getProfile(),
      frontAPI.getSkills(),
      frontAPI.getEducation()
    ])

    profile.value = profileRes.data
    skills.value = skillsRes.data
    education.value = eduRes.data
  } catch (error) {
    console.error('获取数据失败:', error)
  }
})
</script>

<style scoped>
.about {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.about-header {
  text-align: center;
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  position: relative;
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

.about-header h1 {
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
  margin-right: 8px;
}

.about-content {
  max-width: 1200px;
  margin: 0 auto;
}

.info-card, .intro-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
  color: #ffffff;
}

.info-card h2, .intro-card h2, .skills-summary h2, .education-section h2 {
  color: #00f5ff;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.info-item {
  margin-bottom: 15px;
  font-size: 1.1rem;
}

.info-item strong {
  color: #00f5ff;
}

.link {
  color: #00f5ff;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.skills-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 20px;
}

.skill-tag {
  font-size: 1rem;
  padding: 5px 15px;
}

.education-list {
  margin-top: 20px;
}

.education-item {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  border-left: 3px solid #00f5ff;
}

.education-item h3 {
  color: #ffffff;
  margin-bottom: 10px;
  font-size: 1.2rem;
}

.education-item p {
  color: #cccccc;
  margin-bottom: 5px;
}
</style>