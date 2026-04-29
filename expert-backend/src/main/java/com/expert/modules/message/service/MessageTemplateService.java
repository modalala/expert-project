package com.expert.modules.message.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.common.result.PageResult;
import com.expert.modules.message.dto.TemplateCreateRequest;
import com.expert.modules.message.dto.TemplateResponse;
import com.expert.modules.message.entity.MessageTemplate;
import com.expert.modules.message.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageTemplateService {

    @Autowired
    private MessageTemplateMapper templateMapper;

    public PageResult<TemplateResponse> getTemplateList(int page, int size) {
        LambdaQueryWrapper<MessageTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MessageTemplate::getCreateTime);
        List<MessageTemplate> templates = templateMapper.selectList(wrapper);
        List<TemplateResponse> records = templates.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResult.of(records, (long) templates.size(), page, size);
    }

    @Transactional
    public TemplateResponse createTemplate(TemplateCreateRequest request) {
        MessageTemplate template = new MessageTemplate();
        template.setTemplateCode(request.getTemplateCode());
        template.setTemplateName(request.getTemplateName());
        template.setTemplateType(request.getTemplateType());
        template.setTemplateContent(request.getTemplateContent());
        template.setVariables(request.getVariables());
        template.setStatus(1);
        templateMapper.insert(template);
        return toResponse(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        templateMapper.deleteById(id);
    }

    private TemplateResponse toResponse(MessageTemplate template) {
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setTemplateCode(template.getTemplateCode());
        response.setTemplateName(template.getTemplateName());
        response.setTemplateType(template.getTemplateType());
        response.setTemplateContent(template.getTemplateContent());
        response.setVariables(template.getVariables());
        response.setStatus(template.getStatus());
        response.setCreateTime(template.getCreateTime() != null ? template.getCreateTime().toString() : null);
        return response;
    }
}