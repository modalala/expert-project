package com.expert.modules.user.dto;

import lombok.Data;

@Data
public class RoleUpdateRequest {
    private String roleName;
    private String description;
    private Integer status;
}