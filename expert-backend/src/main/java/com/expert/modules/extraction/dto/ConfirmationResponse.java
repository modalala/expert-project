package com.expert.modules.extraction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfirmationResponse {

    private Long id;

    private Long extractionId;

    private Long planId;

    private Long expertId;

    private String expertName;

    private String expertPhone;

    private String expertType;

    private LocalDateTime extractionTime;

    private LocalDateTime notifyTime;

    private LocalDateTime expireTime;

    private String confirmStatus;
}