package com.example.bankcards.service;

import com.example.bankcards.dto.CardLockOrderDto;
import com.example.bankcards.dto.CardLockProcessingDto;
import com.example.bankcards.dto.CloseLockOrderDto;

import java.util.UUID;

public interface CardLockService {
    Long requestCardLock(UUID userId, CardLockOrderDto request);

    CardLockProcessingDto getOrder(UUID adminId, Long orderId);

    void closeOrder(CloseLockOrderDto request);
}
