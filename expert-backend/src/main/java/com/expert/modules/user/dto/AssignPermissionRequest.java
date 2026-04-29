package com.expert.modules.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignPermissionRequest {
    private List<Long> permissionIds;
}