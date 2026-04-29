package com.expert.modules.message.dto;

import lombok.Data;

@Data
public class TemplateResponse {
    private Long id;
    private String templateCode;
    private String templateName;
    private String templateType;
    private String templateContent;
    private String variables;
    private Integer status;
    private String createTime;
}