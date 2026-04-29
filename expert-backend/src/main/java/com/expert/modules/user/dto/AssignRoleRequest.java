package com.expert.modules.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignRoleRequest {
    private List<Long> roleIds;
}