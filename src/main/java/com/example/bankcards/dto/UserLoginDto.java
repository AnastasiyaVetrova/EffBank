package com.example.bankcards.dto;

import jakarta.validation.constraints.Pattern;

public record UserLoginDto(
        @Pattern(regexp = "\\+7\\d{10}", message = "Номер телефона должен начинаться с +7 и содержать 11 цифр")
        String phone,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "Пароль должен состоять из букв и цифр")
        String password) {
}
