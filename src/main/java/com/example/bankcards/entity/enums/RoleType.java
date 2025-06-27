package com.example.bankcards.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип роли пользователя")
public enum RoleType {
    @Schema(description = "Администратор с расширенными правами")
    ADMIN,
    @Schema(description = "Обычный пользователь")
    USER
}
