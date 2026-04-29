package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ReviewListResponse {
    private Long expertId;
    private String expertNo;
    private String name;
    private String phone;
    private String expertType;
    private String expertLevel;
    private String workUnit;
    private String reviewStatus;
    private String source;
    private String createTime;
}