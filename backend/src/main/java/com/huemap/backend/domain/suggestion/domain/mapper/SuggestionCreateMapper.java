package com.huemap.backend.domain.suggestion.domain.mapper;

import org.locationtech.jts.geom.Point;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.huemap.backend.domain.suggestion.domain.Suggestion;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;
import com.huemap.backend.domain.suggestion.dto.response.SuggestionCreateResponse;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SuggestionCreateMapper {
	SuggestionCreateMapper INSTANCE = Mappers.getMapper(SuggestionCreateMapper.class);

	Suggestion toEntity(SuggestionCreateRequest suggestionCreateRequest, Point location, Long userId);

	SuggestionCreateResponse toDto(Suggestion suggestion);
}
