package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_extraction")
public class ExpertExtraction {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private Long expertId;

    private LocalDateTime extractionTime;

    private Integer extractionOrder;

    private Integer isReserve;
}