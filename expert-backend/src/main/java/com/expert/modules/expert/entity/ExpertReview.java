package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_review")
public class ExpertReview {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String reviewType;

    private String reviewStatus;

    private Long reviewerId;

    private LocalDateTime reviewTime;

    private String reviewComment;

    private String rejectReason;

    private String oaFlowId;

    private String oaFlowStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}