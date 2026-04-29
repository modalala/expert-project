package com.expert.modules.expert.dto;

import lombok.Data;

@Data
public class ExpertUpdateRequest {
    private String name;
    private Integer gender;
    private String phone;
    private String email;
    private String idCard;
    private String expertType;
    private String expertLevel;
    private String expertiseAreas;
    private String workUnit;
    private String position;
    private String introduction;
}