package com.musicschool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test endpoint - no authentication required";
    }

    @GetMapping("/")
    public String home() {
        return "Home endpoint - no authentication required";
    }
}
