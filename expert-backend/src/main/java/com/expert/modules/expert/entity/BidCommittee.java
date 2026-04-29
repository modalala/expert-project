package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bid_committee")
public class BidCommittee {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long planId;

    private String committeeName;

    private Long leaderId;

    private Long supervisorId;

    private LocalDateTime bidStartTime;

    private LocalDateTime bidEndTime;

    private String status;

    private Integer isVisible;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}