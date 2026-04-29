package com.expert.modules.extraction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("extraction_scheme")
public class ExtractionScheme {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private String schemeName;

    private Integer extractionCount;

    private String expertTypes;

    private String expertLevels;

    private String expertiseAreas;

    private Integer excludeMonthCount;

    private Integer excludeMaxCount;

    private String excludeExperts;

    private Integer excludeManagement;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}