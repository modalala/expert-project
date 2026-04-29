package com.expert.modules.dict.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.expert.common.result.PageResult;
import com.expert.modules.dict.dto.*;
import com.expert.modules.dict.entity.SysDict;
import com.expert.modules.dict.entity.SysDictItem;
import com.expert.modules.dict.mapper.DictMapper;
import com.expert.modules.dict.mapper.DictItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictItemMapper dictItemMapper;

    public PageResult<DictResponse> getDictList(int page, int size) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDict::getId);
        List<SysDict> dicts = dictMapper.selectList(wrapper);
        List<DictResponse> records = dicts.stream()
                .map(this::toDictResponse)
                .collect(Collectors.toList());
        return PageResult.of(records, (long) dicts.size(), page, size);
    }

    public DictResponse getDictByCode(String dictCode) {
        SysDict dict = dictMapper.selectOne(
            new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictCode, dictCode));
        if (dict == null) {
            return null;
        }
        DictResponse response = toDictResponse(dict);
        List<SysDictItem> items = dictItemMapper.selectList(
            new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getDictCode, dictCode)
                .orderByAsc(SysDictItem::getSortOrder));
        response.setItems(items.stream().map(this::toItemResponse).collect(Collectors.toList()));
        return response;
    }

    @Transactional
    public DictResponse createDict(DictCreateRequest request) {
        SysDict dict = new SysDict();
        dict.setDictCode(request.getDictCode());
        dict.setDictName(request.getDictName());
        dict.setDescription(request.getDescription());
        dict.setStatus(1);
        dictMapper.insert(dict);
        return toDictResponse(dict);
    }

    @Transactional
    public void deleteDict(Long id) {
        SysDict dict = dictMapper.selectById(id);
        if (dict != null) {
            dictItemMapper.delete(
                new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictCode, dict.getDictCode()));
            dictMapper.deleteById(id);
        }
    }

    @Transactional
    public DictItemResponse createDictItem(DictItemCreateRequest request) {
        SysDictItem item = new SysDictItem();
        item.setDictCode(request.getDictCode());
        item.setItemCode(request.getItemCode());
        item.setItemName(request.getItemName());
        item.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        item.setStatus(1);
        dictItemMapper.insert(item);
        return toItemResponse(item);
    }

    @Transactional
    public void deleteDictItem(Long id) {
        dictItemMapper.deleteById(id);
    }

    public List<DictItemResponse> getItemsByDictCode(String dictCode) {
        List<SysDictItem> items = dictItemMapper.selectList(
            new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getDictCode, dictCode)
                .eq(SysDictItem::getStatus, 1)
                .orderByAsc(SysDictItem::getSortOrder));
        return items.stream().map(this::toItemResponse).collect(Collectors.toList());
    }

    private DictResponse toDictResponse(SysDict dict) {
        DictResponse response = new DictResponse();
        response.setId(dict.getId());
        response.setDictCode(dict.getDictCode());
        response.setDictName(dict.getDictName());
        response.setDescription(dict.getDescription());
        response.setStatus(dict.getStatus());
        response.setCreateTime(dict.getCreateTime() != null ? dict.getCreateTime().toString() : null);
        return response;
    }

    private DictItemResponse toItemResponse(SysDictItem item) {
        DictItemResponse response = new DictItemResponse();
        response.setId(item.getId());
        response.setDictCode(item.getDictCode());
        response.setItemCode(item.getItemCode());
        response.setItemName(item.getItemName());
        response.setSortOrder(item.getSortOrder());
        response.setStatus(item.getStatus());
        response.setCreateTime(item.getCreateTime() != null ? item.getCreateTime().toString() : null);
        return response;
    }
}