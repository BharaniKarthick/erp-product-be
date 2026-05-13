package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAlertDTO {
    
    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotBlank(message = "Alert type is required")
    private String alertType;
    
    @NotBlank(message = "Alert name is required")
    private String alertName;
    
    private Boolean isEnabled = true;
    
    private String thresholdValue;
    
    private String alertStatus = "ACTIVE";
    
    private LocalDateTime triggeredAt;
    
    private LocalDateTime resolvedAt;
    
    private String description;
}
