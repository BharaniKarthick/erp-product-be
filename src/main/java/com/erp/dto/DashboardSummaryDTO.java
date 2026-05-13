package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**

 * DTO for Complete Dashboard Summary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private DashboardKPIDTO kpiMetrics;
    private List<LowInventoryAlertDTO> lowInventoryAlerts;
    private List<NegativeProfitAlertDTO> negativeProfitAlerts;
    private List<DelayedOrderAlertDTO> delayedOrderAlerts;
    private List<DashboardRecentOrderDTO> recentOrders;
    
    // Alert counts for quick access
    private Integer lowInventoryCount;
    private Integer negativeProfitCount;
    private Integer delayedOrdersCount;
}
