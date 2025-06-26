package com.example.bankcards.exception;

import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(UUID cardId) {
        super(String.format("Insufficient balance for card %s", cardId));
    }
}
