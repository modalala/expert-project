package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("expert_info")
public class ExpertInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String expertNo;

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private String idCard;

    private String expertType;

    private String expertLevel;

    private String expertiseAreas;

    private String workUnit;

    private String position;

    private String introduction;

    private String photoUrl;

    private String status;

    private String source;

    private Long userId;

    private String reviewStatus;

    private Integer bidCount;

    private BigDecimal scoreAvg;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createBy;

    private Long updateBy;
}