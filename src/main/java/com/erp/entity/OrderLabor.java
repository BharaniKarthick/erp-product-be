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

@Entity
@Table(name = "order_labor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderLabor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labor_id", nullable = false)
    private LaborMaster labor;

    // Only order-specific operational fields are stored here.
    // All labor metadata (name, rate, role, code) is read from labor_master via the FK.
    @Column(name = "duration_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal durationHours;
    
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "shift_date")
    private LocalDate shiftDate = LocalDate.now();
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    

    @PrePersist
    @PreUpdate
    private void calculateTotalCost() {
        if (this.totalCost != null) {
            return;
        }

        if (labor != null && labor.getShiftWage() != null) {
            this.totalCost = labor.getShiftWage();
            return;
        }

        this.totalCost = BigDecimal.ZERO;
    }
}
