package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import com.example.bankcards.security.dto.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String userIdFromUserDetails = userDetails instanceof UserDetailsImpl
                ? ((UserDetailsImpl) userDetails).user().getId().toString() : null;

        return extractUserId(token).equals(userIdFromUserDetails) && !isExpired(token);
    }

    public String extractUserId(String token) {

        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isExpired(String token) {

        return getClaims(token).getExpiration().before(new Date());
    }
}
