package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.UserCardsResponse;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    @Mapping(target = "userId", source = "user.id")
    CardResponse mapToCardResponse(Card card);

    UserCardsResponse mapToUserCardsResponse(Card card);
}
