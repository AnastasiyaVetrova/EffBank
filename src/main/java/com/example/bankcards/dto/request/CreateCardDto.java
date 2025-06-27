package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO для создания новой банковской карты")
public record CreateCardDto(

        @Schema(description = "ID пользователя, для которого создается карта", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID userId,

        @Schema(description = "Срок действия карты в годах (от 1 до 5 лет)", example = "3")
        @Min(1)
        @Max(5)
        @NotNull
        Integer validityPeriodYears,

        @Schema(description = "Начальный баланс карты", example = "1000.00")
        @NotNull
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
        BigDecimal balance
) {
}
