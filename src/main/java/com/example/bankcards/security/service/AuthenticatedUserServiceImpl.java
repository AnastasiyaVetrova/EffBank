package com.example.bankcards.security.service;

import com.example.bankcards.exception.InvalidAuthenticationException;
import com.example.bankcards.security.dto.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidAuthenticationException("The user is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.user().getId();
        } else {
            throw new InvalidAuthenticationException("It is impossible to extract the user ID");
        }
    }
}
