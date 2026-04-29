package com.expert.modules.dict.dto;

import lombok.Data;

@Data
public class DictItemResponse {
    private Long id;
    private String dictCode;
    private String itemCode;
    private String itemName;
    private Integer sortOrder;
    private Integer status;
    private String createTime;
}