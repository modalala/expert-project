package com.expert.modules.extraction.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.extraction.dto.ConfirmationResponse;
import com.expert.modules.extraction.dto.RejectRequest;
import com.expert.modules.extraction.service.ExpertConfirmationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expert Confirmation", description = "Expert confirmation management API")
@RestController
@RequestMapping("/api/extraction/confirm")
public class ExpertConfirmationController {

    @Autowired
    private ExpertConfirmationService confirmationService;

    @Operation(summary = "Get confirmation list by plan ID")
    @GetMapping("/list/{planId}")
    public ApiResponse<List<ConfirmationResponse>> list(@PathVariable Long planId) {
        return ApiResponse.success(confirmationService.getConfirmationsByPlanId(planId));
    }

    @Operation(summary = "Accept confirmation")
    @PostMapping("/{id}/accept")
    public ApiResponse<Void> accept(@PathVariable Long id) {
        confirmationService.acceptConfirmation(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Reject confirmation")
    @PostMapping("/{id}/reject")
    public ApiResponse<Void> reject(@PathVariable Long id, @RequestBody RejectRequest request) {
        confirmationService.rejectConfirmation(id, request.getRejectReason(), request.getRejectComment());
        return ApiResponse.success();
    }

    @Operation(summary = "Mark as timeout")
    @PostMapping("/{id}/timeout")
    public ApiResponse<Void> timeout(@PathVariable Long id) {
        confirmationService.timeoutConfirmation(id);
        return ApiResponse.success();
    }
}