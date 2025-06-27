package com.example.bankcards.controller.openapi;

import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Пользователи")
public interface UserApi {

    @Operation(
            summary = "Назначить первого администратора",
            description = """
                    Назначает текущему пользователю роль ADMIN,
                    если в системе ещё нет пользователей с этой ролью.
                    Повторное выполнение запрещено.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Роль ADMIN успешно назначена"),
                    @ApiResponse(responseCode = "400", description = "Роль ADMIN уже назначена другому пользователю"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
            })
    ResponseEntity<Void> createFirstAdmin();

    @Operation(
            summary = "Назначить роль пользователю (ADMIN)",
            description = """
                    Назначает указанную роль пользователю по ID ("USER" или "ADMIN").
                    Требуется наличие прав администратора.
                    Повторное назначение существующей роли не влияет на результат.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Роль успешно назначена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные или роль уже назначена"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    ResponseEntity<Void> addRole(
            @Parameter(description = "ID пользователя", required = true) UUID userId,
            @Parameter(description = "Роль, которую нужно назначить",
                    required = true,
                    schema = @Schema(allowableValues = {"USER", "ADMIN"})) RoleType role);

    @Operation(
            summary = "Удалить роль у пользователя (ADMIN)",
            description = """
                    Удаляет указанную роль у пользователя с заданным ID ("USER" или "ADMIN").
                    Требуется наличие прав администратора.
                    Если у пользователя нет такой роли — операция всё равно завершается успешно.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Роль успешно удалена"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    ResponseEntity<Void> removeRole(
            @Parameter(description = "ID пользователя", required = true) UUID userId,
            @Parameter(description = "Роль, которую нужно назначить",
                    required = true,
                    schema = @Schema(allowableValues = {"USER", "ADMIN"})) RoleType role);

    @Operation(
            summary = "Обновить данные пользователя (ADMIN)",
            description = """
                    Обновляет информацию о пользователе по переданному идентификатору.
                    Только администратор имеет доступ к этому методу.
                    Обновление происходит частично: поля, отсутствующие в запросе, остаются без изменений.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Данные пользователя успешно обновлены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserRequest dto);
}
