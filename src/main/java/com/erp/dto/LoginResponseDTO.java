package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**

 * DTO for login response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roleName;
    private String token; // Session token or JWT
    private LocalDateTime loginTime;
    private String message;
}
