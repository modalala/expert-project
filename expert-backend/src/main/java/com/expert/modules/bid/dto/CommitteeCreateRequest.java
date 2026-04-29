package com.expert.modules.bid.dto;

import lombok.Data;

@Data
public class CommitteeCreateRequest {
    private Long planId;
    private String committeeName;
}