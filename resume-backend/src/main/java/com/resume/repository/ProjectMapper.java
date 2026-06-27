package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.Project;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}