package com.expert.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("expert_attachment")
public class ExpertAttachment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long expertId;

    private String fileName;

    private String fileType;

    private String fileUrl;

    private Long fileSize;

    @TableLogic
    private Integer isDeleted;

    private LocalDateTime createTime;
}