package com.expert.modules.extraction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.common.result.PageResult;
import com.expert.modules.extraction.dto.SchemeCreateRequest;
import com.expert.modules.extraction.dto.SchemeResponse;
import com.expert.modules.extraction.entity.ExtractionScheme;
import com.expert.modules.extraction.mapper.ExtractionSchemeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtractionSchemeService {

    @Autowired
    private ExtractionSchemeMapper schemeMapper;

    public PageResult<SchemeResponse> getSchemeList(int page, int size) {
        Page<ExtractionScheme> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ExtractionScheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ExtractionScheme::getCreateTime);
        Page<ExtractionScheme> result = schemeMapper.selectPage(pageObj, wrapper);

        List<SchemeResponse> records = result.getRecords().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), page, size);
    }

    public SchemeResponse getSchemeDetail(Long id) {
        ExtractionScheme scheme = schemeMapper.selectById(id);
        if (scheme == null) {
            return null;
        }
        return toResponse(scheme);
    }

    public SchemeResponse getSchemeByPlanId(Long planId) {
        ExtractionScheme scheme = schemeMapper.selectOne(
            new LambdaQueryWrapper<ExtractionScheme>().eq(ExtractionScheme::getPlanId, planId));
        if (scheme == null) {
            return null;
        }
        return toResponse(scheme);
    }

    @Transactional
    public SchemeResponse createScheme(SchemeCreateRequest request) {
        ExtractionScheme scheme = new ExtractionScheme();
        scheme.setPlanId(request.getPlanId());
        scheme.setSchemeName(request.getSchemeName());
        scheme.setExtractionCount(request.getExtractionCount() != null ? request.getExtractionCount() : 5);
        scheme.setExpertTypes(request.getExpertTypes());
        scheme.setExpertLevels(request.getExpertLevels());
        scheme.setExpertiseAreas(request.getExpertiseAreas());
        scheme.setExcludeMonthCount(request.getExcludeMonthCount() != null ? request.getExcludeMonthCount() : 6);
        scheme.setExcludeMaxCount(request.getExcludeMaxCount() != null ? request.getExcludeMaxCount() : 3);
        scheme.setExcludeExperts(request.getExcludeExperts());
        scheme.setExcludeManagement(request.getExcludeManagement() != null ? request.getExcludeManagement() : 0);
        schemeMapper.insert(scheme);
        return toResponse(scheme);
    }

    @Transactional
    public SchemeResponse updateScheme(Long id, SchemeCreateRequest request) {
        ExtractionScheme scheme = schemeMapper.selectById(id);
        if (scheme == null) {
            throw new RuntimeException("Scheme not found");
        }

        if (request.getSchemeName() != null) scheme.setSchemeName(request.getSchemeName());
        if (request.getExtractionCount() != null) scheme.setExtractionCount(request.getExtractionCount());
        if (request.getExpertTypes() != null) scheme.setExpertTypes(request.getExpertTypes());
        if (request.getExpertLevels() != null) scheme.setExpertLevels(request.getExpertLevels());
        if (request.getExpertiseAreas() != null) scheme.setExpertiseAreas(request.getExpertiseAreas());
        if (request.getExcludeMonthCount() != null) scheme.setExcludeMonthCount(request.getExcludeMonthCount());
        if (request.getExcludeMaxCount() != null) scheme.setExcludeMaxCount(request.getExcludeMaxCount());
        if (request.getExcludeExperts() != null) scheme.setExcludeExperts(request.getExcludeExperts());
        if (request.getExcludeManagement() != null) scheme.setExcludeManagement(request.getExcludeManagement());

        schemeMapper.updateById(scheme);
        return toResponse(scheme);
    }

    @Transactional
    public void deleteScheme(Long id) {
        schemeMapper.deleteById(id);
    }

    private SchemeResponse toResponse(ExtractionScheme scheme) {
        SchemeResponse response = new SchemeResponse();
        response.setId(scheme.getId());
        response.setPlanId(scheme.getPlanId());
        response.setSchemeName(scheme.getSchemeName());
        response.setExtractionCount(scheme.getExtractionCount());
        response.setExpertTypes(scheme.getExpertTypes());
        response.setExpertLevels(scheme.getExpertLevels());
        response.setExpertiseAreas(scheme.getExpertiseAreas());
        response.setExcludeMonthCount(scheme.getExcludeMonthCount());
        response.setExcludeMaxCount(scheme.getExcludeMaxCount());
        response.setExcludeExperts(scheme.getExcludeExperts());
        response.setExcludeManagement(scheme.getExcludeManagement());
        response.setCreateTime(scheme.getCreateTime() != null ? scheme.getCreateTime().toString() : null);
        return response;
    }
}