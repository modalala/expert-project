package com.expert.modules.expert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.expert.entity.ExpertInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExpertInfoMapper extends BaseMapper<ExpertInfo> {

    @Select("SELECT * FROM expert_info WHERE phone = #{phone} AND is_deleted = 0")
    ExpertInfo findByPhone(String phone);

    @Select("SELECT * FROM expert_info WHERE id_card = #{idCard} AND is_deleted = 0")
    ExpertInfo findByIdCard(String idCard);
}