package com.expert.modules.expert.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExpertRegisterRequest {

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

    private String source;

    private List<CertificateDTO> certificates;

    private List<EducationDTO> educations;

    private List<AchievementDTO> achievements;
}