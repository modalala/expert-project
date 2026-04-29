package com.expert.modules.user.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.user.dto.*;
import com.expert.modules.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Management", description = "Role CRUD API")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Role list")
    @GetMapping("/list")
    public ApiResponse<List<RoleResponse>> list() {
        return ApiResponse.success(roleService.getRoleList());
    }

    @Operation(summary = "Role detail")
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getById(@PathVariable Long id) {
        RoleResponse role = roleService.getRoleById(id);
        if (role == null) {
            return ApiResponse.error(404, "Role not found");
        }
        return ApiResponse.success(role);
    }

    @Operation(summary = "Create role")
    @PostMapping
    public ApiResponse<Long> create(@RequestBody RoleCreateRequest request) {
        return ApiResponse.success(roleService.createRole(request));
    }

    @Operation(summary = "Update role")
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        roleService.updateRole(id, request);
        return ApiResponse.success();
    }

    @Operation(summary = "Delete role")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Get role permissions")
    @GetMapping("/{id}/permissions")
    public ApiResponse<List<Long>> getPermissions(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRolePermissions(id));
    }

    @Operation(summary = "Assign permissions to role")
    @PostMapping("/{id}/permissions")
    public ApiResponse<Void> assignPermissions(@PathVariable Long id, @RequestBody AssignPermissionRequest request) {
        roleService.assignPermissionsToRole(id, request.getPermissionIds());
        return ApiResponse.success();
    }
}