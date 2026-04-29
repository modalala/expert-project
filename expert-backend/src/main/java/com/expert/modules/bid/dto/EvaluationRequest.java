package com.expert.modules.bid.dto;

import lombok.Data;

@Data
public class EvaluationRequest {
    private Long committeeMemberId;
    private Double score;
    private Boolean isVeto;
    private String vetoReason;
    private String comment;
}