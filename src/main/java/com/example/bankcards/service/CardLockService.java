package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardLockOrderDto;
import com.example.bankcards.dto.response.CardLockProcessingResponse;
import com.example.bankcards.dto.request.CloseLockOrderDto;

import java.util.UUID;

public interface CardLockService {

    Long requestCardLock(UUID userId, CardLockOrderDto request);

    CardLockProcessingResponse getOrder(UUID adminId, Long orderId);

    void closeOrder(CloseLockOrderDto request);
}
