package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.Education;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EducationMapper extends BaseMapper<Education> {
}