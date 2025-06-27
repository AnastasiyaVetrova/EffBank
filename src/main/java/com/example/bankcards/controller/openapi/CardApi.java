package com.example.bankcards.controller.openapi;

import com.example.bankcards.dto.request.CreateCardDto;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.request.UpdateCardStatusRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.dto.response.UserCardsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "Карты")
public interface CardApi {

    @Operation(
            summary = "Создание новой банковской карты (ADMIN)",
            description = """
                    Позволяет администратору создать карту для пользователя.
                    Карта будет активна с начальным балансом и сроком действия в годах.
                    """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Карта успешно создана",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<CardResponse> createCard(@Valid @RequestBody CreateCardDto dto);

    @Operation(
            summary = "Получение списка карт текущего пользователя (USER)",
            description = """
                    Возвращает список карт, принадлежащих авторизованному пользователю.
                    Доступно только для пользователей с ролью USER.
                    Информация выдается постранично.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список карт успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserCardsResponse.class)))),
                    @ApiResponse(responseCode = "403", description = "Нет доступа — пользователь не авторизован или недостаточно прав",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<List<UserCardsResponse>> getUserCards(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page must be greater or equal to 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be greater than 0") int size);

    @Operation(
            summary = "Получение всех карт (ADMIN)",
            description = """
                    Возвращает постраничный список всех карт в системе.
                    Доступно только для пользователей с ролью ADMIN.
                    Поддерживается сортировка и пагинация.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список карт успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CardResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры сортировки или пагинации",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "403", description = "Нет доступа — недостаточно прав",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<Page<CardResponse>> getAllCards(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page must be greater or equal to 1") int page,
            @RequestParam(defaultValue = "50") @Min(value = 1, message = "Size must be greater than 0") int size,
            @Parameter(
                    description = "сортировка по полю",
                    schema = @Schema(allowableValues = {"user", "cardNumber", "expirationDate", "status", "balance"}))
            @RequestParam(defaultValue = "expirationDate") String sortBy,
            @Parameter(
                    schema = @Schema(allowableValues = {"desc", "asc"})
            )
            @RequestParam(defaultValue = "desc") String sortDir);

    @Operation(
            summary = "Обновление статуса карты (ADMIN)",
            description = """
                    Позволяет администратору изменить статус карты.
                    Передавайте `userId`, `cardId` и новый `cardStatus`(принимает значения "ACTIVE", "BLOCKED", "EXPIRED").
                    Если карта уже находится в заданном статусе — возвращается ошибка.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Статус карты успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Карта уже имеет указанный статус или переданы некорректные данные"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения операции"),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю")
            })
    ResponseEntity<Void> updateStatus(@Valid @RequestBody UpdateCardStatusRequest request);

    @Operation(
            summary = "Удалить карту пользователя (ADMIN)",
            description = """
                    Удаляет карту с указанным ID, если её баланс равен 0.
                    Доступно только для администратора.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Карта успешно удалена"),
                    @ApiResponse(responseCode = "400", description = "Баланс карты не равен 0"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю")
            })
    ResponseEntity<Void> deleteCard(
            @Parameter(description = "ID карты", required = true) UUID cardId,
            @Parameter(description = "ID пользователя", required = true) UUID userId);

    @Operation(
            summary = "Получить баланс карты (USER)",
            description = """
                    Позволяет пользователю получить текущий баланс своей банковской карты по её UUID
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Баланс успешно получен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Карта не найдена или не принадлежит пользователю",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<BalanceResponse> getBalance(
            @Parameter(description = "ID карты", required = true) UUID cardId);

    @Operation(
            summary = "Перевод средств между картами пользователя (USER)",
            description = """
                    Позволяет пользователю перевести средства между своими банковскими картами
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Перевод выполнен успешно",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransferResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Недостаточно средств или некорректные данные",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Одна из карт не найдена",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "409", description = "Недостаточно средств",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request);
}
