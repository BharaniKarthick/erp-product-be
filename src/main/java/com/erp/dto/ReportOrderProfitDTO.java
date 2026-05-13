package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Order-wise Profit Breakdown in Reports Table
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportOrderProfitDTO {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal machineCost;
    private BigDecimal totalCost;
    private BigDecimal revenue;
    private BigDecimal profit;
    private BigDecimal profitMarginPercentage;
    private String profitStatus; // "Profit" or "Loss"
}
