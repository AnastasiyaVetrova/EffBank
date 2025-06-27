package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Запрос на обновление статуса карты")
public record UpdateCardStatusRequest(

        @Schema(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID userId,

        @Schema(description = "ID карты", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID cardId,

        @Schema(description = "Новый статус карты")
        @NotNull
        CardStatus cardStatus
) {
}
