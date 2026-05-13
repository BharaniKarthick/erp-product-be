package com.erp.controller;

import com.erp.dto.LoginRequestDTO;
import com.erp.dto.LoginResponseDTO;
import com.erp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**

 * REST Controller for Authentication
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Authentication", description = "User authentication API")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    /**
     * Login endpoint
     */
    @PostMapping("/login")
    @Operation(summary = "User login", 
               description = "Authenticate user with username and password")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authenticationService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", 
               description = "Logout and invalidate session token")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            authenticationService.logout(token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Validate token endpoint
     */
    @GetMapping("/validate")
    @Operation(summary = "Validate token", 
               description = "Check if session token is valid")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        boolean isValid = authenticationService.validateToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Change password endpoint
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change password", 
               description = "Change user password")
    public ResponseEntity<?> changePassword(
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            authenticationService.changePassword(userId, oldPassword, newPassword);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
