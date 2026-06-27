package com.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}