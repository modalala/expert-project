package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ReviewRejectRequest {
    private String rejectReason;
    private String comment;
}