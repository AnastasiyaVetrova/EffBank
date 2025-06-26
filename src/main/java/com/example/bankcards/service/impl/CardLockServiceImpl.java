package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardLockOrderDto;
import com.example.bankcards.dto.CardLockProcessingDto;
import com.example.bankcards.dto.CloseLockOrderDto;
import com.example.bankcards.dto.mapper.CardLockOrderMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardLockOrder;
import com.example.bankcards.entity.enums.OrderStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.CardOwnershipNotFoundException;
import com.example.bankcards.exception.InvalidOrderStatusException;
import com.example.bankcards.exception.OrderNotFoundException;
import com.example.bankcards.repository.CardLockOrderRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class CardLockServiceImpl implements CardLockService {

        private final CardRepository cardRepository;
        private final CardLockOrderRepository cardLockOrderRepository;
        private final CardLockOrderMapper cardLockOrderMapper;

        @Override
        @Transactional
        public Long requestCardLock(UUID userId, CardLockOrderDto request) {
            Card card = cardRepository.findByIdAndUserId(request.cardId(), userId)
                    .orElseThrow(() -> new CardOwnershipNotFoundException(userId, request.cardId()));

            if (card.getStatus() == request.requestedStatus()) {
                throw new BadRequestException("The card is already in this status");
            }

            CardLockOrder cardLockOrder = CardLockOrder.builder()
                    .userId(userId)
                    .cardId(request.cardId())
                    .requestedStatus(request.requestedStatus())
                    .reason(request.reason())
                    .status(OrderStatus.PENDING)
                    .build();

            return cardLockOrderRepository.save(cardLockOrder).getId();
        }

        @Override
        @Transactional(isolation = Isolation.REPEATABLE_READ)
        public CardLockProcessingDto getOrder(UUID adminId, Long orderId) {
            CardLockOrder order = orderId == null ?
                    cardLockOrderRepository.findFirstByStatusOrderByCreatedAtAsc(OrderStatus.PENDING)
                            .orElseThrow(() -> new OrderNotFoundException("Pending order not found")) :
                    cardLockOrderRepository.findById(orderId)
                            .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            order.setAdminId(adminId);
            order.setStatus(OrderStatus.IN_PROGRESS);

            cardLockOrderRepository.save(order);

            return cardLockOrderMapper.mapToCardLockDto(order);
        }

        @Override
        @Transactional
        public void closeOrder(CloseLockOrderDto request) {
            CardLockOrder cardLockOrder = cardLockOrderRepository.findById(request.id())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            if (!cardLockOrder.getStatus().equals(OrderStatus.IN_PROGRESS)) {
                throw new InvalidOrderStatusException(request.id(), cardLockOrder.getStatus());
            }

            cardLockOrder.setClosedAt(LocalDate.now());
            cardLockOrder.setAdminComment(request.adminComment());
            cardLockOrder.setStatus(request.status());

            cardLockOrderRepository.save(cardLockOrder);
        }
    }
