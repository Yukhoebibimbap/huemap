package com.huemap.backend.domain.suggestion.application;

import static com.huemap.backend.common.utils.GeometryUtil.convertPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.suggestion.domain.Suggestion;
import com.huemap.backend.domain.suggestion.domain.SuggestionRepository;
import com.huemap.backend.domain.suggestion.domain.mapper.SuggestionCreateMapper;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;
import com.huemap.backend.domain.suggestion.dto.response.SuggestionCreateResponse;
import com.huemap.backend.domain.suggestion.exception.SuggestionLimitExceededException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SuggestionService {

	private final SuggestionRepository suggestionRepository;

	@Transactional
	public SuggestionCreateResponse save(SuggestionCreateRequest suggestionCreateRequest, Long userId) {

		validateSuggestionCount(userId);

		final Point location = convertPoint(suggestionCreateRequest.getLatitude(),
			suggestionCreateRequest.getLongitude());

		Suggestion suggestion = suggestionRepository.save(
			SuggestionCreateMapper.INSTANCE.toEntity(suggestionCreateRequest, location, userId));

		return SuggestionCreateMapper.INSTANCE.toDto(suggestion);
	}

	public List<SuggestionService> findAllByGuAndTypeAndDate(String gu, BinType type, LocalDateTime startDate,
		LocalDateTime endDate) {
		return null;
	}

	private void validateSuggestionCount(Long userId) {
		LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
		LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

		if (suggestionRepository.countByUserIdAndCreatedAtBetween(userId, startDatetime, endDatetime) >= 3) {
			throw new SuggestionLimitExceededException();
		}
	}

}
