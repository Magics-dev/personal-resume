package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.Skill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkillMapper extends BaseMapper<Skill> {
}