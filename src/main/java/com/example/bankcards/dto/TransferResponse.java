package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID fromCardId,
        BigDecimal fromCardBalanceAfter,
        UUID toCardId,
        BigDecimal toCardBalanceAfter
) {
}
