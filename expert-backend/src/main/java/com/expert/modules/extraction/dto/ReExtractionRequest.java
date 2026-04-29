package com.expert.modules.extraction.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReExtractionRequest {
    private Long planId;
    private Long schemeId;
    private List<Long> excludeExpertIds;
    private Integer extractionCount;
}