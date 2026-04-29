package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class OAApprovalResult {
    private Long expertId;
    private String oaFlowId;
    private String reviewStatus;
    private String expertNo;
    private String generatedPassword;
}