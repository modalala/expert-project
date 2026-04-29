package com.expert.modules.bid.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommitteeResponse {
    private Long id;
    private Long planId;
    private String planNo;
    private String committeeName;
    private String committeeStatus;
    private String createTime;
    private List<MemberResponse> members;

    @Data
    public static class MemberResponse {
        private Long id;
        private Long expertId;
        private String expertNo;
        private String expertName;
        private String memberRole;
        private Double score;
        private Boolean isVeto;
    }
}