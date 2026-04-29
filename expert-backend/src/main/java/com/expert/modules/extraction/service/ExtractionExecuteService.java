package com.expert.modules.extraction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.extraction.dto.ExtractionExecuteRequest;
import com.expert.modules.extraction.dto.ExtractionResultResponse;
import com.expert.modules.extraction.dto.ReExtractionRequest;
import com.expert.modules.extraction.entity.ExtractionScheme;
import com.expert.modules.extraction.mapper.ExtractionSchemeMapper;
import com.expert.modules.expert.entity.ExpertExtraction;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.mapper.ExpertExtractionMapper;
import com.expert.modules.expert.mapper.ExpertInfoMapper;
import com.expert.modules.plan.entity.ProcurementPlan;
import com.expert.modules.plan.mapper.ProcurementPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtractionExecuteService {

    @Autowired
    private ExtractionSchemeMapper schemeMapper;

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertExtractionMapper extractionMapper;

    @Autowired
    private ProcurementPlanMapper planMapper;

    @Transactional
    public ExtractionResultResponse executeExtraction(ExtractionExecuteRequest request) {
        ExtractionScheme scheme = schemeMapper.selectById(request.getSchemeId());
        if (scheme == null) {
            throw new RuntimeException("Scheme not found");
        }

        ProcurementPlan plan = planMapper.selectById(scheme.getPlanId());
        if (plan == null) {
            throw new RuntimeException("Plan not found");
        }

        int count = request.getExtractionCount() != null ?
            request.getExtractionCount() : scheme.getExtractionCount();

        List<ExpertInfo> candidates = findCandidates(scheme);

        List<ExpertInfo> selected;
        if (request.getManualExpertIds() != null && !request.getManualExpertIds().isEmpty()) {
            selected = candidates.stream()
                .filter(e -> request.getManualExpertIds().contains(e.getId()))
                .collect(Collectors.toList());
        } else {
            selected = randomSelect(candidates, count);
        }

        List<ExtractionResultResponse.ExtractedExpert> extractedList = new ArrayList<>();
        int order = 1;
        for (ExpertInfo expert : selected) {
            ExpertExtraction extraction = new ExpertExtraction();
            extraction.setPlanId(plan.getId());
            extraction.setExpertId(expert.getId());
            extraction.setExtractionTime(LocalDateTime.now());
            extraction.setExtractionOrder(order);
            extraction.setIsReserve(order > count ? 1 : 0);
            extractionMapper.insert(extraction);

            ExtractionResultResponse.ExtractedExpert ee = new ExtractionResultResponse.ExtractedExpert();
            ee.setExpertId(expert.getId());
            ee.setExpertNo(expert.getExpertNo());
            ee.setName(expert.getName());
            ee.setExpertType(expert.getExpertType());
            ee.setExpertLevel(expert.getExpertLevel());
            ee.setExtractionOrder(order);
            ee.setIsReserve(order > count);
            extractedList.add(ee);

            order++;
        }

        ExtractionResultResponse response = new ExtractionResultResponse();
        response.setPlanId(plan.getId());
        response.setPlanNo(plan.getPlanNo());
        response.setExtractionTime(LocalDateTime.now().toString());
        response.setExtractedExperts(extractedList);
        response.setTotalCount(Math.min(selected.size(), count));
        response.setReserveCount(Math.max(0, selected.size() - count));

        return response;
    }

    @Transactional
    public ExtractionResultResponse reExecuteExtraction(ReExtractionRequest request) {
        ExtractionScheme scheme = schemeMapper.selectById(request.getSchemeId());
        if (scheme == null) {
            throw new RuntimeException("Scheme not found");
        }

        ProcurementPlan plan = planMapper.selectById(request.getPlanId());
        if (plan == null) {
            throw new RuntimeException("Plan not found");
        }

        int count = request.getExtractionCount() != null ? request.getExtractionCount() : 1;

        List<ExpertInfo> candidates = findCandidates(scheme);

        List<Long> existingExpertIds = extractionMapper.selectList(
            new LambdaQueryWrapper<ExpertExtraction>()
                .eq(ExpertExtraction::getPlanId, request.getPlanId()))
            .stream()
            .map(ExpertExtraction::getExpertId)
            .collect(Collectors.toList());

        candidates = candidates.stream()
            .filter(e -> !existingExpertIds.contains(e.getId()))
            .filter(e -> request.getExcludeExpertIds() == null ||
                         !request.getExcludeExpertIds().contains(e.getId()))
            .collect(Collectors.toList());

        List<ExpertInfo> selected = randomSelect(candidates, count);

        List<ExtractionResultResponse.ExtractedExpert> extractedList = new ArrayList<>();
        int maxOrder = extractionMapper.selectList(
            new LambdaQueryWrapper<ExpertExtraction>()
                .eq(ExpertExtraction::getPlanId, request.getPlanId()))
            .stream()
            .mapToInt(e -> e.getExtractionOrder() != null ? e.getExtractionOrder() : 0)
            .max()
            .orElse(0);

        int order = maxOrder + 1;
        for (ExpertInfo expert : selected) {
            ExpertExtraction extraction = new ExpertExtraction();
            extraction.setPlanId(plan.getId());
            extraction.setExpertId(expert.getId());
            extraction.setExtractionTime(LocalDateTime.now());
            extraction.setExtractionOrder(order);
            extraction.setIsReserve(0);
            extractionMapper.insert(extraction);

            ExtractionResultResponse.ExtractedExpert ee = new ExtractionResultResponse.ExtractedExpert();
            ee.setExpertId(expert.getId());
            ee.setExpertNo(expert.getExpertNo());
            ee.setName(expert.getName());
            ee.setExpertType(expert.getExpertType());
            ee.setExpertLevel(expert.getExpertLevel());
            ee.setExtractionOrder(order);
            ee.setIsReserve(false);
            extractedList.add(ee);

            order++;
        }

        ExtractionResultResponse response = new ExtractionResultResponse();
        response.setPlanId(plan.getId());
        response.setPlanNo(plan.getPlanNo());
        response.setExtractionTime(LocalDateTime.now().toString());
        response.setExtractedExperts(extractedList);
        response.setTotalCount(selected.size());
        response.setReserveCount(0);

        return response;
    }

    private List<ExpertInfo> findCandidates(ExtractionScheme scheme) {
        LambdaQueryWrapper<ExpertInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExpertInfo::getStatus, "NORMAL");
        wrapper.eq(ExpertInfo::getReviewStatus, "RE_PASS");

        if (scheme.getExpertTypes() != null) {
            String[] types = scheme.getExpertTypes().split(",");
            wrapper.in(ExpertInfo::getExpertType, Arrays.asList(types));
        }

        if (scheme.getExpertLevels() != null) {
            String[] levels = scheme.getExpertLevels().split(",");
            wrapper.in(ExpertInfo::getExpertLevel, Arrays.asList(levels));
        }

        if (scheme.getExpertiseAreas() != null) {
            String[] areas = scheme.getExpertiseAreas().split(",");
            for (String area : areas) {
                wrapper.like(ExpertInfo::getExpertiseAreas, area.trim());
            }
        }

        return expertInfoMapper.selectList(wrapper);
    }

    private List<ExpertInfo> randomSelect(List<ExpertInfo> candidates, int count) {
        if (candidates.size() <= count) {
            return candidates;
        }

        Collections.shuffle(candidates);
        return candidates.subList(0, count);
    }

    public List<ExtractionResultResponse.ExtractedExpert> getExtractionsByPlanId(Long planId) {
        List<ExpertExtraction> extractions = extractionMapper.selectList(
            new LambdaQueryWrapper<ExpertExtraction>()
                .eq(ExpertExtraction::getPlanId, planId)
                .orderByAsc(ExpertExtraction::getExtractionOrder));

        return extractions.stream().map(e -> {
            ExpertInfo expert = expertInfoMapper.selectById(e.getExpertId());
            ExtractionResultResponse.ExtractedExpert ee = new ExtractionResultResponse.ExtractedExpert();
            ee.setExpertId(e.getExpertId());
            ee.setExpertNo(expert != null ? expert.getExpertNo() : null);
            ee.setName(expert != null ? expert.getName() : null);
            ee.setExpertType(expert != null ? expert.getExpertType() : null);
            ee.setExpertLevel(expert != null ? expert.getExpertLevel() : null);
            ee.setExtractionOrder(e.getExtractionOrder());
            ee.setIsReserve(e.getIsReserve() != null && e.getIsReserve() == 1);
            return ee;
        }).collect(Collectors.toList());
    }
}