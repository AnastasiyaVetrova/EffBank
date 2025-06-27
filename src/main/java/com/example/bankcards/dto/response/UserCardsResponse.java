package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.util.serializer.CardNumberMaskSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ответ с информацией по карте пользователя")
public record UserCardsResponse(

        @Schema(description = "ID карты", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Номер карты (маскированный)", example = "**** **** **** 1234")
        @JsonSerialize(using = CardNumberMaskSerializer.class)
        String cardNumber,

        @Schema(description = "Дата окончания действия карты", example = "2025-12-31")
        LocalDate expirationDate,

        @Schema(description = "Статус карты", example = "ACTIVE")
        CardStatus status) {
}
