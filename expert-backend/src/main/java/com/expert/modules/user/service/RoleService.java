package com.expert.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.user.dto.*;
import com.expert.modules.user.entity.SysPermission;
import com.expert.modules.user.entity.SysRole;
import com.expert.modules.user.entity.SysRolePermission;
import com.expert.modules.user.mapper.PermissionMapper;
import com.expert.modules.user.mapper.RoleMapper;
import com.expert.modules.user.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    public List<RoleResponse> getRoleList() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysRole::getId);
        List<SysRole> roles = roleMapper.selectList(wrapper);
        return roles.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public RoleResponse getRoleById(Long id) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) {
            return null;
        }
        return toResponse(role);
    }

    @Transactional
    public Long createRole(RoleCreateRequest request) {
        SysRole role = new SysRole();
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());
        role.setCreateTime(LocalDateTime.now());
        roleMapper.insert(role);
        return role.getId();
    }

    @Transactional
    public void updateRole(Long id, RoleUpdateRequest request) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("Role not found");
        }
        if (request.getRoleName() != null) {
            role.setRoleName(request.getRoleName());
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            role.setStatus(request.getStatus());
        }
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        roleMapper.deleteById(id);
    }

    public List<Long> getRolePermissions(Long roleId) {
        return rolePermissionMapper.findPermissionIdsByRoleId(roleId);
    }

    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Delete existing permissions
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);

        // Add new permissions
        for (Long permId : permissionIds) {
            SysRolePermission rp = new SysRolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permId);
            rolePermissionMapper.insert(rp);
        }
    }

    public List<PermissionTreeNode> getPermissionTree() {
        List<SysPermission> allPermissions = permissionMapper.selectList(
            new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getSortOrder)
        );
        return buildTree(allPermissions, 0L);
    }

    private List<PermissionTreeNode> buildTree(List<SysPermission> permissions, Long parentId) {
        List<PermissionTreeNode> nodes = new ArrayList<>();
        for (SysPermission perm : permissions) {
            if (perm.getParentId() != null && perm.getParentId().equals(parentId)) {
                PermissionTreeNode node = new PermissionTreeNode();
                node.setId(perm.getId());
                node.setPermCode(perm.getPermCode());
                node.setPermName(perm.getPermName());
                node.setPermType(perm.getPermType());
                node.setParentId(perm.getParentId());
                node.setPath(perm.getPath());
                node.setIcon(perm.getIcon());
                node.setSort(perm.getSortOrder());
                node.setChildren(buildTree(permissions, perm.getId()));
                nodes.add(node);
            }
        }
        return nodes;
    }

    private RoleResponse toResponse(SysRole role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setStatus(role.getStatus());
        response.setCreateTime(role.getCreateTime() != null ? role.getCreateTime().toString() : null);
        response.setPermissionIds(rolePermissionMapper.findPermissionIdsByRoleId(role.getId()));
        return response;
    }
}