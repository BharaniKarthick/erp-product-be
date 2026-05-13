package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**

 * DTO for Transaction Audit Log in Order Reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTransactionDTO {
    private Long transactionId;
    private String transactionType; // "Stock Deduction", "Labor Entry", "Stock Adjustment", "Payment"
    private String description;
    private LocalDateTime timestamp;
    private String performedBy;
    private BigDecimal amount;
    private String category; // "Inventory Sync", "Wage Disbursement", "Loss Overrun", "Revenue Credit"
    private String icon; // "warehouse", "badge", "warning", "payments"
    private String colorClass; // "blue", "amber", "red", "green"
}
