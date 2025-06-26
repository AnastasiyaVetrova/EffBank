package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CardLockOrderDto(
        @NotNull
        UUID cardId,

        @NotNull
        CardStatus requestedStatus,

        @NotNull
        @Size(max = 255)
        String reason
) {
}
