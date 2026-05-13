package com.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "customer_contact", length = 255)
    private String customerContact;

    @Column(name = "customer_code", length = 50)
    private String customerCode;
    
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate = LocalDate.now();
    
    @Column(name = "required_date")
    private LocalDate requiredDate;
    
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "requested_delivery_date")
    private LocalDate requestedDeliveryDate;
    

    @Column(nullable = false, length = 50)
    private String status = "PENDING"; // PENDING, RUNNING, COMPLETED, ON_HOLD, CANCELLED, READY_TO_SHIP, SHIPPED
    
    @Column(length = 50)
    private String priority = "STANDARD_NORMAL"; // CRITICAL_HIGH, STANDARD_NORMAL, LOW_DEFERRED

    // Product and printing metadata from order intake form
    @Column(name = "product_type", length = 100)
    private String productType;

    @Column(name = "fabric_type", length = 100)
    private String fabricType;

    @Column(name = "gsm")
    private Integer gsm;

    @Column(name = "base_color", length = 100)
    private String baseColor;

    @Column(name = "size_s")
    private Integer sizeS;

    @Column(name = "size_m")
    private Integer sizeM;

    @Column(name = "size_l")
    private Integer sizeL;

    @Column(name = "size_xl")
    private Integer sizeXL;

    @Column(name = "print_type", length = 100)
    private String printType;

    @Column(name = "number_of_colors")
    private Integer numberOfColors;

    @Column(name = "print_placement", length = 100)
    private String printPlacement;

    @Column(name = "design_reference", length = 255)
    private String designReference;

    @Column(name = "order_quantity")
    private Integer orderQuantity;
    
    @Column(name = "warehouse_origin", length = 50)
    private String warehouseOrigin;
    
    @Column(name = "completion_progress", precision = 5, scale = 2)
    private BigDecimal completionProgress = BigDecimal.ZERO; // 0-100
    
    @Column(name = "current_stage", length = 100)
    private String currentStage; // e.g., "Pending", "Material Prep", "Printing", "Finishing"
    
    @Column(name = "payment_status", length = 50)
    private String paymentStatus = "UNPAID";
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    // Financial tracking fields
    @Column(name = "quoted_price", precision = 15, scale = 2)
    private BigDecimal quotedPrice = BigDecimal.ZERO; // Target revenue
    
    @Column(name = "estimated_labor_cost", precision = 15, scale = 2)
    private BigDecimal estimatedLaborCost = BigDecimal.ZERO;
    
    @Column(name = "estimated_material_cost", precision = 15, scale = 2)
    private BigDecimal estimatedMaterialCost = BigDecimal.ZERO;
    
    @Column(name = "estimated_machine_cost", precision = 15, scale = 2)
    private BigDecimal estimatedMachineCost = BigDecimal.ZERO;

    @Column(name = "budget_threshold_percent", precision = 5, scale = 2)
    private BigDecimal budgetThresholdPercent = BigDecimal.valueOf(100);

    @Column(name = "delivery_proximity_alert_enabled")
    private Boolean deliveryProximityAlertEnabled = true;

    @Column(name = "proximity_threshold_hours")
    private Integer proximityThresholdHours = 48;

    @Column(name = "budget_overrun_alert_enabled")
    private Boolean budgetOverrunAlertEnabled = true;
    
    @Column(name = "actual_labor_cost", precision = 15, scale = 2)
    private BigDecimal actualLaborCost = BigDecimal.ZERO;
    
    @Column(name = "actual_material_cost", precision = 15, scale = 2)
    private BigDecimal actualMaterialCost = BigDecimal.ZERO;
    
    @Column(name = "actual_machine_cost", precision = 15, scale = 2)
    private BigDecimal actualMachineCost = BigDecimal.ZERO;
    
    @Column(name = "profit_loss", precision = 15, scale = 2)
    private BigDecimal profitLoss = BigDecimal.ZERO;
    
    @Column(name = "margin_percentage", precision = 5, scale = 2)
    private BigDecimal marginPercentage = BigDecimal.ZERO;
    
    // Legacy fields for compatibility
    @Column(precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;
    
    @Column(name = "delivery_address", columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMaterial> orderMaterials = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLabor> orderLabor = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMachine> orderMachines = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTransaction> orderTransactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAlert> orderAlerts = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper methods to calculate costs
    public void updateActualCosts() {

        this.actualMaterialCost = orderMaterials.stream()
            .filter(material -> material.getMaterialType() == null || "ACTUAL".equalsIgnoreCase(material.getMaterialType()))
            .map(material -> material.getTotalCost() == null ? BigDecimal.ZERO : material.getTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.actualLaborCost = orderLabor.stream()
            .map(labor -> labor.getTotalCost() == null ? BigDecimal.ZERO : labor.getTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.actualMachineCost = orderMachines.stream()
            .map(machine -> machine.getTotalCost() == null ? BigDecimal.ZERO : machine.getTotalCost())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getTotalEstimatedCost() {
        return estimatedLaborCost.add(estimatedMaterialCost).add(estimatedMachineCost);
    }
    
    public BigDecimal getTotalActualCost() {
        return actualLaborCost.add(actualMaterialCost).add(actualMachineCost);
    }
}
