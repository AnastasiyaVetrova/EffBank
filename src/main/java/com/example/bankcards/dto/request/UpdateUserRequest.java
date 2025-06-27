package com.example.bankcards.dto.request;

import com.example.bankcards.util.validator.NoBlankSpaces;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Schema(
        description = "**Передавайте только ID и поля, которые хотите изменить.**\n\nЗапрос на обновление данных пользователя.")
public record UpdateUserRequest(

        @Schema(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID id,

        @Schema(description = "Имя пользователя", example = "Иван")
        @NoBlankSpaces
        String name,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NoBlankSpaces
        String surname,

        @Schema(description = "Телефон пользователя", example = "+79001234567")
        @Pattern(regexp = "\\+7\\d{10}",
                message = "Номер телефона должен начинаться с +7 и содержать 11 цифр")
        String phone
) {
}
