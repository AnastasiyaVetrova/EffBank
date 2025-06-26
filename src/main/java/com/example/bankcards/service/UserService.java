package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.entity.enums.RoleType;

import java.util.UUID;

public interface UserService {
    void createFirstAdmin(UUID userId);

    void addRole(UUID userId, RoleType role);

    void removeRole(UUID userId, RoleType role);

    void updateUser(UpdateUserRequest dto);
}
