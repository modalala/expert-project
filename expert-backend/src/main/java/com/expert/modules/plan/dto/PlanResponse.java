package com.expert.modules.plan.dto;

import lombok.Data;

@Data
public class PlanResponse {
    private Long id;
    private String planNo;
    private String planName;
    private String projectName;
    private String bidTime;
    private String bidLocation;
    private String extractionMode;
    private Integer committeeSize;
    private String planStatus;
    private String createTime;
}