package com.example.bankcards.service.impl;

import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleType;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createFirstAdmin(UUID userId) {
        if (userRepository.existsByRole(RoleType.ADMIN)) {
            throw new BadRequestException("To obtain the ADMIN role, please contact the administrator");
        }

        addRole(userId, RoleType.ADMIN);
    }

    @Override
    @Transactional
    public void addRole(UUID userId, RoleType role) {
        User user = findUserOrThrow(userId);

        user.getRoles().add(role);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRole(UUID userId, RoleType role) {
        User user = findUserOrThrow(userId);

        user.getRoles().remove(role);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserRequest dto) {
        User user = findUserOrThrow(dto.id());

        userRepository.save(userMapper.updateUserRequestDtoToUser(user, dto));
    }

    private User findUserOrThrow(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
