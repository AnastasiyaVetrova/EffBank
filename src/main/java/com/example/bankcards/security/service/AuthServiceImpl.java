package com.example.bankcards.security.service;

import com.example.bankcards.security.dto.UserLoginDto;
import com.example.bankcards.security.dto.UserRegisterDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleType;
import com.example.bankcards.exception.PhoneAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.security.dto.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String register(UserRegisterDto dto) {

        if (userRepository.existsByPhone(dto.phone())) {
            throw new PhoneAlreadyExistsException(dto.phone());
        }

        User user = User.builder()
                .name(dto.name())
                .surname(dto.surname())
                .phone(dto.phone())
                .password(passwordEncoder.encode(dto.password()))
                .roles(Set.of(RoleType.USER))
                .build();

        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }

    @Override
    public String login(UserLoginDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.phone(),dto.password()));

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return jwtUtil.generateToken(userDetails.user());
    }
}
