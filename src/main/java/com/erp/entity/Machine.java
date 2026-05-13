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
@Table(name = "machines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Machine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "machine_code", unique = true, nullable = false, length = 50)
    private String machineCode;
    
    @Column(name = "machine_name", nullable = false, length = 255)
    private String machineName;
    
    @Column(name = "machine_type", length = 100)
    private String machineType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 100)
    private String location;
    
    @Column(name = "hourly_cost", precision = 10, scale = 2)
    private BigDecimal hourlyCost = BigDecimal.ZERO;
    
    @Column(length = 50)
    private String status = "OPERATIONAL"; // OPERATIONAL, MAINTENANCE, BROKEN, RETIRED
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;
    
    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
