package com.expert.modules.dict.dto;

import lombok.Data;

import java.util.List;

@Data
public class DictResponse {
    private Long id;
    private String dictCode;
    private String dictName;
    private String description;
    private Integer status;
    private String createTime;
    private List<DictItemResponse> items;
}