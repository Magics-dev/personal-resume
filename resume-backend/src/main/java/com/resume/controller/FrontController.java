package com.resume.controller;

import com.resume.entity.UserInfo;
import com.resume.service.UserInfoService;
import com.resume.entity.Skill;
import com.resume.service.SkillService;
import com.resume.entity.WorkExperience;
import com.resume.service.WorkExperienceService;
import com.resume.entity.Project;
import com.resume.service.ProjectService;
import com.resume.entity.Education;
import com.resume.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api")
public class FrontController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private WorkExperienceService workExperienceService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EducationService educationService;

    @GetMapping("/profile")
    public UserInfo getProfile() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("开发者");
        userInfo.setTitle("全栈开发工程师");
        userInfo.setIntro("热爱技术，追求卓越");
        userInfo.setEmail("developer@example.com");
        userInfo.setPhone("13800138000");
        userInfo.setGithub("https://github.com/developer");
        userInfo.setLinkedin("https://linkedin.com/in/developer");
        return userInfo;
    }

    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillService.list();
    }

    @GetMapping("/experiences")
    public List<WorkExperience> getExperiences() {
        return workExperienceService.list();
    }

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectService.list();
    }

    @GetMapping("/education")
    public List<Education> getEducation() {
        return educationService.list();
    }

    @PostMapping("/contact")
    public String contact(@RequestBody Map<String, String> contactData) {
        // 这里可以添加发送邮件等逻辑
        return "Message received successfully";
    }
}