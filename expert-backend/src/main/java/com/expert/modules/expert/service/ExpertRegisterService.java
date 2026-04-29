package com.expert.modules.expert.service;

import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.entity.*;
import com.expert.modules.expert.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ExpertRegisterService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;

    @Autowired
    private ExpertCertificateMapper certificateMapper;

    @Autowired
    private ExpertEducationMapper educationMapper;

    @Autowired
    private ExpertAchievementMapper achievementMapper;

    public boolean checkPhoneUnique(String phone) {
        ExpertInfo existing = expertInfoMapper.findByPhone(phone);
        return existing == null;
    }

    public boolean checkIdCardUnique(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return true;
        }
        ExpertInfo existing = expertInfoMapper.findByIdCard(idCard);
        return existing == null;
    }

    @Transactional
    public ExpertRegisterResponse registerExpert(ExpertRegisterRequest request) {
        // Check phone uniqueness
        if (!checkPhoneUnique(request.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }

        // Check ID card uniqueness
        if (request.getIdCard() != null && !request.getIdCard().isEmpty() && !checkIdCardUnique(request.getIdCard())) {
            throw new RuntimeException("ID card already registered");
        }

        // Create expert info
        ExpertInfo expert = new ExpertInfo();
        expert.setExpertNo("EXP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        expert.setName(request.getName());
        expert.setGender(request.getGender());
        expert.setPhone(request.getPhone());
        expert.setEmail(request.getEmail());
        expert.setIdCard(request.getIdCard());
        expert.setExpertType(request.getExpertType());
        expert.setExpertLevel(request.getExpertLevel());
        expert.setExpertiseAreas(request.getExpertiseAreas());
        expert.setWorkUnit(request.getWorkUnit());
        expert.setPosition(request.getPosition());
        expert.setIntroduction(request.getIntroduction());
        expert.setSource(request.getSource());
        expert.setStatus("POTENTIAL");
        expert.setReviewStatus("PENDING");

        expertInfoMapper.insert(expert);

        Long expertId = expert.getId();

        // Save certificates
        if (request.getCertificates() != null) {
            for (CertificateDTO certDto : request.getCertificates()) {
                ExpertCertificate cert = new ExpertCertificate();
                cert.setExpertId(expertId);
                cert.setCertName(certDto.getCertName());
                cert.setCertNo(certDto.getCertNo());
                cert.setIssueOrg(certDto.getIssueOrg());
                cert.setIssueDate(certDto.getIssueDate());
                cert.setValidDate(certDto.getValidDate());
                cert.setCertUrl(certDto.getCertUrl());
                certificateMapper.insert(cert);
            }
        }

        // Save educations
        if (request.getEducations() != null) {
            for (EducationDTO eduDto : request.getEducations()) {
                ExpertEducation edu = new ExpertEducation();
                edu.setExpertId(expertId);
                edu.setSchool(eduDto.getSchool());
                edu.setMajor(eduDto.getMajor());
                edu.setEducation(eduDto.getEducation());
                edu.setDegree(eduDto.getDegree());
                edu.setGraduationDate(eduDto.getGraduationDate());
                educationMapper.insert(edu);
            }
        }

        // Save achievements
        if (request.getAchievements() != null) {
            for (AchievementDTO achDto : request.getAchievements()) {
                ExpertAchievement ach = new ExpertAchievement();
                ach.setExpertId(expertId);
                ach.setAchievementName(achDto.getAchievementName());
                ach.setAchievementType(achDto.getAchievementType());
                ach.setAchievementDesc(achDto.getAchievementDesc());
                ach.setAchievementUrl(achDto.getAchievementUrl());
                achievementMapper.insert(ach);
            }
        }

        // Return response
        ExpertRegisterResponse response = new ExpertRegisterResponse();
        response.setId(expertId);
        response.setName(expert.getName());
        response.setStatus(expert.getStatus());
        response.setReviewStatus(expert.getReviewStatus());

        return response;
    }
}