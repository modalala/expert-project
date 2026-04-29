package com.expert.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest {
    @NotBlank(message = "roleCode cannot be empty")
    private String roleCode;

    @NotBlank(message = "roleName cannot be empty")
    private String roleName;

    private String description;

    private Integer status = 1;
}