package com.musicschool.controller;

import com.musicschool.config.JwtProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    @Autowired(required = false)
    private JwtProvider jwtProvider; // فرض بر وجود یک JwtProvider مشابه JwtRequestFilter

    @PostMapping("/messaging")
    public String sendDemoMessage(@RequestParam Long studentId) {
        if (rabbitTemplate == null) {
            return "RabbitMQ در این محیط پیکربندی نشده است. پیام شبیه‌سازی شده: " + studentId;
        }
        rabbitTemplate.convertAndSend("registerQueue", studentId);
        return "پیام با موفقیت به صف ارسال شد: " + studentId;
    }

    @PostMapping("/jwt")
    public String generateJwt(@RequestParam String username) {
        if (jwtProvider == null) {
            return "JWT Provider در این محیط موجود نیست.";
        }
        String jwt = jwtProvider.createToken(username, List.of("ROLE_USER"));
        return jwt;
    }
}
