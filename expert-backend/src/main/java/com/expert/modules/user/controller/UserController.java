package com.expert.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.user.dto.*;
import com.expert.modules.user.entity.SysUser;
import com.expert.modules.user.mapper.UserMapper;
import com.expert.modules.user.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "用户管理", description = "用户CRUD接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    public ApiResponse<PageResult<UserResponse>> list(UserQueryRequest request) {
        Page<SysUser> page = new Page<>(request.getPage(), request.getSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (request.getUsername() != null) {
            wrapper.like(SysUser::getUsername, request.getUsername());
        }
        if (request.getRealName() != null) {
            wrapper.like(SysUser::getRealName, request.getRealName());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = userMapper.selectPage(page, wrapper);

        List<UserResponse> records = result.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.success(PageResult.of(records, result.getTotal(), request.getPage(), request.getSize()));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(@PathVariable Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        return ApiResponse.success(toResponse(user));
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public ApiResponse<Long> create(@RequestBody UserCreateRequest request) {
        SysUser existing = userMapper.findByUsername(request.getUsername());
        if (existing != null) {
            return ApiResponse.error(1001, "用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(request.getStatus());
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);
        return ApiResponse.success(user.getId());
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
        return ApiResponse.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return ApiResponse.success();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password")
    public ApiResponse<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return ApiResponse.success();
    }

    @Operation(summary = "切换状态")
    @PutMapping("/{id}/status")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return ApiResponse.success();
    }

    @Operation(summary = "获取用户角色")
    @GetMapping("/{id}/roles")
    public ApiResponse<List<Long>> getUserRoles(@PathVariable Long id) {
        return ApiResponse.success(userRoleService.getUserRoles(id));
    }

    @Operation(summary = "分配用户角色")
    @PostMapping("/{id}/roles")
    public ApiResponse<Void> assignRoles(@PathVariable Long id, @RequestBody AssignRoleRequest request) {
        userRoleService.assignRolesToUser(id, request.getRoleIds());
        return ApiResponse.success();
    }

    private UserResponse toResponse(SysUser user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime() != null ? user.getCreateTime().toString() : null);
        return response;
    }
}