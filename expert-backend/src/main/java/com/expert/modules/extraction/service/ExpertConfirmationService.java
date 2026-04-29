package com.expert.modules.extraction.service;

import com.expert.modules.extraction.entity.ExpertConfirmation;
import com.expert.modules.extraction.mapper.ExpertConfirmationMapper;
import com.expert.modules.extraction.dto.ConfirmationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpertConfirmationService {

    @Autowired
    private ExpertConfirmationMapper confirmationMapper;

    public List<ConfirmationResponse> getConfirmationsByPlanId(Long planId) {
        List<Map<String, Object>> results = confirmationMapper.selectByPlanIdWithExpert(planId);
        return results.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void acceptConfirmation(Long id) {
        ExpertConfirmation confirmation = confirmationMapper.selectById(id);
        if (confirmation == null) {
            throw new RuntimeException("Confirmation not found");
        }
        if (!"PENDING".equals(confirmation.getConfirmStatus())) {
            throw new RuntimeException("Confirmation status is not PENDING");
        }
        confirmation.setConfirmStatus("CONFIRMED");
        confirmation.setConfirmTime(LocalDateTime.now());
        confirmationMapper.updateById(confirmation);
    }

    @Transactional
    public void rejectConfirmation(Long id, String rejectReason, String rejectComment) {
        ExpertConfirmation confirmation = confirmationMapper.selectById(id);
        if (confirmation == null) {
            throw new RuntimeException("Confirmation not found");
        }
        if (!"PENDING".equals(confirmation.getConfirmStatus())) {
            throw new RuntimeException("Confirmation status is not PENDING");
        }
        confirmation.setConfirmStatus("REJECTED");
        confirmation.setConfirmTime(LocalDateTime.now());
        confirmation.setRejectReason(rejectReason);
        confirmation.setRejectComment(rejectComment);
        confirmationMapper.updateById(confirmation);
    }

    @Transactional
    public void timeoutConfirmation(Long id) {
        ExpertConfirmation confirmation = confirmationMapper.selectById(id);
        if (confirmation == null) {
            throw new RuntimeException("Confirmation not found");
        }
        if (!"PENDING".equals(confirmation.getConfirmStatus())) {
            throw new RuntimeException("Confirmation status is not PENDING");
        }
        confirmation.setConfirmStatus("TIMEOUT");
        confirmation.setConfirmTime(LocalDateTime.now());
        confirmationMapper.updateById(confirmation);
    }

    private ConfirmationResponse mapToResponse(Map<String, Object> map) {
        ConfirmationResponse response = new ConfirmationResponse();
        response.setId((Long) map.get("id"));
        response.setExtractionId((Long) map.get("extraction_id"));
        response.setPlanId((Long) map.get("plan_id"));
        response.setExpertId((Long) map.get("expert_id"));
        response.setExpertName((String) map.get("expertName"));
        response.setExpertPhone((String) map.get("expertPhone"));
        response.setExpertType((String) map.get("expertType"));
        response.setConfirmStatus((String) map.get("confirm_status"));

        Object extractionTime = map.get("extractionTime");
        if (extractionTime != null) {
            response.setExtractionTime((LocalDateTime) extractionTime);
        }

        Object notifyTime = map.get("notify_time");
        if (notifyTime != null) {
            response.setNotifyTime((LocalDateTime) notifyTime);
        }

        Object expireTime = map.get("expire_time");
        if (expireTime != null) {
            response.setExpireTime((LocalDateTime) expireTime);
        }

        return response;
    }
}