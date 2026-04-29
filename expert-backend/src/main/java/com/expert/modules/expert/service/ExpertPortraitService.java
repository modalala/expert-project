package com.expert.modules.expert.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.expert.dto.ExpertPortraitResponse;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.mapper.ExpertInfoMapper;
import com.expert.modules.expert.mapper.*;
import com.expert.modules.expert.entity.*;
import com.expert.modules.plan.entity.ProcurementPlan;
import com.expert.modules.plan.mapper.ProcurementPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertPortraitService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertExtractionMapper extractionMapper;

    @Autowired
    private ExpertStatusLogMapper statusLogMapper;

    @Autowired
    private ExpertEvaluationMapper evaluationMapper;

    @Autowired
    private BidCommitteeMemberMapper committeeMemberMapper;

    @Autowired
    private BidCommitteeMapper committeeMapper;

    @Autowired
    private ProcurementPlanMapper planMapper;

    public ExpertPortraitResponse getExpertPortrait(Long expertId) {
        ExpertInfo expert = expertInfoMapper.selectById(expertId);
        if (expert == null) {
            return null;
        }

        ExpertPortraitResponse portrait = new ExpertPortraitResponse();
        portrait.setExpertId(expertId);
        portrait.setExpertNo(expert.getExpertNo());
        portrait.setName(expert.getName());
        portrait.setExpertType(expert.getExpertType());
        portrait.setExpertLevel(expert.getExpertLevel());

        // Extraction statistics
        List<ExpertExtraction> extractions = extractionMapper.selectList(
            new LambdaQueryWrapper<ExpertExtraction>().eq(ExpertExtraction::getExpertId, expertId));

        portrait.setTotalExtractions(extractions.size());
        portrait.setConfirmedCount(0);
        portrait.setRejectedCount(0);

        // Extraction history
        List<ExpertPortraitResponse.ExtractionHistory> extractionHistory = extractions.stream()
            .map(e -> {
                ExpertPortraitResponse.ExtractionHistory h = new ExpertPortraitResponse.ExtractionHistory();
                h.setId(e.getId());
                ProcurementPlan plan = planMapper.selectById(e.getPlanId());
                if (plan != null) {
                    h.setPlanNo(plan.getPlanNo());
                    h.setProjectName(plan.getProjectName());
                }
                h.setExtractionTime(e.getExtractionTime() != null ? e.getExtractionTime().toString() : null);
                return h;
            })
            .collect(Collectors.toList());
        portrait.setExtractionHistory(extractionHistory);

        // Status history
        List<ExpertStatusLog> statusLogs = statusLogMapper.selectList(
            new LambdaQueryWrapper<ExpertStatusLog>()
                .eq(ExpertStatusLog::getExpertId, expertId)
                .orderByDesc(ExpertStatusLog::getOperateTime));

        List<ExpertPortraitResponse.StatusHistory> statusHistory = statusLogs.stream()
            .limit(10)
            .map(s -> {
                ExpertPortraitResponse.StatusHistory h = new ExpertPortraitResponse.StatusHistory();
                h.setId(s.getId());
                h.setOldStatus(s.getOldStatus());
                h.setNewStatus(s.getNewStatus());
                h.setReason(s.getReason());
                h.setOperateTime(s.getOperateTime() != null ? s.getOperateTime().toString() : null);
                return h;
            })
            .collect(Collectors.toList());
        portrait.setStatusHistory(statusHistory);

        // Evaluation history
        List<ExpertEvaluation> evaluations = evaluationMapper.selectList(
            new LambdaQueryWrapper<ExpertEvaluation>()
                .eq(ExpertEvaluation::getExpertId, expertId)
                .orderByDesc(ExpertEvaluation::getEvaluateTime));

        if (!evaluations.isEmpty()) {
            double avgScore = evaluations.stream()
                .filter(e -> e.getTotalScore() != null)
                .mapToDouble(e -> e.getTotalScore().doubleValue())
                .average()
                .orElse(0.0);
            portrait.setAvgScore(avgScore);

            int vetoCount = (int) evaluations.stream()
                .filter(e -> e.getIsVeto() != null && e.getIsVeto() == 1)
                .count();
            portrait.setVetoCount(vetoCount);
        } else {
            portrait.setAvgScore(0.0);
            portrait.setVetoCount(0);
        }

        List<ExpertPortraitResponse.EvaluationHistory> evaluationHistory = evaluations.stream()
            .limit(10)
            .map(e -> {
                ExpertPortraitResponse.EvaluationHistory h = new ExpertPortraitResponse.EvaluationHistory();
                h.setId(e.getId());
                BidCommitteeMember member = committeeMemberMapper.selectById(e.getCommitteeMemberId());
                if (member != null) {
                    BidCommittee committee = committeeMapper.selectById(member.getCommitteeId());
                    if (committee != null) {
                        ProcurementPlan plan = planMapper.selectById(committee.getPlanId());
                        if (plan != null) {
                            h.setProjectName(plan.getProjectName());
                        }
                    }
                }
                h.setScore(e.getTotalScore() != null ? e.getTotalScore().doubleValue() : null);
                h.setEvaluateTime(e.getEvaluateTime() != null ? e.getEvaluateTime().toString() : null);
                h.setComment(e.getComment());
                return h;
            })
            .collect(Collectors.toList());
        portrait.setEvaluationHistory(evaluationHistory);

        return portrait;
    }
}