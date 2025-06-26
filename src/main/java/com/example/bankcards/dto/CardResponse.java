package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.util.serializer.CardNumberMaskSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID userId,
        @JsonSerialize(using = CardNumberMaskSerializer.class)
        String cardNumber,
        LocalDate expirationDate,
        CardStatus status,
        BigDecimal balance
) {
}
