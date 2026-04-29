package com.expert.modules.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.modules.dict.entity.SysDictItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictItemMapper extends BaseMapper<SysDictItem> {
}