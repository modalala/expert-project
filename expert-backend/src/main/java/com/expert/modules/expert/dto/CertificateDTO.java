package com.expert.modules.expert.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificateDTO {

    private String certName;

    private String certNo;

    private String issueOrg;

    private LocalDate issueDate;

    private LocalDate validDate;

    private String certUrl;
}