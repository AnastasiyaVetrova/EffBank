package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Запрос на изменение статуса карты (блокировка/разблокировка)")
public record CardLockOrderDto(

        @Schema(description = "ID карты, для которой меняется статус", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID cardId,

        @Schema(description = "Желаемый статус карты", example = "BLOCKED")
        @NotNull
        CardStatus requestedStatus,

        @Schema(description = "Причина блокировки/разблокировки", example = "Пользователь сообщил о краже карты")
        @NotNull
        @Size(max = 255)
        String reason
) {
}
