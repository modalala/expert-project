package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_achievement")
public class ExpertAchievement {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String achievementName;

    private String achievementType;

    private String achievementDesc;

    private String achievementUrl;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}