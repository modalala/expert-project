package com.expert.modules.expert.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExpertPortraitResponse {
    private Long expertId;
    private String expertNo;
    private String name;
    private String expertType;
    private String expertLevel;

    // Statistics
    private Integer totalExtractions;
    private Integer confirmedCount;
    private Integer rejectedCount;
    private Double avgScore;
    private Integer vetoCount;

    // History
    private List<ExtractionHistory> extractionHistory;
    private List<StatusHistory> statusHistory;
    private List<EvaluationHistory> evaluationHistory;

    @Data
    public static class ExtractionHistory {
        private Long id;
        private String planNo;
        private String projectName;
        private String extractionTime;
        private String result;
    }

    @Data
    public static class StatusHistory {
        private Long id;
        private String oldStatus;
        private String newStatus;
        private String reason;
        private String operateTime;
    }

    @Data
    public static class EvaluationHistory {
        private Long id;
        private String projectName;
        private Double score;
        private String evaluateTime;
        private String comment;
    }
}