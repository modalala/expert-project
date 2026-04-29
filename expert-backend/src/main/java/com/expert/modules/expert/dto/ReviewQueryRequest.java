package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ReviewQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String reviewType;
    private String reviewStatus;
    private String name;
}