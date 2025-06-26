package com.example.bankcards.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.UUID;

public interface ExtendedUserDetailsService extends UserDetailsService {

    UserDetails loadUserById(UUID id);
}
