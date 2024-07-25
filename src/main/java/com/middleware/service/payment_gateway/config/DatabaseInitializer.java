package com.middleware.service.payment_gateway.config;

import com.middleware.service.payment_gateway.model.Role;
import com.middleware.service.payment_gateway.model.User;
import com.middleware.service.payment_gateway.repository.RoleRepository;
import com.middleware.service.payment_gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class DatabaseInitializer {

    @Value("${admin.password}")
    private String adminPassword;
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()){
                Role role = new Role("ADMIN", "Admin Role");
                roleRepository.save(role);
            // Check if the database is empty
            if (userRepository.count() == 0) {
                // Create and save admin user
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRoles(Set.of(role));
                userRepository.save(adminUser);
            }
                log.info("Database initialized with admin");
            }
        };
    }
}
