package com.erp.service;

import com.erp.dto.*;
import com.erp.entity.InventoryItem;
import com.erp.entity.Order;
import com.erp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**

 * Service for Dashboard Data Aggregation
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    
    private final OrderRepository orderRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final OrderMaterialRepository orderMaterialRepository;
    private final OrderLaborRepository orderLaborRepository;
    private final OrderMachineRepository orderMachineRepository;
    
    /**
     * Get complete dashboard summary with all metrics and alerts
     */
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        
        // Get KPI metrics
        summary.setKpiMetrics(getKPIMetrics());
        
        // Get alerts
        List<LowInventoryAlertDTO> lowInventoryAlerts = getLowInventoryAlerts();
        List<NegativeProfitAlertDTO> negativeProfitAlerts = getNegativeProfitAlerts();
        List<DelayedOrderAlertDTO> delayedOrderAlerts = getDelayedOrderAlerts();
        
        summary.setLowInventoryAlerts(lowInventoryAlerts);
        summary.setNegativeProfitAlerts(negativeProfitAlerts);
        summary.setDelayedOrderAlerts(delayedOrderAlerts);
        
        // Set alert counts
        summary.setLowInventoryCount(lowInventoryAlerts.size());
        summary.setNegativeProfitCount(negativeProfitAlerts.size());
        summary.setDelayedOrdersCount(delayedOrderAlerts.size());
        
        // Get recent orders
        summary.setRecentOrders(getRecentOrders(7)); // Last 7 days
        
        return summary;
    }
    
    /**
     * Get KPI Metrics
     */
    public DashboardKPIDTO getKPIMetrics() {
        List<Order> allOrders = orderRepository.findAll();
        
        Long totalOrders = (long) allOrders.size();
        Long activeOrders = allOrders.stream()
            .filter(o -> "IN_PRODUCTION".equals(o.getStatus()) || "PENDING".equals(o.getStatus()))
            .count();
        
        // Calculate financial metrics
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Order order : allOrders) {
            // Revenue
            if (order.getTotalAmount() != null) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());
            }
            
            // Cost calculation
            BigDecimal orderCost = calculateOrderCost(order.getId());
            totalCost = totalCost.add(orderCost);
        }
        
        BigDecimal netProfit = totalRevenue.subtract(totalCost);
        BigDecimal profitMargin = calculateProfitMargin(totalRevenue, netProfit);
        
        // Mock growth percentages (could be calculated from historical data)
        String revenueGrowth = "+18.3%";
        String costGrowth = "+4.2%";
        
        // Projected revenue (mock: current + 10%)
        BigDecimal projectedRevenue = totalRevenue.multiply(BigDecimal.valueOf(1.10))
            .setScale(2, RoundingMode.HALF_UP);
        
        return new DashboardKPIDTO(
            totalOrders,
            activeOrders,
            totalCost,
            totalRevenue,
            netProfit,
            profitMargin,
            revenueGrowth,
            costGrowth,
            projectedRevenue
        );
    }
    
    /**
     * Get Low Inventory Alerts
     */
    public List<LowInventoryAlertDTO> getLowInventoryAlerts() {
        List<InventoryItem> allItems = inventoryItemRepository.findAll();
        
        return allItems.stream()
            .filter(item -> {
                int current = item.getCurrentQuantity().intValue();
                int reorder = item.getReorderPoint() != null ? item.getReorderPoint().intValue() : 0;
                return current <= reorder;
            })
            .map(item -> {
                int current = item.getCurrentQuantity().intValue();
                int reorder = item.getReorderPoint() != null ? item.getReorderPoint().intValue() : 0;
                String status = current == 0 ? "Critical" : "Low";
                
                return new LowInventoryAlertDTO(
                    item.getId(),
                    item.getItemCode(),
                    item.getName(),
                    current,
                    reorder,
                    item.getUnitOfMeasure(),
                    status
                );
            })
            .limit(5) // Top 5 critical items
            .collect(Collectors.toList());
    }
    
    /**
     * Get Negative Profit Orders
     */
    public List<NegativeProfitAlertDTO> getNegativeProfitAlerts() {
        List<Order> allOrders = orderRepository.findAll();
        
        List<NegativeProfitAlertDTO> negativeOrders = new ArrayList<>();
        
        for (Order order : allOrders) {
            BigDecimal revenue = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
            BigDecimal cost = calculateOrderCost(order.getId());
            BigDecimal profit = revenue.subtract(cost);
            
            if (profit.compareTo(BigDecimal.ZERO) < 0) {
                negativeOrders.add(new NegativeProfitAlertDTO(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getCustomer().getCompanyName(),
                    cost,
                    revenue,
                    profit,
                    order.getStatus()
                ));
            }
        }
        
        return negativeOrders.stream()
            .limit(10) // Top 10 negative profit orders
            .collect(Collectors.toList());
    }
    
    /**
     * Get Delayed Orders
     */
    public List<DelayedOrderAlertDTO> getDelayedOrderAlerts() {
        List<Order> allOrders = orderRepository.findAll();
        LocalDate today = LocalDate.now();
        
        return allOrders.stream()
            .filter(order -> order.getRequiredDate() != null && 
                           order.getRequiredDate().isBefore(today) &&
                           !"COMPLETED".equals(order.getStatus()) &&
                           !"SHIPPED".equals(order.getStatus()))
            .map(order -> {
                long daysLate = ChronoUnit.DAYS.between(order.getRequiredDate(), today);
                
                return new DelayedOrderAlertDTO(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getCustomer().getCompanyName(),
                    order.getRequiredDate(),
                    today,
                    (int) daysLate,
                    order.getStatus()
                );
            })
            .sorted((a, b) -> b.getDaysLate().compareTo(a.getDaysLate())) // Most delayed first
            .limit(10)
            .collect(Collectors.toList());
    }
    
    /**
     * Get Recent Orders
     */
    public List<DashboardRecentOrderDTO> getRecentOrders(int days) {
        List<Order> allOrders = orderRepository.findAll();
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        

        return allOrders.stream()
            .filter(order -> {
                LocalDate effectiveOrderDate = getEffectiveOrderDate(order);
                return effectiveOrderDate != null && !effectiveOrderDate.isBefore(cutoffDate);
            })
            .map(order -> {
                BigDecimal revenue = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
                BigDecimal cost = calculateOrderCost(order.getId());
                BigDecimal profit = revenue.subtract(cost);

                String customerName = getSafeCustomerName(order);
                String initials = generateInitials(customerName);
                String productType = getProductTypeDescription(order);
                String orderNumber = getSafeOrderNumber(order);
                BigDecimal quotationTotal = getQuotationTotal(order);
                LocalDate effectiveOrderDate = getEffectiveOrderDate(order);
                
                return new DashboardRecentOrderDTO(
                    order.getId(),
                    order.getId(),
                    orderNumber,
                    customerName,
                    initials,
                    "Standard", // Could be enhanced with customer tier logic
                    productType,
                    quotationTotal,
                    order.getQuotedPrice() != null ? order.getQuotedPrice() : BigDecimal.ZERO,
                    order.getOrderQuantity() != null ? order.getOrderQuantity() : 0,
                    order.getOrderQuantity() != null ? order.getOrderQuantity() : 0,
                    order.getStatus(),
                    profit,
                    profit.compareTo(BigDecimal.ZERO) >= 0,
                    effectiveOrderDate
                );
            })
            .sorted((a, b) -> {
                LocalDate dateA = a.getOrderDate() != null ? a.getOrderDate() : LocalDate.MIN;
                LocalDate dateB = b.getOrderDate() != null ? b.getOrderDate() : LocalDate.MIN;
                return dateB.compareTo(dateA);
            }) // Most recent first
            .limit(10)
            .collect(Collectors.toList());
    }
    
    // ========== Helper Methods ==========
    
    private BigDecimal calculateOrderCost(Long orderId) {
        BigDecimal materialCost = orderMaterialRepository.findByOrderId(orderId).stream()
            .map(om -> om.getUnitCost().multiply(om.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        

        BigDecimal laborCost = orderLaborRepository.findByOrderId(orderId).stream()
            .map(ol -> ol.getTotalCost() == null ? BigDecimal.ZERO : ol.getTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal machineCost = orderMachineRepository.findByOrderId(orderId).stream()
            .map(om -> om.getHourlyCost().multiply(om.getUptimeHours()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return materialCost.add(laborCost).add(machineCost);
    }
    
    private BigDecimal calculateProfitMargin(BigDecimal revenue, BigDecimal profit) {
        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return profit.divide(revenue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }
    
    private String generateInitials(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            return "??";
        }
        
        String[] words = companyName.split("\\s+");
        if (words.length >= 2) {
            return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase();
        } else if (words.length == 1 && words[0].length() >= 2) {
            return words[0].substring(0, 2).toUpperCase();
        }
        return companyName.substring(0, Math.min(2, companyName.length())).toUpperCase();
    }

    private String getSafeCustomerName(Order order) {
        if (order.getCustomerName() != null && !order.getCustomerName().isBlank()) {
            return order.getCustomerName();
        }
        if (order.getCustomer() != null && order.getCustomer().getCompanyName() != null) {
            return order.getCustomer().getCompanyName();
        }
        return "Unknown";
    }

    private String getSafeOrderNumber(Order order) {
        if (order.getOrderNumber() != null && !order.getOrderNumber().isBlank()) {
            return order.getOrderNumber();
        }
        return order.getId() != null ? "ORD-" + order.getId() : "N/A";
    }

    private LocalDate getEffectiveOrderDate(Order order) {
        if (order.getOrderDate() != null) {
            return order.getOrderDate();
        }
        if (order.getCreatedAt() != null) {
            return order.getCreatedAt().toLocalDate();
        }
        return null;
    }

    private BigDecimal getQuotationTotal(Order order) {
        if (order.getTotalAmount() != null && order.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
            return order.getTotalAmount();
        }
        BigDecimal unitQuoted = order.getQuotedPrice() != null ? order.getQuotedPrice() : BigDecimal.ZERO;
        Integer qty = order.getOrderQuantity() != null ? order.getOrderQuantity() : 0;
        return unitQuoted.multiply(BigDecimal.valueOf(qty));
    }
    
    private String getProductTypeDescription(Order order) {
        // If order has items, get first item's product name
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            return order.getOrderItems().get(0).getProduct().getName();
        }
        return "Custom Order";
    }
}
