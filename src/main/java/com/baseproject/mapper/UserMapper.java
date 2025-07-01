package com.baseproject.mapper;

import com.baseproject.dto.*;
import com.baseproject.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "isActive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  User toEntity(UserCreateDto user);

  @Mapping(target = "active", source = "user.isActive")
  UserDto toDto(User user);
}
