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
@Table(name = "labor_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LaborMaster {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_code", unique = true, nullable = false, length = 50)
    private String employeeCode;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(length = 255)
    private String email;
    
    @Column(length = 20)
    private String phone;
    
    @Column(name = "job_title", length = 100)
    private String jobTitle;
    
    @Column(length = 100)
    private String department;
    
    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;
    
    @Column(name = "daily_rate", precision = 10, scale = 2)
    private BigDecimal dailyRate;
    
    @Column(name = "shift_type", length = 20)
    private String shiftType = "DAY"; // DAY, NIGHT, ROTATING
    
    @Column(name = "shift_wage", precision = 10, scale = 2)
    private BigDecimal shiftWage;
    
    @Column(name = "photo_url", length = 500)
    private String photoUrl;
    
    @Column(name = "approval_status", length = 20)
    private String approvalStatus = "APPROVED"; // DRAFT, PENDING_APPROVAL, APPROVED, REJECTED
    
    @Column(name = "skill_level", length = 50)
    private String skillLevel;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
