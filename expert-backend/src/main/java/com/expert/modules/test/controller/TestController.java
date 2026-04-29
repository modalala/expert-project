package com.expert.modules.test.controller;

import com.expert.common.result.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "测试接口", description = "用于验证系统基础功能")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "数据库连接测试")
    @GetMapping("/db-conn")
    public ApiResponse<Map<String, Object>> testDbConnection() {
        return ApiResponse.success(new HashMap<>());
    }

    @Operation(summary = "系统信息")
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "专家库管理系统");
        info.put("version", "1.0.0");
        info.put("status", "running");
        return ApiResponse.success(info);
    }

    @Operation(summary = "密码加密测试")
    @PostMapping("/encode")
    public ApiResponse<Map<String, Object>> encodePassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String encoded = passwordEncoder.encode(rawPassword);
        Map<String, Object> result = new HashMap<>();
        result.put("rawPassword", rawPassword);
        result.put("encodedPassword", encoded);
        return ApiResponse.success(result);
    }

    @Operation(summary = "密码验证测试")
    @PostMapping("/verify")
    public ApiResponse<Map<String, Object>> verifyPassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("rawPassword");
        String encodedPassword = request.get("encodedPassword");
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        Map<String, Object> result = new HashMap<>();
        result.put("matches", matches);
        return ApiResponse.success(result);
    }
}