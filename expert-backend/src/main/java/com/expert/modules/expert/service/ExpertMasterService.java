package com.expert.modules.expert.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.common.result.PageResult;
import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.entity.ExpertStatusLog;
import com.expert.modules.expert.mapper.ExpertInfoMapper;
import com.expert.modules.expert.mapper.ExpertStatusLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertMasterService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertStatusLogMapper statusLogMapper;

    public PageResult<ExpertListResponse> getExpertList(ExpertQueryRequest query, int page, int size) {
        Page<ExpertInfo> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ExpertInfo> wrapper = new LambdaQueryWrapper<>();

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(ExpertInfo::getName, query.getName());
        }
        if (query.getExpertType() != null && !query.getExpertType().isEmpty()) {
            wrapper.eq(ExpertInfo::getExpertType, query.getExpertType());
        }
        if (query.getExpertLevel() != null && !query.getExpertLevel().isEmpty()) {
            wrapper.eq(ExpertInfo::getExpertLevel, query.getExpertLevel());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(ExpertInfo::getStatus, query.getStatus());
        }
        if (query.getReviewStatus() != null && !query.getReviewStatus().isEmpty()) {
            wrapper.eq(ExpertInfo::getReviewStatus, query.getReviewStatus());
        }

        wrapper.orderByDesc(ExpertInfo::getCreateTime);
        Page<ExpertInfo> result = expertInfoMapper.selectPage(pageObj, wrapper);

        List<ExpertListResponse> records = result.getRecords().stream()
            .map(this::toListResponse)
            .collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), page, size);
    }

    public ExpertListResponse getExpertDetail(Long id) {
        ExpertInfo expert = expertInfoMapper.selectById(id);
        if (expert == null) {
            return null;
        }
        return toListResponse(expert);
    }

    @Transactional
    public ExpertListResponse updateExpert(Long id, ExpertUpdateRequest request, Long userId) {
        ExpertInfo expert = expertInfoMapper.selectById(id);
        if (expert == null) {
            throw new RuntimeException("Expert not found");
        }

        if (request.getName() != null) expert.setName(request.getName());
        if (request.getGender() != null) expert.setGender(request.getGender());
        if (request.getPhone() != null) expert.setPhone(request.getPhone());
        if (request.getEmail() != null) expert.setEmail(request.getEmail());
        if (request.getIdCard() != null) expert.setIdCard(request.getIdCard());
        if (request.getExpertType() != null) expert.setExpertType(request.getExpertType());
        if (request.getExpertLevel() != null) expert.setExpertLevel(request.getExpertLevel());
        if (request.getExpertiseAreas() != null) expert.setExpertiseAreas(request.getExpertiseAreas());
        if (request.getWorkUnit() != null) expert.setWorkUnit(request.getWorkUnit());
        if (request.getPosition() != null) expert.setPosition(request.getPosition());
        if (request.getIntroduction() != null) expert.setIntroduction(request.getIntroduction());

        expert.setUpdateTime(LocalDateTime.now());
        expertInfoMapper.updateById(expert);

        return toListResponse(expert);
    }

    @Transactional
    public void updateExpertStatus(Long id, String newStatus, String reason, Long userId) {
        ExpertInfo expert = expertInfoMapper.selectById(id);
        if (expert == null) {
            throw new RuntimeException("Expert not found");
        }

        String oldStatus = expert.getStatus();
        expert.setStatus(newStatus);
        expert.setUpdateTime(LocalDateTime.now());
        expertInfoMapper.updateById(expert);

        ExpertStatusLog statusLog = new ExpertStatusLog();
        statusLog.setExpertId(id);
        statusLog.setOldStatus(oldStatus);
        statusLog.setNewStatus(newStatus);
        statusLog.setReason(reason);
        statusLog.setOperateBy(userId);
        statusLog.setOperateTime(LocalDateTime.now());
        statusLogMapper.insert(statusLog);
    }

    @Transactional
    public void deleteExpert(Long id) {
        expertInfoMapper.deleteById(id);
    }

    private ExpertListResponse toListResponse(ExpertInfo expert) {
        ExpertListResponse response = new ExpertListResponse();
        response.setId(expert.getId());
        response.setExpertNo(expert.getExpertNo());
        response.setName(expert.getName());
        response.setGender(expert.getGender());
        response.setPhone(expert.getPhone());
        response.setEmail(expert.getEmail());
        response.setExpertType(expert.getExpertType());
        response.setExpertLevel(expert.getExpertLevel());
        response.setWorkUnit(expert.getWorkUnit());
        response.setPosition(expert.getPosition());
        response.setStatus(expert.getStatus());
        response.setReviewStatus(expert.getReviewStatus());
        response.setCreateTime(expert.getCreateTime() != null ? expert.getCreateTime().toString() : null);
        return response;
    }
}