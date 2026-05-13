package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**

 * DTO for Delayed Order Alerts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayedOrderAlertDTO {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private LocalDate requiredDate;
    private LocalDate currentDate;
    private Integer daysLate;
    private String status;
}
