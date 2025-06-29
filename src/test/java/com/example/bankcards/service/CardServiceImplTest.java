package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.response.UserCardsResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.CardOwnershipNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.util.generator.CardNumberGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование CardService.class")
class CardServiceImplTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardNumberGenerator generator;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardServiceImpl;

    private UUID userId;

    private UUID cardId;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("98765432-1987-6543-2198-987654321987");
        cardId = UUID.fromString("12345678-9021-3456-7890-123456789012");
    }

    @Test
    @DisplayName("Создание карты")
    void createCard_andReturnCardResponse() {
        CreateCardDto cardDto = new CreateCardDto(userId, 3, new BigDecimal(1000));
        String cardNumber = "1234-5678-9012-3456";
        CardResponse expected = getCardResponse(cardNumber);
        Card card = getCard();

        when(userRepository.findById(userId)).thenReturn(Optional.of(getUser()));
        when(generator.generate()).thenReturn(cardNumber);
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.mapToCardResponse(card)).thenReturn(expected);

        CardResponse response = cardServiceImpl.createCard(cardDto);

        assertNotNull(response);
        assertEquals(expected, response);

        verify(userRepository).findById(userId);
        verify(generator).generate();
        verify(cardRepository).save(any(Card.class));
        verify(cardMapper).mapToCardResponse(any(Card.class));
    }


    @Test
    @DisplayName("Получение списка карт пользователя")
    void getCards_andReturnListCards() {
        Pageable pageable = PageRequest.of(0, 5);

        when(cardRepository.findByUserId(userId, pageable))
                .thenReturn(List.of(mock(Card.class), mock(Card.class)));
        when(cardMapper.mapToUserCardsResponse(any(Card.class))).thenReturn(mock(UserCardsResponse.class));

        List<UserCardsResponse> result = cardServiceImpl.getCards(userId, pageable);

        assertEquals(2, result.size());

        verify(cardRepository).findByUserId(userId, pageable);
        verify(cardMapper, times(2)).mapToUserCardsResponse(any(Card.class));
    }

    @Test
    @DisplayName("Получение всех карт")
    void getAllCards_andReturnListCards() {
        Sort sort = Sort.by(Sort.Direction.ASC, "expirationDate");
        Pageable pageable = PageRequest.of(0, 10, sort);

        when(cardRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(mock(Card.class), mock(Card.class)), pageable, 2));
        when(cardMapper.mapToCardResponse(any(Card.class))).thenReturn(mock(CardResponse.class));

        Page<CardResponse> result = cardServiceImpl.getAllCards(pageable);

        assertEquals(2, result.getContent().size());

        verify(cardRepository).findAll(pageable);
        verify(cardMapper, times(2)).mapToCardResponse(any(Card.class));
    }

    @Test
    @DisplayName("Ошибка при получении всех карт — неподдерживаемое поле сортировки")
    void getAllCards_shouldThrowBadRequest_whenPropertyReferenceExceptionThrown() {
        Pageable pageable = mock(Pageable.class);

        when(cardRepository.findAll(pageable)).thenThrow(PropertyReferenceException.class);

        BadRequestException ex = assertThrows(
                BadRequestException.class, () -> cardServiceImpl.getAllCards(pageable));

        assertEquals("Sorting field is not supported", ex.getMessage());

        verify(cardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Ошибка при получении всех карт — некорректные параметры пагинации")
    void getAllCards_shouldThrowBadRequest_whenIllegalArgumentExceptionThrown() {
        Pageable pageable = mock(Pageable.class);

        when(cardRepository.findAll(pageable)).thenThrow(IllegalArgumentException.class);

        BadRequestException ex = assertThrows(
                BadRequestException.class, () -> cardServiceImpl.getAllCards(pageable));

        assertEquals("Invalid pagination parameters", ex.getMessage());

        verify(cardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Обновление статуса карты")
    void updateCardStatus_andUpdate() {

        UpdateCardStatusRequest request = new UpdateCardStatusRequest(userId, cardId, CardStatus.BLOCKED);
        Card card = getCard();

        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(card));

        cardServiceImpl.updateCardStatus(request);

        assertEquals(request.cardStatus(), card.getStatus());

        verify(cardRepository).findByIdAndUserId(cardId, userId);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("Ошибка при обновлении статуса — статус уже установлен")
    void updateCardStatus_shouldThrowBadRequest_ifStatusIsSame() {
        UpdateCardStatusRequest request = new UpdateCardStatusRequest(userId, cardId, CardStatus.ACTIVE);
        Card card = getCard();

        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(card));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> cardServiceImpl.updateCardStatus(request));

        assertEquals("The card is already in this status", exception.getMessage());
        verify(cardRepository, never()).save(card);
    }

    @Test
    @DisplayName("Удаление карты")
    void deleteCard_whenBalanceIsZero_shouldDeleteCard() {
        Card card = getCard();
        card.setBalance(BigDecimal.ZERO);

        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(card));

        cardServiceImpl.delete(userId, cardId);

        verify(cardRepository).findByIdAndUserId(cardId, userId);
        verify(cardRepository).delete(card);
    }

    @Test
    @DisplayName("Ошибка при удалении карты — баланс не ноль")
    void deleteCard_whenBalanceIsNotZero_shouldThrowBadRequest() {
        Card card = getCard();
        card.setBalance(new BigDecimal("100"));

        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(card));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> cardServiceImpl.delete(userId, cardId));

        assertEquals("The card balance is not zero", exception.getMessage());

        verify(cardRepository).findByIdAndUserId(cardId, userId);
        verify(cardRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Получение баланса карты")
    void getBalance_andReturnBalanceResponse() {
        BalanceResponse expected = new BalanceResponse(cardId, new BigDecimal("2500.00"));

        when(cardRepository.findBalanceByUserIdAndId(userId, cardId)).thenReturn(Optional.of(expected));

        BalanceResponse result = cardServiceImpl.getBalance(userId, cardId);

        assertNotNull(result);
        assertEquals(expected.balance(), result.balance());

        verify(cardRepository).findBalanceByUserIdAndId(userId, cardId);
    }

    @Test
    @DisplayName("Ошибка при получении баланса — карта не найдена")
    void getBalance_whenCardNotFound_shouldThrowException() {
        String expectedMessage = new CardOwnershipNotFoundException(cardId, userId).getMessage();

        when(cardRepository.findBalanceByUserIdAndId(userId, cardId)).thenReturn(Optional.empty());

        CardOwnershipNotFoundException ex = assertThrows(
                CardOwnershipNotFoundException.class,
                () -> cardServiceImpl.getBalance(userId, cardId));

        assertEquals(expectedMessage, ex.getMessage());

        verify(cardRepository).findBalanceByUserIdAndId(userId, cardId);
    }

    @Test
    @DisplayName("Перевод средств с одной карты на другую")
    void transfer_andReturnTransferResponse() {
        UUID fromCardId = UUID.randomUUID();
        UUID toCardId = UUID.randomUUID();

        TransferRequest request = new TransferRequest(fromCardId, toCardId, new BigDecimal("100.00"));
        Card fromCard = Card.builder()
                .id(fromCardId)
                .user(getUser())
                .balance(new BigDecimal("200.00"))
                .status(CardStatus.ACTIVE).build();

        Card toCard = Card.builder()
                .id(toCardId)
                .user(getUser())
                .balance(new BigDecimal("50.00"))
                .status(CardStatus.ACTIVE).build();

        when(cardRepository.findByIdAndUserId(fromCardId, userId)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUserId(toCardId, userId)).thenReturn(Optional.of(toCard));

        TransferResponse response = cardServiceImpl.transfer(userId, request);

        assertEquals(new BigDecimal("100.00"), fromCard.getBalance());
        assertEquals(new BigDecimal("150.00"), toCard.getBalance());

        assertEquals(fromCardId, response.fromCardId());
        assertEquals(toCardId, response.toCardId());
        assertEquals(fromCard.getBalance(), response.fromCardBalanceAfter());
        assertEquals(toCard.getBalance(), response.toCardBalanceAfter());

        verify(cardRepository).save(fromCard);
        verify(cardRepository).save(toCard);
    }

    @Test
    @DisplayName("Ошибка при переводе средств — карта не активна")
    void transfer_whenToCardNotActive_shouldThrowException() {
        UUID fromCardId = UUID.randomUUID();
        TransferRequest request = new TransferRequest(fromCardId, cardId, new BigDecimal("100.00"));

        Card fromCard = Card.builder().id(fromCardId).build();
        Card toCard = getCard();
        toCard.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findByIdAndUserId(fromCardId, userId)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUserId(cardId, userId)).thenReturn(Optional.of(toCard));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> cardServiceImpl.transfer(userId, request));

        assertEquals("The transfer card is not active", ex.getMessage());
    }

    @Test
    @DisplayName("Ошибка при переводе средств — недостаточно средств")
    void transfer_whenInsufficientFunds_shouldThrowException() {
        UUID fromCardId = UUID.randomUUID();
        UUID toCardId = UUID.randomUUID();

        String expectedMessage = new InsufficientBalanceException(fromCardId).getMessage();

        TransferRequest request = new TransferRequest(fromCardId, toCardId, new BigDecimal("300.00"));
        Card fromCard = Card.builder()
                .id(fromCardId)
                .user(getUser())
                .balance(new BigDecimal("200.00"))
                .status(CardStatus.ACTIVE).build();

        Card toCard = Card.builder()
                .id(toCardId)
                .user(getUser())
                .balance(new BigDecimal("50.00"))
                .status(CardStatus.ACTIVE).build();

        when(cardRepository.findByIdAndUserId(fromCardId, userId)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUserId(toCardId, userId)).thenReturn(Optional.of(toCard));

        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class,
                () -> cardServiceImpl.transfer(userId, request));

        assertEquals(expectedMessage, ex.getMessage());
    }

    private User getUser() {
        return User.builder().id(userId).build();
    }

    private Card getCard() {
        return Card.builder()
                .id(cardId)
                .user(getUser())
                .status(CardStatus.ACTIVE)
                .build();
    }

    private CardResponse getCardResponse(String cardNumber) {
        return new CardResponse(
                cardId,
                userId,
                cardNumber,
                LocalDate.now(),
                CardStatus.ACTIVE,
                new BigDecimal(1000));
    }
}
