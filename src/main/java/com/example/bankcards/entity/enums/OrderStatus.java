package com.example.bankcards.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус заказа")
public enum OrderStatus {
    @Schema(description = "Ожидает обработки")
    PENDING,
    @Schema(description = "В процессе обработки")
    IN_PROGRESS,
    @Schema(description = "Одобрен")
    APPROVED,
    @Schema(description = "Отклонён")
    REJECTED
}
