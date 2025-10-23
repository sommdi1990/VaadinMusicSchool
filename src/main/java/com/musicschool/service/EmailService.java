package com.musicschool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@musicschool.com");

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    public void sendWelcomeEmail(String to, String studentName) {
        String subject = "Welcome to Music School!";
        String text = String.format(
                "Dear %s,\n\n" +
                        "Welcome to our Music School Management System!\n\n" +
                        "We're excited to have you as part of our community.\n\n" +
                        "Best regards,\n" +
                        "Music School Team",
                studentName
        );
        sendSimpleEmail(to, subject, text);
    }

    public void sendNotificationEmail(String to, String notification) {
        String subject = "Music School Notification";
        String text = String.format(
                "Dear Student,\n\n" +
                        "You have a new notification:\n\n" +
                        "%s\n\n" +
                        "Best regards,\n" +
                        "Music School Team",
                notification
        );
        sendSimpleEmail(to, subject, text);
    }
}
