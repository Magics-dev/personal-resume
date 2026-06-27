CREATE DATABASE IF NOT EXISTS resume_db;
USE resume_db;

-- 用户基本信息表
CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(100),
    avatar VARCHAR(255),
    intro TEXT,
    email VARCHAR(100),
    phone VARCHAR(20),
    github VARCHAR(255),
    linkedin VARCHAR(255),
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 技能分类表
CREATE TABLE IF NOT EXISTS skill_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    sort_order INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 技能表
CREATE TABLE IF NOT EXISTS skill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT,
    name VARCHAR(100) NOT NULL,
    level INT DEFAULT 50,
    sort_order INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES skill_category(id)
);

-- 工作经历表
CREATE TABLE IF NOT EXISTS work_experience (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    technologies TEXT,
    sort_order INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 项目表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    tech_stack TEXT,
    demo_url VARCHAR(255),
    github_url VARCHAR(255),
    image_url VARCHAR(255),
    sort_order INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 教育背景表
CREATE TABLE IF NOT EXISTS education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    school VARCHAR(100) NOT NULL,
    major VARCHAR(100) NOT NULL,
    degree VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    sort_order INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 网站配置表
CREATE TABLE IF NOT EXISTS site_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE,
    config_value TEXT,
    description VARCHAR(255),
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入默认数据
INSERT INTO user_info (name, title, intro, email, github) VALUES
('张三', '全栈开发工程师', '热爱技术，专注于Web开发，拥有5年开发经验', 'zhangsan@example.com', 'https://github.com/zhangsan');

INSERT INTO skill_category (name, sort_order) VALUES
('前端技术', 1), ('后端技术', 2), ('数据库', 3), ('工具软件', 4);

INSERT INTO skill (category_id, name, level, sort_order) VALUES
(1, 'Vue.js', 90, 1),
(1, 'React', 85, 2),
(1, 'TypeScript', 80, 3),
(2, 'Spring Boot', 85, 1),
(2, 'Node.js', 80, 2),
(2, 'Python', 75, 3),
(3, 'MySQL', 85, 1),
(3, 'Redis', 80, 2),
(4, 'Git', 90, 1),
(4, 'Docker', 75, 2);

INSERT INTO work_experience (company, position, start_date, end_date, description, technologies, sort_order) VALUES
('ABC科技有限公司', '高级前端工程师', '2022-01-01', '2024-12-31', '负责公司核心产品前端架构设计和开发', 'Vue.js, TypeScript, Element Plus', 1),
('XYZ互联网公司', '前端开发工程师', '2020-06-01', '2021-12-31', '参与多个Web应用开发，负责UI组件库建设', 'React, JavaScript, Ant Design', 2);

INSERT INTO project (name, description, tech_stack, demo_url, github_url, image_url, sort_order) VALUES
('个人博客系统', '基于Vue3和Spring Boot的博客系统，支持Markdown编辑', 'Vue3, Spring Boot, MySQL, Element Plus', 'https://demo.example.com', 'https://github.com/zhangsan/blog', 'https://via.placeholder.com/400x200', 1),
('电商平台', 'B2C电商平台，包含商品管理、订单系统、支付功能', 'React, Node.js, MongoDB, Express', 'https://shop.example.com', 'https://github.com/zhangsan/shop', 'https://via.placeholder.com/400x200', 2);

INSERT INTO education (school, major, degree, start_date, end_date, description, sort_order) VALUES
('某某大学', '计算机科学与技术', '本科', '2016-09-01', '2020-06-30', '主修课程：数据结构、算法设计、软件工程等', 1);

INSERT INTO site_config (config_key, config_value, description) VALUES
('site_name', '张三的个人简历', '网站名称'),
('site_description', '全栈开发工程师的个人简历网站', '网站描述'),
('site_keywords', '张三,全栈开发,前端,后端,Vue,Spring Boot', '网站关键词');