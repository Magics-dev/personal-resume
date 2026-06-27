<template>
  <div class="admin-profile">
    <div class="admin-header">
      <el-button type="primary" @click="$router.push('/admin/dashboard')">
        <el-icon><ArrowLeft /></el-icon>
        返回仪表盘
      </el-button>
      <h1>个人资料管理</h1>
    </div>

    <div class="profile-content">
      <el-row :gutter="40">
        <el-col :span="8">
          <div class="avatar-section">
            <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <el-avatar
                :size="150"
                :src="profileForm.avatar || 'https://via.placeholder.com/150x150'"
                class="avatar"
              >
                <el-icon><User /></el-icon>
              </el-avatar>
              <div class="upload-tip">
                <el-icon><Camera /></el-icon>
                更换头像
              </div>
            </el-upload>
          </div>
        </el-col>

        <el-col :span="16">
          <div class="form-section">
            <el-form
              ref="profileFormRef"
              :model="profileForm"
              :rules="rules"
              label-width="120px"
              size="large"
            >
              <el-form-item label="姓名" prop="name">
                <el-input v-model="profileForm.name" placeholder="请输入姓名" />
              </el-form-item>

              <el-form-item label="职位" prop="title">
                <el-input v-model="profileForm.title" placeholder="请输入职位" />
              </el-form-item>

              <el-form-item label="个人简介" prop="intro">
                <el-input
                  v-model="profileForm.intro"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入个人简介"
                />
              </el-form-item>

              <el-form-item label="邮箱" prop="email">
                <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
              </el-form-item>

              <el-form-item label="电话" prop="phone">
                <el-input v-model="profileForm.phone" placeholder="请输入电话" />
              </el-form-item>

              <el-form-item label="GitHub" prop="github">
                <el-input v-model="profileForm.github" placeholder="请输入GitHub地址" />
              </el-form-item>

              <el-form-item label="LinkedIn" prop="linkedin">
                <el-input v-model="profileForm.linkedin" placeholder="请输入LinkedIn地址" />
              </el-form-item>

              <el-form-item>
                <el-button
                  type="primary"
                  @click="updateProfile"
                  :loading="loading"
                  class="submit-btn"
                >
                  保存修改
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
import { adminAPI, frontAPI } from '@/api'
import { ArrowLeft, User, Camera } from '@element-plus/icons-vue'

const profileFormRef = ref()
const loading = ref(false)

const profileForm = reactive({
  name: '',
  title: '',
  intro: '',
  email: '',
  phone: '',
  github: '',
  linkedin: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  title: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  intro: [{ required: true, message: '请输入个人简介', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

onMounted(async () => {
  try {
    const response = await frontAPI.getProfile()
    Object.assign(profileForm, { ...response })
  } catch (error) {
    console.error('获取个人信息失败:', error)
    ElMessage.error('加载数据失败')
  }
})

const updateProfile = async () => {
  if (!profileFormRef.value) return

  await profileFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true

      try {
        await adminAPI.updateProfile(profileForm)
        ElMessage.success('个人资料更新成功')
      } catch (error) {
        ElMessage.error('更新失败，请稍后重试')
        console.error('更新失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleAvatarSuccess = (response) => {
  profileForm.avatar = response.url || 'https://via.placeholder.com/150x150'
  ElMessage.success('头像上传成功')
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('上传头像只能是图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像大小不能超过 2MB!')
  }

  return isImage && isLt2M
}
</script>

<style scoped>
.admin-profile {
  padding: 20px;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
  min-height: 100vh;
}

.admin-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.admin-header h1 {
  color: #ffffff;
  font-size: 1.8rem;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.profile-content {
  max-width: 1200px;
  margin: 0 auto;
}

.avatar-section {
  text-align: center;
}

.avatar-uploader {
  display: inline-block;
  position: relative;
}

.avatar {
  cursor: pointer;
  border: 3px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.avatar:hover {
  border-color: #00f5ff;
  transform: scale(1.05);
}

.upload-tip {
  margin-top: 10px;
  color: #888888;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
}

.upload-tip .el-icon {
  color: #00f5ff;
}

.form-section {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
}

.submit-btn {
  width: 200px;
  background: linear-gradient(45deg, #00f5ff, #ff00ff);
  border: none;
  height: 45px;
  font-size: 1rem;
}

.submit-btn:hover {
  background: linear-gradient(45deg, #00d9ff, #e500ff);
}
</style>