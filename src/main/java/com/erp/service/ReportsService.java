package com.erp.service;

import com.erp.dto.*;
import com.erp.entity.*;
import com.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**

 * Service for generating comprehensive business reports
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportsService {
    
    private final OrderRepository orderRepository;
    private final OrderMaterialRepository orderMaterialRepository;
    private final OrderLaborRepository orderLaborRepository;
    private final OrderMachineRepository orderMachineRepository;
    private final OrderTransactionRepository orderTransactionRepository;
    private final InventoryItemRepository inventoryItemRepository;
    
    /**
     * Get KPI Summary for Reports Dashboard
     */
    public ReportKPISummaryDTO getKPISummary(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = getOrdersInDateRange(startDate, endDate);
        
        long totalOrders = orders.size();
        BigDecimal totalRevenue = calculateTotalRevenue(orders);
        BigDecimal totalCost = calculateTotalCost(orders);
        BigDecimal totalProfit = totalRevenue.subtract(totalCost);
        BigDecimal profitMarginPercentage = calculateProfitMargin(totalRevenue, totalProfit);
        
        // Calculate change percentages (mock for now, can be enhanced with historical data)
        String revenueChange = "+8.2%";
        String costChange = "+5.1%";
        
        return new ReportKPISummaryDTO(
            totalOrders,
            totalRevenue,
            totalCost,
            totalProfit,
            profitMarginPercentage,
            revenueChange,
            costChange
        );
    }
    
    /**
     * Get Inventory Usage Trends
     */
    public List<ReportInventoryUsageDTO> getInventoryUsageTrends() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        
        return items.stream()
            .map(item -> {
                // Calculate usage percentage based on initial quantity (mock calculation)
                BigDecimal currentQty = item.getCurrentQuantity();
                int totalCapacity = currentQty.intValue() + 500; // Assuming initial was current + 500
                int used = 500;
                int usagePercentage = (int) ((used * 100.0) / totalCapacity);
                
                String status = determineInventoryStatus(currentQty, item.getReorderPoint());
                String quantityUsed = used + " " + item.getUnitOfMeasure();
                String quantityRemaining = currentQty.intValue() + " " + item.getUnitOfMeasure();
                
                return new ReportInventoryUsageDTO(
                    item.getName(),
                    quantityUsed,
                    quantityRemaining,
                    usagePercentage,
                    status
                );
            })
            .limit(5) // Top 5 items
            .collect(Collectors.toList());
    }
    
    /**
     * Get Order-wise Profit Breakdown with Pagination
     */
    public Map<String, Object> getOrderProfitBreakdown(int page, int size, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = getOrdersInDateRange(startDate, endDate);
        
        List<ReportOrderProfitDTO> profitList = orders.stream()
            .map(this::calculateOrderProfit)
            .collect(Collectors.toList());
        
        // Manual pagination
        int start = page * size;
        List<ReportOrderProfitDTO> paginatedList = profitList.subList(start, Math.min(start + size, profitList.size()));
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", paginatedList);
        result.put("totalElements", profitList.size());
        result.put("totalPages", (int) Math.ceil((double) profitList.size() / size));
        result.put("currentPage", page);
        result.put("size", size);
        
        return result;
    }
    
    /**
     * Get Detailed Order Report
     */
    public ReportOrderDetailDTO getOrderDetailReport(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        ReportOrderDetailDTO detailDTO = new ReportOrderDetailDTO();
        
        // Basic Information
        detailDTO.setOrderId(order.getId());
        detailDTO.setOrderNumber(order.getOrderNumber());
        detailDTO.setCustomerName(order.getCustomer().getCompanyName());
        detailDTO.setOrderDate(order.getOrderDate());
        detailDTO.setRequiredDate(order.getRequiredDate());
        detailDTO.setDeliveryDate(order.getDeliveryDate());
        detailDTO.setStatus(order.getStatus());
        
        // Calculate costs
        BigDecimal materialCost = calculateOrderMaterialCost(orderId);
        BigDecimal laborCost = calculateOrderLaborCost(orderId);
        BigDecimal machineCost = calculateOrderMachineCost(orderId);
        BigDecimal totalCost = materialCost.add(laborCost).add(machineCost);
        
        detailDTO.setTotalMaterialCost(materialCost);
        detailDTO.setTotalLaborCost(laborCost);
        detailDTO.setTotalMachineCost(machineCost);
        detailDTO.setTotalCost(totalCost);
        
        // Financial summary
        BigDecimal quotedPrice = order.getQuotedPrice() != null ? order.getQuotedPrice() : order.getTotalAmount();
        BigDecimal projectedProfit = quotedPrice.subtract(totalCost);
        BigDecimal profitMargin = calculateProfitMargin(quotedPrice, projectedProfit);
        
        detailDTO.setQuotedPrice(quotedPrice);
        detailDTO.setProjectedProfit(projectedProfit);
        detailDTO.setProfitMarginPercentage(profitMargin);
        
        // Calculate cost change from estimate
        BigDecimal estimatedCost = order.getEstimatedLaborCost()
            .add(order.getEstimatedMaterialCost())
            .add(order.getEstimatedMachineCost());
        String costChange = calculatePercentageChange(estimatedCost, totalCost);
        detailDTO.setCostChangeFromEstimate(costChange);
        
        // Related data
        detailDTO.setMaterials(getOrderMaterials(orderId));
        detailDTO.setLabor(getOrderLabor(orderId));
        detailDTO.setMachines(getOrderMachines(orderId));
        detailDTO.setTransactions(getOrderTransactions(orderId));
        
        return detailDTO;
    }
    
    /**
     * Get Revenue vs Cost Analysis
     */
    public ReportRevenueVsCostDTO getRevenueVsCostAnalysis(String period, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = getOrdersInDateRange(startDate, endDate);
        
        Map<String, BigDecimal> revenueByDay = new LinkedHashMap<>();
        Map<String, BigDecimal> costByDay = new LinkedHashMap<>();
        
        // Group by day of week
        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        for (String day : daysOfWeek) {
            revenueByDay.put(day, BigDecimal.ZERO);
            costByDay.put(day, BigDecimal.ZERO);
        }
        
        // Aggregate data by day
        for (Order order : orders) {
            if (order.getOrderDate() != null) {
                String dayOfWeek = order.getOrderDate().getDayOfWeek().name().substring(0, 3);
                
                BigDecimal revenue = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
                BigDecimal cost = calculateSingleOrderCost(order.getId());
                
                revenueByDay.merge(dayOfWeek, revenue, BigDecimal::add);
                costByDay.merge(dayOfWeek, cost, BigDecimal::add);
            }
        }
        
        BigDecimal totalRevenue = revenueByDay.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCost = costByDay.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageDailyProfit = totalRevenue.subtract(totalCost)
            .divide(BigDecimal.valueOf(7), 2, RoundingMode.HALF_UP);
        
        return new ReportRevenueVsCostDTO(
            period,
            revenueByDay,
            costByDay,
            totalRevenue,
            totalCost,
            averageDailyProfit
        );
    }
    
    // ========== Helper Methods ==========
    
    private List<Order> getOrdersInDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate finalStartDate = (startDate == null) ? LocalDate.now().minusMonths(1) : startDate;
        LocalDate finalEndDate = (endDate == null) ? LocalDate.now() : endDate;
        
        return orderRepository.findAll().stream()
            .filter(order -> order.getOrderDate() != null)
            .filter(order -> !order.getOrderDate().isBefore(finalStartDate) && !order.getOrderDate().isAfter(finalEndDate))
            .collect(Collectors.toList());
    }
    
    private BigDecimal calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
            .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateTotalCost(List<Order> orders) {
        return orders.stream()
            .map(order -> calculateSingleOrderCost(order.getId()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateSingleOrderCost(Long orderId) {
        BigDecimal materialCost = calculateOrderMaterialCost(orderId);
        BigDecimal laborCost = calculateOrderLaborCost(orderId);
        BigDecimal machineCost = calculateOrderMachineCost(orderId);
        return materialCost.add(laborCost).add(machineCost);
    }
    
    private BigDecimal calculateOrderMaterialCost(Long orderId) {
        return orderMaterialRepository.findByOrderId(orderId).stream()
            .map(om -> om.getUnitCost().multiply(om.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    

    private BigDecimal calculateOrderLaborCost(Long orderId) {
        return orderLaborRepository.findByOrderId(orderId).stream()
            .map(ol -> ol.getTotalCost() == null ? BigDecimal.ZERO : ol.getTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateOrderMachineCost(Long orderId) {
        return orderMachineRepository.findByOrderId(orderId).stream()
            .map(om -> om.getHourlyCost().multiply(om.getUptimeHours()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateProfitMargin(BigDecimal revenue, BigDecimal profit) {
        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return profit.divide(revenue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }
    
    private String calculatePercentageChange(BigDecimal original, BigDecimal current) {
        if (original.compareTo(BigDecimal.ZERO) == 0) {
            return "+0%";
        }
        BigDecimal change = current.subtract(original)
            .divide(original, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(1, RoundingMode.HALF_UP);
        
        return (change.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "") + change + "%";
    }
    
    private ReportOrderProfitDTO calculateOrderProfit(Order order) {
        BigDecimal materialCost = calculateOrderMaterialCost(order.getId());
        BigDecimal laborCost = calculateOrderLaborCost(order.getId());
        BigDecimal machineCost = calculateOrderMachineCost(order.getId());
        BigDecimal totalCost = materialCost.add(laborCost).add(machineCost);
        
        BigDecimal revenue = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal profit = revenue.subtract(totalCost);
        BigDecimal profitMargin = calculateProfitMargin(revenue, profit);
        
        String profitStatus = profit.compareTo(BigDecimal.ZERO) >= 0 ? "Profit" : "Loss";
        
        return new ReportOrderProfitDTO(
            order.getId(),
            order.getOrderNumber(),
            order.getCustomer().getCompanyName(),
            materialCost,
            laborCost,
            machineCost,
            totalCost,
            revenue,
            profit,
            profitMargin,
            profitStatus
        );
    }
    
    private String determineInventoryStatus(BigDecimal quantity, BigDecimal reorderPoint) {
        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            return "Critical";
        } else if (reorderPoint != null && quantity.compareTo(reorderPoint) <= 0) {
            return "Low";
        } else {
            return "Normal";
        }
    }
    
    private List<ReportMaterialDTO> getOrderMaterials(Long orderId) {
        return orderMaterialRepository.findByOrderId(orderId).stream()
            .map(om -> {
                BigDecimal totalCost = om.getUnitCost().multiply(om.getQuantity());
                String stockStatus = om.getStockStatus() != null ? om.getStockStatus() : "IN_STOCK";
                
                return new ReportMaterialDTO(
                    om.getMaterialName(),
                    "Materials", // Category
                    om.getQuantity(),
                    om.getUnitOfMeasure(),
                    om.getUnitCost(),
                    totalCost,
                    stockStatus
                );
            })
            .collect(Collectors.toList());
    }
    

    private List<ReportLaborDTO> getOrderLabor(Long orderId) {
        return orderLaborRepository.findByOrderId(orderId).stream()
            .filter(ol -> ol.getLabor() != null)
            .map(ol -> {
                LaborMaster lm = ol.getLabor();
                BigDecimal totalCost = ol.getTotalCost() == null ? BigDecimal.ZERO : ol.getTotalCost();
                Integer shifts = ol.getDurationHours().divide(BigDecimal.valueOf(8), 0, RoundingMode.UP).intValue();

                return new ReportLaborDTO(
                    lm.getFirstName() + " " + lm.getLastName(),
                    lm.getEmployeeCode(),
                    shifts,
                    ol.getDurationHours(),
                    BigDecimal.ZERO,
                    totalCost,
                    lm.getJobTitle(),
                    null // Profile image URL not stored in current schema
                );
            })
            .collect(Collectors.toList());
    }
    
    private List<ReportMachineDTO> getOrderMachines(Long orderId) {
        return orderMachineRepository.findByOrderId(orderId).stream()
            .map(om -> {
                BigDecimal totalCost = om.getHourlyCost().multiply(om.getUptimeHours());
                
                return new ReportMachineDTO(
                    om.getMachineName(),
                    om.getMachineCode(),
                    "Machine",
                    om.getUptimeHours(),
                    "hrs",
                    om.getHourlyCost(),
                    totalCost,
                    "Operational" // Status field doesn't exist in entity
                );
            })
            .collect(Collectors.toList());
    }
    
    private List<ReportTransactionDTO> getOrderTransactions(Long orderId) {
        return orderTransactionRepository.findByOrderIdOrderByTransactionDateDesc(orderId).stream()
            .map(ot -> {
                String icon = determineTransactionIcon(ot.getTransactionType());
                String colorClass = determineTransactionColor(ot.getTransactionType());
                String category = "General"; // Category field doesn't exist in entity
                
                return new ReportTransactionDTO(
                    ot.getId(),
                    ot.getTransactionType(),
                    ot.getActionDescription(),
                    ot.getTransactionDate(),
                    ot.getUserName(),
                    ot.getCostImpact(),
                    category,
                    icon,
                    colorClass
                );
            })
            .limit(10) // Latest 10 transactions
            .collect(Collectors.toList());
    }
    
    private String determineTransactionIcon(String transactionType) {
        if (transactionType == null) return "receipt";
        return switch (transactionType.toLowerCase()) {
            case "stock deduction", "material_usage" -> "warehouse";
            case "labor entry", "labor_assignment" -> "badge";
            case "stock adjustment", "adjustment" -> "warning";
            case "payment", "payment_received" -> "payments";
            case "machine_usage" -> "precision_manufacturing";
            default -> "receipt";
        };
    }
    
    private String determineTransactionColor(String transactionType) {
        if (transactionType == null) return "blue";
        return switch (transactionType.toLowerCase()) {
            case "stock deduction", "material_usage" -> "blue";
            case "labor entry", "labor_assignment" -> "amber";
            case "stock adjustment", "adjustment" -> "red";
            case "payment", "payment_received" -> "green";
            default -> "blue";
        };
    }
}
