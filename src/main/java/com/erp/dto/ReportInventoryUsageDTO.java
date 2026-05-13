package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 * DTO for Inventory Usage Trends in Reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportInventoryUsageDTO {
    private String materialName;
    private String quantityUsed;
    private String quantityRemaining;
    private Integer usagePercentage; // 0-100
    private String status; // "Normal", "Low", "Critical"
}
