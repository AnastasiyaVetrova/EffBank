package com.example.bankcards.controller;

import com.example.bankcards.dto.BalanceResponse;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CreateCardDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.UpdateCardStatusRequest;
import com.example.bankcards.dto.UserCardsResponse;
import com.example.bankcards.security.service.AuthenticatedUserService;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CreateCardDto dto) {

        return ResponseEntity.ok(cardService.createCard(dto));
    }

    @GetMapping()
    public ResponseEntity<List<UserCardsResponse>> getCard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UUID userId = authenticatedUserService.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(cardService.getCards(userId, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<CardResponse>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "expirationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/status")
    public ResponseEntity<Void> updateStatus(@RequestBody @Valid UpdateCardStatusRequest request) {

        cardService.updateCardStatus(request);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID userId, @PathVariable UUID cardId) {

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
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        UUID userId = authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(cardService.transfer(userId, request));
    }
}
