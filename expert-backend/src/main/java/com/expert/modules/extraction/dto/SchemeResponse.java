package com.expert.modules.extraction.dto;

import lombok.Data;

@Data
public class SchemeResponse {
    private Long id;
    private Long planId;
    private String schemeName;
    private Integer extractionCount;
    private String expertTypes;
    private String expertLevels;
    private String expertiseAreas;
    private Integer excludeMonthCount;
    private Integer excludeMaxCount;
    private String excludeExperts;
    private Integer excludeManagement;
    private String createTime;
}