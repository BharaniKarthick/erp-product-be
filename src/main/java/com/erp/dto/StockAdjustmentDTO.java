package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for stock adjustment operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentDTO {
    
    @NotNull(message = "Inventory item ID is required")
    private Long inventoryItemId;
    
    @NotBlank(message = "Movement type is required")
    private String movementType; // STOCK_IN, STOCK_OUT
    
    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;
    
    @NotBlank(message = "Reason is required")
    private String reason; // Purchase Received, Customer Return, Damage/Wastage, Inventory Audit, Sample Production, Production Usage
    
    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;
    
    private String notes;
    
    // Optional: link to order or reference
    private String linkedOrderId;
    
    private String referenceType; // PO, MO, SO, RT
    
    private Long referenceId;
}
