package com.example.bankcards.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDto(

        @Schema(description = "Имя пользователя", example = "Алексей")
        @NotBlank(message = "Поле не может быть пустым")
        String name,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotBlank(message = "Поле не может быть пустым")
        String surname,

        @Schema(description = "Телефон в формате +7XXXXXXXXXX", example = "+79998887766")
        @NotNull
        @Pattern(regexp = "\\+7\\d{10}", message = "Номер телефона должен начинаться с +7 и содержать 11 цифр")
        String phone,

        @Schema(description = "Пароль (минимум 8 символов, буквы и цифры)", example = "Pass1234")
        @NotNull
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "Пароль должен состоять из букв и цифр")
        String password) {
}
