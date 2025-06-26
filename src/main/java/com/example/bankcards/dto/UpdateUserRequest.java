package com.example.bankcards.dto;

import com.example.bankcards.util.validator.NoBlankSpaces;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UpdateUserRequest(
        @NotNull
        UUID id,

        @NoBlankSpaces
        String name,

        @NoBlankSpaces
        String surname,

        @Pattern(regexp = "\\+7\\d{10}",
                message = "Номер телефона должен начинаться с +7 и содержать 11 цифр")
        String phone,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "Пароль должен состоять из букв и цифр")
        String password
) {
}
