package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ExpertListResponse {
    private Long id;
    private String expertNo;
    private String name;
    private Integer gender;
    private String phone;
    private String email;
    private String expertType;
    private String expertLevel;
    private String workUnit;
    private String position;
    private String status;
    private String reviewStatus;
    private String createTime;
}