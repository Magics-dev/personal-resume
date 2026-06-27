package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.SkillCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkillCategoryMapper extends BaseMapper<SkillCategory> {
}