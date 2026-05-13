package com.erp.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiEstimateResponseDTO {

    private List<MaterialEstimate> materials;
    private List<LaborEstimate> laborHours;
    private double totalEstimatedMaterialCost;
    private double totalEstimatedLaborCost;
    private double totalOperationalCost;
    private String quotationAnalysis;
    private String quotationVerdict;

    @Data
    public static class MaterialEstimate {
        private String materialName;
        private double quantity;
        private String unitOfMeasure;
        private double estimatedUnitCost;
        private double totalCost;
        private String rationale;
    }

    @Data
    public static class LaborEstimate {
        private String role;
        private double hours;
        private double hourlyRate;
        private double totalCost;
        private String rationale;
    }
}
