package com.musicschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Music School Management System.
 * 
 * This application provides a comprehensive management system for music schools,
 * including student management, course scheduling, instructor management,
 * and financial tracking.
 * 
 * @author Music School Development Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class MusicSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicSchoolApplication.class, args);
    }
}
