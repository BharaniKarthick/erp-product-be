package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Material Consumption in Order Reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMaterialDTO {
    private String materialName;
    private String category; // "Dyes", "Chemicals", "Solvents", "Finishers"
    private BigDecimal quantityUsed;
    private String unit;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private String stockStatus; // "Available", "Low Stock", "Out of Stock"
}
