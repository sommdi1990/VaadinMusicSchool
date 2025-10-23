package com.musicschool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Vaadin application.
 */
// Temporarily disabled for testing
// @Configuration
// @EnableWebSecurity
// @Order(2) // Lower priority than WebSecurityConfig
public class VaadinSecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain vaadinFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/dashboard/**", "/students/**", "/instructors/**", "/courses/**",
                        "/enrollments/**", "/reports/**", "/settings/**", "/api/**")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("Music School Management"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
