package com.expert.modules.message.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.message.entity.MessageLog;
import com.expert.modules.message.entity.MessageTemplate;
import com.expert.modules.message.mapper.MessageLogMapper;
import com.expert.modules.message.mapper.MessageTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsNotifyService {

    private static final Logger log = LoggerFactory.getLogger(SmsNotifyService.class);

    @Autowired
    private MessageTemplateMapper templateMapper;

    @Autowired
    private MessageLogMapper logMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    // SMS API configuration (placeholder for integration)
    private String smsApiUrl = "https://sms-api.example.com/send";
    private String smsApiKey = "placeholder";

    public void setSmsConfig(String apiUrl, String apiKey) {
        this.smsApiUrl = apiUrl;
        this.smsApiKey = apiKey;
    }

    @Transactional
    public boolean sendSms(String templateCode, String phone, Map<String, String> params) {
        MessageTemplate template = templateMapper.selectOne(
            new LambdaQueryWrapper<MessageTemplate>()
                .eq(MessageTemplate::getTemplateCode, templateCode)
                .eq(MessageTemplate::getTemplateType, "SMS")
                .eq(MessageTemplate::getStatus, 1));

        if (template == null) {
            log.error("SMS template not found: {}", templateCode);
            return false;
        }

        String content = fillTemplate(template.getTemplateContent(), params);

        MessageLog messageLog = new MessageLog();
        messageLog.setTemplateId(template.getId());
        messageLog.setMessageType("SMS");
        messageLog.setReceiver(phone);
        messageLog.setContent(content);
        messageLog.setSendStatus("PENDING");
        messageLog.setCreateTime(LocalDateTime.now());
        logMapper.insert(messageLog);

        try {
            // Placeholder SMS API call
            Map<String, Object> body = new HashMap<>();
            body.put("phone", phone);
            body.put("content", content);
            body.put("apiKey", smsApiKey);

            // For testing, log success without actual API call
            log.info("SMS prepared for: {} - Content: {}", phone, content);

            messageLog.setSendStatus("SUCCESS");
            messageLog.setSendTime(LocalDateTime.now());
            logMapper.updateById(messageLog);

            return true;
        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage());
            messageLog.setSendStatus("FAILED");
            messageLog.setErrorMsg(e.getMessage());
            messageLog.setSendTime(LocalDateTime.now());
            logMapper.updateById(messageLog);
            return false;
        }
    }

    private String fillTemplate(String templateContent, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return templateContent;
        }
        String result = templateContent;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}