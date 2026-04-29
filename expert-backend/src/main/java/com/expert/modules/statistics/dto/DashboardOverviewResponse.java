package com.expert.modules.statistics.dto;

import lombok.Data;

@Data
public class DashboardOverviewResponse {
    private Long totalExperts;
    private Long currentMonthBids;
    private Long pendingReviewExperts;
    private Long ongoingExtractions;
    private Long normalExperts;
    private Long seniorExperts;
}