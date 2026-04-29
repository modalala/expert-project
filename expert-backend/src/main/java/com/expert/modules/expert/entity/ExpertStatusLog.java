package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_status_log")
public class ExpertStatusLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String oldStatus;

    private String newStatus;

    private String reason;

    private Long operateBy;

    private LocalDateTime operateTime;
}