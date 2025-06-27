package com.example.bankcards.repository;

import com.example.bankcards.entity.CardLockOrder;
import com.example.bankcards.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardLockOrderRepository extends JpaRepository<CardLockOrder, Long> {

    Optional<CardLockOrder> findFirstByStatusOrderByCreatedAtAsc(OrderStatus orderStatus);
}
