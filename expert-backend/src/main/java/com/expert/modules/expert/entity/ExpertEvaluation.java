package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("expert_evaluation")
public class ExpertEvaluation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long committeeMemberId;

    private Long expertId;

    private Long evaluatorId;

    private BigDecimal totalScore;

    private Integer isVeto;

    private String vetoReason;

    private String comment;

    private LocalDateTime evaluateTime;
}