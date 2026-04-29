package com.expert.modules.expert.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewDetailResponse {
    private Long id;
    private String expertNo;
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
    private String status;
    private String reviewStatus;
    private String source;
    private List<CertificateDTO> certificates;
    private List<EducationDTO> educations;
    private List<AchievementDTO> achievements;
}