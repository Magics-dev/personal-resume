<template>
  <div class="admin-education">
    <div class="admin-header">
      <el-button type="primary" @click="$router.push('/admin/dashboard')">
        <el-icon><ArrowLeft /></el-icon>
        返回仪表盘
      </el-button>
      <h1>教育背景管理</h1>
      <el-button type="success" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加教育
      </el-button>
    </div>

    <div class="education-content">
      <el-table :data="educations" style="width: 100%" stripe>
        <el-table-column prop="school" label="学校" width="200">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.school" size="small" />
            <span v-else>{{ row.school }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="major" label="专业" width="200">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.major" size="small" />
            <span v-else>{{ row.major }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="degree" label="学历" width="100">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.degree" size="small" />
            <span v-else>{{ row.degree }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始时间" width="150">
          <template #default="{ row }">
            <el-date-picker
              v-if="row.editing"
              v-model="row.startDate"
              type="date"
              size="small"
              format="YYYY-MM-DD"
            />
            <span v-else>{{ formatDate(row.startDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="结束时间" width="150">
          <template #default="{ row }">
            <el-date-picker
              v-if="row.editing"
              v-model="row.endDate"
              type="date"
              size="small"
              format="YYYY-MM-DD"
            />
            <span v-else>{{ row.endDate ? formatDate(row.endDate) : '至今' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button
              v-if="row.editing"
              type="success"
              size="small"
              @click="saveEducation(row)"
            >
              保存
            </el-button>
            <el-button
              v-if="row.editing"
              type="info"
              size="small"
              @click="cancelEdit(row)"
            >
              取消
            </el-button>
            <el-button
              v-if="!row.editing"
              type="primary"
              size="small"
              @click="editEducation(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="!row.editing"
              type="danger"
              size="small"
              @click="deleteEducation(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加教育对话框 -->
      <el-dialog
        v-model="showAddDialog"
        title="添加教育背景"
        width="500px"
        :before-close="handleAddDialogClose"
      >
        <el-form
          ref="addEducationFormRef"
          :model="newEducation"
          :rules="educationRules"
          label-width="100px"
        >
          <el-form-item label="学校名称" prop="school">
            <el-input v-model="newEducation.school" placeholder="请输入学校名称" />
          </el-form-item>
          <el-form-item label="专业" prop="major">
            <el-input v-model="newEducation.major" placeholder="请输入专业" />
          </el-form-item>
          <el-form-item label="学历" prop="degree">
            <el-select v-model="newEducation.degree" placeholder="请选择学历">
              <el-option label="专科" value="专科" />
              <el-option label="本科" value="本科" />
              <el-option label="硕士" value="硕士" />
              <el-option label="博士" value="博士" />
            </el-select>
          </el-form-item>
          <el-form-item label="开始时间" prop="startDate">
            <el-date-picker
              v-model="newEducation.startDate"
              type="date"
              placeholder="选择开始时间"
              format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="结束时间" prop="endDate">
            <el-date-picker
              v-model="newEducation.endDate"
              type="date"
              placeholder="选择结束时间（可选）"
              format="YYYY-MM-DD"
            />
            <el-checkbox v-model="isCurrent">至今</el-checkbox>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input
              v-model="newEducation.description"
              type="textarea"
              :rows="3"
              placeholder="请输入描述信息"
            />
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder">
            <el-input-number
              v-model="newEducation.sortOrder"
              :min="0"
              placeholder="请输入排序"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button type="primary" @click="addEducation">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminAPI, frontAPI } from '@/api'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'
import { formatDate } from '@/utils'

const educations = ref([])
const showAddDialog = ref(false)
const addEducationFormRef = ref()
const isCurrent = ref(false)
const loading = ref(false)

const newEducation = reactive({
  school: '',
  major: '',
  degree: '',
  startDate: '',
  endDate: '',
  description: '',
  sortOrder: 0
})

const educationRules = {
  school: [{ required: true, message: '请输入学校名称', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
}

onMounted(async () => {
  loadEducations()
})

const loadEducations = async () => {
  try {
    const response = await frontAPI.getEducation()
    educations.value = response.data.map(edu => ({
      ...edu,
      editing: false
    }))
  } catch (error) {
    console.error('获取教育背景失败:', error)
    ElMessage.error('加载失败')
  }
}

const editEducation = (edu) => {
  edu.editing = true
}

const cancelEdit = (edu) => {
  edu.editing = false
  loadEducations()
}

const saveEducation = async (edu) => {
  try {
    await adminAPI.updateEducation(edu.id, {
      school: edu.school,
      major: edu.major,
      degree: edu.degree,
      startDate: edu.startDate,
      endDate: edu.endDate,
      description: edu.description,
      sortOrder: edu.sortOrder
    })
    ElMessage.success('教育背景更新成功')
    edu.editing = false
  } catch (error) {
    ElMessage.error('更新失败')
    console.error('更新失败:', error)
  }
}

const addEducation = async () => {
  if (!addEducationFormRef.value) return

  await addEducationFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isCurrent.value) {
          newEducation.endDate = null
        }

        await adminAPI.createEducation(newEducation)
        ElMessage.success('教育背景添加成功')
        showAddDialog.value = false
        resetNewEducation()
        loadEducations()
      } catch (error) {
        ElMessage.error('添加失败')
        console.error('添加失败:', error)
      }
    }
  })
}

const deleteEducation = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条教育背景吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await adminAPI.deleteEducation(id)
    ElMessage.success('删除成功')
    loadEducations()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const resetNewEducation = () => {
  newEducation.school = ''
  newEducation.major = ''
  newEducation.degree = ''
  newEducation.startDate = ''
  newEducation.endDate = ''
  newEducation.description = ''
  newEducation.sortOrder = 0
  isCurrent.value = false
}

const handleAddDialogClose = () => {
  resetNewEducation()
  addEducationFormRef.value?.clearValidate()
}

watch(isCurrent, (val) => {
  if (val) {
    newEducation.endDate = null
  }
})
</script>

<style scoped>
.admin-education {
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
  flex: 1;
}

.education-content {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
}

.education-content :deep(.el-table) {
  background: rgba(255, 255, 255, 0.02);
  color: #ffffff;
}

.education-content :deep(.el-table th) {
  background: rgba(0, 245, 255, 0.1);
  color: #00f5ff;
  border-bottom: 2px solid rgba(0, 245, 255, 0.2);
}

.education-content :deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.education-content :deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.education-content :deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}
</style>