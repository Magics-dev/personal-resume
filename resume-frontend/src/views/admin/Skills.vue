<template>
  <div class="admin-skills">
    <div class="admin-header">
      <el-button type="primary" @click="$router.push('/admin/dashboard')">
        <el-icon><ArrowLeft /></el-icon>
        返回仪表盘
      </el-button>
      <h1>技能管理</h1>
      <el-button type="success" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加技能
      </el-button>
    </div>

    <div class="skills-content">
      <div class="skills-table">
        <el-table :data="skills" style="width: 100%" stripe>
          <el-table-column prop="name" label="技能名称" width="200">
            <template #default="{ row }">
              <el-input v-if="row.editing" v-model="row.name" size="small" />
              <span v-else>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="level" label="熟练度" width="200">
            <template #default="{ row }">
              <el-slider
                v-if="row.editing"
                v-model="row.level"
                :min="0"
                :max="100"
                show-input
              />
              <el-progress v-else :percentage="row.level" :color="getLevelColor(row.level)" />
            </template>
          </el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="150">
            <template #default="{ row }">
              <el-input-number
                v-if="row.editing"
                v-model="row.sortOrder"
                :min="0"
                size="small"
              />
              <span v-else>{{ row.sortOrder }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template #default="{ row }">
              <el-button
                v-if="row.editing"
                type="success"
                size="small"
                @click="saveSkill(row)"
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
                @click="editSkill(row)"
              >
                编辑
              </el-button>
              <el-button
                v-if="!row.editing"
                type="danger"
                size="small"
                @click="deleteSkill(row.id)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 添加技能对话框 -->
      <el-dialog
        v-model="showAddDialog"
        title="添加技能"
        width="500px"
        :before-close="handleAddDialogClose"
      >
        <el-form
          ref="addSkillFormRef"
          :model="newSkill"
          :rules="skillRules"
          label-width="80px"
        >
          <el-form-item label="技能名称" prop="name">
            <el-input v-model="newSkill.name" placeholder="请输入技能名称" />
          </el-form-item>
          <el-form-item label="熟练度" prop="level">
            <el-slider
              v-model="newSkill.level"
              :min="0"
              :max="100"
              show-input
            />
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder">
            <el-input-number
              v-model="newSkill.sortOrder"
              :min="0"
              placeholder="请输入排序"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button type="primary" @click="addSkill">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminAPI, frontAPI } from '@/api'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'

const skills = ref([])
const showAddDialog = ref(false)
const addSkillFormRef = ref()
const loading = ref(false)

const newSkill = reactive({
  name: '',
  level: 50,
  sortOrder: 0
})

const skillRules = {
  name: [{ required: true, message: '请输入技能名称', trigger: 'blur' }],
  level: [{ required: true, message: '请设置熟练度', trigger: 'blur' }],
  sortOrder: [{ required: true, message: '请设置排序', trigger: 'blur' }]
}

onMounted(async () => {
  loadSkills()
})

const loadSkills = async () => {
  try {
    const response = await frontAPI.getSkills()
    skills.value = response.data.map(skill => ({
      ...skill,
      editing: false
    }))
  } catch (error) {
    console.error('获取技能列表失败:', error)
    ElMessage.error('加载失败')
  }
}

const editSkill = (skill) => {
  skill.editing = true
}

const cancelEdit = (skill) => {
  skill.editing = false
  loadSkills()
}

const saveSkill = async (skill) => {
  try {
    await adminAPI.updateSkill(skill.id, {
      name: skill.name,
      level: skill.level,
      sortOrder: skill.sortOrder
    })
    ElMessage.success('技能更新成功')
    skill.editing = false
  } catch (error) {
    ElMessage.error('更新失败')
    console.error('更新失败:', error)
  }
}

const addSkill = async () => {
  if (!addSkillFormRef.value) return

  await addSkillFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await adminAPI.createSkill(newSkill)
        ElMessage.success('技能添加成功')
        showAddDialog.value = false
        resetNewSkill()
        loadSkills()
      } catch (error) {
        ElMessage.error('添加失败')
        console.error('添加失败:', error)
      }
    }
  })
}

const deleteSkill = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个技能吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await adminAPI.deleteSkill(id)
    ElMessage.success('删除成功')
    loadSkills()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const resetNewSkill = () => {
  newSkill.name = ''
  newSkill.level = 50
  newSkill.sortOrder = 0
}

const handleAddDialogClose = () => {
  resetNewSkill()
  addSkillFormRef.value?.clearValidate()
}

const getLevelColor = (level) => {
  if (level >= 80) return '#67c23a'
  if (level >= 60) return '#e6a23c'
  if (level >= 40) return '#f56c6c'
  return '#909399'
}
</script>

<style scoped>
.admin-skills {
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

.skills-content {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
}

.skills-table {
  margin-top: 20px;
}

.skills-table :deep(.el-table) {
  background: rgba(255, 255, 255, 0.02);
  color: #ffffff;
}

.skills-table :deep(.el-table th) {
  background: rgba(0, 245, 255, 0.1);
  color: #00f5ff;
  border-bottom: 2px solid rgba(0, 245, 255, 0.2);
}

.skills-table :deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.skills-table :deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.skills-table :deep(.el-slider__button) {
  background: #00f5ff;
}

.skills-table :deep(.el-progress-bar__outer) {
  background-color: rgba(255, 255, 255, 0.1);
}
</style>