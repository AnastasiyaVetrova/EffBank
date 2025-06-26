package com.example.bankcards.security.service;

import com.example.bankcards.security.dto.UserLoginDto;
import com.example.bankcards.security.dto.UserRegisterDto;

public interface AuthService {

    String register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);
}
