package com.expert.modules.statistics.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.statistics.dto.ChartDistributionItem;
import com.expert.modules.statistics.dto.DashboardOverviewResponse;
import com.expert.modules.statistics.dto.MonthlyTrendItem;
import com.expert.modules.statistics.service.DashboardStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dashboard Statistics", description = "首页统计数据接口")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardStatisticsService statisticsService;

    @Operation(summary = "获取首页概览统计")
    @GetMapping("/overview")
    public ApiResponse<DashboardOverviewResponse> getOverview() {
        return ApiResponse.success(statisticsService.getOverview());
    }

    @Operation(summary = "专家类型分布统计")
    @GetMapping("/expert-type-distribution")
    public ApiResponse<List<ChartDistributionItem>> getExpertTypeDistribution() {
        return ApiResponse.success(statisticsService.getExpertTypeDistribution());
    }

    @Operation(summary = "专家级别分布统计")
    @GetMapping("/expert-level-distribution")
    public ApiResponse<List<ChartDistributionItem>> getExpertLevelDistribution() {
        return ApiResponse.success(statisticsService.getExpertLevelDistribution());
    }

    @Operation(summary = "专家状态分布统计")
    @GetMapping("/expert-status-distribution")
    public ApiResponse<List<ChartDistributionItem>> getExpertStatusDistribution() {
        return ApiResponse.success(statisticsService.getExpertStatusDistribution());
    }

    @Operation(summary = "专家来源分布统计")
    @GetMapping("/expert-source-distribution")
    public ApiResponse<List<ChartDistributionItem>> getExpertSourceDistribution() {
        return ApiResponse.success(statisticsService.getExpertSourceDistribution());
    }

    @Operation(summary = "月度评标趋势统计")
    @GetMapping("/monthly-bid-trend")
    public ApiResponse<List<MonthlyTrendItem>> getMonthlyBidTrend(
            @RequestParam(defaultValue = "6") Integer months) {
        return ApiResponse.success(statisticsService.getMonthlyBidTrend(months));
    }
}