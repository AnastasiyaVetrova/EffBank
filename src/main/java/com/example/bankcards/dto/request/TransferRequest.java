package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Запрос на перевод средств с одной карты на другую")
public record TransferRequest(

        @Schema(description = "ID карты, с которой осуществляется перевод", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID fromCardId,

        @Schema(description = "ID карты, на которую осуществляется перевод", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID toCardId,

        @Schema(description = "Сумма перевода", example = "999.99")
        @NotNull
        @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0")
        @DecimalMax(value = "50000.00", message = "Сумма перевода должна быть меньше 50000.00")
        @Digits(integer = 5, fraction = 2)
        BigDecimal amount
) {
}
