package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**

 * DTO for Revenue vs Cost Analysis Chart Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRevenueVsCostDTO {
    private String period; // "Weekly", "Monthly", "Custom"
    private Map<String, BigDecimal> revenueByDay; // e.g., "MON": 45000
    private Map<String, BigDecimal> costByDay; // e.g., "MON": 32000
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal averageDailyProfit;
}
