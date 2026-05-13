package com.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InventoryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_code", unique = true, nullable = false, length = 50)
    private String itemCode;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private InventoryCategory category;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure = "KG";
    
    @Column(name = "current_quantity", precision = 15, scale = 3)
    private BigDecimal currentQuantity = BigDecimal.ZERO;
    
    @Column(name = "minimum_quantity", precision = 15, scale = 3)
    private BigDecimal minimumQuantity = BigDecimal.ZERO;
    
    @Column(name = "maximum_quantity", precision = 15, scale = 3)
    private BigDecimal maximumQuantity;
    
    @Column(name = "reorder_point", precision = 15, scale = 3)
    private BigDecimal reorderPoint;
    
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost = BigDecimal.ZERO;
    

    @Column(length = 100)
    private String location;
    
    // Additional specifications (can be stored as JSONB for flexibility)
    @Column(length = 100)
    private String weight; // e.g., "180 GSM"
   
    @Column(length = 100)
    private String width; // e.g., "150 CM"
    
    @Column(length = 255)
    private String supplier;
    
    // Alert configuration
    @Column(name = "low_stock_alerts_enabled")
    private Boolean lowStockAlertsEnabled = true;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper method to determine stock status
    public String getStockStatus() {
        if (currentQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return "Out of Stock";
        } else if (minimumQuantity != null && currentQuantity.compareTo(minimumQuantity) <= 0) {
            return "Low Stock";
        }
        return "Healthy";
    }
    
    // Helper method to calculate total value
    public BigDecimal getTotalValue() {
        return currentQuantity.multiply(unitCost);
    }
}
