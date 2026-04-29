package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("expert_certificate")
public class ExpertCertificate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String certName;

    private String certNo;

    private String issueOrg;

    private LocalDate issueDate;

    private LocalDate validDate;

    private String certUrl;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}