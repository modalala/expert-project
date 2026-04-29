package com.expert.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.message.entity.MessageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageLogMapper extends BaseMapper<MessageLog> {
}