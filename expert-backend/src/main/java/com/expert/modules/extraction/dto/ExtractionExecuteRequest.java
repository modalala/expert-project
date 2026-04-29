package com.expert.modules.extraction.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExtractionExecuteRequest {
    private Long schemeId;
    private Integer extractionCount;
    private List<Long> manualExpertIds;
}