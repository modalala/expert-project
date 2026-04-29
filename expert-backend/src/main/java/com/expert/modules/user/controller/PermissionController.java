package com.expert.modules.user.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.user.dto.PermissionTreeNode;
import com.expert.modules.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permission Management", description = "Permission API")
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Permission tree")
    @GetMapping("/tree")
    public ApiResponse<List<PermissionTreeNode>> tree() {
        return ApiResponse.success(roleService.getPermissionTree());
    }
}