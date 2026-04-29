package com.expert.modules.extraction.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExtractionResultResponse {
    private Long extractionId;
    private Long planId;
    private String planNo;
    private String extractionTime;
    private List<ExtractedExpert> extractedExperts;
    private Integer totalCount;
    private Integer reserveCount;

    @Data
    public static class ExtractedExpert {
        private Long expertId;
        private String expertNo;
        private String name;
        private String expertType;
        private String expertLevel;
        private Integer extractionOrder;
        private Boolean isReserve;
    }
}