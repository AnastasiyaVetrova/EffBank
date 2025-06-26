package com.example.bankcards.controller;

import com.example.bankcards.security.dto.UserLoginDto;
import com.example.bankcards.security.dto.UserRegisterDto;
import com.example.bankcards.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto userDto) {

        return ResponseEntity.ok(authService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userDto) {

        return ResponseEntity.ok(authService.login(userDto));
    }
}
