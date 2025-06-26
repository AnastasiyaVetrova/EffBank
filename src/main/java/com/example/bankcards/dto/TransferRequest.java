package com.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull
        UUID fromCardId,

        @NotNull
        UUID toCardId,

        @NotNull
        @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0")
        @DecimalMax(value = "50000.00", message = "Сумма перевода должна быть меньше 50000.00")
        @Digits(integer = 5, fraction = 2)
        BigDecimal amount
) {
}
