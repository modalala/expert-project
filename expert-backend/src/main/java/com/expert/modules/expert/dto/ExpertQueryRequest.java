package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ExpertQueryRequest {
    private String name;
    private String expertType;
    private String expertLevel;
    private String status;
    private String reviewStatus;
}