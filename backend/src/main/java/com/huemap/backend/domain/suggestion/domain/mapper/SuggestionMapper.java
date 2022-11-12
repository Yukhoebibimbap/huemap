package com.huemap.backend.domain.suggestion.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.domain.suggestion.domain.Suggestion;
import com.huemap.backend.domain.suggestion.dto.response.SuggestionResponse;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SuggestionMapper {
	SuggestionMapper INSTANCE = Mappers.getMapper(SuggestionMapper.class);

	@Mapping(expression = "java(suggestion.getLocation().getX())", target = "longitude")
	@Mapping(expression = "java(suggestion.getLocation().getY())", target = "latitude")
	SuggestionResponse toDto(Suggestion suggestion);
}
