package com.example.bankcards.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginDto(

        @Schema(description = "Номер телефона пользователя", example = "+71234567890")
        String phone,

        @Schema(description = "Пароль пользователя", example = "Pass1234")
        String password) {
}
