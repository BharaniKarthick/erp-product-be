package com.erp.config;

import com.erp.entity.Role;
import com.erp.entity.User;
import com.erp.repository.RoleRepository;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeAdminUser() {
        return args -> {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ADMIN");
                        role.setDescription("Administrator with full access");
                        return roleRepository.save(role);
                    });

            User adminUser = userRepository.findByUsername("admin")
                    .orElseGet(User::new);

            adminUser.setUsername("admin");
            adminUser.setEmail("admin@printflow.com");
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setIsActive(true);
            adminUser.setRole(adminRole);
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));

            userRepository.save(adminUser);
        };
    }
}
