package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**

 * DTO for creating/updating Labor Master records
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaborMasterDTO {
    private Long id;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String email;
    private String phone;
    
    @NotBlank(message = "Job title is required")
    private String jobTitle;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    
    @NotNull(message = "Shift wage is required")
    @Positive(message = "Shift wage must be positive")
    private BigDecimal shiftWage;
    
    @NotBlank(message = "Shift type is required")
    private String shiftType; // DAY, NIGHT, ROTATING
    
    private String photoUrl;
    private String skillLevel;
    private String approvalStatus; // DRAFT, PENDING_APPROVAL, APPROVED, REJECTED
    private Boolean isActive;
    private LocalDate hireDate;
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
