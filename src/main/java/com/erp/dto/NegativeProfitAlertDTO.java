package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**

 * DTO for Negative Profit Order Alerts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NegativeProfitAlertDTO {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private BigDecimal totalCost;
    private BigDecimal revenue;
    private BigDecimal profitLoss; // Negative value
    private String status;
}
