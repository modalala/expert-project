package com.expert.modules.dict.dto;

import lombok.Data;

@Data
public class DictCreateRequest {
    private String dictCode;
    private String dictName;
    private String description;
}