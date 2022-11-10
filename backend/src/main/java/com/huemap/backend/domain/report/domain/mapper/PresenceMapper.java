package com.huemap.backend.domain.report.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.domain.report.dto.response.PresenceCreateResponse;
import com.huemap.backend.domain.report.domain.Presence;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PresenceMapper {
  PresenceMapper INSTANCE = Mappers.getMapper(PresenceMapper.class);

  PresenceCreateResponse toDto(Presence presence);
}
