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
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50)
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255)
    private String name;
    
    private String description;
    
    private Long categoryId;
    private String categoryName;
    
    @Size(max = 20)
    private String unitOfMeasure;
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    
    private BigDecimal costPrice;
    private Boolean isActive;
}
