package com.app.security2023.auth.controller;

import com.app.security2023.auth.dto.AuthRequest;
import com.app.security2023.auth.dto.RegisterRequest;
import com.app.security2023.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/register")
    //public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            return ResponseEntity.ok(service.register(req));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/authenticate")
    //public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest req) {
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest req) {
        try {
            return ResponseEntity.ok(service.authenticate(req));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }
}
