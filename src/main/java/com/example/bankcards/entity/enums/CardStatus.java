package com.example.bankcards.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус банковской карты")
public enum CardStatus {
    @Schema(description = "Активна и доступна для использования")
    ACTIVE,
    @Schema(description = "Заблокирована, операции не выполняются")
    BLOCKED,
    @Schema(description = "Истёк срок действия карты")
    EXPIRED
}
