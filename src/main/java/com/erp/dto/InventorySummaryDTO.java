package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for inventory summary statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryDTO {
    
    // Total quantities by type
    private BigDecimal totalSolventsOnHand;
    private String totalSolventsUnit = "L";
    private BigDecimal solventsGrowthPercent;
    
    // Alert counts
    private Integer lowStockItemsCount;
    
    // Financial
    private BigDecimal totalInventoryValue;
    
    // Last update
    private String lastUpdateCycle; // e.g., "24h"
    
    // Category breakdown
    private Integer totalItems;
    private Integer dyesCount;
    private Integer chemicalsCount;
    private Integer solventsCount;
    private Integer auxiliariesCount;
    
    // Storage capacity
    private BigDecimal storageCapacityUsedPercent;
}
