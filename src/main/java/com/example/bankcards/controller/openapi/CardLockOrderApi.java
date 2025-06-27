package com.example.bankcards.controller.openapi;

import com.example.bankcards.dto.request.CardLockOrderDto;
import com.example.bankcards.dto.request.CloseLockOrderDto;
import com.example.bankcards.dto.response.CardLockProcessingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Блокировка/разблокировка")
public interface CardLockOrderApi {
    @Operation(
            summary = "Запрос на блокировку/разблокировку карты (USER)",
            description = """
                    Позволяет пользователю отправить запрос на смену статуса карты (например, заблокировать её).
                    Передавайте `userId`, `cardId` и новый `cardStatus`(принимает значения "ACTIVE", "BLOCKED").
                    Если карта уже находится в заданном статусе — возвращается ошибка.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запрос на изменение статуса карты успешно создан",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса или карта уже имеет указанный статус",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<Long> requestCardLock(@Valid @RequestBody CardLockOrderDto request);

    @Operation(
            summary = "Получить задание на обработку блокировки карты (ADMIN)",
            description = """
                    Возвращает заказ на блокировку карты для администратора.
                    Если `orderId` не передан, выбирается первый доступный заказ со статусом `PENDING`.
                    Заказ переводится в статус `IN_PROGRESS` и привязывается к текущему администратору.
                    Доступно только для пользователей с ролью ADMIN.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ на блокировку карты успешно получен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardLockProcessingResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<CardLockProcessingResponse> getOrder(@RequestParam(required = false) Long orderId);

    @Operation(
            summary = "Завершить заказ на блокировку карты (ADMIN)",
            description = """
                    Закрывает заказ на блокировку карты.
                    Заказ должен находиться в статусе `IN_PROGRESS`, в противном случае будет выброшено исключение.
                    Доступно только для пользователей с ролью ADMIN.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Заказ успешно закрыт"),
                    @ApiResponse(responseCode = "400", description = "Некорректный статус заказа или невалидные данные"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            })
    ResponseEntity<Void> closeOrder(@Valid @RequestBody CloseLockOrderDto request);
}
