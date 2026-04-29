package com.expert.modules.expert.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.common.result.PageResult;
import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.entity.*;
import com.expert.modules.expert.mapper.*;
import com.expert.modules.user.entity.SysUser;
import com.expert.modules.user.mapper.UserMapper;
import com.expert.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpertReviewService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertCertificateMapper certificateMapper;

    @Autowired
    private ExpertEducationMapper educationMapper;

    @Autowired
    private ExpertAchievementMapper achievementMapper;

    @Autowired
    private ExpertReviewMapper reviewMapper;

    @Autowired
    private ExpertReviewLogMapper reviewLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PageResult<ReviewListResponse> getReviewList(ReviewQueryRequest request, String reviewType) {
        Page<ExpertInfo> page = new Page<>(request.getPage(), request.getSize());

        LambdaQueryWrapper<ExpertInfo> wrapper = new LambdaQueryWrapper<>();

        // Filter by review status based on review type
        if ("INIT".equals(reviewType)) {
            wrapper.eq(ExpertInfo::getReviewStatus, "PENDING");
        } else if ("REVIEW".equals(reviewType)) {
            wrapper.eq(ExpertInfo::getReviewStatus, "INIT_PASS");
        }

        if (request.getName() != null) {
            wrapper.like(ExpertInfo::getName, request.getName());
        }

        wrapper.orderByDesc(ExpertInfo::getCreateTime);

        Page<ExpertInfo> result = expertInfoMapper.selectPage(page, wrapper);

        List<ReviewListResponse> records = result.getRecords().stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), request.getPage(), request.getSize());
    }

    public ReviewDetailResponse getReviewDetail(Long expertId) {
        ExpertInfo expert = expertInfoMapper.selectById(expertId);
        if (expert == null) {
            return null;
        }

        ReviewDetailResponse response = new ReviewDetailResponse();
        response.setId(expert.getId());
        response.setExpertNo(expert.getExpertNo());
        response.setName(expert.getName());
        response.setGender(expert.getGender());
        response.setPhone(expert.getPhone());
        response.setEmail(expert.getEmail());
        response.setIdCard(expert.getIdCard());
        response.setExpertType(expert.getExpertType());
        response.setExpertLevel(expert.getExpertLevel());
        response.setExpertiseAreas(expert.getExpertiseAreas());
        response.setWorkUnit(expert.getWorkUnit());
        response.setPosition(expert.getPosition());
        response.setIntroduction(expert.getIntroduction());
        response.setStatus(expert.getStatus());
        response.setReviewStatus(expert.getReviewStatus());
        response.setSource(expert.getSource());

        // Load certificates
        LambdaQueryWrapper<ExpertCertificate> certWrapper = new LambdaQueryWrapper<>();
        certWrapper.eq(ExpertCertificate::getExpertId, expertId);
        List<ExpertCertificate> certificates = certificateMapper.selectList(certWrapper);
        response.setCertificates(certificates.stream()
                .map(this::toCertificateDTO)
                .collect(Collectors.toList()));

        // Load educations
        LambdaQueryWrapper<ExpertEducation> eduWrapper = new LambdaQueryWrapper<>();
        eduWrapper.eq(ExpertEducation::getExpertId, expertId);
        List<ExpertEducation> educations = educationMapper.selectList(eduWrapper);
        response.setEducations(educations.stream()
                .map(this::toEducationDTO)
                .collect(Collectors.toList()));

        // Load achievements
        LambdaQueryWrapper<ExpertAchievement> achWrapper = new LambdaQueryWrapper<>();
        achWrapper.eq(ExpertAchievement::getExpertId, expertId);
        List<ExpertAchievement> achievements = achievementMapper.selectList(achWrapper);
        response.setAchievements(achievements.stream()
                .map(this::toAchievementDTO)
                .collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public void reviewPass(Long expertId, Long reviewerId, ReviewPassRequest request) {
        ExpertInfo expert = expertInfoMapper.selectById(expertId);
        if (expert == null) {
            throw new BusinessException(404, "Expert not found");
        }

        // Validate expert review status
        if (!"PENDING".equals(expert.getReviewStatus())) {
            throw new BusinessException(400, "Expert review status is not PENDING, cannot review. Current status: " + expert.getReviewStatus());
        }

        // Update expert review status
        expert.setReviewStatus("INIT_PASS");
        expert.setUpdateTime(LocalDateTime.now());
        expertInfoMapper.updateById(expert);

        // Create review record
        ExpertReview review = new ExpertReview();
        review.setExpertId(expertId);
        review.setReviewType("INIT");
        review.setReviewStatus("PASS");
        review.setReviewerId(reviewerId);
        review.setReviewTime(LocalDateTime.now());
        review.setReviewComment(request.getComment());
        reviewMapper.insert(review);

        // Create review log
        ExpertReviewLog log = new ExpertReviewLog();
        log.setReviewId(review.getId());
        log.setOperateType("PASS");
        log.setOperatorId(reviewerId);
        log.setOperatorName("Reviewer");
        log.setComment(request.getComment());
        log.setOperateTime(LocalDateTime.now());
        reviewLogMapper.insert(log);
    }

    @Transactional
    public void reviewReject(Long expertId, Long reviewerId, ReviewRejectRequest request) {
        ExpertInfo expert = expertInfoMapper.selectById(expertId);
        if (expert == null) {
            throw new BusinessException(404, "Expert not found");
        }

        // Validate expert review status
        if (!"PENDING".equals(expert.getReviewStatus())) {
            throw new BusinessException(400, "Expert review status is not PENDING, cannot reject. Current status: " + expert.getReviewStatus());
        }

        // Update expert review status
        expert.setReviewStatus("INIT_REJECT");
        expert.setUpdateTime(LocalDateTime.now());
        expertInfoMapper.updateById(expert);

        // Create review record
        ExpertReview review = new ExpertReview();
        review.setExpertId(expertId);
        review.setReviewType("INIT");
        review.setReviewStatus("REJECT");
        review.setReviewerId(reviewerId);
        review.setReviewTime(LocalDateTime.now());
        review.setReviewComment(request.getComment());
        review.setRejectReason(request.getRejectReason());
        reviewMapper.insert(review);

        // Create review log
        ExpertReviewLog log = new ExpertReviewLog();
        log.setReviewId(review.getId());
        log.setOperateType("REJECT");
        log.setOperatorId(reviewerId);
        log.setOperatorName("Reviewer");
        log.setComment(request.getRejectReason() + ": " + request.getComment());
        log.setOperateTime(LocalDateTime.now());
        reviewLogMapper.insert(log);
    }

    private ReviewListResponse toListResponse(ExpertInfo expert) {
        ReviewListResponse response = new ReviewListResponse();
        response.setExpertId(expert.getId());
        response.setExpertNo(expert.getExpertNo());
        response.setName(expert.getName());
        response.setPhone(expert.getPhone());
        response.setExpertType(expert.getExpertType());
        response.setExpertLevel(expert.getExpertLevel());
        response.setWorkUnit(expert.getWorkUnit());
        response.setReviewStatus(expert.getReviewStatus());
        response.setSource(expert.getSource());
        response.setCreateTime(expert.getCreateTime() != null ? expert.getCreateTime().toString() : null);
        return response;
    }

    private CertificateDTO toCertificateDTO(ExpertCertificate cert) {
        CertificateDTO dto = new CertificateDTO();
        dto.setCertName(cert.getCertName());
        dto.setCertNo(cert.getCertNo());
        dto.setIssueOrg(cert.getIssueOrg());
        dto.setIssueDate(cert.getIssueDate());
        dto.setValidDate(cert.getValidDate());
        dto.setCertUrl(cert.getCertUrl());
        return dto;
    }

    private EducationDTO toEducationDTO(ExpertEducation edu) {
        EducationDTO dto = new EducationDTO();
        dto.setSchool(edu.getSchool());
        dto.setMajor(edu.getMajor());
        dto.setEducation(edu.getEducation());
        dto.setDegree(edu.getDegree());
        dto.setGraduationDate(edu.getGraduationDate());
        return dto;
    }

    private AchievementDTO toAchievementDTO(ExpertAchievement ach) {
        AchievementDTO dto = new AchievementDTO();
        dto.setAchievementName(ach.getAchievementName());
        dto.setAchievementType(ach.getAchievementType());
        dto.setAchievementDesc(ach.getAchievementDesc());
        dto.setAchievementUrl(ach.getAchievementUrl());
        return dto;
    }

    // ==================== Re-Review (OA Approval) ====================

    public PageResult<ReviewListResponse> getReReviewList(ReviewQueryRequest request) {
        Page<ExpertInfo> page = new Page<>(request.getPage(), request.getSize());

        LambdaQueryWrapper<ExpertInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExpertInfo::getReviewStatus, "INIT_PASS");

        if (request.getName() != null) {
            wrapper.like(ExpertInfo::getName, request.getName());
        }

        wrapper.orderByDesc(ExpertInfo::getCreateTime);

        Page<ExpertInfo> result = expertInfoMapper.selectPage(page, wrapper);

        List<ReviewListResponse> records = result.getRecords().stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), request.getPage(), request.getSize());
    }

    @Transactional
    public String submitOAApproval(Long expertId, Long reviewerId) {
        ExpertInfo expert = expertInfoMapper.selectById(expertId);
        if (expert == null) {
            throw new RuntimeException("Expert not found");
        }

        // Generate OA flow ID
        String oaFlowId = "OA-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%04d", expertId);

        // Create review record for RE_REVIEW
        ExpertReview review = new ExpertReview();
        review.setExpertId(expertId);
        review.setReviewType("RE_REVIEW");
        review.setReviewStatus("PENDING");
        review.setReviewerId(reviewerId);
        review.setReviewTime(LocalDateTime.now());
        review.setOaFlowId(oaFlowId);
        review.setOaFlowStatus("PENDING");
        reviewMapper.insert(review);

        // Create review log
        ExpertReviewLog log = new ExpertReviewLog();
        log.setReviewId(review.getId());
        log.setOperateType("SUBMIT_OA");
        log.setOperatorId(reviewerId);
        log.setOperatorName("Reviewer");
        log.setComment("Submitted to OA approval");
        log.setOperateTime(LocalDateTime.now());
        reviewLogMapper.insert(log);

        return oaFlowId;
    }

    @Transactional
    public OAApprovalResult handleOAApprovalCallback(String oaFlowId, String status, String comment) {
        // Find review by OA flow ID
        LambdaQueryWrapper<ExpertReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExpertReview::getOaFlowId, oaFlowId);
        ExpertReview review = reviewMapper.selectOne(wrapper);

        if (review == null) {
            throw new RuntimeException("OA flow not found: " + oaFlowId);
        }

        ExpertInfo expert = expertInfoMapper.selectById(review.getExpertId());
        if (expert == null) {
            throw new RuntimeException("Expert not found");
        }

        OAApprovalResult result = new OAApprovalResult();
        result.setExpertId(expert.getId());
        result.setOaFlowId(oaFlowId);

        // Update review record
        review.setOaFlowStatus(status);
        review.setReviewStatus(status.equals("PASS") ? "PASS" : "REJECT");
        review.setReviewComment(comment);
        review.setReviewTime(LocalDateTime.now());
        reviewMapper.updateById(review);

        if ("PASS".equals(status)) {
            // OA approval passed - generate expert account and set status to NORMAL
            String generatedPassword = generateExpertAccount(expert);
            String expertNo = generateExpertNo(expert);

            expert.setStatus("NORMAL");
            expert.setReviewStatus("RE_PASS");
            expert.setExpertNo(expertNo);
            expert.setUpdateTime(LocalDateTime.now());
            expertInfoMapper.updateById(expert);

            // Create review log
            ExpertReviewLog log = new ExpertReviewLog();
            log.setReviewId(review.getId());
            log.setOperateType("OA_PASS");
            log.setOperatorId(0L);
            log.setOperatorName("OA System");
            log.setComment("OA approval passed. Expert account generated.");
            log.setOperateTime(LocalDateTime.now());
            reviewLogMapper.insert(log);

            result.setReviewStatus("RE_PASS");
            result.setExpertNo(expertNo);
            result.setGeneratedPassword(generatedPassword);
        } else {
            // OA approval rejected
            expert.setReviewStatus("RE_REJECT");
            expert.setUpdateTime(LocalDateTime.now());
            expertInfoMapper.updateById(expert);

            // Create review log
            ExpertReviewLog log = new ExpertReviewLog();
            log.setReviewId(review.getId());
            log.setOperateType("OA_REJECT");
            log.setOperatorId(0L);
            log.setOperatorName("OA System");
            log.setComment("OA approval rejected: " + comment);
            log.setOperateTime(LocalDateTime.now());
            reviewLogMapper.insert(log);

            result.setReviewStatus("RE_REJECT");
        }

        return result;
    }

    private String generateExpertAccount(ExpertInfo expert) {
        // Create sys_user account for expert
        SysUser user = new SysUser();
        user.setUsername(expert.getPhone()); // Use phone as username
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setRealName(expert.getName());
        user.setPhone(expert.getPhone());
        user.setEmail(expert.getEmail());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        // Bind user_id to expert
        expert.setUserId(user.getId());
        expertInfoMapper.updateById(expert);

        return randomPassword;
    }

    private String generateExpertNo(ExpertInfo expert) {
        String year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        return "EXP-" + year + "-" + String.format("%04d", expert.getId());
    }
}