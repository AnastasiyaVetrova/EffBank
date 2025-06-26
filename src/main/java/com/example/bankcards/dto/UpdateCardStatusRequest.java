package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateCardStatusRequest(
        @NotNull
        UUID userId,
        @NotNull
        UUID cardId,
        @NotNull
        CardStatus cardStatus
) {
}
