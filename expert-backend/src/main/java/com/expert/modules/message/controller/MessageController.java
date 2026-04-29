package com.expert.modules.message.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.message.dto.SendMessageRequest;
import com.expert.modules.message.dto.TemplateCreateRequest;
import com.expert.modules.message.dto.TemplateResponse;
import com.expert.modules.message.service.MessageTemplateService;
import com.expert.modules.message.service.WeChatNotifyService;
import com.expert.modules.message.service.EmailNotifyService;
import com.expert.modules.message.service.SmsNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Message", description = "Message template and notification API")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageTemplateService templateService;

    @Autowired
    private WeChatNotifyService weChatService;

    @Autowired
    private EmailNotifyService emailService;

    @Autowired
    private SmsNotifyService smsService;

    @Operation(summary = "Template list")
    @GetMapping("/template/list")
    public ApiResponse<PageResult<TemplateResponse>> templateList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(templateService.getTemplateList(page, size));
    }

    @Operation(summary = "Create template")
    @PostMapping("/template")
    public ApiResponse<TemplateResponse> createTemplate(@RequestBody TemplateCreateRequest request) {
        TemplateResponse response = templateService.createTemplate(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Delete template")
    @DeleteMapping("/template/{id}")
    public ApiResponse<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Send WeChat message")
    @PostMapping("/wechat/send")
    public ApiResponse<Map<String, Object>> sendWeChat(@RequestBody SendMessageRequest request) {
        boolean success = weChatService.sendWeChatMessage(
            request.getTemplateCode(),
            request.getReceiver(),
            request.getParams());
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "Message sent successfully" : "Failed to send message");
        return ApiResponse.success(result);
    }

    @Operation(summary = "Set WeChat webhook URL")
    @PostMapping("/wechat/config")
    public ApiResponse<Void> setWeChatConfig(@RequestBody Map<String, String> config) {
        String webhookUrl = config.get("webhookUrl");
        if (webhookUrl != null) {
            weChatService.setWechatWebhookUrl(webhookUrl);
        }
        return ApiResponse.success();
    }

    @Operation(summary = "Send Email")
    @PostMapping("/email/send")
    public ApiResponse<Map<String, Object>> sendEmail(@RequestBody SendMessageRequest request) {
        boolean success = emailService.sendEmail(
            request.getTemplateCode(),
            request.getReceiver(),
            request.getParams());
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "Email sent successfully" : "Failed to send email");
        return ApiResponse.success(result);
    }

    @Operation(summary = "Send SMS")
    @PostMapping("/sms/send")
    public ApiResponse<Map<String, Object>> sendSms(@RequestBody SendMessageRequest request) {
        boolean success = smsService.sendSms(
            request.getTemplateCode(),
            request.getReceiver(),
            request.getParams());
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "SMS sent successfully" : "Failed to send SMS");
        return ApiResponse.success(result);
    }
}