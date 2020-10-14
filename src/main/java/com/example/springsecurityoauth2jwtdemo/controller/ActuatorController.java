package com.example.springsecurityoauth2jwtdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actuator")
public class ActuatorController {
    @RequestMapping("/health")
    public ResponseEntity<Map<String, String>> getHealth() {
        Map<String, String> healthCheck = new HashMap<>();
        healthCheck.put("status", "UP");
        return new ResponseEntity<>(healthCheck, HttpStatus.OK);
    }
}
