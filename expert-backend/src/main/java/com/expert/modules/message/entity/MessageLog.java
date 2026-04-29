package com.expert.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message_log")
public class MessageLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long templateId;

    private String messageType;

    private String receiver;

    private String content;

    private String sendStatus;

    private LocalDateTime sendTime;

    private String errorMsg;

    private LocalDateTime createTime;
}