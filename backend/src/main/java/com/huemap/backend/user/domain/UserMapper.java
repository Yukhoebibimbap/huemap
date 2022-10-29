package com.huemap.backend.user.domain;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

}
