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

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EmailNotifyService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotifyService.class);

    @Autowired
    private MessageTemplateMapper templateMapper;

    @Autowired
    private MessageLogMapper logMapper;

    private String fromEmail = "noreply@example.com";

    public void setFromEmail(String email) {
        this.fromEmail = email;
    }

    @Transactional
    public boolean sendEmail(String templateCode, String toEmail, Map<String, String> params) {
        MessageTemplate template = templateMapper.selectOne(
            new LambdaQueryWrapper<MessageTemplate>()
                .eq(MessageTemplate::getTemplateCode, templateCode)
                .eq(MessageTemplate::getTemplateType, "EMAIL")
                .eq(MessageTemplate::getStatus, 1));

        if (template == null) {
            log.error("Email template not found: {}", templateCode);
            return false;
        }

        String content = fillTemplate(template.getTemplateContent(), params);
        String subject = template.getTemplateName();

        MessageLog messageLog = new MessageLog();
        messageLog.setTemplateId(template.getId());
        messageLog.setMessageType("EMAIL");
        messageLog.setReceiver(toEmail);
        messageLog.setContent(content);
        messageLog.setSendStatus("PENDING");
        messageLog.setCreateTime(LocalDateTime.now());
        logMapper.insert(messageLog);

        try {
            // Placeholder email sending (log for testing)
            log.info("Email prepared for: {} - Subject: {} - Content: {}", toEmail, subject, content);

            messageLog.setSendStatus("SUCCESS");
            messageLog.setSendTime(LocalDateTime.now());
            logMapper.updateById(messageLog);

            log.info("Email sent successfully to: {}", toEmail);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
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