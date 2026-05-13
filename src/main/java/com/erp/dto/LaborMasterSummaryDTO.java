package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Labor Master Dashboard Statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaborMasterSummaryDTO {
    private Long totalWorkforce;
    private Long activeEmployees;
    private BigDecimal averageShiftWage;
    private Long activeRoles;
    private Long pendingApproval;
    private String growthPercentage; // e.g., "+4%"
}
