package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating new inventory items from the UI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemCreateDTO {
    
    @NotBlank(message = "Item name is required")
    private String itemName;
    
    @NotBlank(message = "Type is required")
    private String type; // Dye, Chemical, Auxiliary, Raw Material
    
    @NotBlank(message = "Unit of measure is required")
    private String unit; // kg, litre, gram, drum, m
    
    @NotNull(message = "Cost per unit is required")
    @Positive(message = "Cost per unit must be positive")
    private BigDecimal costPerUnit;
    
    @NotNull(message = "Opening stock is required")
    private BigDecimal openingStock;
    
    // Alert configuration
    private BigDecimal lowStockThreshold;
    
    private Boolean lowStockAlertsEnabled = true;
    
    // Additional fields
    private String description;
    
    private String location;
    
    // Specifications (for textiles)
    private String weight; // e.g., "180 GSM"
    
    private String width; // e.g., "150 CM"
    
    private String supplier;
}
