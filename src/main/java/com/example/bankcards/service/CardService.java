package com.example.bankcards.service;

import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.UserCardsResponse;
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
