package com.expert.modules.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleResponse {
    private Long id;
    private String roleCode;
    private String roleName;
    private String description;
    private Integer status;
    private String createTime;
    private List<Long> permissionIds;
}