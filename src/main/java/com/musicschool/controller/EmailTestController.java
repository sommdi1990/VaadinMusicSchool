package com.musicschool.controller;

import com.musicschool.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> sendTestEmail(@RequestParam String email) {
        try {
            emailService.sendSimpleEmail(
                    email,
                    "Test Email from Music School",
                    "This is a test email to verify email functionality."
            );

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Test email sent successfully to " + email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/welcome")
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(
            @RequestParam String email,
            @RequestParam String name) {
        try {
            emailService.sendWelcomeEmail(email, name);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Welcome email sent successfully to " + email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send welcome email: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> emailHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Email service is running");
        response.put("message", "Email functionality is available");
        return ResponseEntity.ok(response);
    }
}
