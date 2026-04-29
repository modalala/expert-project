package com.expert.modules.plan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanUpdateRequest {
    private String planName;
    private String projectName;
    private LocalDateTime bidTime;
    private String bidLocation;
    private String extractionMode;
    private Integer committeeSize;
}