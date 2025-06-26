package com.example.bankcards.repository;

import com.example.bankcards.dto.BalanceResponse;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findByUserId(UUID userId, Pageable pageable);

    Optional<Card> findByIdAndUserId(UUID cardId, UUID userId);

    @Query("""
                SELECT new com.example.bankcards.dto.BalanceResponse(c.id,c.balance)
                FROM Card c
                WHERE c.id=:cardId AND c.user.id=:userId
            """)
    Optional<BalanceResponse> findBalanceByUserIdAndId(@Param("userId") UUID userId, @Param("cardId") UUID cardId);
}
