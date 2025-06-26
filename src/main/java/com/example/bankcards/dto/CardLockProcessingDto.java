package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardLockProcessingDto(
        long id,
        UUID userId,
        UUID adminId,
        CardStatus requestedStatus,
        String reason,
        LocalDate createdAt,
        String adminComment,
        OrderStatus status
) {
}
