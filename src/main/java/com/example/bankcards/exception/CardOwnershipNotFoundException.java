package com.example.bankcards.exception;

import java.util.UUID;

public class CardOwnershipNotFoundException extends RuntimeException {
    public CardOwnershipNotFoundException(UUID cardId, UUID userId) {
        super(String.format("User with id = %s does not have a card with id = %s", userId, cardId));
    }
}
