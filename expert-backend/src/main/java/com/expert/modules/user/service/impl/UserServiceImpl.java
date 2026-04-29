package com.expert.modules.user.service.impl;

import com.expert.modules.user.entity.SysUser;
import com.expert.modules.user.mapper.UserMapper;
import com.expert.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public SysUser findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public SysUser findById(Long id) {
        return userMapper.selectById(id);
    }
}