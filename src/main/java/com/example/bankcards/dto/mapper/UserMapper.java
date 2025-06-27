package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.request.UpdateUserRequest;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "cards", ignore = true)
    @Mapping(target = "password", ignore = true)
    User updateUserRequestDtoToUser(@MappingTarget User user, UpdateUserRequest dto);
}
