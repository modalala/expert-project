package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ExpertRegisterResponse {

    private Long id;

    private String name;

    private String status;

    private String reviewStatus;
}