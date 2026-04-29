package com.expert.modules.bid.dto;

import lombok.Data;

@Data
public class EvaluationResponse {
    private Long id;
    private Long committeeMemberId;
    private Long expertId;
    private String expertNo;
    private String expertName;
    private Long evaluatorId;
    private String evaluatorName;
    private Double totalScore;
    private Boolean isVeto;
    private String vetoReason;
    private String comment;
    private String evaluateTime;
}