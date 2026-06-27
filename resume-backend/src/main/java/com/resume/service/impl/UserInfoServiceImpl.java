package com.resume.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.entity.UserInfo;
import com.resume.repository.UserInfoMapper;
import com.resume.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}