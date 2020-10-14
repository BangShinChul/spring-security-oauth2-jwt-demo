package com.example.springsecurityoauth2jwtdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/hello")
    public ResponseEntity<String> sayHi() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

}
