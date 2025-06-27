package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.util.serializer.CardNumberMaskSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Информация о карте")
public record CardResponse(

        @Schema(description = "ID карты", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "ID владельца карты", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "Номер карты (маскированный)", example = "**** **** **** 1234")
        @JsonSerialize(using = CardNumberMaskSerializer.class)
        String cardNumber,

        @Schema(description = "Срок действия карты", example = "2025-12-31")
        LocalDate expirationDate,

        @Schema(description = "Статус карты", example = "ACTIVE")
        CardStatus status,

        @Schema(description = "Баланс карты", example = "1000.00")
        BigDecimal balance
) {
}
