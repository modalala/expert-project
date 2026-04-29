package com.expert.modules.bid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.bid.dto.CommitteeCreateRequest;
import com.expert.modules.bid.dto.CommitteeResponse;
import com.expert.modules.expert.entity.BidCommittee;
import com.expert.modules.expert.entity.BidCommitteeMember;
import com.expert.modules.expert.entity.ExpertExtraction;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.mapper.*;
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
public class BidCommitteeService {

    @Autowired
    private BidCommitteeMapper committeeMapper;

    @Autowired
    private BidCommitteeMemberMapper memberMapper;

    @Autowired
    private ExpertExtractionMapper extractionMapper;

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ProcurementPlanMapper planMapper;

    public CommitteeResponse getCommitteeByPlanId(Long planId) {
        BidCommittee committee = committeeMapper.selectOne(
            new LambdaQueryWrapper<BidCommittee>().eq(BidCommittee::getPlanId, planId));
        if (committee == null) {
            return null;
        }
        return toResponse(committee);
    }

    public CommitteeResponse getCommitteeDetail(Long id) {
        BidCommittee committee = committeeMapper.selectById(id);
        if (committee == null) {
            return null;
        }
        return toResponse(committee);
    }

    @Transactional
    public CommitteeResponse createCommittee(CommitteeCreateRequest request) {
        ProcurementPlan plan = planMapper.selectById(request.getPlanId());
        if (plan == null) {
            throw new RuntimeException("Plan not found");
        }

        BidCommittee committee = new BidCommittee();
        committee.setPlanId(request.getPlanId());
        committee.setCommitteeName(request.getCommitteeName());
        committee.setStatus("FORMING");
        committee.setIsVisible(0);
        committee.setCreateTime(LocalDateTime.now());
        committeeMapper.insert(committee);

        List<ExpertExtraction> extractions = extractionMapper.selectList(
            new LambdaQueryWrapper<ExpertExtraction>()
                .eq(ExpertExtraction::getPlanId, request.getPlanId())
                .eq(ExpertExtraction::getIsReserve, 0));

        for (ExpertExtraction extraction : extractions) {
            BidCommitteeMember member = new BidCommitteeMember();
            member.setCommitteeId(committee.getId());
            member.setExpertId(extraction.getExpertId());
            member.setMemberRole("EXPERT");
            member.setCreateTime(LocalDateTime.now());
            memberMapper.insert(member);
        }

        return toResponse(committee);
    }

    private CommitteeResponse toResponse(BidCommittee committee) {
        CommitteeResponse response = new CommitteeResponse();
        response.setId(committee.getId());
        response.setPlanId(committee.getPlanId());

        ProcurementPlan plan = planMapper.selectById(committee.getPlanId());
        if (plan != null) {
            response.setPlanNo(plan.getPlanNo());
        }

        response.setCommitteeStatus(committee.getStatus());
        response.setCreateTime(committee.getCreateTime() != null ? committee.getCreateTime().toString() : null);

        List<BidCommitteeMember> members = memberMapper.selectList(
            new LambdaQueryWrapper<BidCommitteeMember>()
                .eq(BidCommitteeMember::getCommitteeId, committee.getId()));

        List<CommitteeResponse.MemberResponse> memberResponses = members.stream()
            .map(m -> {
                CommitteeResponse.MemberResponse mr = new CommitteeResponse.MemberResponse();
                mr.setId(m.getId());
                mr.setExpertId(m.getExpertId());
                ExpertInfo expert = expertInfoMapper.selectById(m.getExpertId());
                if (expert != null) {
                    mr.setExpertNo(expert.getExpertNo());
                    mr.setExpertName(expert.getName());
                }
                mr.setMemberRole(m.getMemberRole());
                mr.setScore(m.getScore() != null ? m.getScore().doubleValue() : null);
                mr.setIsVeto(m.getIsVeto() != null && m.getIsVeto() == 1);
                return mr;
            })
            .collect(Collectors.toList());

        response.setMembers(memberResponses);
        return response;
    }
}