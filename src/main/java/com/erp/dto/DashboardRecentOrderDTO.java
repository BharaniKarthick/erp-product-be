package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**

 * DTO for Recent Orders on Dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRecentOrderDTO {

    private Long id; // frontend compatibility
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String customerInitials; // e.g., "NA" for NexGen Agency
    private String customerTier; // "Priority Client", "Bulk Contract", "Standard", "New Client"
    private String productType; // Brief description
    private BigDecimal totalAmount; // quotation total
    private BigDecimal quotedPrice; // quotation unit price
    private Integer orderQuantity;
    private Integer quantity; // legacy frontend compatibility
    private String status;
    private BigDecimal profitLoss;
    private Boolean isProfitable;
    private LocalDate orderDate;
}
