package com.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLaborDTO {

    private Long id;

    // orderId is optional when used within nested order payloads; path variable is used for /orders/{id}/labor API.
    private Long orderId;

    @NotNull(message = "Labor master ID is required")
    private Long laborId;

    // Order-specific operational fields
    @NotNull(message = "Duration hours is required")
    @Positive(message = "Duration hours must be positive")
    private BigDecimal durationHours;

    private LocalDate shiftDate;

    private String notes;

    private BigDecimal totalCost;

    // Read-only metadata resolved from labor_master (populated on response, not persisted in order_labor)
    private String employeeCode;
    private String employeeName;
    private String jobTitle;
    private String department;
    private String shiftType;
    private String skillLevel;
}
