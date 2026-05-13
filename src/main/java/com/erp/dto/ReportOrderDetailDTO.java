package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**

 * DTO for Detailed Order Financial Report
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportOrderDetailDTO {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private LocalDate orderDate;
    private LocalDate requiredDate;
    private LocalDate deliveryDate;
    private String status;
    
    // Financial Summary
    private BigDecimal totalCost;
    private BigDecimal quotedPrice;
    private BigDecimal projectedProfit;
    private BigDecimal profitMarginPercentage;
    private String costChangeFromEstimate; // e.g., "+12%"
    
    // Detailed Costs
    private BigDecimal totalMaterialCost;
    private BigDecimal totalLaborCost;
    private BigDecimal totalMachineCost;
    
    // Related Data
    private List<ReportMaterialDTO> materials;
    private List<ReportLaborDTO> labor;
    private List<ReportMachineDTO> machines;
    private List<ReportTransactionDTO> transactions;
}
