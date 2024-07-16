package com.middleware.service.payment_gateway.config;

import com.middleware.service.payment_gateway.model.AppUser;
import com.middleware.service.payment_gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the database is empty
            if (userRepository.count() == 0) {
                // Create and save admin user
                AppUser adminUser = new AppUser();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(passwordEncoder.encode("adminPassword"));
                adminUser.setRole("ADMIN");
                userRepository.save(adminUser);

                // Create and save regular user
                AppUser regularUser = new AppUser();
                regularUser.setUsername("user");
                regularUser.setEmail("user@example.com");
                regularUser.setPassword(passwordEncoder.encode("userPassword"));
                regularUser.setRole("USER");
                userRepository.save(regularUser);

                log.info("Database initialized with admin and regular user.");
            }
        };
    }
}
