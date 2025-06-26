package com.example.bankcards.service;

import com.example.bankcards.dto.BalanceResponse;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.CreateCardDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.dto.UpdateCardStatusRequest;
import com.example.bankcards.dto.UserCardsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CardService {

    CardResponse createCard(CreateCardDto dto);

    List<UserCardsResponse> getCards(UUID userId, Pageable pageable);

    Page<CardResponse> getAllCards(Pageable pageable);

    void updateCardStatus(UpdateCardStatusRequest request);

    void delete(UUID userId, UUID cardId);

    BalanceResponse getBalance(UUID userId, UUID cardId);

    TransferResponse transfer(UUID userId, TransferRequest request);
}
