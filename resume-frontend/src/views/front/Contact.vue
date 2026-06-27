<template>
  <div class="contact">
    <div class="contact-header">
      <h1>联系方式</h1>
      <div class="header-line"></div>
    </div>

    <div class="contact-content">
      <el-row :gutter="40">
        <el-col :span="12">
          <div class="contact-info">
            <h2>联系信息</h2>
            <div class="info-list">
              <div class="info-item">
                <el-icon><Message /></el-icon>
                <span>{{ profile.email }}</span>
              </div>
              <div class="info-item" v-if="profile.phone">
                <el-icon><Phone /></el-icon>
                <span>{{ profile.phone }}</span>
              </div>
              <div class="info-item" v-if="profile.github">
                <el-icon><Link /></el-icon>
                <a :href="profile.github" target="_blank">{{ profile.github }}</a>
              </div>
              <div class="info-item" v-if="profile.linkedin">
                <el-icon><Link /></el-icon>
                <a :href="profile.linkedin" target="_blank">{{ profile.linkedin }}</a>
              </div>
            </div>
          </div>

          <div class="social-links">
            <h2>社交媒体</h2>
            <div class="social-icons">
              <el-button
                v-for="social in socialLinks"
                :key="social.name"
                type="primary"
                circle
                @click="openUrl(social.url)"
              >
                <el-icon><component :is="social.icon" /></el-icon>
              </el-button>
            </div>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="contact-form">
            <h2>发送消息</h2>
            <el-form
              ref="contactFormRef"
              :model="contactForm"
              :rules="rules"
              label-width="80px"
              size="large"
            >
              <el-form-item label="姓名" prop="name">
                <el-input v-model="contactForm.name" placeholder="请输入您的姓名" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="contactForm.email" placeholder="请输入您的邮箱" />
              </el-form-item>
              <el-form-item label="主题" prop="subject">
                <el-input v-model="contactForm.subject" placeholder="请输入消息主题" />
              </el-form-item>
              <el-form-item label="消息" prop="message">
                <el-input
                  v-model="contactForm.message"
                  type="textarea"
                  :rows="6"
                  placeholder="请输入您的消息"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitForm"
                  :loading="loading"
                  class="submit-btn"
                >
                  发送消息
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { frontAPI } from '@/api'
import { Message, Phone, Link, Chrome, Wechat, Github } from '@element-plus/icons-vue'

const contactFormRef = ref()
const profile = ref({
  email: '',
  phone: '',
  github: '',
  linkedin: ''
})

const contactForm = reactive({
  name: '',
  email: '',
  subject: '',
  message: ''
})

const loading = ref(false)
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  subject: [{ required: true, message: '请输入主题', trigger: 'blur' }],
  message: [{ required: true, message: '请输入消息内容', trigger: 'blur' }]
}

const socialLinks = [
  { name: 'GitHub', icon: 'Github', url: 'https://github.com' },
  { name: '微信', icon: 'Wechat', url: '#' },
  { name: '个人网站', icon: 'Chrome', url: 'https://example.com' }
]

onMounted(async () => {
  try {
    const response = await frontAPI.getProfile()
    profile.value = response.data
  } catch (error) {
    console.error('获取个人信息失败:', error)
  }
})

const submitForm = async () => {
  if (!contactFormRef.value) return

  await contactFormRef.value.validate(async (valid, fields) => {
    if (valid) {
      loading.value = true
      try {
        await frontAPI.submitContact(contactForm)
        ElMessage.success('消息发送成功！我会尽快回复您。')
        // 重置表单
        contactForm.name = ''
        contactForm.email = ''
        contactForm.subject = ''
        contactForm.message = ''
      } catch (error) {
        ElMessage.error('发送失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}

const openUrl = (url) => {
  window.open(url, '_blank')
}
</script>

<style scoped>
.contact {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.contact-header {
  text-align: center;
  margin-bottom: 60px;
}

.contact-header h1 {
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

.contact-content {
  max-width: 1200px;
  margin: 0 auto;
}

.contact-info, .social-links, .contact-form {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
  color: #ffffff;
}

.contact-info h2, .social-links h2, .contact-form h2 {
  color: #00f5ff;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 1.1rem;
}

.info-item .el-icon {
  color: #00f5ff;
  font-size: 1.2rem;
}

.info-item a {
  color: #00f5ff;
  text-decoration: none;
  transition: color 0.3s;
}

.info-item a:hover {
  color: #ff00ff;
}

.social-icons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.social-icons .el-button {
  width: 60px;
  height: 60px;
  font-size: 1.2rem;
}

.submit-btn {
  width: 100%;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  border: none;
  font-size: 1.1rem;
  padding: 15px 0;
}

.submit-btn:hover {
  background: linear-gradient(45deg, #00d9ff, #e500ff);
}
</style>