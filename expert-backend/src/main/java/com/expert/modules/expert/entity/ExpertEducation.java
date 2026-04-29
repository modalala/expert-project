package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("expert_education")
public class ExpertEducation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String school;

    private String major;

    private String education;

    private String degree;

    private LocalDate graduationDate;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}