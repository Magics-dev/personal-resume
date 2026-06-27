<template>
  <div class="admin-experience">
    <div class="admin-header">
      <el-button type="primary" @click="$router.push('/admin/dashboard')">
        <el-icon><ArrowLeft /></el-icon>
        返回仪表盘
      </el-button>
      <h1>工作经历管理</h1>
      <el-button type="success" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加经历
      </el-button>
    </div>

    <div class="experience-content">
      <el-table :data="experiences" style="width: 100%" stripe>
        <el-table-column prop="company" label="公司" width="200">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.company" size="small" />
            <span v-else>{{ row.company }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="职位" width="200">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.position" size="small" />
            <span v-else>{{ row.position }}</span>
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
        <el-table-column prop="description" label="描述">
          <template #default="{ row }">
            <el-input
              v-if="row.editing"
              v-model="row.description"
              type="textarea"
              :rows="2"
              size="small"
            />
            <span v-else>{{ row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button
              v-if="row.editing"
              type="success"
              size="small"
              @click="saveExperience(row)"
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
              @click="editExperience(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="!row.editing"
              type="danger"
              size="small"
              @click="deleteExperience(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加经历对话框 -->
      <el-dialog
        v-model="showAddDialog"
        title="添加工作经历"
        width="600px"
        :before-close="handleAddDialogClose"
      >
        <el-form
          ref="addExperienceFormRef"
          :model="newExperience"
          :rules="experienceRules"
          label-width="100px"
        >
          <el-form-item label="公司名称" prop="company">
            <el-input v-model="newExperience.company" placeholder="请输入公司名称" />
          </el-form-item>
          <el-form-item label="职位" prop="position">
            <el-input v-model="newExperience.position" placeholder="请输入职位" />
          </el-form-item>
          <el-form-item label="开始时间" prop="startDate">
            <el-date-picker
              v-model="newExperience.startDate"
              type="date"
              placeholder="选择开始时间"
              format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="结束时间" prop="endDate">
            <el-date-picker
              v-model="newExperience.endDate"
              type="date"
              placeholder="选择结束时间（可选）"
              format="YYYY-MM-DD"
            />
            <el-checkbox v-model="isCurrent">至今</el-checkbox>
          </el-form-item>
          <el-form-item label="工作描述" prop="description">
            <el-input
              v-model="newExperience.description"
              type="textarea"
              :rows="4"
              placeholder="请输入工作描述"
            />
          </el-form-item>
          <el-form-item label="技术栈" prop="technologies">
            <el-input
              v-model="newExperience.technologies"
              placeholder="请输入技术栈，用逗号分隔"
            />
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder">
            <el-input-number
              v-model="newExperience.sortOrder"
              :min="0"
              placeholder="请输入排序"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button type="primary" @click="addExperience">确定</el-button>
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

const experiences = ref([])
const showAddDialog = ref(false)
const addExperienceFormRef = ref()
const isCurrent = ref(false)
const loading = ref(false)

const newExperience = reactive({
  company: '',
  position: '',
  startDate: '',
  endDate: '',
  description: '',
  technologies: '',
  sortOrder: 0
})

const experienceRules = {
  company: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  description: [{ required: true, message: '请输入工作描述', trigger: 'blur' }]
}

onMounted(async () => {
  loadExperiences()
})

const loadExperiences = async () => {
  try {
    const response = await frontAPI.getExperiences()
    experiences.value = response.data.map(exp => ({
      ...exp,
      editing: false
    }))
  } catch (error) {
    console.error('获取工作经历失败:', error)
    ElMessage.error('加载失败')
  }
}

const editExperience = (exp) => {
  exp.editing = true
}

const cancelEdit = (exp) => {
  exp.editing = false
  loadExperiences()
}

const saveExperience = async (exp) => {
  try {
    await adminAPI.updateExperience(exp.id, {
      company: exp.company,
      position: exp.position,
      startDate: exp.startDate,
      endDate: exp.endDate,
      description: exp.description,
      technologies: exp.technologies,
      sortOrder: exp.sortOrder
    })
    ElMessage.success('工作经历更新成功')
    exp.editing = false
  } catch (error) {
    ElMessage.error('更新失败')
    console.error('更新失败:', error)
  }
}

const addExperience = async () => {
  if (!addExperienceFormRef.value) return

  await addExperienceFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isCurrent.value) {
          newExperience.endDate = null
        }

        await adminAPI.createExperience(newExperience)
        ElMessage.success('工作经历添加成功')
        showAddDialog.value = false
        resetNewExperience()
        loadExperiences()
      } catch (error) {
        ElMessage.error('添加失败')
        console.error('添加失败:', error)
      }
    }
  })
}

const deleteExperience = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条工作经历吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await adminAPI.deleteExperience(id)
    ElMessage.success('删除成功')
    loadExperiences()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const resetNewExperience = () => {
  newExperience.company = ''
  newExperience.position = ''
  newExperience.startDate = ''
  newExperience.endDate = ''
  newExperience.description = ''
  newExperience.technologies = ''
  newExperience.sortOrder = 0
  isCurrent.value = false
}

const handleAddDialogClose = () => {
  resetNewExperience()
  addExperienceFormRef.value?.clearValidate()
}

watch(isCurrent, (val) => {
  if (val) {
    newExperience.endDate = null
  }
})
</script>

<style scoped>
.admin-experience {
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

.experience-content {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
}

.experience-content :deep(.el-table) {
  background: rgba(255, 255, 255, 0.02);
  color: #ffffff;
}

.experience-content :deep(.el-table th) {
  background: rgba(0, 245, 255, 0.1);
  color: #00f5ff;
  border-bottom: 2px solid rgba(0, 245, 255, 0.2);
}

.experience-content :deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.experience-content :deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.experience-content :deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}
</style>