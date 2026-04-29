package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bid_committee_member")
public class BidCommitteeMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long committeeId;

    private Long expertId;

    private String memberRole;

    private BigDecimal score;

    private Integer isVeto;

    private LocalDateTime createTime;
}