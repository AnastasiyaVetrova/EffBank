package com.example.bankcards.controller.openapi;

import com.example.bankcards.security.dto.UserLoginDto;
import com.example.bankcards.security.dto.UserRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Регистрация/Аутентификация")
public interface AuthApi {

    @Operation(
            summary = "Регистрация нового пользователя",
            description = """
                    Создаёт нового пользователя и возвращает JWT токен
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или пользователь уже существует",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto userDto);

    @Operation(
            summary = "Аутентификация пользователя",
            description = """
                    Авторизует пользователя и возвращает JWT токен
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход в систему",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "401", description = "Неверные данные для входа",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
            })
    ResponseEntity<String> login(@RequestBody UserLoginDto userDto);
}
