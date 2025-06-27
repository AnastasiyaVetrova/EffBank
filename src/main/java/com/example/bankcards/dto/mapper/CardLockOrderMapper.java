package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.response.CardLockProcessingResponse;
import com.example.bankcards.entity.CardLockOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardLockOrderMapper {

    CardLockProcessingResponse mapToCardLockDto(CardLockOrder cardLockOrder);
}
