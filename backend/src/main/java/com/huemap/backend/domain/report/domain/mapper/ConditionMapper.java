package com.huemap.backend.domain.report.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.domain.bin.domain.Bin;
import com.huemap.backend.domain.report.domain.Condition;
import com.huemap.backend.domain.report.domain.Image;
import com.huemap.backend.domain.report.dto.response.ConditionCreateResponse;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ConditionMapper {
  ConditionMapper INSTANCE = Mappers.getMapper(ConditionMapper.class);

  Condition toEntity(Long userId, Bin bin, Image image);

  ConditionCreateResponse toDto(Condition condition);
}
