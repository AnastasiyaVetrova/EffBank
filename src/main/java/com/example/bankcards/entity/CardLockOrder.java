package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card_lock_order")
public class CardLockOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "card_id", nullable = false)
    private UUID cardId;

    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "requested_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus requestedStatus;

    private String reason;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "closed_at")
    private LocalDate closedAt;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
