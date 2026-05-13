package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive DTO for inventory item detail view
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDetailDTO {
    
    // Basic Info
    private Long id;
    private String itemCode;
    private String name;
    private String description;
    
    // Category
    private Long categoryId;
    private String categoryName;
    
    // Stock Info
    private BigDecimal currentStock;
    private String unitOfMeasure;
    private BigDecimal unitCost;
    
    // Thresholds
    private BigDecimal minimumQuantity;
    private BigDecimal reorderPoint;
    
    // Status
    private String stockStatus; // Healthy, Low Stock, Out of Stock
    private Boolean isActive;
    
    // Location
    private String location;
    
    // Specifications (stored as JSON in database)
    private String weight;
    private String width;
    private String supplier;
    
    // Alert Configuration
    private Boolean lowStockAlertsEnabled;
    
    // Calculated Values
    private BigDecimal totalValue; // currentStock * unitCost
    
    // Recent Transactions
    private List<InventoryTransactionDTO> recentTransactions;
    
    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
