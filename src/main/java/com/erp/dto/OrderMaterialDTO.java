package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMaterialDTO {
    
    private Long id;
    

    // Optional for order create payloads; path variable is used for /orders/{id}/materials API.
    private Long orderId;
    
    private Long inventoryItemId;
    
    private String materialCode;
    
    @NotBlank(message = "Material name is required")
    private String materialName;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;
    
    private String unitOfMeasure = "KG";
    
    @NotNull(message = "Unit cost is required")
    @Positive(message = "Unit cost must be positive")
    private BigDecimal unitCost;
    
    private BigDecimal totalCost;
    
    private String stockStatus = "IN_STOCK";

    // ESTIMATED (from create/edit form) or ACTUAL (consumed during production).
    private String materialType;
    
    private String notes;
}
