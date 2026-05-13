package com.erp.service;

import com.erp.dto.LoginRequestDTO;
import com.erp.dto.LoginResponseDTO;
import com.erp.entity.User;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**

 * Service for Authentication Operations
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Authenticate user with username and password
     */
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Find user by username
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate session token (simplified - in production use JWT)
        String sessionToken = UUID.randomUUID().toString();
        
        // Build response
        return new LoginResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole() != null ? user.getRole().getName() : "UNKNOWN",
            sessionToken,
            LocalDateTime.now(),
            "Login successful"
        );
    }
    
    /**
     * Logout user (invalidate session)
     */
    public void logout(String token) {
        // In memory session management or JWT invalidation
        // For simplicity, this is a no-op
        // In production, you would invalidate the session/token
    }
    
    /**
     * Validate session token
     */
    public boolean validateToken(String token) {
        // In production, validate JWT or check session store
        // For simplicity, return true
        return token != null && !token.isEmpty();
    }
    
    /**
     * Register a new user (for admin use)
     */
    @Transactional
    public User registerUser(String username, String email, String password, 
                             String firstName, String lastName, Long roleId) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(true);
        
        // Note: Role setting would require RoleRepository
        // For simplicity, leaving it null for now
        
        return userRepository.save(user);
    }
    
    /**
     * Change user password
     */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Set new password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
