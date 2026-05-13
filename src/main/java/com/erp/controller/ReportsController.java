package com.erp.controller;

import com.erp.dto.*;
import com.erp.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**

 * REST Controller for Reports and Analytics
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Reports", description = "Reports and Analytics API for performance tracking and profitability analysis")
public class ReportsController {
    
    private final ReportsService reportsService;
    
    /**
     * Get KPI Summary for Reports Dashboard
     * 
     * GET /api/reports/kpi-summary?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/kpi-summary")
    @Operation(
        summary = "Get KPI Summary",
        description = "Retrieve key performance indicators including total orders, revenue, cost, profit, and margins"
    )
    public ResponseEntity<ReportKPISummaryDTO> getKPISummary(
        @Parameter(description = "Start date for the report period (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @Parameter(description = "End date for the report period (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ReportKPISummaryDTO kpiSummary = reportsService.getKPISummary(startDate, endDate);
        return ResponseEntity.ok(kpiSummary);
    }
    
    /**
     * Get Inventory Usage Trends
     * 
     * GET /api/reports/inventory-usage
     */
    @GetMapping("/inventory-usage")
    @Operation(
        summary = "Get Inventory Usage Trends",
        description = "Retrieve inventory consumption patterns and stock status for top materials"
    )
    public ResponseEntity<List<ReportInventoryUsageDTO>> getInventoryUsageTrends() {
        List<ReportInventoryUsageDTO> usageTrends = reportsService.getInventoryUsageTrends();
        return ResponseEntity.ok(usageTrends);
    }
    
    /**
     * Get Order-wise Profit Breakdown with Pagination
     * 
     * GET /api/reports/order-profit?page=0&size=10&startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/order-profit")
    @Operation(
        summary = "Get Order-wise Profit Breakdown",
        description = "Retrieve detailed profit/loss analysis for each order with material, labor, and machine cost breakdown"
    )
    public ResponseEntity<Map<String, Object>> getOrderProfitBreakdown(
        @Parameter(description = "Page number (0-indexed)")
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(description = "Number of records per page")
        @RequestParam(defaultValue = "10") int size,
        
        @Parameter(description = "Start date for filtering orders (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @Parameter(description = "End date for filtering orders (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Object> profitBreakdown = reportsService.getOrderProfitBreakdown(page, size, startDate, endDate);
        return ResponseEntity.ok(profitBreakdown);
    }
    
    /**
     * Get Detailed Order Report
     * 
     * GET /api/reports/order-detail/{orderId}
     */
    @GetMapping("/order-detail/{orderId}")
    @Operation(
        summary = "Get Detailed Order Report",
        description = "Retrieve comprehensive cost analysis for a specific order including materials, labor, machines, and transaction audit log"
    )
    public ResponseEntity<ReportOrderDetailDTO> getOrderDetailReport(
        @Parameter(description = "Order ID", required = true)
        @PathVariable Long orderId
    ) {
        ReportOrderDetailDTO orderDetail = reportsService.getOrderDetailReport(orderId);
        return ResponseEntity.ok(orderDetail);
    }
    
    /**
     * Get Revenue vs Cost Analysis Chart Data
     * 
     * GET /api/reports/revenue-vs-cost?period=Weekly&startDate=2024-01-01&endDate=2024-01-07
     */
    @GetMapping("/revenue-vs-cost")
    @Operation(
        summary = "Get Revenue vs Cost Analysis",
        description = "Retrieve daily revenue and cost comparison data for visualization in charts"
    )
    public ResponseEntity<ReportRevenueVsCostDTO> getRevenueVsCostAnalysis(
        @Parameter(description = "Report period: Weekly, Monthly, or Custom")
        @RequestParam(defaultValue = "Weekly") String period,
        
        @Parameter(description = "Start date for analysis (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @Parameter(description = "End date for analysis (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ReportRevenueVsCostDTO revenueVsCost = reportsService.getRevenueVsCostAnalysis(period, startDate, endDate);
        return ResponseEntity.ok(revenueVsCost);
    }
    
    /**
     * Export Report as CSV (placeholder for future implementation)
     * 
     * POST /api/reports/export
     */
    @PostMapping("/export")
    @Operation(
        summary = "Export Report as CSV",
        description = "Export reports in CSV format for external analysis (placeholder)"
    )
    public ResponseEntity<String> exportReportAsCSV(
        @Parameter(description = "Report type: kpi, orders, inventory")
        @RequestParam String reportType,
        
        @Parameter(description = "Start date (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @Parameter(description = "End date (YYYY-MM-DD)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        // Future enhancement: Generate CSV file
        return ResponseEntity.ok("CSV export feature coming soon");
    }
}
