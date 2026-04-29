package com.expert.modules.user.service;

import com.expert.modules.user.entity.SysUser;

public interface UserService {

    SysUser findByUsername(String username);

    SysUser findById(Long id);
}