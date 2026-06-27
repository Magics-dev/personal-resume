<template>
  <div class="admin-projects">
    <div class="admin-header">
      <el-button type="primary" @click="$router.push('/admin/dashboard')">
        <el-icon><ArrowLeft /></el-icon>
        返回仪表盘
      </el-button>
      <h1>项目管理</h1>
      <el-button type="success" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加项目
      </el-button>
    </div>

    <div class="projects-content">
      <el-table :data="projects" style="width: 100%" stripe>
        <el-table-column prop="name" label="项目名称" width="200">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.name" size="small" />
            <span v-else>{{ row.name }}</span>
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
        <el-table-column prop="techStack" label="技术栈">
          <template #default="{ row }">
            <el-input v-if="row.editing" v-model="row.techStack" size="small" />
            <div v-else class="tech-tags">
              <el-tag
                v-for="tech in getTechArray(row.techStack)"
                :key="tech"
                type="info"
                size="small"
              >
                {{ tech }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="100">
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
              @click="saveProject(row)"
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
              @click="editProject(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="!row.editing"
              type="danger"
              size="small"
              @click="deleteProject(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加项目对话框 -->
      <el-dialog
        v-model="showAddDialog"
        title="添加项目"
        width="600px"
        :before-close="handleAddDialogClose"
      >
        <el-form
          ref="addProjectFormRef"
          :model="newProject"
          :rules="projectRules"
          label-width="100px"
        >
          <el-form-item label="项目名称" prop="name">
            <el-input v-model="newProject.name" placeholder="请输入项目名称" />
          </el-form-item>
          <el-form-item label="项目描述" prop="description">
            <el-input
              v-model="newProject.description"
              type="textarea"
              :rows="3"
              placeholder="请输入项目描述"
            />
          </el-form-item>
          <el-form-item label="技术栈" prop="techStack">
            <el-input
              v-model="newProject.techStack"
              placeholder="请输入技术栈，用逗号分隔"
            />
          </el-form-item>
          <el-form-item label="演示地址" prop="demoUrl">
            <el-input
              v-model="newProject.demoUrl"
              placeholder="请输入在线演示地址"
            />
          </el-form-item>
          <el-form-item label="GitHub地址" prop="githubUrl">
            <el-input
              v-model="newProject.githubUrl"
              placeholder="请输入GitHub地址"
            />
          </el-form-item>
          <el-form-item label="项目图片" prop="imageUrl">
            <el-input
              v-model="newProject.imageUrl"
              placeholder="请输入图片地址"
            />
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder">
            <el-input-number
              v-model="newProject.sortOrder"
              :min="0"
              placeholder="请输入排序"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button type="primary" @click="addProject">确定</el-button>
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

const projects = ref([])
const showAddDialog = ref(false)
const addProjectFormRef = ref()
const loading = ref(false)

const newProject = reactive({
  name: '',
  description: '',
  techStack: '',
  demoUrl: '',
  githubUrl: '',
  imageUrl: '',
  sortOrder: 0
})

const projectRules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入项目描述', trigger: 'blur' }]
}

onMounted(async () => {
  loadProjects()
})

const loadProjects = async () => {
  try {
    const response = await frontAPI.getProjects()
    projects.value = response.data.map(project => ({
      ...project,
      editing: false
    }))
  } catch (error) {
    console.error('获取项目列表失败:', error)
    ElMessage.error('加载失败')
  }
}

const getTechArray = (techString) => {
  if (!techString) return []
  return techString.split(',').map(tech => tech.trim())
}

const editProject = (project) => {
  project.editing = true
}

const cancelEdit = (project) => {
  project.editing = false
  loadProjects()
}

const saveProject = async (project) => {
  try {
    await adminAPI.updateProject(project.id, {
      name: project.name,
      description: project.description,
      techStack: project.techStack,
      demoUrl: project.demoUrl,
      githubUrl: project.githubUrl,
      imageUrl: project.imageUrl,
      sortOrder: project.sortOrder
    })
    ElMessage.success('项目更新成功')
    project.editing = false
  } catch (error) {
    ElMessage.error('更新失败')
    console.error('更新失败:', error)
  }
}

const addProject = async () => {
  if (!addProjectFormRef.value) return

  await addProjectFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await adminAPI.createProject(newProject)
        ElMessage.success('项目添加成功')
        showAddDialog.value = false
        resetNewProject()
        loadProjects()
      } catch (error) {
        ElMessage.error('添加失败')
        console.error('添加失败:', error)
      }
    }
  })
}

const deleteProject = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个项目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await adminAPI.deleteProject(id)
    ElMessage.success('删除成功')
    loadProjects()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const resetNewProject = () => {
  newProject.name = ''
  newProject.description = ''
  newProject.techStack = ''
  newProject.demoUrl = ''
  newProject.githubUrl = ''
  newProject.imageUrl = ''
  newProject.sortOrder = 0
}

const handleAddDialogClose = () => {
  resetNewProject()
  addProjectFormRef.value?.clearValidate()
}
</script>

<style scoped>
.admin-projects {
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

.projects-content {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 30px;
}

.projects-content :deep(.el-table) {
  background: rgba(255, 255, 255, 0.02);
  color: #ffffff;
}

.projects-content :deep(.el-table th) {
  background: rgba(0, 245, 255, 0.1);
  color: #00f5ff;
  border-bottom: 2px solid rgba(0, 245, 255, 0.2);
}

.projects-content :deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.projects-content :deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.projects-content :deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}
</style>