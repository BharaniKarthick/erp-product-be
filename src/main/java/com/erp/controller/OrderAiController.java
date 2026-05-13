package com.erp.controller;

import com.erp.dto.AiEstimateRequestDTO;
import com.erp.dto.AiEstimateResponseDTO;
import com.erp.service.OrderAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OrderAiController {

    private final OrderAiService orderAiService;

    @PostMapping("/estimate")
    public ResponseEntity<AiEstimateResponseDTO> estimateProductionCosts(
            @RequestBody AiEstimateRequestDTO request) {
        AiEstimateResponseDTO result = orderAiService.estimateProductionCosts(request);
        return ResponseEntity.ok(result);
    }
}
