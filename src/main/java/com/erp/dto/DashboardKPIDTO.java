package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Dashboard KPI Metrics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPIDTO {
    private Long totalOrders;
    private Long activeOrders;
    private BigDecimal totalCost;
    private BigDecimal totalRevenue;
    private BigDecimal netProfit;
    private BigDecimal profitMarginPercentage;
    private String revenueGrowthPercentage; // e.g., "+18.3%"
    private String costGrowthPercentage; // e.g., "+4.2%"
    private BigDecimal projectedRevenue; // End of month projection
}
