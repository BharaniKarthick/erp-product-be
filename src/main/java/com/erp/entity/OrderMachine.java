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
@Table(name = "order_machines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderMachine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;
    
    @Column(name = "machine_code", length = 50)
    private String machineCode;
    
    @Column(name = "machine_name", length = 255)
    private String machineName;
    
    @Column(name = "process_description", nullable = false, length = 255)
    private String processDescription;
    
    @Column(name = "uptime_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal uptimeHours;
    
    @Column(name = "hourly_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyCost;
    
    @Column(name = "total_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
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
        if (uptimeHours != null && hourlyCost != null) {
            this.totalCost = uptimeHours.multiply(hourlyCost);
        }
    }
}
