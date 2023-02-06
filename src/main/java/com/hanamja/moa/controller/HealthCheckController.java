package com.hanamja.moa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/health")
public class HealthCheckController {

    @GetMapping(value = "/check")
    public ResponseEntity<?> serverCheck(){
        return ResponseEntity.ok().body("server check");
    }
}
