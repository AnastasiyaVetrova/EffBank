package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Ответ на успешный перевод средств между картами")
public record TransferResponse(

        @Schema(description = "ID карты, с которой был выполнен перевод", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID fromCardId,

        @Schema(description = "Оставшийся баланс на карте отправителя после перевода", example = "1000.00")
        BigDecimal fromCardBalanceAfter,

        @Schema(description = "ID карты, на которую был выполнен перевод", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID toCardId,

        @Schema(description = "Баланс карты получателя после перевода", example = "2500.50")
        BigDecimal toCardBalanceAfter
) {
}
