package com.musicschool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration for the Music School Management System.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.musicschool.repository")
@EnableTransactionManagement
public class JpaConfig {
    // JPA configuration is handled by Spring Boot auto-configuration
    // This class serves as a central place for JPA-related configuration
}
