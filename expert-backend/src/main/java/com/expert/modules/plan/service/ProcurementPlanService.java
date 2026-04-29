package com.expert.modules.plan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.common.result.PageResult;
import com.expert.modules.plan.dto.*;
import com.expert.modules.plan.entity.ProcurementPlan;
import com.expert.modules.plan.mapper.ProcurementPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcurementPlanService {

    @Autowired
    private ProcurementPlanMapper planMapper;

    public PageResult<PlanResponse> getPlanList(int page, int size) {
        Page<ProcurementPlan> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ProcurementPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ProcurementPlan::getCreateTime);
        Page<ProcurementPlan> result = planMapper.selectPage(pageObj, wrapper);
        List<PlanResponse> records = result.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResult.of(records, result.getTotal(), page, size);
    }

    public PlanResponse getPlanDetail(Long id) {
        ProcurementPlan plan = planMapper.selectById(id);
        if (plan == null) {
            return null;
        }
        return toResponse(plan);
    }

    @Transactional
    public PlanResponse createPlan(PlanCreateRequest request, Long userId) {
        ProcurementPlan plan = new ProcurementPlan();
        plan.setPlanName(request.getPlanName());
        plan.setProjectName(request.getProjectName());
        plan.setBidTime(request.getBidTime());
        plan.setBidLocation(request.getBidLocation());
        plan.setExtractionMode(request.getExtractionMode());
        plan.setCommitteeSize(request.getCommitteeSize());
        plan.setPlanStatus("DRAFT");
        plan.setCreateBy(userId);
        planMapper.insert(plan);

        // Generate plan number
        String planNo = generatePlanNo(plan.getId());
        plan.setPlanNo(planNo);
        planMapper.updateById(plan);

        return toResponse(plan);
    }

    @Transactional
    public PlanResponse updatePlan(Long id, PlanUpdateRequest request, Long userId) {
        ProcurementPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("Plan not found");
        }
        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }
        if (request.getProjectName() != null) {
            plan.setProjectName(request.getProjectName());
        }
        if (request.getBidTime() != null) {
            plan.setBidTime(request.getBidTime());
        }
        if (request.getBidLocation() != null) {
            plan.setBidLocation(request.getBidLocation());
        }
        if (request.getExtractionMode() != null) {
            plan.setExtractionMode(request.getExtractionMode());
        }
        if (request.getCommitteeSize() != null) {
            plan.setCommitteeSize(request.getCommitteeSize());
        }
        plan.setUpdateBy(userId);
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.updateById(plan);
        return toResponse(plan);
    }

    @Transactional
    public void deletePlan(Long id) {
        planMapper.deleteById(id);
    }

    @Transactional
    public PlanResponse updatePlanStatus(Long id, String status, Long userId) {
        ProcurementPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("Plan not found");
        }
        plan.setPlanStatus(status);
        plan.setUpdateBy(userId);
        plan.setUpdateTime(LocalDateTime.now());
        planMapper.updateById(plan);
        return toResponse(plan);
    }

    private String generatePlanNo(Long id) {
        String year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        return "PLAN-" + year + "-" + String.format("%04d", id);
    }

    private PlanResponse toResponse(ProcurementPlan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setPlanNo(plan.getPlanNo());
        response.setPlanName(plan.getPlanName());
        response.setProjectName(plan.getProjectName());
        response.setBidTime(plan.getBidTime() != null ? plan.getBidTime().toString() : null);
        response.setBidLocation(plan.getBidLocation());
        response.setExtractionMode(plan.getExtractionMode());
        response.setCommitteeSize(plan.getCommitteeSize());
        response.setPlanStatus(plan.getPlanStatus());
        response.setCreateTime(plan.getCreateTime() != null ? plan.getCreateTime().toString() : null);
        return response;
    }
}