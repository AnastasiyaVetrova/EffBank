package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.openapi.UserApi;
import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.enums.RoleType;
import com.example.bankcards.security.service.AuthenticatedUserService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @PatchMapping("/first-admin")
    public ResponseEntity<Void> createFirstAdmin() {
        UUID userId = authenticatedUserService.getCurrentUserId();

        userService.createFirstAdmin(userId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/role/{role}")
    public ResponseEntity<Void> addRole(@PathVariable UUID userId, @PathVariable RoleType role) {

        userService.addRole(userId, role);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/role/{role}")
    public ResponseEntity<Void> removeRole(@PathVariable UUID userId, @PathVariable RoleType role) {

        userService.removeRole(userId, role);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping()
    public ResponseEntity<Void> updateUser(UpdateUserRequest dto) {

        userService.updateUser(dto);

        return ResponseEntity.noContent().build();
    }
}
