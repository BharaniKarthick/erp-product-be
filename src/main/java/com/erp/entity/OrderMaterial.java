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
@Table(name = "order_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderMaterial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;
    
    @Column(name = "material_code", length = 50)
    private String materialCode;
    
    @Column(name = "material_name", nullable = false, length = 255)
    private String materialName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure = "KG";
    
    @Column(name = "unit_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "total_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "stock_status", length = 50)
    private String stockStatus = "IN_STOCK"; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK, REQUIRED_PO

    @Column(name = "material_type", length = 20)
    private String materialType = "ACTUAL"; // ESTIMATED, ACTUAL
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper method to calculate total cost
    @PrePersist
    @PreUpdate
    private void calculateTotalCost() {
        if (quantity != null && unitCost != null) {
            this.totalCost = quantity.multiply(unitCost);
        }
        if (materialType == null || materialType.isBlank()) {
            this.materialType = "ACTUAL";
        }
    }
}
