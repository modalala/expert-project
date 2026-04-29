package com.expert.modules.extraction.controller;

import com.expert.common.result.ApiResponse;
import com.expert.common.result.PageResult;
import com.expert.modules.extraction.dto.SchemeCreateRequest;
import com.expert.modules.extraction.dto.SchemeResponse;
import com.expert.modules.extraction.dto.ExtractionExecuteRequest;
import com.expert.modules.extraction.dto.ExtractionResultResponse;
import com.expert.modules.extraction.dto.ReExtractionRequest;
import com.expert.modules.extraction.service.ExtractionSchemeService;
import com.expert.modules.extraction.service.ExtractionExecuteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Extraction Scheme", description = "Extraction scheme configuration API")
@RestController
@RequestMapping("/api/extraction/scheme")
public class ExtractionSchemeController {

    @Autowired
    private ExtractionSchemeService schemeService;

    @Autowired
    private ExtractionExecuteService executeService;

    @Operation(summary = "Scheme list")
    @GetMapping("/list")
    public ApiResponse<PageResult<SchemeResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(schemeService.getSchemeList(page, size));
    }

    @Operation(summary = "Scheme detail")
    @GetMapping("/{id}")
    public ApiResponse<SchemeResponse> detail(@PathVariable Long id) {
        SchemeResponse response = schemeService.getSchemeDetail(id);
        if (response == null) {
            return ApiResponse.error(404, "Scheme not found");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Scheme by plan ID")
    @GetMapping("/plan/{planId}")
    public ApiResponse<SchemeResponse> getByPlanId(@PathVariable Long planId) {
        SchemeResponse response = schemeService.getSchemeByPlanId(planId);
        if (response == null) {
            return ApiResponse.error(404, "Scheme not found for plan");
        }
        return ApiResponse.success(response);
    }

    @Operation(summary = "Create scheme")
    @PostMapping
    public ApiResponse<SchemeResponse> create(@RequestBody SchemeCreateRequest request) {
        SchemeResponse response = schemeService.createScheme(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Update scheme")
    @PutMapping("/{id}")
    public ApiResponse<SchemeResponse> update(
            @PathVariable Long id,
            @RequestBody SchemeCreateRequest request) {
        SchemeResponse response = schemeService.updateScheme(id, request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Delete scheme")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ApiResponse.success();
    }

    @Operation(summary = "Execute extraction")
    @PostMapping("/execute")
    public ApiResponse<ExtractionResultResponse> execute(@RequestBody ExtractionExecuteRequest request) {
        ExtractionResultResponse response = executeService.executeExtraction(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "Get extractions by plan ID")
    @GetMapping("/result/plan/{planId}")
    public ApiResponse<List<ExtractionResultResponse.ExtractedExpert>> getResults(@PathVariable Long planId) {
        return ApiResponse.success(executeService.getExtractionsByPlanId(planId));
    }

    @Operation(summary = "Re-extract experts")
    @PostMapping("/re-execute")
    public ApiResponse<ExtractionResultResponse> reExecute(@RequestBody ReExtractionRequest request) {
        ExtractionResultResponse response = executeService.reExecuteExtraction(request);
        return ApiResponse.success(response);
    }
}