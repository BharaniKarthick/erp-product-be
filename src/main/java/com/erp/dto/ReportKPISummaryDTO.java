package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Reports Dashboard KPI Summary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportKPISummaryDTO {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private BigDecimal profitMarginPercentage;
    private String revenueChangePercentage; // e.g., "+8.2%"
    private String costChangePercentage; // e.g., "+5.1%"
}
