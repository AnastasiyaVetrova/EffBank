package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationException(org.springframework.security.core.AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<String> handleInvalidJwtException(InvalidAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(err -> err.getField() + ": " + err.getDefaultMessage())
                        .toList()
        );
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleRequestProcessingException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Request processing error, check the request parameters");
    }

    @ExceptionHandler({PhoneAlreadyExistsException.class, BadRequestException.class, InvalidOrderStatusException.class})
    public ResponseEntity<String> badRequestException(PhoneAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, CardOwnershipNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<String> notFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> insufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
