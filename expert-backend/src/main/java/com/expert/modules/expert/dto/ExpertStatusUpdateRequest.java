package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ExpertStatusUpdateRequest {
    private String status;
    private String reason;
}