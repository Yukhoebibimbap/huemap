package com.huemap.backend.report.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.report.domain.Closure;
import com.huemap.backend.report.dto.response.ClosureCreateResponse;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ClosureMapper {
  Mappers INSTANCE = Mappers.getMapper(Mappers.class);

  ClosureCreateResponse toDto(Closure closure);

}
