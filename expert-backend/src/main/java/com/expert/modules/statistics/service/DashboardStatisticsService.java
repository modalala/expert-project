package com.expert.modules.statistics.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.expert.entity.ExpertExtraction;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.mapper.ExpertExtractionMapper;
import com.expert.modules.expert.mapper.ExpertInfoMapper;
import com.expert.modules.plan.entity.ProcurementPlan;
import com.expert.modules.plan.mapper.ProcurementPlanMapper;
import com.expert.modules.statistics.dto.ChartDistributionItem;
import com.expert.modules.statistics.dto.DashboardOverviewResponse;
import com.expert.modules.statistics.dto.MonthlyTrendItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardStatisticsService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertExtractionMapper extractionMapper;

    @Autowired
    private ProcurementPlanMapper planMapper;

    private static final Map<String, String> EXPERT_TYPE_MAP = Map.of(
        "TECH", "技术类", "ECON", "经济类", "LAW", "法律类", "MGMT", "管理类"
    );

    private static final Map<String, String> EXPERT_LEVEL_MAP = Map.of(
        "JUNIOR", "初级", "INTERMEDIATE", "中级", "SENIOR", "高级", "EXPERT", "资深"
    );

    private static final Map<String, String> EXPERT_STATUS_MAP = Map.of(
        "POTENTIAL", "潜在专家", "NORMAL", "正常", "SUSPENDED", "暂停", "BLACKLIST", "黑名单"
    );

    private static final Map<String, String> EXPERT_SOURCE_MAP = Map.of(
        "PUBLIC", "公开注册", "INTERNAL", "内部推荐"
    );

    public DashboardOverviewResponse getOverview() {
        DashboardOverviewResponse overview = new DashboardOverviewResponse();

        Long totalExperts = expertInfoMapper.selectCount(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
        );
        overview.setTotalExperts(totalExperts);

        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        Long currentMonthBids = extractionMapper.selectCount(
            new LambdaQueryWrapper<ExpertExtraction>()
                .ge(ExpertExtraction::getExtractionTime, monthStart)
        );
        overview.setCurrentMonthBids(currentMonthBids);

        Long pendingReview = expertInfoMapper.selectCount(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .eq(ExpertInfo::getReviewStatus, "PENDING")
        );
        overview.setPendingReviewExperts(pendingReview);

        Long ongoingExtractions = planMapper.selectCount(
            new LambdaQueryWrapper<ProcurementPlan>()
                .eq(ProcurementPlan::getIsDeleted, 0)
                .in(ProcurementPlan::getPlanStatus, "PENDING", "EXTRACTED", "CONFIRMED")
        );
        overview.setOngoingExtractions(ongoingExtractions);

        Long normalExperts = expertInfoMapper.selectCount(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .eq(ExpertInfo::getStatus, "NORMAL")
        );
        overview.setNormalExperts(normalExperts);

        Long seniorExperts = expertInfoMapper.selectCount(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .eq(ExpertInfo::getExpertLevel, "SENIOR")
        );
        overview.setSeniorExperts(seniorExperts);

        return overview;
    }

    public List<ChartDistributionItem> getExpertTypeDistribution() {
        List<ExpertInfo> allExperts = expertInfoMapper.selectList(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .select(ExpertInfo::getExpertType)
        );

        Map<String, Long> countMap = allExperts.stream()
            .filter(e -> e.getExpertType() != null)
            .collect(Collectors.groupingBy(
                ExpertInfo::getExpertType,
                Collectors.counting()
            ));

        long total = allExperts.stream().filter(e -> e.getExpertType() != null).count();
        return buildDistributionList(countMap, EXPERT_TYPE_MAP, total);
    }

    public List<ChartDistributionItem> getExpertLevelDistribution() {
        List<ExpertInfo> allExperts = expertInfoMapper.selectList(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .select(ExpertInfo::getExpertLevel)
        );

        Map<String, Long> countMap = allExperts.stream()
            .filter(e -> e.getExpertLevel() != null)
            .collect(Collectors.groupingBy(
                ExpertInfo::getExpertLevel,
                Collectors.counting()
            ));

        long total = allExperts.stream().filter(e -> e.getExpertLevel() != null).count();
        return buildDistributionList(countMap, EXPERT_LEVEL_MAP, total);
    }

    public List<ChartDistributionItem> getExpertStatusDistribution() {
        List<ExpertInfo> allExperts = expertInfoMapper.selectList(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .select(ExpertInfo::getStatus)
        );

        Map<String, Long> countMap = allExperts.stream()
            .filter(e -> e.getStatus() != null)
            .collect(Collectors.groupingBy(
                ExpertInfo::getStatus,
                Collectors.counting()
            ));

        long total = allExperts.stream().filter(e -> e.getStatus() != null).count();
        return buildDistributionList(countMap, EXPERT_STATUS_MAP, total);
    }

    public List<ChartDistributionItem> getExpertSourceDistribution() {
        List<ExpertInfo> allExperts = expertInfoMapper.selectList(
            new LambdaQueryWrapper<ExpertInfo>()
                .eq(ExpertInfo::getIsDeleted, 0)
                .select(ExpertInfo::getSource)
        );

        Map<String, Long> countMap = allExperts.stream()
            .filter(e -> e.getSource() != null)
            .collect(Collectors.groupingBy(
                ExpertInfo::getSource,
                Collectors.counting()
            ));

        long total = allExperts.stream().filter(e -> e.getSource() != null).count();
        return buildDistributionList(countMap, EXPERT_SOURCE_MAP, total);
    }

    public List<MonthlyTrendItem> getMonthlyBidTrend(Integer months) {
        List<MonthlyTrendItem> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            LocalDateTime monthStart = ym.atDay(1).atStartOfDay();
            LocalDateTime monthEnd = ym.plusMonths(1).atDay(1).atStartOfDay();

            MonthlyTrendItem item = new MonthlyTrendItem();
            item.setMonth(ym.format(formatter));
            item.setMonthLabel(ym.getMonthValue() + "月");

            Long extractionCount = extractionMapper.selectCount(
                new LambdaQueryWrapper<ExpertExtraction>()
                    .ge(ExpertExtraction::getExtractionTime, monthStart)
                    .lt(ExpertExtraction::getExtractionTime, monthEnd)
            );
            item.setExtractionCount(extractionCount);

            Long bidCount = planMapper.selectCount(
                new LambdaQueryWrapper<ProcurementPlan>()
                    .eq(ProcurementPlan::getIsDeleted, 0)
                    .eq(ProcurementPlan::getPlanStatus, "BID_END")
                    .ge(ProcurementPlan::getUpdateTime, monthStart)
                    .lt(ProcurementPlan::getUpdateTime, monthEnd)
            );
            item.setBidCount(bidCount);

            result.add(item);
        }

        return result;
    }

    private List<ChartDistributionItem> buildDistributionList(
            Map<String, Long> countMap,
            Map<String, String> labelMap,
            long total) {

        List<ChartDistributionItem> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : labelMap.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            Long count = countMap.getOrDefault(code, 0L);

            ChartDistributionItem item = new ChartDistributionItem();
            item.setCode(code);
            item.setName(name);
            item.setCount(count);
            item.setPercentage(total > 0 ? (count * 100.0 / total) : 0.0);

            list.add(item);
        }

        return list;
    }
}