package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для закрытия заявки на блокировку карты")
public record CloseLockOrderDto(

        @Schema(description = "ID заявки на блокировку", example = "1")
        @NotNull
        Long id,

        @Schema(description = "Итоговый статус заявки", example = "APPROVED")
        @NotNull
        OrderStatus status,

        @Schema(description = "Комментарий администратора", example = "Проверка завершена, блокировка одобрена")
        @Size(max = 255)
        String adminComment
) {
}
