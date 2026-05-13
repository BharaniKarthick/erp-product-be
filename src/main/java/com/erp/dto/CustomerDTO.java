package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    
    @NotBlank(message = "Customer code is required")
    @Size(max = 50)
    private String customerCode;
    
    @NotBlank(message = "Company name is required")
    @Size(max = 255)
    private String companyName;
    
    @Size(max = 100)
    private String contactPerson;
    
    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String email;
    
    @Size(max = 20)
    private String phone;
    
    @Size(max = 20)
    private String mobile;
    
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String taxId;
    private BigDecimal creditLimit;
    private String paymentTerms;
    private Boolean isActive;
    private String notes;
}
