package com.expert.modules.dict.dto;

import lombok.Data;

@Data
public class DictItemCreateRequest {
    private String dictCode;
    private String itemCode;
    private String itemName;
    private Integer sortOrder;
}