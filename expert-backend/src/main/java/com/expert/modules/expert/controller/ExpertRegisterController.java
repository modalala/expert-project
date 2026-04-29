package com.expert.modules.expert.controller;

import com.expert.common.result.ApiResponse;
import com.expert.modules.expert.dto.*;
import com.expert.modules.expert.service.ExpertRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Expert Registration", description = "Expert registration API")
@RestController
@RequestMapping("/api/expert")
public class ExpertRegisterController {

    @Autowired
    private ExpertRegisterService expertRegisterService;

    @Operation(summary = "Register expert")
    @PostMapping("/register")
    public ApiResponse<ExpertRegisterResponse> register(@RequestBody ExpertRegisterRequest request) {
        try {
            ExpertRegisterResponse response = expertRegisterService.registerExpert(request);
            return ApiResponse.success(response);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @Operation(summary = "Check phone uniqueness")
    @GetMapping("/check-phone")
    public ApiResponse<Boolean> checkPhone(@RequestParam String phone) {
        return ApiResponse.success(expertRegisterService.checkPhoneUnique(phone));
    }

    @Operation(summary = "Check ID card uniqueness")
    @GetMapping("/check-idcard")
    public ApiResponse<Boolean> checkIdCard(@RequestParam String idCard) {
        return ApiResponse.success(expertRegisterService.checkIdCardUnique(idCard));
    }
}