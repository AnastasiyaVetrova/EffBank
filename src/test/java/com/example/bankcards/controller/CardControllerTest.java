package com.example.bankcards.controller;

import com.example.bankcards.controller.impl.CardController;
import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.response.UserCardsResponse;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.CardOwnershipNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.security.JwtAuthFilter;
import com.example.bankcards.security.service.AuthenticatedUserService;
import com.example.bankcards.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CardController.class)
@AutoConfigureMockMvc
@DisplayName("Тестирование CardController.class")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private CardService cardService;

    @MockBean
    private AuthenticatedUserService authenticatedUserService;

    private UUID userId;

    private UUID cardId;

    @BeforeEach
    void setUp() throws Exception {

        userId = UUID.fromString("98765432-1987-6543-2198-987654321987");
        cardId = UUID.fromString("12345678-9021-3456-7890-123456789012");

        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Создание карты")
    void createCard_thenReturnCreatedResponseAnd200Status() throws Exception {
        CreateCardDto dto = new CreateCardDto(UUID.randomUUID(), 3, BigDecimal.valueOf(1000));

        CardResponse expectedResponse = new CardResponse(
                UUID.randomUUID(),
                dto.userId(),
                "1234567891",
                LocalDate.now().plusYears(3),
                CardStatus.ACTIVE,
                dto.balance());

        when(cardService.createCard(any(CreateCardDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id().toString()))
                .andExpect(jsonPath("$.userId").value(expectedResponse.userId().toString()))
                .andExpect(jsonPath("$.balance").value(expectedResponse.balance().intValue()))
                .andExpect(jsonPath("$.status").value(expectedResponse.status().toString()));

        verify(cardService).createCard(any(CreateCardDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 400 создания карты")
    void createCard_withInvalidDto_shouldReturn400Status() throws Exception {
        CreateCardDto invalidDto = new CreateCardDto(UUID.randomUUID(), 1, BigDecimal.valueOf(-10));

        mockMvc.perform(post("/api/v1/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Получение всех карт пользователя")
    void getUserCards_thenReturnListAnd200Status() throws Exception {

        List<UserCardsResponse> cards = List.of(mock(UserCardsResponse.class), mock(UserCardsResponse.class));

        when(authenticatedUserService.getCurrentUserId()).thenReturn(userId);
        when(cardService.getCards(eq(userId), any(Pageable.class))).thenReturn(cards);

        mockMvc.perform(get("/api/v1/cards")
                        .param("page", "1")
                        .param("size", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(cards.size()));
    }

    @Test
    @WithMockUser
    @DisplayName("Получение админом всех карт")
    void getAllCards_thenReturnListAnd200Status() throws Exception {

        List<CardResponse> cards = List.of(mock(CardResponse.class), mock(CardResponse.class));
        Page<CardResponse> pageResult = new PageImpl<>(cards,
                PageRequest.of(0, 2,
                        Sort.by("id").ascending()), cards.size());

        when(cardService.getAllCards(any(Pageable.class))).thenReturn(pageResult);

        mockMvc.perform(get("/api/v1/cards/all")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sortBy", "id")
                        .param("sortDir", "asc")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(cards.size()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Ошибка 400 при попытке получить все карты")
    void getAllCards_withInvalidSort_shouldReturn400Status() throws Exception {

        when(cardService.getAllCards(any(Pageable.class))).thenThrow(new BadRequestException("Invalid sort"));

        mockMvc.perform(get("/api/v1/cards/all")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sortBy", "id")
                        .param("sortDir", "invalidDirection")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Обновление статуса карты")
    void updateStatus_andReturn204Status() throws Exception {
        UpdateCardStatusRequest request = new UpdateCardStatusRequest(
                UUID.randomUUID(), UUID.randomUUID(), CardStatus.BLOCKED);

        doNothing().when(cardService).updateCardStatus(any(UpdateCardStatusRequest.class));

        mockMvc.perform(patch("/api/v1/cards/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(cardService).updateCardStatus(any(UpdateCardStatusRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 400 при попытке обновить статус карты")
    void updateStatus_whenBadRequest_shouldReturn400Status() throws Exception {
        UpdateCardStatusRequest request = new UpdateCardStatusRequest(
                UUID.randomUUID(), UUID.randomUUID(), CardStatus.BLOCKED);

        doThrow(new BadRequestException("The card is already in this status"))
                .when(cardService).updateCardStatus(any(UpdateCardStatusRequest.class));

        mockMvc.perform(patch("/api/v1/cards/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 404 при попытке обновить статус карты")
    void updateStatus_whenNotFound_shouldReturn404Status() throws Exception {
        UpdateCardStatusRequest request = new UpdateCardStatusRequest(
                UUID.randomUUID(), UUID.randomUUID(), CardStatus.BLOCKED);

        doThrow(new CardOwnershipNotFoundException(cardId, userId))
                .when(cardService).updateCardStatus(any(UpdateCardStatusRequest.class));

        mockMvc.perform(patch("/api/v1/cards/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Удаление карты")
    void deleteCard_thenReturnBalanceAnd204Status() throws Exception {

        doNothing().when(cardService).delete(userId, cardId);

        mockMvc.perform(delete("/api/v1/cards/{cardId}/users/{userId}", cardId, userId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(cardService).delete(userId, cardId);
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 400 при попытке удалить карту")
    void deleteCard_whenBalanceNotZero_shouldReturn400Status() throws Exception {

        doThrow(new BadRequestException("The card balance is not zero"))
                .when(cardService).delete(userId, cardId);

        mockMvc.perform(delete("/api/v1/cards/{cardId}/users/{userId}", cardId, userId)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Получение баланса карты пользователем")
    void getBalance_thenReturnBalanceAnd200Status() throws Exception {

        BalanceResponse balanceResponse = new BalanceResponse(cardId, BigDecimal.valueOf(1500));

        when(authenticatedUserService.getCurrentUserId()).thenReturn(userId);
        when(cardService.getBalance(userId, cardId)).thenReturn(balanceResponse);

        mockMvc.perform(get("/api/v1/cards/{cardId}/balance", cardId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(balanceResponse.balance()));
    }

    @Test
    @WithMockUser
    @DisplayName("Перевод средств с карты на карту")
    void transfer_thenReturnTransferResponseAnd200Status() throws Exception {

        TransferRequest request = getTransferRequest();
        TransferResponse response = new TransferResponse(
                cardId, BigDecimal.valueOf(100), request.toCardId(), BigDecimal.valueOf(1000));

        when(authenticatedUserService.getCurrentUserId()).thenReturn(userId);
        when(cardService.transfer(userId, request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fromCardId").value(response.fromCardId().toString()))
                .andExpect(jsonPath("$.fromCardBalanceAfter").value(response.fromCardBalanceAfter()))
                .andExpect(jsonPath("$.toCardId").value(response.toCardId().toString()))
                .andExpect(jsonPath("$.toCardBalanceAfter").value(response.toCardBalanceAfter()));
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 400 при переводе средств")
    void transfer_whenBadRequest_shouldReturn400Status() throws Exception {

        TransferRequest request = getTransferRequest();

        when(authenticatedUserService.getCurrentUserId()).thenReturn(userId);
        when(cardService.transfer(userId, request)).thenThrow(new BadRequestException("The transfer card is not active"));

        mockMvc.perform(post("/api/v1/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Ошибка 404 при переводе средств")
    void transfer_whenCardNotFound_shouldReturn404Status() throws Exception {
        TransferRequest request = getTransferRequest();

        when(authenticatedUserService.getCurrentUserId()).thenReturn(userId);
        when(cardService.transfer(userId, request)).thenThrow(new InsufficientBalanceException(cardId));

        mockMvc.perform(post("/api/v1/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    private TransferRequest getTransferRequest() {
        UUID toCardId = UUID.randomUUID();
        return new TransferRequest(
                cardId, toCardId, BigDecimal.valueOf(1000));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
