package com.expert.modules.dict.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.dict.dto.*;
import com.expert.modules.dict.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dictionary", description = "Data dictionary API")
@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @Operation(summary = "Dict list")
    @GetMapping("/list")
    public ApiResponse<PageResult<DictResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(dictService.getDictList(page, size));
    }

    @Operation(summary = "Get dict by code")
    @GetMapping("/{dictCode}")
    public ApiResponse<DictResponse> getByCode(@PathVariable String dictCode) {
        DictResponse response = dictService.getDictByCode(dictCode);
        if (response == null) {
            return ApiResponse.error(404, "Dict not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get dict items by code")
    @GetMapping("/{dictCode}/items")
    public ApiResponse<List<DictItemResponse>> getItems(@PathVariable String dictCode) {
        return ApiResponse.success(dictService.getItemsByDictCode(dictCode));
    }

    @Operation(summary = "Create dict")
    @PostMapping
    public ApiResponse<DictResponse> create(@RequestBody DictCreateRequest request) {
        DictResponse response = dictService.createDict(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Delete dict")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        dictService.deleteDict(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Create dict item")
    @PostMapping("/item")
    public ApiResponse<DictItemResponse> createItem(@RequestBody DictItemCreateRequest request) {
        DictItemResponse response = dictService.createDictItem(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Delete dict item")
    @DeleteMapping("/item/{id}")
    public ApiResponse<Void> deleteItem(@PathVariable Long id) {
        dictService.deleteDictItem(id);
        return ApiResponse.success();
    }
}