package com.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderAlert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // DELIVERY_PROXIMITY, BUDGET_OVERRUN, LOW_INVENTORY, DELAY
    
    @Column(name = "alert_name", nullable = false, length = 255)
    private String alertName;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled = true;
    
    @Column(name = "threshold_value", length = 100)
    private String thresholdValue; // e.g., "48 Hours", "100%"
    
    @Column(name = "alert_status", length = 50)
    private String alertStatus = "ACTIVE"; // ACTIVE, TRIGGERED, RESOLVED, DISMISSED
    
    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
