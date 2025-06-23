package com.example.bankcards.service;

import com.example.bankcards.dto.UserLoginDto;
import com.example.bankcards.dto.UserRegisterDto;

public interface AuthService {

    String register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);
}
