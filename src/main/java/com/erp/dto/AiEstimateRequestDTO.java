package com.erp.dto;

import lombok.Data;

@Data
public class AiEstimateRequestDTO {
    private String productType;
    private String fabricType;
    private Integer gsm;
    private String baseColor;
    private Integer orderQuantity;
    private String printType;
    private Integer numberOfColors;
    private String printPlacement;
    private String specialInstructions;
    private Integer sizeS;
    private Integer sizeM;
    private Integer sizeL;
    private Integer sizeXL;
    private Double quotedUnitPrice;
}
