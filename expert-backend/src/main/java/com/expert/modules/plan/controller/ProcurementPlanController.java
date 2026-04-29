package com.expert.modules.plan.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.plan.dto.*;
import com.expert.modules.plan.service.ProcurementPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Procurement Plan", description = "Procurement plan API")
@RestController
@RequestMapping("/api/plan")
public class ProcurementPlanController {

    @Autowired
    private ProcurementPlanService planService;

    @Operation(summary = "Plan list")
    @GetMapping("/list")
    public ApiResponse<PageResult<PlanResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(planService.getPlanList(page, size));
    }

    @Operation(summary = "Plan detail")
    @GetMapping("/{id}")
    public ApiResponse<PlanResponse> detail(@PathVariable Long id) {
        PlanResponse response = planService.getPlanDetail(id);
        if (response == null) {
            return ApiResponse.error(404, "Plan not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Create plan")
    @PostMapping
    public ApiResponse<PlanResponse> create(
            @RequestBody PlanCreateRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        PlanResponse response = planService.createPlan(request, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Update plan")
    @PutMapping("/{id}")
    public ApiResponse<PlanResponse> update(
            @PathVariable Long id,
            @RequestBody PlanUpdateRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        PlanResponse response = planService.updatePlan(id, request, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Delete plan")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        planService.deletePlan(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Update plan status")
    @PutMapping("/{id}/status")
    public ApiResponse<PlanResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody PlanStatusRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        PlanResponse response = planService.updatePlanStatus(id, request.getStatus(), userId);
        return ApiResponse.success(response);
    }
}