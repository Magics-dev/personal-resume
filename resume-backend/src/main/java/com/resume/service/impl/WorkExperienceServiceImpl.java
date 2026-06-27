package com.resume.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.entity.WorkExperience;
import com.resume.repository.WorkExperienceMapper;
import com.resume.service.WorkExperienceService;
import org.springframework.stereotype.Service;

@Service
public class WorkExperienceServiceImpl extends ServiceImpl<WorkExperienceMapper, WorkExperience> implements WorkExperienceService {
}