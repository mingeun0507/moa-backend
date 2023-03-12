package com.hanamja.moa.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/health")
public class HealthCheckController {

    @GetMapping(value = "/check")
    public ResponseEntity<?> serverCheck() {
        return ResponseEntity.ok().body("server check hello");
    }

    @GetMapping(value = "/time")
    public ResponseEntity<LocalDateTime> serverTime() {
        return ResponseEntity.ok().body(LocalDateTime.now());
    }
}
