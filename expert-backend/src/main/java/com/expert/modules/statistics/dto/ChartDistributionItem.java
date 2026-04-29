package com.expert.modules.statistics.dto;

import lombok.Data;

@Data
public class ChartDistributionItem {
    private String code;
    private String name;
    private Long count;
    private Double percentage;
}