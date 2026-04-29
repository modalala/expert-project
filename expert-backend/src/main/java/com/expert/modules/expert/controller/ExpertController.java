package com.expert.modules.expert.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.service.ExpertPortraitService;
import com.expert.modules.expert.service.ExpertMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Expert Portrait", description = "Expert portrait and statistics API")
@RestController
@RequestMapping("/api/expert")
public class ExpertController {

    @Autowired
    private ExpertPortraitService portraitService;

    @Autowired
    private ExpertMasterService masterService;

    @Operation(summary = "Get expert portrait")
    @GetMapping("/{expertId}/portrait")
    public ApiResponse<ExpertPortraitResponse> getPortrait(@PathVariable Long expertId) {
        ExpertPortraitResponse portrait = portraitService.getExpertPortrait(expertId);
        if (portrait == null) {
            return ApiResponse.error(404, "Expert not found");
        }
        return ApiResponse.success(portrait);
    }

    @Operation(summary = "Expert list")
    @GetMapping("/list")
    public ApiResponse<PageResult<ExpertListResponse>> list(
            ExpertQueryRequest query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(masterService.getExpertList(query, page, size));
    }

    @Operation(summary = "Expert detail")
    @GetMapping("/{id}")
    public ApiResponse<ExpertListResponse> detail(@PathVariable Long id) {
        ExpertListResponse response = masterService.getExpertDetail(id);
        if (response == null) {
            return ApiResponse.error(404, "Expert not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Update expert")
    @PutMapping("/{id}")
    public ApiResponse<ExpertListResponse> update(
            @PathVariable Long id,
            @RequestBody ExpertUpdateRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ExpertListResponse response = masterService.updateExpert(id, request, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Update expert status")
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody ExpertStatusUpdateRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        masterService.updateExpertStatus(id, request.getStatus(), request.getReason(), userId);
        return ApiResponse.success();
    }

    @Operation(summary = "Delete expert")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        masterService.deleteExpert(id);
        return ApiResponse.success();
    }
}