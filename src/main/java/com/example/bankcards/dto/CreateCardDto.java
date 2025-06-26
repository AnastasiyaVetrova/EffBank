package com.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCardDto(
        @NotNull
        UUID userId,

        @Min(1)
        @Max(5)
        Integer validityPeriodYears,

        @NotNull
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
        BigDecimal balance
) {
}
