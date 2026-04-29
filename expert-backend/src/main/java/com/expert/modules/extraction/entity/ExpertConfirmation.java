package com.expert.modules.extraction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_confirmation")
public class ExpertConfirmation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long extractionId;

    private Long planId;

    private Long expertId;

    private String confirmStatus;  // PENDING, CONFIRMED, REJECTED, TIMEOUT

    private LocalDateTime confirmTime;

    private String rejectReason;

    private String rejectComment;

    private LocalDateTime notifyTime;

    private String ssoToken;

    private LocalDateTime expireTime;
}