package com.expert.modules.expert.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.service.ExpertReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Expert Review", description = "Expert review API")
@RestController
@RequestMapping("/api/review")
public class ExpertReviewController {

    @Autowired
    private ExpertReviewService reviewService;

    @Operation(summary = "Review list")
    @GetMapping("/list")
    public ApiResponse<PageResult<ReviewListResponse>> list(
            ReviewQueryRequest request,
            @RequestParam(defaultValue = "INIT") String reviewType) {
        return ApiResponse.success(reviewService.getReviewList(request, reviewType));
    }

    @Operation(summary = "Review detail")
    @GetMapping("/{expertId}")
    public ApiResponse<ReviewDetailResponse> detail(@PathVariable Long expertId) {
        ReviewDetailResponse response = reviewService.getReviewDetail(expertId);
        if (response == null) {
            return ApiResponse.error(404, "Expert not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Review pass")
    @PostMapping("/{expertId}/pass")
    public ApiResponse<Void> pass(
            @PathVariable Long expertId,
            @RequestBody ReviewPassRequest request,
            Authentication authentication) {
        Long reviewerId = (Long) authentication.getPrincipal();
        reviewService.reviewPass(expertId, reviewerId, request);
        return ApiResponse.success();
    }

    @Operation(summary = "Review reject")
    @PostMapping("/{expertId}/reject")
    public ApiResponse<Void> reject(
            @PathVariable Long expertId,
            @RequestBody ReviewRejectRequest request,
            Authentication authentication) {
        Long reviewerId = (Long) authentication.getPrincipal();
        reviewService.reviewReject(expertId, reviewerId, request);
        return ApiResponse.success();
    }

    // ==================== Re-Review (OA Approval) ====================

    @Operation(summary = "Re-review list")
    @GetMapping("/re-list")
    public ApiResponse<PageResult<ReviewListResponse>> reReviewList(ReviewQueryRequest request) {
        return ApiResponse.success(reviewService.getReReviewList(request));
    }

    @Operation(summary = "Submit OA approval")
    @PostMapping("/{expertId}/oa")
    public ApiResponse<String> submitOA(
            @PathVariable Long expertId,
            Authentication authentication) {
        Long reviewerId = (Long) authentication.getPrincipal();
        String oaFlowId = reviewService.submitOAApproval(expertId, reviewerId);
        return ApiResponse.success(oaFlowId);
    }

    @Operation(summary = "OA approval callback")
    @PostMapping("/oa/callback")
    public ApiResponse<OAApprovalResult> oaCallback(@RequestBody OAApprovalCallbackRequest request) {
        try {
            OAApprovalResult result = reviewService.handleOAApprovalCallback(
                request.getOaFlowId(), request.getStatus(), request.getComment());
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}