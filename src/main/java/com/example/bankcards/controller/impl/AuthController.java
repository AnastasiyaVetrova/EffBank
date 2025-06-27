package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.openapi.AuthApi;
import com.example.bankcards.security.dto.UserLoginDto;
import com.example.bankcards.security.dto.UserRegisterDto;
import com.example.bankcards.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(UserRegisterDto userDto) {

        return ResponseEntity.ok(authService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(UserLoginDto userDto) {

        return ResponseEntity.ok(authService.login(userDto));
    }
}
