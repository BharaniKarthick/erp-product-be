package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Labor Tracking in Order Reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportLaborDTO {
    private String staffMemberName;
    private String staffMemberCode;
    private Integer shiftsWorked;
    private BigDecimal hoursWorked;
    private BigDecimal ratePerHour;
    private BigDecimal totalLaborCost;
    private String role;
    private String profileImageUrl;
}
