package com.expert.modules.statistics.dto;

import lombok.Data;

@Data
public class MonthlyTrendItem {
    private String month;
    private String monthLabel;
    private Long extractionCount;
    private Long bidCount;
}