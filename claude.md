# 个人简历网站项目 - 快速上手指南

## 技术栈与版本

### 后端
- **Spring Boot**: 3.2.0
- **Java**: 17
- **MyBatis-Plus**: 3.5.3.1
- **MySQL Connector**: 8.x
- **Lombok**: 1.18.30 (可选)

### 前端
- **Vue**: 3.3.4
- **TypeScript**: 支持
- **Vite**: 4.4.5
- **Element Plus**: 2.3.9
- **Vue Router**: 4.2.4
- **Pinia**: 2.1.6

## 目录结构

```
resume-project/
├── resume-backend/      # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/resume/    # Java源码
│   │   │   │   ├── controller/    # 控制器
│   │   │   │   ├── service/      # 服务层
│   │   │   │   ├── repository/   # 数据访问层
│   │   │   │   └── entity/      # 实体类
│   │   │   └── resources/
│   │   │       └── application.yml # 配置文件
│   └── pom.xml                 # Maven配置
├── resume-frontend/    # 前端项目
│   ├── src/
│   │   ├── views/     # 页面组件
│   │   ├── api/       # API调用
│   │   ├── router/    # 路由配置
│   │   └── utils/     # 工具函数
│   └── package.json  # 依赖配置
└── database/         # 数据库脚本
```

## 运行命令

### 后端启动
```bash
cd resume-backend
mvn spring-boot:run
```

### 前端开发
```bash
cd resume-frontend
npm install
npm run dev
```

## 关键配置位置

### 后端配置
- **端口配置**: `resume-backend/src/main/resources/application.yml:2`
- **数据库配置**: `resume-backend/src/main/resources/application.yml:6-9`
- **CORS配置**: `resume-backend/src/main/resources/application.yml:17-21`
- **MyBatis配置**: `resume-backend/src/main/resources/application.yml:11-14`

### 前端配置
- **API基础URL**: `resume-frontend/src/api/index.js:4`
- **路由配置**: `resume-frontend/src/router/index.js`
- **依赖管理**: `resume-frontend/package.json`

## 硬性约束

### 版本兼容性
- **Spring Boot 3.2.0** 必须使用 **MyBatis-Plus 3.5.3.1**
- **Java 17** 是必需的运行环境
- **MySQL 5.7+** 数据库版本

### 端口使用
- **后端**: 8080 (可配置)
- **前端**: 3004 (可配置)

### 安全要求
- **密码**: 使用强密码
- **CORS**: 仅允许指定 origins
- **数据验证**: 前后端都需要数据验证

## API规范

### 前台API (http://localhost:8080/api)
- `GET /profile` - 获取个人信息
- `GET /skills` - 获取技能列表
- `GET /experiences` - 获取工作经历
- `GET /projects` - 获取项目列表
- `GET /education` - 获取教育背景
- `POST /contact` - 提交联系表单

### 后台API (http://localhost:8080/api/admin)
- `POST /login` - 管理员登录 (admin/admin123)
- `GET /profile` - 获取个人信息
- `PUT /profile` - 更新个人信息
- `POST /skills` - 创建技能
- `PUT /skills/{id}` - 更新技能
- `DELETE /skills/{id}` - 删除技能
- `POST /experiences` - 创建工作经历
- `PUT /experiences/{id}` - 更新工作经历
- `DELETE /experiences/{id}` - 删除工作经历
- `POST /projects` - 创建项目
- `PUT /projects/{id}` - 更新项目
- `DELETE /projects/{id}` - 删除项目
- `POST /education` - 创建教育背景
- `PUT /education/{id}` - 更新教育背景
- `DELETE /education/{id}` - 删除教育背景

## 数据模型

### UserInfo
```json
{
  "id": 1,
  "name": "string",
  "title": "string",
  "intro": "string",
  "email": "string",
  "phone": "string",
  "github": "string",
  "linkedin": "string"
}
```

### Skill
```json
{
  "id": 1,
  "name": "string",
  "level": "integer",
  "category": "string"
}
```

### WorkExperience
```json
{
  "id": 1,
  "company": "string",
  "position": "string",
  "startDate": "date",
  "endDate": "date",
  "description": "string"
}
```

### Project
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "techStack": "string",
  "link": "string"
}
```

### Education
```json
{
  "id": 1,
  "school": "string",
  "degree": "string",
  "major": "string",
  "startDate": "date",
  "endDate": "date"
}
```

## 注意事项

1. **数据库初始化**: 需要手动创建 `resume_db` 数据库
2. **API认证**: 后台API使用简单登录认证 (admin/admin123)
3. **前端路由**: 前端使用 Vue Router 进行页面导航
4. **状态管理**: 使用 Pinia 进行状态管理
5. **UI组件**: 使用 Element Plus 组件库

---

**自问**: AI 只看这个能正确操作项目吗？
✓ 是的，这个文档包含了项目特有的关键信息，包括技术栈版本、目录结构、运行命令、关键配置位置、硬性约束和API规范，足以让AI正确操作项目。