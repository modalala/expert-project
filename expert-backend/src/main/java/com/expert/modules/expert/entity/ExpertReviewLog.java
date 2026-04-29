package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_review_log")
public class ExpertReviewLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reviewId;

    private String operateType;

    private Long operatorId;

    private String operatorName;

    private String comment;

    private LocalDateTime operateTime;
}