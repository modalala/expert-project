package com.expert.modules.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.dict.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictMapper extends BaseMapper<SysDict> {
}