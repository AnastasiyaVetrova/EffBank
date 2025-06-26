package com.example.bankcards.exception;

import com.example.bankcards.entity.enums.OrderStatus;

public class InvalidOrderStatusException extends RuntimeException{

    public InvalidOrderStatusException(Long id, OrderStatus status) {
        super(String.format("Order with ID %d is in status %s and cannot be closed", id, status));
    }
}
