package com.erp.service;

import com.erp.dto.AiEstimateRequestDTO;
import com.erp.dto.AiEstimateResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.model}")
    private String groqModel;

    public AiEstimateResponseDTO estimateProductionCosts(AiEstimateRequestDTO request) {
        String prompt = buildPrompt(request);

        Map<String, Object> body = Map.of(
            "model", groqModel,
            "temperature", 0.2,
            "max_tokens", 1500,
            "messages", List.of(
                Map.of("role", "system", "content",
                    "You are an expert garment and textile production cost estimator. " +
                    "Always respond ONLY with a valid JSON object — no prose, no markdown, no explanation. " +
                    "Never wrap with ```json or any code block. Output raw JSON only."),
                Map.of("role", "user", "content", prompt)
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, String.class);
            return parseGroqResponse(response.getBody());
        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage(), e);
            throw new RuntimeException("AI estimation failed: " + e.getMessage());
        }
    }

    private String buildPrompt(AiEstimateRequestDTO req) {
        double totalQuotation = (req.getQuotedUnitPrice() != null ? req.getQuotedUnitPrice() : 0) *
                                (req.getOrderQuantity() != null ? req.getOrderQuantity() : 0);
        return String.format("""
            Estimate production costs for this garment/textile printing order:

            Product Type: %s
            Fabric Type: %s
            GSM: %s
            Base Color: %s
            Order Quantity: %d pieces
            Print Type: %s
            Number of Colors: %s
            Print Placement: %s
            Sizes (S/M/L/XL): %s/%s/%s/%s
            Special Instructions: %s
            Quoted Unit Price: $%.2f per piece (Total Quotation: $%.2f)

            Respond with this exact JSON structure:
            {
              "materials": [
                {
                  "materialName": "string",
                  "quantity": number,
                  "unitOfMeasure": "string",
                  "estimatedUnitCost": number,
                  "totalCost": number,
                  "rationale": "string"
                }
              ],
              "laborHours": [
                {
                  "role": "string",
                  "hours": number,
                  "hourlyRate": number,
                  "totalCost": number,
                  "rationale": "string"
                }
              ],
              "totalEstimatedMaterialCost": number,
              "totalEstimatedLaborCost": number,
              "totalOperationalCost": number,
              "quotationAnalysis": "string (2-3 sentence analysis of whether the quotation price covers costs)",
              "quotationVerdict": "PROFITABLE" | "BREAK_EVEN" | "LOSS"
            }
            """,
            req.getProductType(), req.getFabricType(),
            req.getGsm() != null ? req.getGsm() + " GSM" : "Not specified",
            req.getBaseColor() != null ? req.getBaseColor() : "Not specified",
            req.getOrderQuantity() != null ? req.getOrderQuantity() : 0,
            req.getPrintType(),
            req.getNumberOfColors() != null ? req.getNumberOfColors() : "Not specified",
            req.getPrintPlacement() != null ? req.getPrintPlacement() : "Not specified",
            req.getSizeS() != null ? req.getSizeS() : 0,
            req.getSizeM() != null ? req.getSizeM() : 0,
            req.getSizeL() != null ? req.getSizeL() : 0,
            req.getSizeXL() != null ? req.getSizeXL() : 0,
            req.getSpecialInstructions() != null ? req.getSpecialInstructions() : "None",
            req.getQuotedUnitPrice() != null ? req.getQuotedUnitPrice() : 0.0,
            totalQuotation
        );
    }

    private AiEstimateResponseDTO parseGroqResponse(String rawBody) throws Exception {
        JsonNode root = objectMapper.readTree(rawBody);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        // Strip any accidental markdown code fences
        content = content.replaceAll("(?s)```json\\s*", "").replaceAll("(?s)```\\s*", "").trim();

        JsonNode json = objectMapper.readTree(content);

        AiEstimateResponseDTO result = new AiEstimateResponseDTO();

        List<AiEstimateResponseDTO.MaterialEstimate> materials = new ArrayList<>();
        for (JsonNode m : json.path("materials")) {
            AiEstimateResponseDTO.MaterialEstimate mat = new AiEstimateResponseDTO.MaterialEstimate();
            mat.setMaterialName(m.path("materialName").asText());
            mat.setQuantity(m.path("quantity").asDouble());
            mat.setUnitOfMeasure(m.path("unitOfMeasure").asText());
            mat.setEstimatedUnitCost(m.path("estimatedUnitCost").asDouble());
            mat.setTotalCost(m.path("totalCost").asDouble());
            mat.setRationale(m.path("rationale").asText());
            materials.add(mat);
        }
        result.setMaterials(materials);

        List<AiEstimateResponseDTO.LaborEstimate> laborHours = new ArrayList<>();
        for (JsonNode l : json.path("laborHours")) {
            AiEstimateResponseDTO.LaborEstimate labor = new AiEstimateResponseDTO.LaborEstimate();
            labor.setRole(l.path("role").asText());
            labor.setHours(l.path("hours").asDouble());
            labor.setHourlyRate(l.path("hourlyRate").asDouble());
            labor.setTotalCost(l.path("totalCost").asDouble());
            labor.setRationale(l.path("rationale").asText());
            laborHours.add(labor);
        }
        result.setLaborHours(laborHours);

        result.setTotalEstimatedMaterialCost(json.path("totalEstimatedMaterialCost").asDouble());
        result.setTotalEstimatedLaborCost(json.path("totalEstimatedLaborCost").asDouble());
        result.setTotalOperationalCost(json.path("totalOperationalCost").asDouble());
        result.setQuotationAnalysis(json.path("quotationAnalysis").asText());
        result.setQuotationVerdict(json.path("quotationVerdict").asText("UNKNOWN"));

        return result;
    }
}
