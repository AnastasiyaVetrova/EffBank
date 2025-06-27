package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.openapi.CardLockOrderApi;
import com.example.bankcards.dto.request.CardLockOrderDto;
import com.example.bankcards.dto.response.CardLockProcessingResponse;
import com.example.bankcards.dto.request.CloseLockOrderDto;
import com.example.bankcards.security.service.AuthenticatedUserService;
import com.example.bankcards.service.CardLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/card-lock")
@RequiredArgsConstructor
public class CardLockOrderController implements CardLockOrderApi {

    private final CardLockService cardLockService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> requestCardLock(CardLockOrderDto request) {
        UUID userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(cardLockService.requestCardLock(userId, request));
    }

    @PatchMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardLockProcessingResponse> getOrder(Long orderId) {
        UUID adminId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(cardLockService.getOrder(adminId, orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/response")
    public ResponseEntity<Void> closeOrder(CloseLockOrderDto request) {
        cardLockService.closeOrder(request);

        return ResponseEntity.noContent().build();
    }
}
