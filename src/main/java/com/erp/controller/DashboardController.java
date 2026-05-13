package com.erp.controller;

import com.erp.dto.*;
import com.erp.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * REST Controller for Dashboard Data
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Dashboard", description = "Dashboard overview and alerts API")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * Get complete dashboard summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get complete dashboard summary", 
               description = "Returns KPI metrics, all alert types, and recent orders")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }
    
    /**
     * Get KPI metrics only
     */
    @GetMapping("/kpi")
    @Operation(summary = "Get KPI metrics", 
               description = "Returns total orders, costs, revenue, profit, and growth percentages")
    public ResponseEntity<DashboardKPIDTO> getKPIMetrics() {
        return ResponseEntity.ok(dashboardService.getKPIMetrics());
    }
    
    /**
     * Get low inventory alerts
     */
    @GetMapping("/alerts/low-inventory")
    @Operation(summary = "Get low inventory alerts", 
               description = "Returns items with critical or low stock levels")
    public ResponseEntity<List<LowInventoryAlertDTO>> getLowInventoryAlerts() {
        return ResponseEntity.ok(dashboardService.getLowInventoryAlerts());
    }
    
    /**
     * Get negative profit orders
     */
    @GetMapping("/alerts/negative-profit")
    @Operation(summary = "Get negative profit orders", 
               description = "Returns orders with profit loss")
    public ResponseEntity<List<NegativeProfitAlertDTO>> getNegativeProfitAlerts() {
        return ResponseEntity.ok(dashboardService.getNegativeProfitAlerts());
    }
    
    /**
     * Get delayed orders
     */
    @GetMapping("/alerts/delayed")
    @Operation(summary = "Get delayed orders", 
               description = "Returns orders past their required delivery date")
    public ResponseEntity<List<DelayedOrderAlertDTO>> getDelayedOrderAlerts() {
        return ResponseEntity.ok(dashboardService.getDelayedOrderAlerts());
    }
    
    /**
     * Get recent orders
     */
    @GetMapping("/recent-orders")
    @Operation(summary = "Get recent orders", 
               description = "Returns recent orders from last N days (default: 7)")
    public ResponseEntity<List<DashboardRecentOrderDTO>> getRecentOrders(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(dashboardService.getRecentOrders(days));
    }
}
