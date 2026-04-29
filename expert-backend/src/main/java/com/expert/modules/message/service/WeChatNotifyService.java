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
public class WeChatNotifyService {

    private static final Logger log = LoggerFactory.getLogger(WeChatNotifyService.class);

    @Autowired
    private MessageTemplateMapper templateMapper;

    @Autowired
    private MessageLogMapper logMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    // 企业微信Webhook URL（配置后替换）
    private String wechatWebhookUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=YOUR_KEY";

    public void setWechatWebhookUrl(String url) {
        this.wechatWebhookUrl = url;
    }

    @Transactional
    public boolean sendWeChatMessage(String templateCode, String receiver, Map<String, String> params) {
        MessageTemplate template = templateMapper.selectOne(
            new LambdaQueryWrapper<MessageTemplate>()
                .eq(MessageTemplate::getTemplateCode, templateCode)
                .eq(MessageTemplate::getStatus, 1));

        if (template == null) {
            log.error("Template not found: {}", templateCode);
            return false;
        }

        String content = fillTemplate(template.getTemplateContent(), params);

        MessageLog messageLog = new MessageLog();
        messageLog.setTemplateId(template.getId());
        messageLog.setMessageType("WECHAT");
        messageLog.setReceiver(receiver);
        messageLog.setContent(content);
        messageLog.setSendStatus("PENDING");
        messageLog.setCreateTime(LocalDateTime.now());
        logMapper.insert(messageLog);

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("msgtype", "text");

            Map<String, String> text = new HashMap<>();
            text.put("content", content);
            body.put("text", text);

            String response = restTemplate.postForObject(wechatWebhookUrl, body, String.class);
            log.info("WeChat response: {}", response);

            messageLog.setSendStatus("SUCCESS");
            messageLog.setSendTime(LocalDateTime.now());
            logMapper.updateById(messageLog);

            return true;
        } catch (Exception e) {
            log.error("Failed to send WeChat message: {}", e.getMessage());
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
        }
        return result;
    }

    public MessageTemplate getTemplateByCode(String templateCode) {
        return templateMapper.selectOne(
            new LambdaQueryWrapper<MessageTemplate>()
                .eq(MessageTemplate::getTemplateCode, templateCode));
    }
}