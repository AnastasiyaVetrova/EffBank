package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.CardLockProcessingDto;
import com.example.bankcards.entity.CardLockOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardLockOrderMapper {

    CardLockProcessingDto mapToCardLockDto(CardLockOrder cardLockOrder);
}
