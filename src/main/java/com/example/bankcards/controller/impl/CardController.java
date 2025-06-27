package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.openapi.CardApi;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.UserCardsResponse;
import com.example.bankcards.security.service.AuthenticatedUserService;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController implements CardApi {

    private final CardService cardService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardResponse> createCard(CreateCardDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(dto));
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserCardsResponse>> getUserCards(int page, int size) {

        UUID userId = authenticatedUserService.getCurrentUserId();
        Pageable pageable = PageRequest.of(page - 1, size);

        return ResponseEntity.ok(cardService.getCards(userId, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<CardResponse>> getAllCards(
            int page, int size, String sortBy, String sortDir) {

        Sort.Direction direction = sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/status")
    public ResponseEntity<Void> updateStatus(UpdateCardStatusRequest request) {

        cardService.updateCardStatus(request);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cardId}/users/{userId}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID cardId, @PathVariable UUID userId) {

        cardService.delete(userId, cardId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable UUID cardId) {
        UUID userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(cardService.getBalance(userId, cardId));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(TransferRequest request) {
        UUID userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(cardService.transfer(userId, request));
    }
}
