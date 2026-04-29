package com.expert.modules.extraction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.extraction.entity.ExpertConfirmation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpertConfirmationMapper extends BaseMapper<ExpertConfirmation> {

    @Select("SELECT ec.*, e.name as expertName, e.phone as expertPhone, e.expert_type as expertType, " +
            "ee.extraction_time as extractionTime " +
            "FROM expert_confirmation ec " +
            "LEFT JOIN expert_info e ON ec.expert_id = e.id " +
            "LEFT JOIN expert_extraction ee ON ec.extraction_id = ee.id " +
            "WHERE ec.plan_id = #{planId}")
    List<Map<String, Object>> selectByPlanIdWithExpert(Long planId);
}