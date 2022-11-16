package com.huemap.backend.domain.user.domain;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.domain.user.dto.request.UserCreateRequest;
import com.huemap.backend.domain.user.dto.response.UserCreateResponse;
import com.huemap.backend.domain.user.dto.response.UserLoginResponse;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toEntity(UserCreateRequest userCreateRequest);

  UserCreateResponse toCreateDto(User user);

  UserLoginResponse toLoginDto(User user);
}
