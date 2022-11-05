package com.huemap.backend.report.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.report.domain.Presence;
import com.huemap.backend.report.dto.response.PresenceCreateResponse;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PresenceMapper {
  PresenceMapper INSTANCE = Mappers.getMapper(PresenceMapper.class);

  PresenceCreateResponse toDto(Presence presence);
}
