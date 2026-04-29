package com.expert.modules.expert.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDTO {

    private String school;

    private String major;

    private String education;

    private String degree;

    private LocalDate graduationDate;
}