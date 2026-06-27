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
@RequestMapping("/api/admin")
public class AdminController {

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

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginData) {
        // 简单的登录逻辑，实际应该使用JWT
        String username = loginData.get("username");
        String password = loginData.get("password");

        if ("admin".equals(username) && "admin123".equals(password)) {
            return "success";
        }
        return "fail";
    }

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

    @PutMapping("/profile")
    public boolean updateProfile(@RequestBody UserInfo userInfo) {
        return userInfoService.updateById(userInfo);
    }

    @PostMapping("/skills")
    public boolean createSkill(@RequestBody Skill skill) {
        return skillService.save(skill);
    }

    @PutMapping("/skills/{id}")
    public boolean updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        skill.setId(id);
        return skillService.updateById(skill);
    }

    @DeleteMapping("/skills/{id}")
    public boolean deleteSkill(@PathVariable Long id) {
        return skillService.removeById(id);
    }

    @PostMapping("/experiences")
    public boolean createExperience(@RequestBody WorkExperience experience) {
        return workExperienceService.save(experience);
    }

    @PutMapping("/experiences/{id}")
    public boolean updateExperience(@PathVariable Long id, @RequestBody WorkExperience experience) {
        experience.setId(id);
        return workExperienceService.updateById(experience);
    }

    @DeleteMapping("/experiences/{id}")
    public boolean deleteExperience(@PathVariable Long id) {
        return workExperienceService.removeById(id);
    }

    @PostMapping("/projects")
    public boolean createProject(@RequestBody Project project) {
        return projectService.save(project);
    }

    @PutMapping("/projects/{id}")
    public boolean updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return projectService.updateById(project);
    }

    @DeleteMapping("/projects/{id}")
    public boolean deleteProject(@PathVariable Long id) {
        return projectService.removeById(id);
    }

    @PostMapping("/education")
    public boolean createEducation(@RequestBody Education education) {
        return educationService.save(education);
    }

    @PutMapping("/education/{id}")
    public boolean updateEducation(@PathVariable Long id, @RequestBody Education education) {
        education.setId(id);
        return educationService.updateById(education);
    }

    @DeleteMapping("/education/{id}")
    public boolean deleteEducation(@PathVariable Long id) {
        return educationService.removeById(id);
    }
}