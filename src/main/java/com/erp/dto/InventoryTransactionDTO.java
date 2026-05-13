package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for inventory transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionDTO {
    
    private Long id;
    
    private String transactionType; // PURCHASE, USAGE, ADJUSTMENT, RETURN, TRANSFER
    
    private Long inventoryItemId;
    private String inventoryItemName;
    
    private String referenceNumber;
    private String referenceType; // ORDER, PURCHASE_ORDER, ADJUSTMENT, etc.
    private Long referenceId;
    
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private BigDecimal balanceAfter;
    
    private LocalDate transactionDate;
    private String notes;
    
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
}
