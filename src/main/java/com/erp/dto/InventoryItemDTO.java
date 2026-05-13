package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private Long id;
    
    @NotBlank(message = "Item code is required")
    @Size(max = 50)
    private String itemCode;
    
    @NotBlank(message = "Item name is required")
    @Size(max = 255)
    private String name;
    
    private String description;
    
    private Long categoryId;
    private String categoryName;
    
    @Size(max = 20)
    private String unitOfMeasure;
    
    @NotNull(message = "Current quantity is required")
    private BigDecimal currentQuantity;
    
    private BigDecimal minimumQuantity;
    private BigDecimal maximumQuantity;
    private BigDecimal reorderPoint;
    
    @Positive(message = "Unit cost must be positive")
    private BigDecimal unitCost;
    
    private String location;
    private Boolean isActive;
}
