package com.expert.modules.bid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.modules.bid.dto.EvaluationRequest;
import com.expert.modules.bid.dto.EvaluationResponse;
import com.expert.modules.expert.entity.BidCommitteeMember;
import com.expert.modules.expert.entity.ExpertEvaluation;
import com.expert.modules.expert.entity.ExpertInfo;
import com.expert.modules.expert.mapper.BidCommitteeMemberMapper;
import com.expert.modules.expert.mapper.ExpertEvaluationMapper;
import com.expert.modules.expert.mapper.ExpertInfoMapper;
import com.expert.modules.user.entity.SysUser;
import com.expert.modules.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpertEvaluationService {

    @Autowired
    private ExpertEvaluationMapper evaluationMapper;

    @Autowired
    private BidCommitteeMemberMapper memberMapper;

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private UserMapper userMapper;

    public List<EvaluationResponse> getEvaluationsByCommittee(Long committeeId) {
        List<BidCommitteeMember> members = memberMapper.selectList(
            new LambdaQueryWrapper<BidCommitteeMember>()
                .eq(BidCommitteeMember::getCommitteeId, committeeId));

        return members.stream()
            .map(m -> getEvaluationByMember(m.getId()))
            .filter(e -> e != null)
            .collect(Collectors.toList());
    }

    public EvaluationResponse getEvaluationByMember(Long memberId) {
        ExpertEvaluation eval = evaluationMapper.selectOne(
            new LambdaQueryWrapper<ExpertEvaluation>()
                .eq(ExpertEvaluation::getCommitteeMemberId, memberId));

        if (eval == null) {
            return null;
        }
        return toResponse(eval);
    }

    @Transactional
    public EvaluationResponse submitEvaluation(EvaluationRequest request, Long evaluatorId) {
        BidCommitteeMember member = memberMapper.selectById(request.getCommitteeMemberId());
        if (member == null) {
            throw new RuntimeException("Committee member not found");
        }

        ExpertEvaluation existing = evaluationMapper.selectOne(
            new LambdaQueryWrapper<ExpertEvaluation>()
                .eq(ExpertEvaluation::getCommitteeMemberId, request.getCommitteeMemberId()));

        ExpertEvaluation eval;
        if (existing != null) {
            eval = existing;
        } else {
            eval = new ExpertEvaluation();
            eval.setCommitteeMemberId(request.getCommitteeMemberId());
            eval.setExpertId(member.getExpertId());
            eval.setEvaluatorId(evaluatorId);
        }

        if (request.getScore() != null) {
            eval.setTotalScore(BigDecimal.valueOf(request.getScore()));
        }
        eval.setIsVeto(request.getIsVeto() != null && request.getIsVeto() ? 1 : 0);
        if (request.getVetoReason() != null) {
            eval.setVetoReason(request.getVetoReason());
        }
        if (request.getComment() != null) {
            eval.setComment(request.getComment());
        }
        eval.setEvaluateTime(LocalDateTime.now());

        if (existing != null) {
            evaluationMapper.updateById(eval);
        } else {
            evaluationMapper.insert(eval);
        }

        return toResponse(eval);
    }

    private EvaluationResponse toResponse(ExpertEvaluation eval) {
        EvaluationResponse response = new EvaluationResponse();
        response.setId(eval.getId());
        response.setCommitteeMemberId(eval.getCommitteeMemberId());
        response.setExpertId(eval.getExpertId());

        ExpertInfo expert = expertInfoMapper.selectById(eval.getExpertId());
        if (expert != null) {
            response.setExpertNo(expert.getExpertNo());
            response.setExpertName(expert.getName());
        }

        response.setEvaluatorId(eval.getEvaluatorId());
        SysUser evaluator = userMapper.selectById(eval.getEvaluatorId());
        if (evaluator != null) {
            response.setEvaluatorName(evaluator.getRealName());
        }

        response.setTotalScore(eval.getTotalScore() != null ? eval.getTotalScore().doubleValue() : null);
        response.setIsVeto(eval.getIsVeto() != null && eval.getIsVeto() == 1);
        response.setVetoReason(eval.getVetoReason());
        response.setComment(eval.getComment());
        response.setEvaluateTime(eval.getEvaluateTime() != null ? eval.getEvaluateTime().toString() : null);
        return response;
    }
}