package com.expert.modules.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("procurement_plan")
public class ProcurementPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String planNo;

    private String planName;

    private String projectName;

    private LocalDateTime bidTime;

    private String bidLocation;

    private String extractionMode;

    private Integer committeeSize;

    private String planStatus;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createBy;

    private Long updateBy;
}