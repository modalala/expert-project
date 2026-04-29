package com.expert.modules.message.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SendMessageRequest {
    private String templateCode;
    private String receiver;
    private Map<String, String> params;
}