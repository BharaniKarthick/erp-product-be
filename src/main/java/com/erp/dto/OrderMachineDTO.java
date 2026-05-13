package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMachineDTO {
    
    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    private Long machineId;
    
    private String machineCode;
    
    private String machineName;
    
    @NotBlank(message = "Process description is required")
    private String processDescription;
    
    @NotNull(message = "Uptime hours is required")
    @Positive(message = "Uptime hours must be positive")
    private BigDecimal uptimeHours;
    
    @NotNull(message = "Hourly cost is required")
    @Positive(message = "Hourly cost must be positive")
    private BigDecimal hourlyCost;
    
    private BigDecimal totalCost;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String notes;
}
