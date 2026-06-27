<template>
  <div class="skills">
    <div class="skills-header">
      <el-button
        type="primary"
        @click="$router.push('/')"
        class="return-btn"
      >
        <el-icon><ArrowLeft /></el-icon>
      </el-button>
      <h1>技能展示</h1>
      <div class="header-line"></div>
    </div>

    <div class="skills-content">
      <div class="skills-categories">
        <div v-for="category in categories" :key="category.name" class="category-section">
          <h2>{{ category.name }}</h2>
          <div class="skills-list">
            <div v-for="skill in category.skills" :key="skill.name" class="skill-item">
              <div class="skill-info">
                <span class="skill-name">{{ skill.name }}</span>
                <span class="skill-level">{{ skill.level }}%</span>
              </div>
              <div class="skill-bar">
                <div class="skill-progress" :style="{ width: skill.level + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="skills-chart">
        <h2>技能雷达图</h2>
        <div ref="chartContainer" class="chart-container"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { frontAPI } from '@/api'
import * as echarts from 'echarts'

const skills = ref([])
const categories = ref([])
const chartContainer = ref(null)

onMounted(async () => {
  try {
    const response = await frontAPI.getSkills()
    skills.value = response.data

    // 按分类组织技能
    const categoryMap = new Map()
    skills.value.forEach(skill => {
      if (!categoryMap.has(skill.categoryId)) {
        categoryMap.set(skill.categoryId, {
          name: getCategoryName(skill.categoryId),
          skills: []
        })
      }
      categoryMap.get(skill.categoryId).skills.push(skill)
    })

    // 排序
    categories.value = Array.from(categoryMap.values()).map(cat => ({
      ...cat,
      skills: cat.skills.sort((a, b) => a.sortOrder - b.sortOrder)
    }))

    // 渲染图表
    nextTick(() => {
      renderChart()
    })
  } catch (error) {
    console.error('获取技能数据失败:', error)
  }
})

const getCategoryName = (categoryId) => {
  const names = {
    1: '前端技术',
    2: '后端技术',
    3: '数据库',
    4: '工具软件'
  }
  return names[categoryId] || '其他'
}

const renderChart = () => {
  if (!chartContainer.value) return

  const chart = echarts.init(chartContainer.value)

  const skillNames = skills.value.map(s => s.name)
  const skillLevels = skills.value.map(s => s.level)

  const option = {
    radar: {
      indicator: skillNames.map(name => ({
        name: name,
        max: 100
      })),
      shape: 'polygon',
      splitNumber: 5,
      axisName: {
        color: '#00f5ff',
        fontSize: 14
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      },
      splitArea: {
        show: false
      },
      axisLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      }
    },
    series: [{
      name: '技能水平',
      type: 'radar',
      data: [{
        value: skillLevels,
        name: '技能',
        lineStyle: {
          color: '#00f5ff',
          width: 2
        },
        areaStyle: {
          color: 'rgba(0, 245, 255, 0.2)'
        },
        itemStyle: {
          color: '#00f5ff'
        }
      }]
    }]
  }

  chart.setOption(option)

  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}
</script>

<style scoped>
.skills {
  padding: 60px 20px;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 100%);
}

.skills-header {
  text-align: center;
  margin-bottom: 60px;
}

.skills-header {
  text-align: center;
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  position: relative;
}

.skills-header h1 {
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

.skills-content {
  max-width: 1200px;
  margin: 0 auto;
}

.skills-categories {
  margin-bottom: 60px;
}

.category-section {
  margin-bottom: 40px;
}

.category-section h2 {
  color: #00f5ff;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.skills-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.skill-item {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.skill-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.skill-name {
  color: #ffffff;
  font-size: 1.1rem;
}

.skill-level {
  color: #00f5ff;
  font-weight: bold;
}

.skill-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

.skill-progress {
  height: 100%;
  background: linear-gradient(90deg, #00f5ff, #ff00ff);
  border-radius: 4px;
  transition: width 1s ease-in-out;
}

.skills-chart {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.skills-chart h2 {
  color: #00f5ff;
  margin-bottom: 20px;
  font-size: 1.5rem;
  text-align: center;
}

.chart-container {
  width: 100%;
  height: 400px;
}
</style>