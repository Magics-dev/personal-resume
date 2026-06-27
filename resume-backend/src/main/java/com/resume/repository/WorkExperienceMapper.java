package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.WorkExperience;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkExperienceMapper extends BaseMapper<WorkExperience> {
}