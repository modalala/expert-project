package com.expert.modules.extraction.dto;

import lombok.Data;

@Data
public class RejectRequest {
    private String rejectReason;
    private String rejectComment;
}