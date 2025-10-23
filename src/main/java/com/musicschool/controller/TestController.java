package com.musicschool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test endpoint - no authentication required";
    }

    // Removed home endpoint to allow Vaadin routing to work properly
    // @GetMapping("/")
    // public String home() {
    //     return "Home endpoint - no authentication required";
    // }
}
