package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.UserCardsResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.CardOwnershipNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.generator.CardNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberGenerator generator;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardResponse createCard(CreateCardDto dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(() -> new UserNotFoundException(dto.userId()));

        Card card = Card.builder()
                .cardNumber(generator.generate())
                .user(user)
                .expirationDate(LocalDate.now().plusYears(dto.validityPeriodYears()))
                .status(CardStatus.ACTIVE)
                .balance(dto.balance())
                .build();

        Card savedCard = cardRepository.save(card);

        return cardMapper.mapToCardResponse(savedCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCardsResponse> getCards(UUID userId, Pageable pageable) {

        return cardRepository.findByUserId(userId, pageable).stream()
                .map(cardMapper::mapToUserCardsResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAllCards(Pageable pageable) {
        try {
            return cardRepository.findAll(pageable).map(cardMapper::mapToCardResponse);

        } catch (PropertyReferenceException ex) {
            throw new BadRequestException("Sorting field is not supported");
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid pagination parameters");
        }
    }

    @Override
    @Transactional
    public void updateCardStatus(UpdateCardStatusRequest request) {
        Card card = findCardOrThrow(request.cardId(), request.userId());

        if (card.getStatus() == request.cardStatus()) {
            throw new BadRequestException("The card is already in this status");
        }

        card.setStatus(request.cardStatus());
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID cardId) {
        Card card = findCardOrThrow(cardId, userId);

        if (card.getBalance() != null && card.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BadRequestException("The card balance is not zero");
        }
        cardRepository.delete(card);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(UUID userId, UUID cardId) {

        return cardRepository.findBalanceByUserIdAndId(userId, cardId)
                .orElseThrow(() -> new CardOwnershipNotFoundException(cardId, userId));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransferResponse transfer(UUID userId, TransferRequest request) {
        Card fromCard = findCardOrThrow(request.fromCardId(), userId);
        Card toCard = findCardOrThrow(request.toCardId(), userId);

        if (toCard.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("The transfer card is not active");
        }

            if (fromCard.getBalance() == null || fromCard.getBalance().compareTo(request.amount()) < 0) {
                throw new InsufficientBalanceException(fromCard.getId());
            }

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));

        BigDecimal toBalance = toCard.getBalance() == null ? BigDecimal.ZERO : toCard.getBalance();

        toCard.setBalance(toBalance.add(request.amount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        return new TransferResponse(
                fromCard.getId(),
                fromCard.getBalance(),
                toCard.getId(),
                toCard.getBalance());
    }

    private Card findCardOrThrow(UUID cardId, UUID userId) {

        return cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new CardOwnershipNotFoundException(cardId, userId));
    }
}
