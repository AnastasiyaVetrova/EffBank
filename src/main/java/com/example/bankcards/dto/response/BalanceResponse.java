package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Ответ с информацией о балансе карты")
public record BalanceResponse(

        @Schema(description = "ID карты", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID cardId,

        @Schema(description = "Баланс карты", example = "1500.75")
        BigDecimal balance
) {
}
