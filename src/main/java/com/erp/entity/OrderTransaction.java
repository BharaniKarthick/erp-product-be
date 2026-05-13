package com.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();
    
    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType; // MATERIAL_DEDUCTION, LABOR_ENTRY, MACHINE_ALLOCATION, STOCK_ADJUSTMENT, STATUS_CHANGE
    
    @Column(name = "action_description", nullable = false, length = 500)
    private String actionDescription;
    
    @Column(name = "quantity_or_duration", length = 50)
    private String quantityOrDuration; // e.g., "120 kg", "1 Shift", "2.5 h"
    
    @Column(name = "user_name", length = 100)
    private String userName;
    
    @Column(name = "cost_impact", precision = 15, scale = 2)
    private BigDecimal costImpact; // Can be NULL for non-cost transactions
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
