package com.expert.modules.bid.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.bid.dto.CommitteeCreateRequest;
import com.expert.modules.bid.dto.CommitteeResponse;
import com.expert.modules.bid.dto.EvaluationRequest;
import com.expert.modules.bid.dto.EvaluationResponse;
import com.expert.modules.bid.service.BidCommitteeService;
import com.expert.modules.bid.service.ExpertEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bid Committee", description = "Bid committee management API")
@RestController
@RequestMapping("/api/bid/committee")
public class BidCommitteeController {

    @Autowired
    private BidCommitteeService committeeService;

    @Autowired
    private ExpertEvaluationService evaluationService;

    @Operation(summary = "Get committee by plan ID")
    @GetMapping("/plan/{planId}")
    public ApiResponse<CommitteeResponse> getByPlanId(@PathVariable Long planId) {
        CommitteeResponse response = committeeService.getCommitteeByPlanId(planId);
        if (response == null) {
            return ApiResponse.error(404, "Committee not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get committee detail")
    @GetMapping("/{id}")
    public ApiResponse<CommitteeResponse> detail(@PathVariable Long id) {
        CommitteeResponse response = committeeService.getCommitteeDetail(id);
        if (response == null) {
            return ApiResponse.error(404, "Committee not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Create committee")
    @PostMapping
    public ApiResponse<CommitteeResponse> create(@RequestBody CommitteeCreateRequest request) {
        CommitteeResponse response = committeeService.createCommittee(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Submit evaluation")
    @PostMapping("/evaluation")
    public ApiResponse<EvaluationResponse> submitEvaluation(
            @RequestBody EvaluationRequest request,
            Authentication authentication) {
        Long evaluatorId = (Long) authentication.getPrincipal();
        EvaluationResponse response = evaluationService.submitEvaluation(request, evaluatorId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get evaluations by committee")
    @GetMapping("/{committeeId}/evaluations")
    public ApiResponse<List<EvaluationResponse>> getEvaluations(@PathVariable Long committeeId) {
        return ApiResponse.success(evaluationService.getEvaluationsByCommittee(committeeId));
    }
}