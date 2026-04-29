package com.expert.modules.user.dto;

import lombok.Data;

@Data
public class UserQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String username;
    private String realName;
    private Integer status;
}