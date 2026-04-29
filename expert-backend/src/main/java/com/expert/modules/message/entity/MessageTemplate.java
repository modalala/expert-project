package com.expert.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message_template")
public class MessageTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String templateCode;

    private String templateName;

    private String templateType;

    private String templateContent;

    private String variables;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}