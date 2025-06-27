package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Информация о заявке на блокировку/разблокировку карты")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardLockProcessingResponse(

        @Schema(description = "ID заявки", example = "1")
        long id,

        @Schema(description = "ID пользователя, подавшего заявку", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "ID карты, которой нужно поменять статус", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID cardId,

        @Schema(description = "ID администратора, обработавшего заявку", example = "123e4567-e89b-12d3-a456-426614174000", nullable = true)
        UUID adminId,

        @Schema(description = "Запрошенный статус карты", example = "BLOCKED")
        CardStatus requestedStatus,

        @Schema(description = "Причина изменения статуса", example = "Подозрение на мошенничество")
        String reason,

        @Schema(description = "Дата создания заявки", example = "2025-06-27")
        LocalDate createdAt,

        @Schema(description = "Комментарий администратора", example = "Подтверждено", nullable = true)
        String adminComment,

        @Schema(description = "Текущий статус заявки", example = "IN_PROGRESS")
        OrderStatus status
) {
}
