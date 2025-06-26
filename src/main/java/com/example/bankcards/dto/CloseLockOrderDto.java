package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CloseLockOrderDto(
        @NotNull
        Long id,

        @NotNull
        OrderStatus status,

        @Size(max = 255)
        String adminComment
) {
}
