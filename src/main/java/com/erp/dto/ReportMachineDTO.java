package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Machine & Utility Usage in Order Reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMachineDTO {
    private String resourceName;
    private String machineCode;
    private String resourceType; // "Machine", "Utility"
    private BigDecimal usageHours;
    private String usageUnit; // "hrs", "kWh", etc.
    private BigDecimal costPerUnit;
    private BigDecimal totalCost;
    private String status; // "Operational", "Maintenance", "Idle"
}
