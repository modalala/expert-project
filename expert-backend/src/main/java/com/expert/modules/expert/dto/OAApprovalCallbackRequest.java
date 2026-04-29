package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class OAApprovalCallbackRequest {
    private String oaFlowId;
    private String status;
    private String comment;
}