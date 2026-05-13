package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTransactionDTO {
    
    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    private String orderNumber;
    
    private String customerName;
    
    private LocalDateTime transactionDate;
    
    @NotBlank(message = "Transaction type is required")
    private String transactionType;
    
    @NotBlank(message = "Action description is required")
    private String actionDescription;
    
    private String quantityOrDuration;
    
    private String userName;
    
    private BigDecimal costImpact;
    
    private String notes;
    
    private Long createdBy;
}
